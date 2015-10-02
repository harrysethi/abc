/**
 * 
 */
package domain;

/**
 * @author harinder
 *
 */
public class TransPotential {
	private char charI;
	private char charIplus1;
	private double value;

	public TransPotential(char charI, char charIplus1, double value) {
		this.charI = charI;
		this.charIplus1 = charIplus1;
		this.value = value;
	}
}
