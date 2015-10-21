package domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LB_variableNode extends LB_baseNode {
	public InGraphNode inGraphNode;
	
	public Map<Object, List<Object>> potentials;
	
	public LB_variableNode(InGraphNode inGraphNode) {
		this.inGraphNode = inGraphNode;
		
		this.potentials = new HashMap<Object, List<Object>>();
		//FactorHelper.createFactorProduct(potentials, inGraphNode, 1.0);;
	}
	
	@Override
	public String toString() {
		return this.inGraphNode.toString();
	}
}
