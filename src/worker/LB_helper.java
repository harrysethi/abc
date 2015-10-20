/**
 * 
 */
package worker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import constants.FactorType;
import constants.ModelType;
import domain.InGraph;
import domain.InGraphNode;
import domain.LB_factorNode;
import domain.LB_graph;
import domain.LB_variableNode;

/**
 * @author harinder
 *
 */
public class LB_helper {
	
	public static Map<InGraph, LB_graph> create_LB_graphs(List<InGraph> inGraphs, ModelType modelType) {
		Map<InGraph, LB_graph> lb_graph_map = new HashMap<InGraph, LB_graph>();
		
		for (InGraph inGraph : inGraphs) {
			LB_graph lb_Graph = make_LB_graph(inGraph, modelType);
			lb_graph_map.put(inGraph, lb_Graph);
		}
		
		
		return lb_graph_map;
	}
	
	private static LB_graph make_LB_graph(InGraph inGraph, ModelType modelType) {
		LB_graph lb_Graph = new LB_graph();
		
		add_LB_nodes(inGraph, lb_Graph, modelType);
		add_edges(lb_Graph);
		
		return lb_Graph;
	}
	
	private static void add_edges(LB_graph lb_graph) {
		for(LB_factorNode factorNode : lb_graph.factorNodes) {
			LB_variableNode lb_variableNode1 = factorNode.variableNode1;
			LB_variableNode lb_variableNode2 = factorNode.variableNode2;
			
			factorNode.addEdge(lb_variableNode1);
			lb_variableNode1.addEdge(factorNode);
			
			if(lb_variableNode2 != null) {
				factorNode.addEdge(lb_variableNode2);
				lb_variableNode2.addEdge(factorNode);
			}
		}
	}

	private static void add_LB_nodes(InGraph inGraph, LB_graph lb_Graph, ModelType modelType) {

		addVariable_n_ocrFactorNodes(inGraph, lb_Graph);
		
		switch (modelType) {
		case PAIR_SKIP_MODEL:
			addPairSkipFactorNodes(lb_Graph);
			//intentionally left "break"

		case SKIP_MODEL:
			addSkipFactorNodes(lb_Graph);
			//intentionally left "break"

		case TRANSITION_MODEL:
			addTransitionFactorNodes(lb_Graph);
			//intentionally left "break"

		case OCR_MODEL:
			//nothing required here
			break;
		}
		
	}

	private static void addPairSkipFactorNodes(LB_graph lb_Graph) {
		//pair-skip factor nodes
		for(int i=0;i<lb_Graph.variableNodes.size()-1;i++) {
			for(int j=i+1;j<lb_Graph.variableNodes.size();j++) {	
				LB_variableNode variableNode1 = lb_Graph.variableNodes.get(i);
				LB_variableNode variableNode2 = lb_Graph.variableNodes.get(j);
				
				if(variableNode1.inGraphNode.wordNumType == variableNode2.inGraphNode.wordNumType) continue;
				if(!variableNode1.inGraphNode.imgID.equals(variableNode2.inGraphNode.imgID)) continue;
				
				LB_factorNode lb_factorNode = new LB_factorNode(variableNode1, variableNode2, FactorType.PAIR_SKIP);
				lb_Graph.factorNodes.add(lb_factorNode);
			}
		}
	}

	private static void addSkipFactorNodes(LB_graph lb_Graph) {
		//skip factor nodes
		for(int i=0;i<lb_Graph.variableNodes.size()-1;i++) {
			for(int j=i+1;j<lb_Graph.variableNodes.size();j++) {
				LB_variableNode variableNode1 = lb_Graph.variableNodes.get(i);
				LB_variableNode variableNode2 = lb_Graph.variableNodes.get(j);
				
				if(variableNode1.inGraphNode.wordNumType != variableNode2.inGraphNode.wordNumType) continue;
				if(!variableNode1.inGraphNode.imgID.equals(variableNode2.inGraphNode.imgID)) continue;
				
				LB_factorNode lb_factorNode = new LB_factorNode(variableNode1, variableNode2, FactorType.SKIP);
				lb_Graph.factorNodes.add(lb_factorNode);
			}
		}
	}

	private static void addTransitionFactorNodes(LB_graph lb_Graph) {
		//transition factor nodes
		for(int i=0;i<lb_Graph.variableNodes.size()-1;i++) {
			LB_variableNode variableNode1 = lb_Graph.variableNodes.get(i);
			LB_variableNode variableNode2 = lb_Graph.variableNodes.get(i+1);
			
			if(variableNode1.inGraphNode.wordNumType != variableNode2.inGraphNode.wordNumType) continue;
			
			LB_factorNode lb_factorNode = new LB_factorNode(variableNode1, variableNode2, FactorType.TRANSITION);
			lb_Graph.factorNodes.add(lb_factorNode);
		}
	}

	private static void addVariable_n_ocrFactorNodes(InGraph inGraph,
			LB_graph lb_Graph) {
		//variable nodes and ocr factor nodes
		for(InGraphNode inGraphNode : inGraph.nodes_w1) {
			LB_variableNode lb_variableNode = new LB_variableNode(inGraphNode);
			lb_Graph.variableNodes.add(lb_variableNode);
			
			LB_factorNode lb_factorNode = new LB_factorNode(lb_variableNode);
			lb_Graph.factorNodes.add(lb_factorNode);
		}
		
		for(InGraphNode inGraphNode : inGraph.nodes_w2) {
			LB_variableNode lb_VariableNode = new LB_variableNode(inGraphNode);
			lb_Graph.variableNodes.add(lb_VariableNode);
			
			LB_factorNode lb_FactorNode = new LB_factorNode(lb_VariableNode);
			lb_Graph.factorNodes.add(lb_FactorNode);
		}
	}
	
/*	public static Map<Object, List<Object>> createEmptyPotential(List<LB_VariableNode> variables) {
		
		Map<Object, List<Object>> potentials = new HashMap<Object, List<Object>>();
		for (LB_VariableNode lb_VariableNode : variables) {
			List<Object> potentialList = new ArrayList<Object>();
			potentials.put(lb_VariableNode, potentialList);
		}
		
		List<Object> potentialList = new ArrayList<Object>();
		potentials.put("Value", potentialList);
		
		return potentials;
	}*/
}
