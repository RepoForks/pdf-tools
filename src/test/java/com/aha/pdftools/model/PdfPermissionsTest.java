package com.aha.pdftools.model;

import static org.junit.Assert.*;
import static junitparams.JUnitParamsRunner.$; 

import java.util.HashSet;
import java.util.Set;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.itextpdf.text.pdf.PdfWriter;

@RunWith(JUnitParamsRunner.class)
public class PdfPermissionsTest {

	private static int ALL_PERMISSIONS = 0xfffffffc;
	private static int NO_PERMISSIONS = 0xfffff0c0;
	private static String ASSEMBLY = "assembly";
	private static String COPY = "copy";
	private static String PRINT = "printing";
	private static String FILL_IN = "fillIn";
	private static String DEGRADED_PRINT = "degradedPrinting";
	private static String ANNOTATION = "modifyAnnotation";
	private static String CONTENT = "modifyContents";
	private static String SCREEN = "screenReaders";
	
	
	@Test
	public void allPermissions() {
		PdfPermissions perm = new PdfPermissions();
		assertTrue(perm.isAssembly());
		assertTrue(perm.isCopy());
		assertTrue(perm.isDegradedPrinting());
		assertTrue(perm.isFillIn());
		assertTrue(perm.isModifyAnnotations());
		assertTrue(perm.isModifyContents());
		assertTrue(perm.isPrinting());
		assertTrue(perm.isScreenReaders());
		assertEquals(ALL_PERMISSIONS, perm.getPermissions());
	}

	@Test
	@Parameters(method = "permissionValues")
	public void permissions(int pdfPerm, Set<String> permissions) {
		PdfPermissions perm = new PdfPermissions(pdfPerm);
		assertPermission(perm.isAssembly(), ASSEMBLY, permissions);
		assertPermission(perm.isCopy(), COPY, permissions);
		assertPermission(perm.isDegradedPrinting(), DEGRADED_PRINT, permissions);
		assertPermission(perm.isPrinting(), PRINT, permissions);
		assertPermission(perm.isFillIn(), FILL_IN, permissions);
		assertPermission(perm.isModifyAnnotations(), ANNOTATION, permissions);
		assertPermission(perm.isModifyContents(), CONTENT, permissions);
		assertPermission(perm.isScreenReaders(), SCREEN, permissions);
		assertEquals(pdfPerm, perm.getPermissions());
	}

	private void assertPermission(boolean actual, String name, Set<String> permissions) {
		assertEquals(name, permissions.contains(name), actual);
	}
	
	private Set<String> $$(String... permissions) {
		HashSet<String> set = new HashSet<String>();
		for (int i = 0; i < permissions.length; i++) {
			set.add(permissions[i]);
		}
		return set;
	}
	
	Object[] permissionValues() {
		return $(
				$(ALL_PERMISSIONS, $$(ASSEMBLY, COPY, PRINT, DEGRADED_PRINT, FILL_IN, ANNOTATION, CONTENT, SCREEN)),
				$((ALL_PERMISSIONS ^ PdfWriter.ALLOW_PRINTING) & ALL_PERMISSIONS, $$(ASSEMBLY, COPY, FILL_IN, ANNOTATION, CONTENT, SCREEN)),
				$(NO_PERMISSIONS | PdfWriter.ALLOW_ASSEMBLY, $$(ASSEMBLY)),
				$(NO_PERMISSIONS | PdfWriter.ALLOW_COPY, $$(COPY)),
				$(NO_PERMISSIONS | PdfWriter.ALLOW_DEGRADED_PRINTING, $$(DEGRADED_PRINT)),
				$(NO_PERMISSIONS | PdfWriter.ALLOW_FILL_IN, $$(FILL_IN)),
				$(NO_PERMISSIONS | PdfWriter.ALLOW_MODIFY_ANNOTATIONS, $$(ANNOTATION)),
				$(NO_PERMISSIONS | PdfWriter.ALLOW_MODIFY_CONTENTS, $$(CONTENT)),
				$(NO_PERMISSIONS | PdfWriter.ALLOW_PRINTING, $$(PRINT, DEGRADED_PRINT)),
				$(NO_PERMISSIONS | PdfWriter.ALLOW_SCREENREADERS, $$(SCREEN))
				);
	}
}
