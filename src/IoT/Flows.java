package IoT;

import java.io.*;
import java.util.LinkedList;

import IoT.FlowStructure;

public class Flows implements Serializable {
	private LinkedList<FlowStructure> currentflows;
	
	
public LinkedList<FlowStructure> getCurrentflows() {
		return currentflows;
	}

	public void setCurrentflows(LinkedList<FlowStructure> currentflows) {
		this.currentflows = currentflows;
	}

public Flows() {
	currentflows =new LinkedList<FlowStructure>();
	}


}
