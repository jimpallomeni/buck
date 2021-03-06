import label

import unittest


class LabelTest(unittest.TestCase):
    def test_can_parse_full_label_from_string(self):
        l = label.from_string('cell//package:name')
        self.assertEqual(l.name, 'name')
        self.assertEqual(l.package, 'package')
        self.assertEqual(l.cell, 'cell')

    def test_can_parse_label_without_cell(self):
        l = label.from_string('//package:name')
        self.assertEqual(l.name, 'name')
        self.assertEqual(l.package, 'package')
        self.assertIsNone(l.cell)

    def test_can_parse_label_with_multilevel_package(self):
        l = label.from_string('cell//pkg/subpkg:name')
        self.assertEqual(l.name, 'name')
        self.assertEqual(l.package, 'pkg/subpkg')
        self.assertEqual(l.cell, 'cell')

    def test_cannot_parse_invalid_label(self):
        with self.assertRaisesRegex(AssertionError, "Invalid label 'cell/pkg:name'"):
            label.from_string('cell/pkg:name')

    def test_can_resolve_path_to_build_file(self):
        l = label.from_string('cell//pkg:name')
        cell_roots = {
            'cell': '/repo/cell',
        }
        self.assertEqual('/repo/cell/pkg/BUCK', l.get_build_file_path(cell_roots, 'BUCK'))

    def test_can_convert_to_import_string(self):
        self.assertEqual('cell//pkg:name', label.from_string('cell//pkg:name').to_import_string())
