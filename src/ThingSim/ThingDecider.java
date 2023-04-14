package ThingSim;

import java.io.IOException;
import java.util.ArrayList;

import IoT.CloudFog;
import IoT.EcoSystem;
import IoT.ReadExcelFileDemo;

public class ThingDecider {
	public static void main(String args[]) throws IOException{
		 ArrayList<Double> QoS =new ArrayList<Double>();
	QoS=ReadExcelFileDemo.GetQoSs();

		 ArrayList<CloudFog> devices =new ArrayList<CloudFog>();
		 devices=EcoSystem.getStorage();
		 
		CloudFog storage=devices.get((int) (Math.random()*(devices.size())));
		
	
		new MyThing(storage.getMac(),    storage.getIP(),storage.getPort());
	

	

	}
	}


