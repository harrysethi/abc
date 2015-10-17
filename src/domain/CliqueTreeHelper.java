/**
 * 
 */
package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import constants.ModelType;
import constants.WordNumType;

/**
 * @author harinder
 *
 */
public class CliqueTreeHelper {
	public static List<CliqueTree> makeCliqueTree(InGraph inGraph, ModelType modelType) {
		List<CliqueTree> cliqueTrees = new ArrayList<CliqueTree>();
		
		
		switch (modelType) {
		case PAIR_SKIP_MODEL:
			CliqueTree cliqueTree_pairSkipModel = getCliqueTreeRemovingSubsets(inGraph, cliqueTrees, WordNumType.BOTH);
			joinAllEdges_kruskals(cliqueTree_pairSkipModel);
			break;
			
		case SKIP_MODEL:
			// intentionally missed 'break'
			
		case TRANSITION_MODEL:
			CliqueTree cliqueTree1 = getCliqueTreeRemovingSubsets(inGraph, cliqueTrees, WordNumType.WORD_NUM_1);
			CliqueTree cliqueTree2 = getCliqueTreeRemovingSubsets(inGraph, cliqueTrees, WordNumType.WORD_NUM_2);
			
			joinAllEdges_kruskals(cliqueTree1);
			joinAllEdges_kruskals(cliqueTree2);
			break;
			
		case OCR_MODEL:
			getCliqueTreeRemovingSubsets(inGraph, cliqueTrees, WordNumType.BOTH);
			//returning the completely disconnected clique tree
			break;
		}
		
		return cliqueTrees;
	}

	private static void joinAllEdges_kruskals(CliqueTree cliqueTree) {
		List<CliqueTreeEdge> cliqueTreeEdges = joinAllEdges(cliqueTree.nodes);
		kruskals(cliqueTreeEdges, cliqueTree.nodes.size());
	}

	private static CliqueTree getCliqueTreeRemovingSubsets(
			InGraph inGraph, List<CliqueTree> cliqueTrees, WordNumType wordNumType) {
		CliqueTree cliqueTree = new CliqueTree();
		cliqueTrees.add(cliqueTree);
		List<CliqueTreeNode> cliqueTreeNodes = getCliqueTreeNodes(inGraph, wordNumType);
		cliqueTree.nodes = cliqueTreeNodes;
		
		removeSubsets(cliqueTreeNodes);
		return cliqueTree;
	}
	
	static class Subset
	{
	    int parent;
	    int rank;
	}
	
	private static int find(Subset subsets[], int i)
	{
	    // find root and make root as parent of i (path compression)
	    if (subsets[i].parent != i)
	        subsets[i].parent = find(subsets, subsets[i].parent);
	 
	    return subsets[i].parent;
	}
	
	private static void union(Subset subsets[], int x, int y)
	{
	    int xroot = find(subsets, x);
	    int yroot = find(subsets, y);
	 
	    if (subsets[xroot].rank < subsets[yroot].rank)
	        subsets[xroot].parent = yroot;
	    else if (subsets[xroot].rank > subsets[yroot].rank)
	        subsets[yroot].parent = xroot;
	 
	    else
	    {
	        subsets[yroot].parent = xroot;
	        subsets[xroot].rank++;
	    }
	}
	
	
	private static void kruskals(List<CliqueTreeEdge> cliqueTreeEdges, int numOfNodes) {
		List<CliqueTreeEdge> spanningTreeEdges = new ArrayList<CliqueTreeEdge>();
		
		int maxEdgeWeight = getMaxEdgeWeight(cliqueTreeEdges);
		setEdgeWeight_maxMinus(cliqueTreeEdges, maxEdgeWeight);
		
		Collections.sort(cliqueTreeEdges);
		
		// Create V subsets with single elements
		Subset subsets[] = new Subset[numOfNodes];
	    for (int v = 0; v < numOfNodes; ++v)
	    {
	    	subsets[v] = new Subset();
	        subsets[v].parent = v;
	        subsets[v].rank = 0;
	    }
		
		int edgePtr = 0;
		
		while(spanningTreeEdges.size() < numOfNodes-1) {
			CliqueTreeEdge nextEdge = cliqueTreeEdges.get(edgePtr++);
			
			int x = find(subsets, nextEdge.getSrc().getNodeID());
	        int y = find(subsets, nextEdge.getDest().getNodeID());
			
	        if (x != y)
	        {
	        	spanningTreeEdges.add(nextEdge);
	        	nextEdge.isPresent = true;
	            union(subsets, x, y);
	        }
			
		}
		
		//return spanningTreeEdges;
	}

	private static void setEdgeWeight_maxMinus(List<CliqueTreeEdge> cliqueTEdges, int maxEdgeWt) {
		for(CliqueTreeEdge cliqueTreeEdge : cliqueTEdges)
			cliqueTreeEdge.edgeWeight_maxMinus = maxEdgeWt - cliqueTreeEdge.getEdgeWeight();
	}

	private static int getMaxEdgeWeight(List<CliqueTreeEdge> cliqueTreeEdges) {
		int maxEdgeWeight = cliqueTreeEdges.get(0).getEdgeWeight();
		for(int i=1;i<cliqueTreeEdges.size();i++) {
			if(maxEdgeWeight<cliqueTreeEdges.get(i).getEdgeWeight()) 
				maxEdgeWeight = cliqueTreeEdges.get(i).getEdgeWeight();
		}
		
		return maxEdgeWeight;
	}
	
	private static List<CliqueTreeEdge> joinAllEdges(List<CliqueTreeNode> cliqueTreeNodes) {
		List<CliqueTreeEdge> cliqueTreeEdges = new ArrayList<CliqueTreeEdge>();
				
		for (int i = 0; i < cliqueTreeNodes.size(); i++) {
			for (int j = i + 1; j < cliqueTreeNodes.size(); j++) {
				//if(i==j) continue;
				
				CliqueTreeNode cliqueTreeNode1 = cliqueTreeNodes.get(i);
				CliqueTreeNode cliqueTreeNode2 = cliqueTreeNodes.get(j);
				
				Set<InGraphNode> intersection = new HashSet<InGraphNode>(cliqueTreeNode1.belongingNodes); // use the copy constructor
				intersection.retainAll(cliqueTreeNode2.belongingNodes);
				int edgeWeight = intersection.size();
				
				CliqueTreeEdge cliqueTreeEdge1 = new CliqueTreeEdge(cliqueTreeNode1, cliqueTreeNode2, edgeWeight);
				CliqueTreeEdge cliqueTreeEdge2 = new CliqueTreeEdge(cliqueTreeNode2, cliqueTreeNode1, edgeWeight);
				
				cliqueTreeEdges.add(cliqueTreeEdge1);
				cliqueTreeNode1.adjList.add(cliqueTreeEdge1);
				cliqueTreeNode2.adjList.add(cliqueTreeEdge2);
			}
		}
		
		return cliqueTreeEdges;
	}
	
	private static void removeSubsets(List<CliqueTreeNode> cliqueTreeNodes) {
		for(int i=0;i<cliqueTreeNodes.size();i++) {
			for(int j=i+1;j<cliqueTreeNodes.size();j++) {
				CliqueTreeNode cliqueTreeNode1 = cliqueTreeNodes.get(i);
				CliqueTreeNode cliqueTreeNode2 = cliqueTreeNodes.get(j);

				Set<InGraphNode> belongingNodes1 = cliqueTreeNode1.belongingNodes;
				Set<InGraphNode> belongingNodes2 = cliqueTreeNode2.belongingNodes;
				
				Set<InGraphNode> intersection = new HashSet<InGraphNode>(belongingNodes1); // use the copy constructor
				intersection.retainAll(belongingNodes2);
				
				if(intersection.size()==belongingNodes1.size()){
					cliqueTreeNodes.remove(cliqueTreeNode1);
					continue;
				}
				
				if(intersection.size()==belongingNodes2.size()){
					cliqueTreeNodes.remove(cliqueTreeNode2);
					continue;
				}
			}
		}
	}
	
	private static List<CliqueTreeNode> getCliqueTreeNodes(InGraph inGraph, WordNumType wordNumType) {
		
		List<CliqueTreeNode> cliqueTreeNodes = new ArrayList<CliqueTreeNode>();
		
		while (true) {
			InGraphNode minFillNode = getMinFillNode(inGraph, wordNumType);
			if(minFillNode == null) break; //no more active nodes - break

			minFillNode.isActive = false;
			CliqueTreeNode cliqueTreeNode = new CliqueTreeNode();
			cliqueTreeNodes.add(cliqueTreeNode);
			
			//adding belonging nodes
			cliqueTreeNode.belongingNodes.add(minFillNode);
			for(InGraphNodeAdjacency inGraphNodeAdjacency : minFillNode.adjList) {
				InGraphNode inGraphNode = inGraphNodeAdjacency.inGraphNode;
				if(!inGraphNode.isActive) continue;
				cliqueTreeNode.belongingNodes.add(inGraphNode);
			}
		}
		
		
		return cliqueTreeNodes;
	}
	
	public static InGraphNode getMinFillNode(InGraph inGraph, WordNumType wordNumType) {
		int minFillEdges = Integer.MAX_VALUE;
		InGraphNode minFillNode = null;
		
		if(wordNumType != WordNumType.WORD_NUM_2) {
			for (int i = 0; i < inGraph.nodes_w1.length; i++) {
				int fillEdgesReq = 0;
				InGraphNode inGraphNode = inGraph.nodes_w1[i];
				if(!inGraphNode.isActive) continue; //we are only interested in active nodes
				
				int adjLen = inGraphNode.adjList.size();
	
				for (int j = 0; j < adjLen - 1; j++) {
					InGraphNode firstNode = inGraphNode.adjList.get(j).inGraphNode;
					if(!firstNode.isActive) continue; //we are only interested in active nodes
					
					for (int k = j + 1; k < adjLen; k++) {
						InGraphNode secondNode = inGraphNode.adjList.get(k).inGraphNode;
						if(!secondNode.isActive) continue;
						
						if(!firstNode.isAdjacent(secondNode)) fillEdgesReq ++;
					}
				}
				
				if(minFillEdges>fillEdgesReq) {
					minFillEdges = fillEdgesReq;
					minFillNode = inGraph.nodes_w1[i];
					
					if(fillEdgesReq == 0) return minFillNode; //We can't have -ve fillEdges..this is min
				}
			}
		}
		
		if(wordNumType != WordNumType.WORD_NUM_1) {
			for (int i = 0; i < inGraph.nodes_w2.length; i++) {
				int fillEdgesReq = 0;
				InGraphNode inGraphNode = inGraph.nodes_w2[i];
				if(!inGraphNode.isActive) continue; //we are only interested in active nodes
				
				int adjLen = inGraphNode.adjList.size();
	
				for (int j = 0; j < adjLen - 1; j++) {
					InGraphNode firstNode = inGraphNode.adjList.get(j).inGraphNode;
					if(!firstNode.isActive) continue; //we are only interested in active nodes
					
					for (int k = j + 1; k < adjLen; k++) {
						InGraphNode secondNode = inGraphNode.adjList.get(k).inGraphNode;
						if(!secondNode.isActive) continue;
						
						if(!firstNode.isAdjacent(secondNode)) fillEdgesReq ++;
					}
				}
				
				if(minFillEdges > fillEdgesReq) {
					minFillEdges = fillEdgesReq;
					minFillNode = inGraph.nodes_w2[i];
					
					if(fillEdgesReq == 0) return minFillNode; //We can't have -ve fillEdges..this is min
				}
			}
		}
	
		return minFillNode;
	}
}
