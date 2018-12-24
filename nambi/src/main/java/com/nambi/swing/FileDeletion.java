package com.nambi.swing;

import java.io.File;

import javax.swing.*;

public class FileDeletion {
	public static JFrame deletionWindow = new JFrame("Folder Delete");

	public FileDeletion() {
		deletionWindow.setSize(500, 500);
		JLabel label = new JLabel("Selected Directory is " + Home.selectedDirectory);
		deletionWindow.add(label);
		deletionWindow.setVisible(true);
	}

	public void deleteEmptyFiles(File directorySelected) {
		File[] listOfFiles = directorySelected.listFiles();
		if (listOfFiles.length == 0) {
			System.out.println("Selected directory itself Empty");
		}
	}

}
