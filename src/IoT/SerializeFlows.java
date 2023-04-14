package IoT;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeFlows {
	public static void serializeDataOut(Flows ish)throws IOException{
	    String fileName= "MyFlows.txt";
	    FileOutputStream fos = new FileOutputStream(fileName);
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(ish);
	    oos.close();
	    fos.close();
	}

	public static Flows serializeDataIn() throws IOException, ClassNotFoundException{
	   String fileName= "MyFlows.txt";
	   FileInputStream fin;
	   Flows flow=null;
	try {
		fin = new FileInputStream(fileName);

	   ObjectInputStream ois = new ObjectInputStream(fin);
	   flow= (Flows) ois.readObject();
	   ois.close();
	   fin.close();
	} catch (FileNotFoundException e) {
	flow=new Flows();
	e.printStackTrace();
	}
	   return flow;

	}
}
