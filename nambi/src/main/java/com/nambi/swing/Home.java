package com.nambi.swing;

import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Home extends JFrame {
	FileDeletion folderProcess = new FileDeletion();
	private static final long serialVersionUID = -2035400489179934479L;

	public Home() {
		final JLabel label = new JLabel();
		label.setBounds(25, 10, 400, 30);
		add(label);

		JButton delete = new JButton("Delete");
		final JButton selectDirButton = new JButton("Select Folder");

		selectDirButton.setBackground(Color.white);
		delete.setBackground(Color.white);
		selectDirButton.setBounds(0, 50, 200, 40);
		delete.setBounds(0, 150, 200, 40);

		add(selectDirButton);
		add(delete);
		final JList<File> selectedfolderList = new JList<File>();
		selectedfolderList.setBounds(210, 50, 450, 305);
		add(selectedfolderList);
		selectedfolderList.setVisible(true);

		setSize(700, 500);
		setLayout(null);
		selectDirButton.setFocusPainted(false);

		selectDirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setBounds(210, 50, 300, 500);
				add(chooser);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
					selectDirButton.setEnabled(false);
					File selectedDirectory = chooser.getSelectedFile();
					label.setText("Selected Folder: " + selectedDirectory);
					List<File> empty = folderProcess.getEmptyFolderList(selectedDirectory);
					System.out.println("Selected folder: " + chooser.getSelectedFile());
					// DefaultListModel<File> model = new DefaultListModel<File>();
					selectedfolderList.setListData(empty.toArray(new File[empty.size()]));
				}
			}
		});
		delete.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				folderProcess.deleteFolders();
				selectedfolderList.removeAll();
				JOptionPane.showMessageDialog(null, "Deleted Successfully", "Delete", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Home d = new Home();
				d.setVisible(true);
			}
		});
		/*
		 * Home homeWindow = new Home(); homeWindow.setVisible(true);
		 * homeWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
		 */

	}
}
