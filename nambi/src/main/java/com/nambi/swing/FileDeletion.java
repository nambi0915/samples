package com.nambi.swing;

import java.io.File;

public class FileDeletion {

	public void deleteEmptyFiles(File directorySelected) {
		File[] listOfFiles = directorySelected.listFiles();
		if (listOfFiles.length == 0) {
			System.out.println("Selected directory itself Empty");
		}
	}

}
