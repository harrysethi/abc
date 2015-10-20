/**
 * 
 */
package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author harinder
 *
 */
public class LB_baseNode {
	public List<LB_edge> edges;
	
	public LB_baseNode() {
		this.edges = new ArrayList<LB_edge>();
	}
	
	public void addEdge(LB_baseNode dest) {
		LB_edge lb_edge = new  LB_edge(this, dest);
		this.edges.add(lb_edge);
	}
	
}
