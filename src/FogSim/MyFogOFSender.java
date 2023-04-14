package FogSim;

import java.io.*;
import java.net.*;
import IoT.*;

public class MyFogOFSender  {
	ServerSocket ss;
	static Socket s;
	DataInputStream dis;
	static DataOutputStream dos;
	
	public MyFogOFSender(String IP, int Port, FlowStructure f) {
		try {
			s = new Socket(IP, Port);
			System.out.println(s);
			dos = new DataOutputStream(s.getOutputStream());
			SendFromFog(f);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static String getMac() {
		String Mac = "";
		InetAddress ip;
		try {

			ip = InetAddress.getLocalHost();

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);

			byte[] mac = network.getHardwareAddress();

			Mac = mac.toString();

		} catch (UnknownHostException e) {

			e.printStackTrace();

		} catch (SocketException e) {

			e.printStackTrace();
		}
		return Mac;
	}
	public static void SendFromFog( FlowStructure f ) throws IOException, ClassNotFoundException {
		String s1;
		String Mac=getMac();
			s1 = readFile(f.getFlowName());
			dos.writeUTF(Mac);
			dos.flush();
			dos.writeUTF(s1);
			dos.flush();

	}

		private static String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}

	

}