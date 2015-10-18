/**
 * 
 */
package domain;

import java.util.ArrayList;
import java.util.List;

import constants.WordNumType;

/**
 * @author harinder
 *
 */
public class InGraphNode {
	//private static int nodeCounter;
	
	public WordNumType wordNumType;
	public int nodeID;
	public String imgID;
	public List<InGraphNodeAdjacency> adjList;
	public boolean isActive;
	
	private String key;
	
	//public boolean isOCRfactorAssigned;
	
	public InGraphNode(int nodeID, String imgID, WordNumType wordNumType){
		//InGraphNode.nodeCounter++;
		//this.nodeId = InGraphNode.nodeCounter;
		this.nodeID = nodeID;
		this.imgID = imgID; 
		this.wordNumType = wordNumType;
		this.adjList = new ArrayList<InGraphNodeAdjacency>();
		this.isActive = true;
		
		this.key = this.nodeID + "<=>" + this.wordNumType;
		
		//this.isOCRfactorAssigned = false;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public boolean isAdjacent(InGraphNode inGraphNode) {
		for (InGraphNodeAdjacency inGraphNodeAdjacency : adjList) {
			if(inGraphNodeAdjacency.inGraphNode.nodeID == inGraphNode.nodeID) return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.nodeID + ":" + this.imgID + "|" + this.wordNumType + "|" + this.isActive;
	}
}
