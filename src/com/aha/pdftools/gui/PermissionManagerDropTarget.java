package com.aha.pdftools.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.aha.pdftools.PdfPermissionManagerGui;

class PermissionManagerDropTarget extends DropTargetAdapter {
    private static DataFlavor uriListFlavor = null;
    static {
        try {
            uriListFlavor = new DataFlavor("text/uri-list; class=java.lang.String");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PdfPermissionManagerGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private final PermissionManager manager;
    
    public PermissionManagerDropTarget(PermissionManager manager) {
		this.manager = manager;
	}
    
    private boolean openFiles(List<File> files) {
    	List<File> finalFiles = new ArrayList<File>();
    	for (File file : files) {
			if (!file.canRead()) {
				continue;
			}
			if (file.isDirectory()) {
	    		finalFiles.addAll(PermissionManager.filesInFolder(file));
	    	} else {
	    		finalFiles.add(file);
	    	}
		}
    	
    	if (finalFiles.isEmpty()) {
    		return false;
    	} else {
    		manager.insertFiles(finalFiles);
    		return true;
    	}
    }
	
	@Override
	public void drop(DropTargetDropEvent dtde) {
		if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            dtde.acceptDrop(DnDConstants.ACTION_COPY);
            Transferable trans = dtde.getTransferable();
            try {
                @SuppressWarnings("unchecked")
				List<File> filelist = (List<File>) trans.getTransferData(DataFlavor.javaFileListFlavor);
                dtde.dropComplete(openFiles(filelist));
            } catch (Exception e) {
                dtde.dropComplete(false);
            }
        } else if (dtde.isDataFlavorSupported(uriListFlavor)) {
            dtde.acceptDrop(DnDConstants.ACTION_COPY);
            Transferable trans = dtde.getTransferable();
            try {
                String uris = (String) trans.getTransferData(uriListFlavor);
                StringTokenizer tokenizer = new StringTokenizer(uris);
                List<File> files = new ArrayList<File>();
                while (tokenizer.hasMoreTokens()) {
                	URI uri = new URI(tokenizer.nextToken());
                	files.add(new File(uri));
                }
                dtde.dropComplete(openFiles(files));
            } catch (Exception e) {
                dtde.dropComplete(false);
            }
        } else {
            dtde.rejectDrop();
        }
	}
}