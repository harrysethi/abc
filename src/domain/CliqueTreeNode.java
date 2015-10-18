/**
 * 
 */
package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * @author harinder
 *
 */
public class CliqueTreeNode {
	private static int nodeCounter;

	private int nodeID;
	public Set<InGraphNode> belongingNodes;
	public List<CliqueTreeEdge> adjList;
	
	public List<Factor> factors;
	
	//public Vector<Vector<String>> factorProduct;
	//public String [][]factorProduct;
	//public List<List<Object>> factorProduct;
	public Map<Object, List<Object>> factorProduct;

	public CliqueTreeNode() {
		this.nodeID = CliqueTreeNode.nodeCounter;
		CliqueTreeNode.nodeCounter++;
		
		this.belongingNodes = new HashSet<InGraphNode>();
		this.adjList = new ArrayList<CliqueTreeEdge>();
		this.factors = new ArrayList<Factor>();
		
		this.factorProduct = new HashMap<Object, List<Object>>();
	}

	public int getNodeID() {
		return this.nodeID;
	}
	
	@Override
	public String toString(){
		return this.belongingNodes.toString();
	}
	
	public CliqueTreeEdge getCliqueTreeEdge(CliqueTreeNode cliqueTreeNode) {
		for (CliqueTreeEdge cliqueTreeEdge : adjList) {
			if(cliqueTreeEdge.getDest() == cliqueTreeNode)
				return cliqueTreeEdge;
		}
		
		return null;
	}

}
