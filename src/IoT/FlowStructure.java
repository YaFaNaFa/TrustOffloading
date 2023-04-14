package IoT;

import org.joda.time.*;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FlowStructure implements Serializable, Comparable<FlowStructure> {
	String flowName;
	String source;
	LocalDateTime receptiontime;
	int nbline;
	Interval validity;
	CloudFog Recipient;
	  public int compareTo(FlowStructure o) {
	    return getValidity().getStart().compareTo(o.getValidity().getStart());
	  }
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public LocalDateTime getReceptiontime() {
		return receptiontime;
	}
	public void setReceptiontime(LocalDateTime receptiontime) {
		this.receptiontime = receptiontime;
	}
	public int getNbline() {
		return nbline;
	}
	public void setNbline(int nbline) {
		this.nbline = nbline;
	}
	public Interval getValidity() {
		return validity;
	}
	public void setValidity(Interval validity) {
		this.validity = validity;
	}
	
	public CloudFog getRecipient() {
		return Recipient;
	}
	public void setRecipient(CloudFog receipient) {
		this.Recipient = receipient;
	}
	public FlowStructure(String flowName, String source, LocalDateTime receptiontime, int nbline, Interval validity) {
		super();
		this.flowName = flowName;
		this.source = source;
		this.receptiontime = receptiontime;
		this.nbline = nbline;
		this.validity = validity;
		this.Recipient=null;
	}
	

}
