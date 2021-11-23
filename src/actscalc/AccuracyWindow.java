package actscalc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

public class AccuracyWindow implements ActionListener {

	static JFrame frame = new JFrame("Accuracy Calculator");
	static JPanel panel;
	static JPanel panel2;
	static JButton submit;
	static JTextField inaccuracy;
	static JTextField jitterMerge;
	static JTextField targetDist;
	static JTextField iterations;
	static JLabel[] outputn = new JLabel[10];
	static JLabel[] outputn2 = new JLabel[10];
	static JLabel[] outputn3 = new JLabel[10];
	
	/**
	 * Creates a frame, that exits program on close, is visible, and has h64.png icon.
	 */
	public static void GenerateFrame() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		ImageIcon icon = new ImageIcon("images/h64.png");
		frame.setIconImage(icon.getImage());
	}
	
	/**
	 * Creates and adds components of AccuracyWindow.
	 */
	public AccuracyWindow() {
		GenerateFrame();
		
		panel = new JPanel();
		panel2 = new JPanel();
		GridBagLayout bagLayout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		panel.setLayout(bagLayout);
		panel2.setLayout(bagLayout);

		inaccuracy = new JTextField("0.006");
		jitterMerge = new JTextField("0.8");
		targetDist = new JTextField("16");
		iterations = new JTextField("10");
		
		inaccuracy.setPreferredSize(new Dimension(60, 20));
		jitterMerge.setPreferredSize(new Dimension(60, 20));
		targetDist.setPreferredSize(new Dimension(60, 20));
		iterations.setPreferredSize(new Dimension(60, 20));
		
		JLabel inaccuracyLabel = new JLabel("Inaccuracy");
		JLabel jitterMergeLabel = new JLabel("Jitter Merge");
		JLabel targetDistLabel = new JLabel("Distance to Target (tiles)");
		JLabel iterationsLabel = new JLabel("Number of Shots (max 10)");
		
		JLabel output = new JLabel("Pixels:");
		JLabel output2 = new JLabel("Tiles:");
		JLabel output3 = new JLabel("TileDiff:");
		for (int i = 0; i < 10; i++) {
			outputn[i] = new JLabel(" ");
			outputn2[i]= new JLabel(" ");
			outputn3[i] = new JLabel(" ");
		}
		
		submit = new JButton("Calculate");
		submit.addActionListener(this);
		
		c.weightx = 0.5;
		c.anchor = GridBagConstraints.LINE_END;
		c.insets = new Insets(2, 2, 2, 5);
		
		c.gridx = 0; c.gridy = 0;
		panel.add(inaccuracyLabel, c);
		
		c.gridx = 1; c.gridy = 0;
		panel.add(inaccuracy, c);
		
		c.gridx = 0; c.gridy = 1;
		panel.add(jitterMergeLabel, c);
		
		c.gridx = 1; c.gridy = 1;
		panel.add(jitterMerge, c);
		
		c.gridx = 0; c.gridy = 2;
		panel.add(targetDistLabel, c);
		
		c.gridx = 1; c.gridy = 2;
		panel.add(targetDist, c);
		
		c.gridx = 0; c.gridy = 3;
		panel.add(iterationsLabel, c);
		
		c.gridx = 1; c.gridy = 3;
		panel.add(iterations, c);
		
		
		c.gridx = 0; c.gridy = 4;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(submit, c);
		c.gridwidth = 1;
		
		c.gridx = 0; c.gridy = 0;
		panel2.add(output, c);
		
		c.gridx = 1;
		panel2.add(output2, c);
		
		c.gridx = 2;
		panel2.add(output3, c);
		
		for (int i = 0; i < 10; i++) {
			c.gridx = 0; c.gridy = i + 1;
			panel2.add(outputn[i], c);
			c.gridx = 1;
			panel2.add(outputn2[i], c);
			c.gridx = 2;
			panel2.add(outputn3[i], c);
		}
		
		frame.add(panel);
		frame.add(panel2, BorderLayout.SOUTH);
		frame.pack();
	}
	
	/**
	 * Creates an AccuracyWindow object.
	 * @param args
	 */
	public static void main(String[] args) {
		new AccuracyWindow();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			Double.parseDouble(inaccuracy.getText());
			Double.parseDouble(jitterMerge.getText());
			Double.parseDouble(targetDist.getText());
			Integer.parseInt(iterations.getText());
			if (Integer.parseInt(iterations.getText()) <= 0 || Integer.parseInt(iterations.getText()) > 10) throw new Exception("iterations less than 1, or greater than 10");
		} catch (Exception exception) {
			System.out.println(exception);
			outputn[0].setText("Invalid Input");
			for (int i = 1; i < 10; i++) {
				outputn[i].setText(" ");
				outputn2[i].setText(" ");
				outputn3[i].setText(" ");
				outputn2[0].setText(" ");
			}
			return;
		}
		
		new Accuracy(Double.parseDouble(inaccuracy.getText()),
				Double.parseDouble(jitterMerge.getText()),
				Double.parseDouble(targetDist.getText()));
		double[] hot = new double[2];
		hot = Accuracy.firstShot();
		double shotX = hot[0];
		double shotY = hot[1];
		hot[0] = Math.round(hot[0] * 10);
		hot[1] = Math.round(hot[1] * 10);
		hot[0] /= 10;
		hot[1] /= 10;
		
		outputn[0].setText(hot[0] + ", " + hot[1]);  
		
		hot[0] /= 16;
		hot[0] = Math.round(hot[0] * 100);
		hot[0] /= 100;
		hot[1] /= 16;
		hot[1] = Math.round(hot[1] * 100);
		hot[1] /= 100;
		outputn2[0].setText(hot[0] + ", " + hot[1]);
		double[] prev = { hot[0], hot[1] };
		
		for (int i = 1; i < Integer.parseInt(iterations.getText()); i++) {
			hot = Accuracy.nextShot(shotX, shotY);
			shotX = hot[0];
			shotY = hot[1];
			hot[0] = Math.round(hot[0] * 10);
			hot[1] = Math.round(hot[1] * 10);
			hot[0] /= 10;
			hot[1] /= 10;
			
			outputn[i].setText(hot[0] + ", " + hot[1]);
			
			hot[0] /= 16;
			hot[0] = Math.round(hot[0] * 100);
			hot[0] /= 100;
			hot[1] /= 16;
			hot[1] = Math.round(hot[1] * 100);
			hot[1] /= 100;
			outputn2[i].setText(hot[0] + ", " + hot[1]);
			
			double[] diff = { hot[0] - prev[0], hot[1] - prev[1] };
			diff[0] = Math.round(diff[0] * 100);
			diff[0] /= 100;
			diff[1] = Math.round(diff[1] * 100);
			diff[1] /= 100;
			outputn3[i].setText(diff[0] + ", " + diff[1]);
			prev[0] = hot[0]; prev[1] = hot[1];
		}
		for (int i = Integer.parseInt(iterations.getText()); i < 10; i++) {
			outputn[i].setText(" ");
			outputn2[i].setText(" ");
			outputn3[i].setText(" ");
		}
	}
}
