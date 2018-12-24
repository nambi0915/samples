package com.nambi.swing;

import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;

public class Home extends JFrame {

	private static final long serialVersionUID = -2035400489179934479L;
	public static File selectedDirectory = new File("");

	public Home() {
		JButton delete = new JButton("Delete");
		final JButton selectDirButton = new JButton("Select Folder");

		selectDirButton.setBackground(Color.white);
		delete.setBackground(Color.white);
		selectDirButton.setBounds(0, 100, 200, 40);
		delete.setBounds(0, 140, 200, 40);

		add(selectDirButton);
		add(delete);

		setSize(700, 500);
		setLayout(null);
		selectDirButton.setFocusPainted(false);

		JList<String> folderList = new JList<String>();
		folderList.setBounds(210, 50, 450, 175);
		add(folderList);

		JList<File> selectedfolderList = new JList<File>();
		selectedfolderList.setBounds(210, 50, 450, 175);
		add(selectedfolderList);

		JList<String> deletefolderList = new JList<String>();
		deletefolderList.setBounds(210, 250, 450, 175);
		add(deletefolderList);
		selectDirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setBounds(210, 50, 300, 500);
				add(chooser);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
					selectDirButton.setEnabled(false);
					selectedDirectory = chooser.getSelectedFile();
					System.out.println("Selected folder: " + chooser.getSelectedFile());
				}
			}
		});

	}

	public static void main(String args[]) {
		Home homeWindow = new Home();
		homeWindow.setVisible(true);
		homeWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}
}
