/**
 * 
 */
package domain;

/**
 * @author harinder
 *
 */
public class CliqueTreeHelper {
	public static InGraphNode getMinFillEdge(InGraph inGraph) {
		int minFillEdges = Integer.MAX_VALUE;
		InGraphNode minFillNode = null;
		
		for (int i = 0; i < inGraph.nodes_w1.length; i++) {
			int fillEdgesReq = 0;
			InGraphNode inGraphNode = inGraph.nodes_w1[i];
			int adjLen = inGraphNode.adjList.size();

			for (int j = 0; j < adjLen - 1; j++) {
				for (int k = j + 1; k < adjLen; k++) {
					InGraphNode firstNode = inGraphNode.adjList.get(j).inGraphNode;
					InGraphNode secondNode = inGraphNode.adjList.get(k).inGraphNode;
					
					if(!firstNode.isAdjacent(secondNode)) fillEdgesReq ++;
				}
			}
			
			if(minFillEdges>fillEdgesReq) {
				minFillEdges = fillEdgesReq;
				minFillNode = inGraph.nodes_w1[i];
				
				if(fillEdgesReq == 0) return minFillNode; //We can't have -ve fillEdges..this is min
			}
		}
		
		for (int i = 0; i < inGraph.nodes_w2.length; i++) {
			int fillEdgesReq = 0;
			InGraphNode inGraphNode = inGraph.nodes_w2[i];
			int adjLen = inGraphNode.adjList.size();

			for (int j = 0; j < adjLen - 1; j++) {
				for (int k = j + 1; k < adjLen; k++) {
					InGraphNode firstNode = inGraphNode.adjList.get(j).inGraphNode;
					InGraphNode secondNode = inGraphNode.adjList.get(k).inGraphNode;
					
					if(!firstNode.isAdjacent(secondNode)) fillEdgesReq ++;
				}
			}
			
			if(minFillEdges>fillEdgesReq) {
				minFillEdges = fillEdgesReq;
				minFillNode = inGraph.nodes_w2[i];
				
				if(fillEdgesReq == 0) return minFillNode; //We can't have -ve fillEdges..this is min
			}
		}
		
		return minFillNode;
	}
}
