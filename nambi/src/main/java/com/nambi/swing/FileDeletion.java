package com.nambi.swing;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileDeletion {

	static List<File> emptyFoldersList = new ArrayList<File>();

	public List<File> getEmptyFolderList(File selectedDirectory) {
		List<File> subDirectories = new ArrayList<File>();

		if (selectedDirectory.listFiles().length == 0) {
			emptyFoldersList.add(selectedDirectory);
			return emptyFoldersList;
		} else {
			subDirectories = getSubdirectories(selectedDirectory);
			System.out.println(subDirectories);
		}
		return emptyFoldersList;
	}

	/**
	 * Finds the list of sub directories of the given directory.Sub directory is a
	 * directory which is either empty or not has a sub directory.
	 * 
	 * @param selectedDirectory
	 * @return
	 */
	public static List<File> getSubdirectories(File selectedDirectory) {
		List<File> subDirectoryList = new ArrayList<File>(Arrays.asList(selectedDirectory.listFiles()));
		System.err.println(subDirectoryList);

		for (int i = 0; i < subDirectoryList.size();) {
			boolean dirWithOutSubDir = false;
			File fileOrFolder = subDirectoryList.get(i);
			// System.err.println(file);
			if (fileOrFolder.isDirectory()) {
				if (fileOrFolder.listFiles().length != 0) {
					List<File> tempList = Arrays.asList(fileOrFolder.listFiles());
					for (File folder : tempList) {
						// Directory with sub directory
						if (folder.isDirectory()) {
							dirWithOutSubDir = true;
							// System.err.println(folder);
							subDirectoryList.remove(fileOrFolder);
							subDirectoryList.add(folder);
						}
					}
					// Directory without sub Directory
					if (dirWithOutSubDir == false)
						i++;

				}
				// Empty Directory
				else {
					if (!emptyFoldersList.contains(fileOrFolder))
						emptyFoldersList.add(fileOrFolder);
					// System.err.println(file);
					if (!subDirectoryList.contains(fileOrFolder)) {
						subDirectoryList.add(fileOrFolder);
					}
					i++;
				}
			} else
				subDirectoryList.remove(fileOrFolder);
		}
		// System.err.println("function over");
		// System.out.println(subList);

		return subDirectoryList;
	}

	/**
	 * Deletes all the folders in emptyFoldersList
	 */
	public void deleteFolders() {
		for (File emptyFolder : emptyFoldersList) {
			emptyFolder.delete();
		}

	}

}
