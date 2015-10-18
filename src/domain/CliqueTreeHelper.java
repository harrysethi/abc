/**
 * 
 */
package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import constants.Consts;
import constants.FactorType;
import constants.ModelType;
import constants.WordNumType;

/**
 * @author harinder
 *
 */
public class CliqueTreeHelper {
	public static List<CliqueTree> makeCliqueTree(InGraph inGraph, ModelType modelType) {
		List<CliqueTree> cliqueTrees = makeCliqueTree_withoutFactors(inGraph, modelType);
		
		for (CliqueTree cliqueTree : cliqueTrees) {
			assignFactors(cliqueTree, modelType);
			factorMultiplication(cliqueTree);
		}
		
		return cliqueTrees;
	}
	
	private static void factorMultiplication(CliqueTree cliqueTree) {
		for(CliqueTreeNode cliqueTreeNode : cliqueTree.nodes) {
			
			//add titles
			for(InGraphNode inGraphNode : cliqueTreeNode.belongingNodes) {
				List<Object> belongingNodeList = new ArrayList<Object>();
				cliqueTreeNode.factorProduct.put(inGraphNode, belongingNodeList);
			}
			
			List<Object> valueList = new ArrayList<Object>();
			cliqueTreeNode.factorProduct.put("Value", valueList);

			
			//add characters
			List<InGraphNode> belongingNodes = new ArrayList<InGraphNode>(cliqueTreeNode.belongingNodes);
			addCharacters(cliqueTreeNode, belongingNodes, 0, belongingNodes.size()-1);

			for(Factor factor : cliqueTreeNode.factors) {
				List<Object> factorList_specific2Node_1 = searchSpecificFactorList(cliqueTreeNode, factor.inGraphNode1);
				List<Object> factorList_specific2Node_2 = searchSpecificFactorList(cliqueTreeNode, factor.inGraphNode2);
				
				switch (factor.factorType) {
				case OCR:
					for(int i=0; i<factorList_specific2Node_1.size(); i++) {
						char currChar_ocr = (char)factorList_specific2Node_1.get(i);
						Double prob_ocr = Potentials.getOcrFactor(factor.inGraphNode1.imgID, currChar_ocr);
						
						double currProb_ocr = (double)valueList.get(i);
						valueList.set(i, currProb_ocr*prob_ocr);
					}
					
					break;
					
				case TRANSITION:
					for(int i=0; i<factorList_specific2Node_1.size(); i++) {
						char currChar_transition_1 = (char)factorList_specific2Node_1.get(i);
						char currChar_transition_2 = (char)factorList_specific2Node_2.get(i);
						
						Double prob_transition = Potentials.getTransFactor(currChar_transition_1, currChar_transition_2);
						
						double currProb_transition = (double)valueList.get(i);
						valueList.set(i, currProb_transition*prob_transition);
					}
					
					break;
					
				case SKIP:
					for(int i=0; i<factorList_specific2Node_1.size(); i++) {
						char currChar_transition_1 = (char)factorList_specific2Node_1.get(i);
						char currChar_transition_2 = (char)factorList_specific2Node_2.get(i);
						
						Double prob_skip = Potentials.getSkipFactor(currChar_transition_1, currChar_transition_2);
						
						double currProb_skip = (double)valueList.get(i);
						valueList.set(i, currProb_skip*prob_skip);
					}
					
					break;

				case PAIR_SKIP:
					for(int i=0; i<factorList_specific2Node_1.size(); i++) {
						char currChar_transition_1 = (char)factorList_specific2Node_1.get(i);
						char currChar_transition_2 = (char)factorList_specific2Node_2.get(i);
						
						Double prob_pairSkip = Potentials.getPairSkipFactor(currChar_transition_1, currChar_transition_2);
						
						double currProb_pairSkip = (double)valueList.get(i);
						valueList.set(i, currProb_pairSkip*prob_pairSkip);
					}
					
					break;
				}
			}
			
			System.out.println();
		}
	}

	private static List<Object> searchSpecificFactorList(CliqueTreeNode cliqueTreeNode,
			InGraphNode inGraphNode) {
		return cliqueTreeNode.factorProduct.get(inGraphNode);
	}
	
	private static void addCharacters(CliqueTreeNode cliqueTreeNode, List<InGraphNode> belongingNodes, int l_index, int r_index) {
		if(l_index == belongingNodes.size()) {
			for(int i=0;i<Math.pow(10, l_index);i++)
				cliqueTreeNode.factorProduct.get("Value").add(1.0);
			return;
		}
		
		for(int k=0;k<Math.pow(10, l_index);k++) {
			for(int i=0;i<10;i++) {
				for(int j=0;j<Math.pow(10, r_index);j++) { 
					cliqueTreeNode.factorProduct.get(belongingNodes.get(l_index)).add(Consts.characters[i]);
				}
			}
		}
		addCharacters(cliqueTreeNode, belongingNodes, ++l_index, --r_index);
	}
	
	private static void assignFactors(CliqueTree cliqueTree, ModelType modelType) {
		switch (modelType) {
		case PAIR_SKIP_MODEL:
			assignPairSkipFactors(cliqueTree);
			//intentionally missing 'break'
			
		case SKIP_MODEL:
			assignSkipFactors(cliqueTree);
			//intentionally missing 'break'
			
		case TRANSITION_MODEL:
			assignTransitionFactors(cliqueTree);
			//intentionally missing 'break'
			
		case OCR_MODEL:
			assignOCRfactors(cliqueTree);
			break;
		}
	}

	private static void assignPairSkipFactors(CliqueTree cliqueTree) {
		Set<String> pairSkipFactorsAssigned = new HashSet<String>();
		
		for (CliqueTreeNode cliqueTreeNode : cliqueTree.nodes) {
			List<InGraphNode> belongingNodes = new ArrayList<InGraphNode>(cliqueTreeNode.belongingNodes);
			
			for(int i=0; i<belongingNodes.size(); i++) {
				for(int j=i+1; j<belongingNodes.size(); j++) {
					InGraphNode belongingNode1 = belongingNodes.get(i);
					InGraphNode belongingNode2 = belongingNodes.get(j);
					
					if(!belongingNode1.imgID.equals(belongingNode2.imgID)) continue;
					if(belongingNode1.wordNumType == belongingNode2.wordNumType) continue;
					
					//there exists a transition factor
					String key1 = getKey(belongingNode1, belongingNode2);
					String key2 = getKey(belongingNode2, belongingNode1);
					if(pairSkipFactorsAssigned.contains(key1) || pairSkipFactorsAssigned.contains(key2)) continue;
					
					pairSkipFactorsAssigned.add(key1);
					Factor factor = new Factor();
					cliqueTreeNode.factors.add(factor);
					factor.factorType = FactorType.PAIR_SKIP;
					factor.inGraphNode1 = belongingNode1;
					factor.inGraphNode2 = belongingNode2;
					
				}
			}
		}
	}

	private static void assignSkipFactors(CliqueTree cliqueTree) {
		Set<String> skipFactorsAssigned = new HashSet<String>();
		
		for (CliqueTreeNode cliqueTreeNode : cliqueTree.nodes) {
			List<InGraphNode> belongingNodes = new ArrayList<InGraphNode>(cliqueTreeNode.belongingNodes);
			
			for(int i=0; i<belongingNodes.size(); i++) {
				for(int j=i+1; j<belongingNodes.size(); j++) {
					InGraphNode belongingNode1 = belongingNodes.get(i);
					InGraphNode belongingNode2 = belongingNodes.get(j);
					
					if(!belongingNode1.imgID.equals(belongingNode2.imgID)) continue;
					if(belongingNode1.wordNumType != belongingNode2.wordNumType) continue;
					
					//there exists a transition factor
					String key1 = getKey(belongingNode1, belongingNode2);
					String key2 = getKey(belongingNode2, belongingNode1);
					if(skipFactorsAssigned.contains(key1) || skipFactorsAssigned.contains(key2)) continue;
					
					skipFactorsAssigned.add(key1);
					Factor factor = new Factor();
					cliqueTreeNode.factors.add(factor);
					factor.factorType = FactorType.SKIP;
					factor.inGraphNode1 = belongingNode1;
					factor.inGraphNode2 = belongingNode2;
					
				}
			}
		}
	}

	private static void assignTransitionFactors(CliqueTree cliqueTree) {
		Set<String> transitionFactorsAssigned = new HashSet<String>();
		
		for (CliqueTreeNode cliqueTreeNode : cliqueTree.nodes) {
			List<InGraphNode> belongingNodes = new ArrayList<InGraphNode>(cliqueTreeNode.belongingNodes);
			
			for(int i=0; i<belongingNodes.size(); i++) {
				for(int j=i+1; j<belongingNodes.size(); j++) {
					InGraphNode belongingNode1 = belongingNodes.get(i);
					InGraphNode belongingNode2 = belongingNodes.get(j);
					
					if(Math.abs(belongingNode1.nodeID-belongingNode2.nodeID) != 1) continue;
					if(belongingNode1.wordNumType != belongingNode2.wordNumType) continue;
					//transition factors exist only between nodes of a same word
					
					//there exists a transition factor
					String key1 = getKey(belongingNode1, belongingNode2);
					String key2 = getKey(belongingNode2, belongingNode1);
					if(transitionFactorsAssigned.contains(key1) || transitionFactorsAssigned.contains(key2)) continue;
					
					transitionFactorsAssigned.add(key1);
					Factor factor = new Factor();
					cliqueTreeNode.factors.add(factor);
					factor.factorType = FactorType.TRANSITION;
					factor.inGraphNode1 = belongingNode1;
					factor.inGraphNode2 = belongingNode2;
					
				}
			}
		}
	}

	private static String getKey(InGraphNode belongingNode1,
			InGraphNode belongingNode2) {
		return belongingNode1.getKey() + ":" + belongingNode2.getKey();
	}

	private static void assignOCRfactors(CliqueTree cliqueTree) {
		Set<String> ocrFactorsAssigned = new HashSet<String>();
		
		for (CliqueTreeNode cliqueTreeNode : cliqueTree.nodes) {
			Set<InGraphNode> belongingNodes = cliqueTreeNode.belongingNodes;
			for (InGraphNode inGraphNode : belongingNodes) {
				if(ocrFactorsAssigned.contains(inGraphNode.getKey())) continue;
				
				ocrFactorsAssigned.add(inGraphNode.getKey());
				
				Factor ocrFactor = new Factor();
				cliqueTreeNode.factors.add(ocrFactor);
				ocrFactor.factorType = FactorType.OCR;
				ocrFactor.inGraphNode1 = inGraphNode;
			}
		}
	}

	private static List<CliqueTree> makeCliqueTree_withoutFactors(
			InGraph inGraph, ModelType modelType) {
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
		kruskals(cliqueTreeEdges, cliqueTree.nodes);
		backEdgesArePresentAsWell(cliqueTree);
	}

	private static void backEdgesArePresentAsWell(CliqueTree cliqueTree) {
		for(int i=0;i<cliqueTree.nodes.size();i++) {
				CliqueTreeNode cliqueTreeNode = cliqueTree.nodes.get(i);
				for (CliqueTreeEdge cliqueTreeEdge : cliqueTreeNode.adjList) {
					if(!cliqueTreeEdge.isPresent) continue;
					CliqueTreeEdge backEdge = cliqueTreeEdge.getDest().getCliqueTreeEdge(cliqueTreeNode);
					backEdge.isPresent = true;
				}
		}
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
	
	private static int find(Map<Integer, Subset> subsets, int i)
	{
	    // find root and make root as parent of i (path compression)
	    if (subsets.get(i).parent != i)
	        subsets.get(i).parent = find(subsets, subsets.get(i).parent);
	 
	    return subsets.get(i).parent;
	}
	
	private static void union(Map<Integer, Subset> subsets, int x, int y)
	{
	    int xroot = find(subsets, x);
	    int yroot = find(subsets, y);
	 
	    if (subsets.get(xroot).rank < subsets.get(yroot).rank)
	        subsets.get(xroot).parent = yroot;
	    else if (subsets.get(xroot).rank > subsets.get(yroot).rank)
	        subsets.get(yroot).parent = xroot;
	 
	    else
	    {
	        subsets.get(yroot).parent = xroot;
	        subsets.get(xroot).rank++;
	    }
	}
	
	
	private static void kruskals(List<CliqueTreeEdge> cliqueTreeEdges, List<CliqueTreeNode> cliqueTreeNodes) {
		List<CliqueTreeEdge> spanningTreeEdges = new ArrayList<CliqueTreeEdge>();
		
		int maxEdgeWeight = getMaxEdgeWeight(cliqueTreeEdges);
		setEdgeWeight_maxMinus(cliqueTreeEdges, maxEdgeWeight);
		
		Collections.sort(cliqueTreeEdges);
		
		Map<Integer, Subset> subsets = new HashMap<Integer, Subset>();
	    
	    for (CliqueTreeNode cliqueTreeNode : cliqueTreeNodes) {
	    	Subset subset = new Subset();
			subsets.put(cliqueTreeNode.getNodeID(), subset);
			subset.parent = cliqueTreeNode.getNodeID();
			subset.rank = 0;
		}
		
		int edgePtr = 0;
		
		while(spanningTreeEdges.size() < cliqueTreeNodes.size()-1) {
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
