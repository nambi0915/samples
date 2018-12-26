package com.nambi.swing;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class Home extends JFrame {
	FileDeletion folderProcess = new FileDeletion();
	private static final long serialVersionUID = -2035400489179934479L;

	public Home() {
		final JLabel label = new JLabel();
		label.setBounds(25, 10, 400, 30);
		add(label);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		JButton delete = new JButton("Delete");
		final JButton selectDirButton = new JButton("Select Folder");

		selectDirButton.setBackground(Color.white);
		delete.setBackground(Color.white);
		selectDirButton.setBounds(0, 50, 200, 40);

		delete.setBounds(0, 100, 200, 40);
		controlPanel.setBounds(210, 50, 450, 200);

		selectDirButton.setFocusPainted(false);
		add(selectDirButton);
		add(delete);
		final JList<File> selectedfolderList = new JList<File>();
		selectedfolderList.setLayoutOrientation(JList.VERTICAL);
		// selectedfolderList.setBounds(210, 50, 450, 200);
		selectedfolderList.setBorder(BorderFactory.createLoweredBevelBorder());
		// selectedfolderList.setVisibleRowCount(5);
		// add(selectedfolderList);
		// selectedfolderList.setVisible(true);

		JScrollPane vegListScrollPane = new JScrollPane(selectedfolderList);

		controlPanel.add(vegListScrollPane);
		add(controlPanel);
		setSize(700, 400);
		setLayout(null);

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
