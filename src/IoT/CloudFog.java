package IoT;

import java.util.List;

public class CloudFog  
{
	private String Mac;
	private String IP;
	private int Port;
	private List<QoE> QoEs;
	private double TS;
	private String type;
	
	public double getTS() {
		return TS;
	}
	public void setTS(double tS) {
		TS = tS;
	}
	public void setQoE(List<QoE> qoE) {
		QoEs = qoE;
	}
	public String getMac() {
		return Mac;
	}
	public void setMac(String mac) {
		Mac = mac;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public int getPort() {
		return Port;
	}
	public void setPort(int port) {
		Port = port;
	}
	
	public List<QoE> getQoE() {
		return QoEs;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public CloudFog(String mac, String iP, int port, List<QoE> qoEs,
			 String type) {
		super();
		Mac = mac;
		IP = iP;
		Port = port;
		QoEs = qoEs;
		this.type = type;
	}

		
}
