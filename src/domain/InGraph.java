/**
 * 
 */
package domain;


/**
 * @author harinder
 *
 */
public class InGraph {
	//public List<InGraphNode> nodes_w1 = new ArrayList<InGraphNode>();
	//public List<InGraphNode> nodes_w2 = new ArrayList<InGraphNode>();
	
	public InGraphNode nodes_w1[];
	public InGraphNode nodes_w2[];
	
	public InGraph(int len) {
		nodes_w1 = new InGraphNode[len];
		nodes_w2 = new InGraphNode[len];
	}
}
