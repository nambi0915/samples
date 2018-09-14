package main;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

/**
 * Audio tone generator, using the Java sampled sound API.
 * 
 * @author andrew Thompson
 * @version 2007/12/6
 */

public final class Sound extends JFrame {

	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = -3395062643525727075L;
	static AudioFormat audioFormat;
	static SourceDataLine sourceDataLine;

	public Sound() {
		super("Audio Sound");
		// Use current OS look and feel.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Internal Look And Feel Setting Error.");
			System.err.println(e);
		}

		JPanel mainPanel = new JPanel(new BorderLayout());

		final JSlider frequencyTuner = new JSlider(JSlider.VERTICAL, 200, 2000, 441);
		frequencyTuner.setPaintLabels(true);
		frequencyTuner.setPaintTicks(true);
		frequencyTuner.setMajorTickSpacing(200);
		frequencyTuner.setMinorTickSpacing(100);
		frequencyTuner.setToolTipText("Tone (in Hertz or cycles per second - middle C is 441 Hz)");
		frequencyTuner.setBorder(new TitledBorder("Frequency"));
		mainPanel.add(frequencyTuner, BorderLayout.CENTER);

		final JSlider timeSlider = new JSlider(JSlider.VERTICAL, 0, 2000, 1000);
		timeSlider.setPaintLabels(true);
		timeSlider.setPaintTicks(true);
		timeSlider.setMajorTickSpacing(200);
		timeSlider.setMinorTickSpacing(100);
		timeSlider.setToolTipText("Duration in milliseconds");
		timeSlider.setBorder(new TitledBorder("Length"));
		mainPanel.add(timeSlider, BorderLayout.EAST);

		final JSlider volSlider = new JSlider(JSlider.VERTICAL, 0, 100, 20);
		volSlider.setPaintLabels(true);
		volSlider.setPaintTicks(true);
		volSlider.setSnapToTicks(false);
		volSlider.setMajorTickSpacing(20);
		volSlider.setMinorTickSpacing(10);
		volSlider.setToolTipText("Volume 0 - none, 100 - full");
		volSlider.setBorder(new TitledBorder("Volume"));
		mainPanel.add(volSlider, BorderLayout.WEST);

		final JCheckBox isHarmonic = new JCheckBox("Add Harmonic", true);
		isHarmonic.setToolTipText("..else pure sine tone");

		JButton generateSound = new JButton("Generate Tone");
		generateSound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					generateTone(frequencyTuner.getValue(), timeSlider.getValue(), (int) (volSlider.getValue() * 1.28),
							isHarmonic.isSelected());
				} catch (LineUnavailableException lue) {
					System.out.println(lue);
				}
			}
		});

		JPanel pNorth = new JPanel(new BorderLayout());
		pNorth.add(generateSound, BorderLayout.WEST);

		pNorth.add(isHarmonic, BorderLayout.EAST);

		mainPanel.add(pNorth, BorderLayout.NORTH);
		mainPanel.setBorder(new javax.swing.border.EmptyBorder(5, 3, 5, 3));

		getContentPane().add(mainPanel);
		pack();
		setLocation(0, 20);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		String address = "/image/tone32x32.png";
		URL url = getClass().getResource(address);

		if (url != null) {
			Image icon = Toolkit.getDefaultToolkit().getImage(url);
			setIconImage(icon);
		}
	}

	/**
	 * Generates a tone.
	 * 
	 * @param hz
	 *            Base frequency (neglecting harmonic) of the tone in cycles per
	 *            second
	 * @param msecs
	 *            The number of milliseconds to play the tone.
	 * @param volume
	 *            Volume, form 0 (mute) to 100 (max).
	 * @param addHarmonic
	 *            Whether to add an harmonic, one octave up.
	 */
	public static void generateTone(int hz, int msecs, int volume, boolean addHarmonic)
			throws LineUnavailableException {

		float frequency = 44100;
		byte[] buf;
		AudioFormat af;
		if (addHarmonic) {
			buf = new byte[2];
			af = new AudioFormat(frequency, 8, 2, true, false);
		} else {
			buf = new byte[1];
			af = new AudioFormat(frequency, 8, 1, true, false);
		}
		SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
		sdl = AudioSystem.getSourceDataLine(af);
		sdl.open(af);
		sdl.start();
		for (int i = 0; i < msecs * frequency / 1000; i++) {
			double angle = i / (frequency / hz) * 2.0 * Math.PI;
			buf[0] = (byte) (Math.sin(angle) * volume);

			if (addHarmonic) {
				double angle2 = (i) / (frequency / hz) * 2.0 * Math.PI;
				buf[1] = (byte) (Math.sin(2 * angle2) * volume * 0.6);
				sdl.write(buf, 0, 2);
			} else {
				sdl.write(buf, 0, 1);
			}
		}
		sdl.drain();
		sdl.stop();
		sdl.close();
	}

	public static void main(String[] args) {
		Runnable runObj = new Runnable() {
			public void run() {
				Sound soundObj = new Sound();
				soundObj.setVisible(true);
			}
		};
		SwingUtilities.invokeLater(runObj);
	}
}
