/**
 * 
 */
package domain;

/**
 * @author harinder
 *
 */
public class OcrPotential {
	private int imageID;
	private char character;
	private double prob;

	public OcrPotential(int imageID, char character, double prob) {
		this.imageID = imageID;
		this.character = character;
		this.prob = prob;
	}
}
