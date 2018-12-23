package demo;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class SwingDemo {
	public static void main(String args[]) {
		JFrame frame = new JFrame("File Explorer");
		JButton button = new JButton("Go!");
		button.setBackground(Color.white);
		button.setBounds(100, 100, 60, 30);
		frame.add(button);
		frame.setSize(300, 300);
		frame.setLayout(null);
		frame.setVisible(true);
		ActionListener openFileExplorer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					System.err.println(chooser.getSelectedFile());

				}
			}
		};
		button.addActionListener(openFileExplorer);

	}
}
