/**
 * 
 */
package domain;

/**
 * @author harinder
 *
 */
public class CliqueTreeEdge implements Comparable<CliqueTreeEdge> {
	private CliqueTreeNode src;
	private CliqueTreeNode dest;
	private int edgeWeight;
	public int edgeWeight_maxMinus;
	public boolean isPresent;

	public CliqueTreeEdge(CliqueTreeNode src, CliqueTreeNode dest, int edgeWeight) {
		this.src = src;
		this.dest = dest;
		this.edgeWeight = edgeWeight;
		isPresent = false;
	}

	public CliqueTreeNode getDest() {
		return dest;
	}
	
	public CliqueTreeNode getSrc() {
		return src;
	}

	public int getEdgeWeight() {
		return edgeWeight;
	}
	
	@Override
	public String toString() {
		return src + "=>" + dest + "|:" + edgeWeight;
	}

	@Override
	public int compareTo(CliqueTreeEdge compareTo) {
		return this.edgeWeight_maxMinus - compareTo.edgeWeight_maxMinus;
	}

}
