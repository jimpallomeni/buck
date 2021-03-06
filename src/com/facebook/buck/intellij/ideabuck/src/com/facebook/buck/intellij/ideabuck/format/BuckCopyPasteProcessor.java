/*
 * Copyright 2015-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.intellij.ideabuck.format;

import com.facebook.buck.intellij.ideabuck.build.BuckBuildUtil;
import com.facebook.buck.intellij.ideabuck.lang.BuckFile;
import com.facebook.buck.intellij.ideabuck.lang.psi.BuckPsiUtils;
import com.facebook.buck.intellij.ideabuck.lang.psi.BuckTypes;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.intellij.codeInsight.editorActions.CopyPastePreProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RawText;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.TokenType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiUtilCore;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;

public class BuckCopyPasteProcessor implements CopyPastePreProcessor {

  private static final Pattern UNSOLVED_DEPENDENCY_PATTERN =
      Pattern.compile("^(package|import)?\\s*([\\w\\./]*);?\\s*$");
  private static final Pattern SOLVED_DEPENDENCY_PATTERN =
      Pattern.compile("^\\s*[\\w/-]*:[\\w-]+\\s*$");

  @Nullable
  @Override
  public String preprocessOnCopy(PsiFile psiFile, int[] ints, int[] ints1, String s) {
    return null;
  }

  @Override
  public String preprocessOnPaste(
      Project project, PsiFile psiFile, Editor editor, String text, RawText rawText) {
    if (!(psiFile instanceof BuckFile)) {
      return text;
    }
    final Document document = editor.getDocument();
    PsiDocumentManager.getInstance(project).commitDocument(document);
    final SelectionModel selectionModel = editor.getSelectionModel();

    // Pastes in block selection mode (column mode) are not handled by a CopyPasteProcessor.
    final int selectionStart = selectionModel.getSelectionStart();
    final PsiElement element = psiFile.findElementAt(selectionStart);
    if (element == null) {
      return text;
    }

    if (BuckPsiUtils.hasElementType(
        element.getNode(),
        TokenType.WHITE_SPACE,
        BuckTypes.SINGLE_QUOTED_STRING,
        BuckTypes.DOUBLE_QUOTED_STRING)) {
      PsiElement property = BuckPsiUtils.findAncestorWithType(element, BuckTypes.PROPERTY);
      if (checkPropertyName(property)) {
        return formatPasteText(text, element, project);
      }
    }
    return text;
  }

  protected boolean checkPropertyName(PsiElement property) {
    if (property == null) {
      return false;
    }
    PsiElement leftValue = property.getFirstChild();
    if (leftValue == null || leftValue.getNode().getElementType() != BuckTypes.PROPERTY_LVALUE) {
      return false;
    }
    leftValue = leftValue.getFirstChild();
    if (leftValue == null || leftValue.getNode().getElementType() != BuckTypes.IDENTIFIER) {
      return false;
    }
    if (leftValue.getText().equals("deps") || leftValue.getText().equals("visibility")) {
      return true;
    }
    return false;
  }

  /**
   * Converts raw paste text to a format suitable for insertion in a buck dependency list (e.g.,
   * {@code deps} or {@code visibility} at the point of the given element. The paste text may span
   * multiple lines. If every non-blank line can be resolved into a dependency, the expanded text is
   * returned, but if any non-blank line in unresolvable, the original paste text is returned.
   *
   * <p>This method detects the location in the parse tree from the given element and will
   * quote-wrap dependencies accordingly.
   *
   * <p>Example resolutions (note that these are the unwrapped results; wrapped results are
   * double-quoted with a trailing comma):
   *
   * <p>Java imports: {@code "import com.example.activity.MyFirstActivity" ->
   * "//java/com/example/activity:activity" }
   *
   * <p>Java packages: {@code "package com.example.activity;" ->
   * "//java/com/example/activity:activity" }
   *
   * <p>Fully qualified Java classnames: {@code "com.example.activity.MyFirstActivity" ->
   * "//java/com/example/activity:activity" }
   *
   * <p>Unqualified Java classnames: {@code "MyFirstActivity" ->
   * "//java/com/example/activity:activity" } (when there is a unique match for the classname)
   *
   * <p>BUCK paths: {@code "//java/com/example/activity/BUCK" ->
   * "//java/com/example/activity:activity" }
   *
   * <p>BUCK targets: {@code "//java/com/example/activity:activity" ->
   * "//java/com/example/activity:activity" }
   *
   * <p>Multiline pastes: {@code "import com.foo.Foo;\nimport com.bar.Bar;" ->
   * "//java/com/foo:foo\n//java/com/bar:bar"}
   */
  private String formatPasteText(String text, PsiElement element, Project project) {
    Iterable<String> paths = Splitter.on('\n').trimResults().omitEmptyStrings().split(text);
    List<String> results = new ArrayList<>();
    for (String path : paths) {
      String resolution = null;

      Matcher matcher = UNSOLVED_DEPENDENCY_PATTERN.matcher(path);
      if (matcher.matches()) {
        resolution = resolveUnsolvedBuckDependency(element, project, matcher.group(2));
      } else if (SOLVED_DEPENDENCY_PATTERN.matcher(path).matches()) {
        resolution = buildSolvedBuckDependency(path);
      } // else we don't know how to format this

      if (resolution == null) {
        // Any non-target results in no formatting
        return text;
      }
      results.add(resolution);
    }
    return Joiner.on('\n').skipNulls().join(results);
  }

  private String buildSolvedBuckDependency(String path) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('"');
    if (!(path.startsWith("//") || path.startsWith(":"))) {
      if (path.startsWith("/")) {
        stringBuilder.append('/');
      } else {
        stringBuilder.append("//");
      }
    }
    return stringBuilder.append(path).append("\",").toString();
  }

  @Nullable
  private String resolveUnsolvedBuckDependency(PsiElement element, Project project, String path) {
    VirtualFile buckFile = referenceNameToBuckFile(project, path);
    if (buckFile != null) {
      path = buckFile.getPath().replaceFirst(project.getBasePath(), "");
      path = "/" + path.replace('.', '/');
      path = path.substring(0, path.lastIndexOf("/"));

      String target = BuckBuildUtil.extractBuckTarget(project, buckFile);
      if (target != null) {
        path += target;
      } else {
        String lastWord = path.substring(path.lastIndexOf("/") + 1, path.length());
        path += ":" + lastWord;
      }
      if (element.getNode().getElementType() == TokenType.WHITE_SPACE) {
        path = "\"" + path + "\",";
      }
      return path;
    } else {
      return null;
    }
  }

  private VirtualFile referenceNameToBuckFile(Project project, String reference) {
    // First test if it is a absolute path of a file.
    {
      VirtualFileManager virtualFileManager = VirtualFileManager.getInstance();
      VirtualFile file = virtualFileManager.findFileByUrl("file://" + reference);
      if (file == null) {
        // We might be in an integration test ...
        file = virtualFileManager.findFileByUrl("temp://" + reference);
      }
      if (file != null) {
        return BuckBuildUtil.getBuckFileFromDirectory(file.getParent());
      }
    }

    // Try class firstly.
    {
      PsiClass foundClass = null;
      GlobalSearchScope projectScope = GlobalSearchScope.allScope(project);
      if (reference.indexOf('.') >= 0) {
        // A fully qualified name
        foundClass = JavaPsiFacade.getInstance(project).findClass(reference, projectScope);
      } else {
        // A short name
        final PsiClass[] classes =
            PsiShortNamesCache.getInstance(project).getClassesByName(reference, projectScope);
        if (classes != null && classes.length == 1) {
          foundClass = classes[0];
        }
        // TODO(shemitz) Can we show a chooser if we have multiple candidates?
        // That might be confusing with multi-line pastes
      }
      if (foundClass != null) {
        VirtualFile file = PsiUtilCore.getVirtualFile(foundClass);
        return BuckBuildUtil.getBuckFileFromDirectory(file.getParent());
      }
    }

    // Then try package.
    PsiPackage packageElement = JavaPsiFacade.getInstance(project).findPackage(reference);
    if (packageElement != null) {
      PsiDirectory directory = packageElement.getDirectories()[0];
      return BuckBuildUtil.getBuckFileFromDirectory(directory.getVirtualFile());
    }

    // Extract the package from the reference.
    int index = reference.lastIndexOf(".");
    if (index == -1) {
      return null;
    }
    reference = reference.substring(0, index);

    // Try to find the package again.
    packageElement = JavaPsiFacade.getInstance(project).findPackage(reference);
    if (packageElement != null) {
      PsiDirectory directory = packageElement.getDirectories()[0];
      return BuckBuildUtil.getBuckFileFromDirectory(directory.getVirtualFile());
    }
    return null;
  }
}
