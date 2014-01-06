/*
 * Copyright (C) 2012  Armin Häberling
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.aha.pdftools.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.aha.pdftools.Messages;

public class PdfFileFilter extends FileFilter implements java.io.FileFilter {

	public static final String PDF_EXTENSION = ".pdf"; //$NON-NLS-1$

	@Override
	public boolean accept(File f) {
		if (f.isFile()) {
			return f.getName().endsWith(PDF_EXTENSION);
		}
		return f.isDirectory();
	}

	@Override
	public String getDescription() {
		return Messages.getString("PdfFileFilter.PdfFiles"); //$NON-NLS-1$
	}

}
