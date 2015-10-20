/**
 * 
 */
package domain;

/**
 * @author harinder
 *
 */
public class LB_edge {
	public LB_baseNode src;
	public LB_baseNode dest;
	
	public LB_edge(LB_baseNode src, LB_baseNode dest) {
		this.src = src;
		this.dest = dest;
	}
	
	@Override
	public String toString() {
		return this.src + "->" + this.dest;
	}
}
