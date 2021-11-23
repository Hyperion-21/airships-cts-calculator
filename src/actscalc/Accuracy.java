package actscalc;

import java.util.Random;

public class Accuracy {
	
	private static double inaccuracy;
	private static double jitterMerge;
	private static double targetDist;
	
	private static double shotX = 0;
	private static double shotY = 0;
	private static Random rand = new Random();
	private static double gaus;
	
	/**
	 * Sets the input to the inaccuracy/jitterMerge equation
	 * 
	 * @param inaccuracy
	 * @param jitterMerge
	 * @param targetDist
	 */
	public Accuracy(double inaccuracy, double jitterMerge, double targetDist) {
		Accuracy.inaccuracy = inaccuracy;
		Accuracy.jitterMerge = jitterMerge;
		Accuracy.targetDist = targetDist;
	}
	
	/**
	 * Prints the X and Y coordinates of the first shot, in pixels. 
	 * Will also set variables shotX and shotY to the outputs.
	 * 
	 * @return double[] shotCoords
	 */
	public static double[] firstShot() {
		gaus = rand.nextGaussian();
		// shotX = targetX / 16 + gaus * inaccuracy * Math.sqrt(Math.pow((weaponX - targetX), 2) + Math.pow((weaponY - targetY), 2)) * 16;
		shotX = gaus * inaccuracy * targetDist * 256;
		gaus = rand.nextGaussian();
		shotY = gaus * inaccuracy * targetDist * 256;
		System.out.println(shotX + ", " + shotY);
		double[] shotCoords = { shotX, shotY };
		return shotCoords;
	}
	
	
	/**
	 * Prints the X and Y coordinates of subsequent shots, in pixels.
	 * Will also set variables shotX and shotY to the outputs.
	 * 
	 * @param shotX X-Position of the previous shot, in pixels.
	 * @param shotY Y-Position of the previous shot, in pixels.
	 * @return double[] shotCoords
	 */
	public static double[] nextShot(double shotX, double shotY) {
		gaus = rand.nextGaussian();
		shotX = jitterMerge * shotX + (1 - jitterMerge) * (gaus * inaccuracy * targetDist * 256);
		gaus = rand.nextGaussian();
		shotY = jitterMerge * shotY + (1 - jitterMerge) * (gaus * inaccuracy * targetDist * 256);
		System.out.println(shotX + ", " + shotY);
		double[] shotCoords = { shotX, shotY };
		return shotCoords;
	}
	
	/**
	 * Runs Accuracy.firstShot(), then Accuracy.nextShot() 'arg' times.
	 * 
	 * @param arg
	 */
	public static void main(int arg) {
		
		firstShot();
		for (int i = 0; i < arg; i++) nextShot(shotX, shotY);
		
	}
}
