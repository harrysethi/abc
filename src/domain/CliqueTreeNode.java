/**
 * 
 */
package domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author harinder
 *
 */
public class CliqueTreeNode {
	private static int nodeCounter;

	private int nodeID;
	public Set<InGraphNode> belongingNodes;
	public List<CliqueTreeEdge> adjList;

	public CliqueTreeNode() {
		this.nodeID = CliqueTreeNode.nodeCounter;
		CliqueTreeNode.nodeCounter++;
		
		this.belongingNodes = new HashSet<InGraphNode>();
		this.adjList = new ArrayList<CliqueTreeEdge>();
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
