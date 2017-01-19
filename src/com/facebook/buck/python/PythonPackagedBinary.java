/*
 * Copyright 2012-present Facebook, Inc.
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

package com.facebook.buck.python;

import static com.facebook.buck.rules.BuildableProperties.Kind.PACKAGING;

import com.facebook.buck.model.BuildTargets;
import com.facebook.buck.model.HasBuildTarget;
import com.facebook.buck.rules.AddToRuleKey;
import com.facebook.buck.rules.BuildContext;
import com.facebook.buck.rules.BuildRuleParams;
import com.facebook.buck.rules.BuildTargetSourcePath;
import com.facebook.buck.rules.BuildableContext;
import com.facebook.buck.rules.BuildableProperties;
import com.facebook.buck.rules.CommandTool;
import com.facebook.buck.rules.HasRuntimeDeps;
import com.facebook.buck.rules.SourcePath;
import com.facebook.buck.rules.SourcePathResolver;
import com.facebook.buck.rules.SourcePathRuleFinder;
import com.facebook.buck.rules.Tool;
import com.facebook.buck.rules.args.SourcePathArg;
import com.facebook.buck.step.Step;
import com.facebook.buck.step.fs.MakeCleanDirectoryStep;
import com.facebook.buck.step.fs.MkdirStep;
import com.facebook.buck.step.fs.RmStep;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.nio.file.Path;
import java.util.stream.Stream;

public class PythonPackagedBinary extends PythonBinary implements HasRuntimeDeps {

  private static final BuildableProperties OUTPUT_TYPE = new BuildableProperties(PACKAGING);

  private final SourcePathRuleFinder ruleFinder;
  @AddToRuleKey
  private final Tool builder;
  @AddToRuleKey
  private final ImmutableList<String> buildArgs;
  private final Tool pathToPexExecuter;
  @AddToRuleKey
  private final String mainModule;
  @AddToRuleKey
  private final PythonPackageComponents components;
  @AddToRuleKey
  private final PythonEnvironment pythonEnvironment;
  @AddToRuleKey
  private final ImmutableSet<String> preloadLibraries;
  private final boolean cache;

  protected PythonPackagedBinary(
      BuildRuleParams params,
      SourcePathResolver resolver,
      SourcePathRuleFinder ruleFinder,
      PythonPlatform pythonPlatform,
      Tool builder,
      ImmutableList<String> buildArgs,
      Tool pathToPexExecuter,
      String pexExtension,
      PythonEnvironment pythonEnvironment,
      String mainModule,
      PythonPackageComponents components,
      ImmutableSet<String> preloadLibraries,
      boolean cache,
      boolean legacyOutputPath) {
    super(
        params,
        resolver,
        pythonPlatform,
        mainModule,
        components,
        preloadLibraries,
        pexExtension,
        legacyOutputPath);
    this.ruleFinder = ruleFinder;
    this.builder = builder;
    this.buildArgs = buildArgs;
    this.pathToPexExecuter = pathToPexExecuter;
    this.pythonEnvironment = pythonEnvironment;
    this.mainModule = mainModule;
    this.components = components;
    this.preloadLibraries = preloadLibraries;
    this.cache = cache;
  }

  @Override
  public BuildableProperties getProperties() {
    return OUTPUT_TYPE;
  }

  @Override
  public Tool getExecutableCommand() {
    return new CommandTool.Builder(pathToPexExecuter)
        .addArg(
            new SourcePathArg(
                getResolver(),
                new BuildTargetSourcePath(getBuildTarget(), getBinPath())))
        .build();
  }

  @Override
  public ImmutableList<Step> getBuildSteps(
      BuildContext context,
      BuildableContext buildableContext) {

    ImmutableList.Builder<Step> steps = ImmutableList.builder();
    Path binPath = getBinPath();

    // Make sure the parent directory exists.
    steps.add(new MkdirStep(getProjectFilesystem(), binPath.getParent()));

    // Delete any other pex that was there (when switching between pex styles).
    steps.add(new RmStep(
        getProjectFilesystem(),
        binPath,
        RmStep.Mode.FORCED,
        RmStep.Mode.RECURSIVE));

    Path workingDirectory = BuildTargets.getGenPath(
        getProjectFilesystem(),
        getBuildTarget(),
        "__%s__working_directory");
    steps.add(new MakeCleanDirectoryStep(getProjectFilesystem(), workingDirectory));

    SourcePathResolver resolver = context.getSourcePathResolver();

    // Generate and return the PEX build step.
    steps.add(
        new PexStep(
            getProjectFilesystem(),
            builder.getEnvironment(),
            ImmutableList.<String>builder()
                .addAll(builder.getCommandPrefix(resolver))
                .addAll(buildArgs)
                .build(),
            pythonEnvironment.getPythonPath(),
            pythonEnvironment.getPythonVersion(),
            workingDirectory,
            binPath,
            mainModule,
            resolver.getMappedPaths(components.getModules()),
            resolver.getMappedPaths(components.getResources()),
            resolver.getMappedPaths(components.getNativeLibraries()),
            ImmutableSet.copyOf(
                resolver.getAllAbsolutePaths(components.getPrebuiltLibraries())),
            preloadLibraries,
            components.isZipSafe().orElse(true)));

    // Record the executable package for caching.
    buildableContext.recordArtifact(getBinPath());

    return steps.build();
  }

  @Override
  public Stream<SourcePath> getRuntimeDeps() {
    return Stream.concat(
        super.getRuntimeDeps(),
        pathToPexExecuter.getDeps(ruleFinder)
            .stream()
            .map(HasBuildTarget::getBuildTarget)
            .map(BuildTargetSourcePath::new));
  }

  @Override
  public boolean isCacheable() {
    return cache;
  }

}
