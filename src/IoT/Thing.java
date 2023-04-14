package IoT;

public class Thing {
	private String tMac;
	private String tIP;
	private int tPort;
	public String gettMac() {
		return tMac;
	}
	
	public static int getleg(){
		return 1;
	}
	public void settMac(String tMac) {
		this.tMac = tMac;
	}
	public String gettIP() {
		return tIP;
	}
	public void settIP(String tIP) {
		this.tIP = tIP;
	}
	public int gettPort() {
		return tPort;
	}
	public void settPort(int tPort) {
		this.tPort = tPort;
	}
	public Thing(String tMac, String tIP, int tPort) {
		super();
		this.tMac = tMac;
		this.tIP = tIP;
		this.tPort = tPort;
	}
	
}
