package com.nambi.swing;

import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Home {

	public static JFrame homeWindow = new JFrame("Folder Select");

	public static File selectedDirectory = new File("");

	public Home() {
		JButton button = new JButton("Select");
		button.setBackground(Color.white);
		button.setBounds(100, 100, 60, 30);
		homeWindow.add(button);
		homeWindow.setSize(300, 300);
		homeWindow.setLayout(null);
		// frame.setVisible(true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
					selectedDirectory = chooser.getSelectedFile();
					System.out.println("Selected folder: " + chooser.getSelectedFile());
					homeWindow.setVisible(false);
					new FileDeletion();
				}
			}
		});

	}

	public static void main(String args[]) {
		new Home();
		homeWindow.setVisible(true);

	}
}
