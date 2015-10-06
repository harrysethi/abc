/**
 * 
 */
package domain;

import java.util.List;

/**
 * @author harinder
 *
 */
public class CliqueTreeNode {
	int cliqueTreeNodeID;
	List<InGraphNode> belongingNodes;
	List<CliqueTreeNode> adjList;
}
