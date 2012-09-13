package com.aha.pdftools.gui;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import com.aha.pdftools.model.Reorderable;

/**
 * Handles drag & drop row reordering
 */
@SuppressWarnings("serial")
public class TableRowTransferHandler extends AbstractTableRowTransferHandler {
	public TableRowTransferHandler(JTable table) {
		super(table, new ActivationDataFlavor(Integer.class, DataFlavor.javaJVMLocalObjectMimeType, "Integer Row Index"));
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		assert (c == table);
		return new DataHandler(new Integer(table.getSelectedRow()), localObjectFlavor.getMimeType());
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport info) {
		JTable target = (JTable) info.getComponent();
		JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
		int index = dl.getRow();
		int max = table.getModel().getRowCount();
		if (index < 0 || index > max)
			index = max;
		target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		try {
			Integer rowFrom = (Integer) info.getTransferable().getTransferData(localObjectFlavor);
			if (rowFrom != -1 && rowFrom != index) {
				((Reorderable)table.getModel()).reorder(rowFrom, index);
				if (index > rowFrom)
					index--;
				target.getSelectionModel().addSelectionInterval(index, index);
				return true;
			}
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, e.getMessage(), e);
		}
		return false;
	}
}
