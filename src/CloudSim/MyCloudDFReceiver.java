package CloudSim;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.joda.time.Interval;

import IoT.*;

public class MyCloudDFReceiver 
{
    static ServerSocket ss;
    static Socket s;
    static DataInputStream dis;
    DataOutputStream dos;
  int port;
    public  MyCloudDFReceiver(int Port){
    	try
        {
    		System.out.println("Cloud started");
            ss=new ServerSocket(Port);
            s=ss.accept();
            dis= new DataInputStream(s.getInputStream());
            CollectFromThing();

        }
        catch(Exception e)
        {
             System.out.println(e);
        }
    }
    
   
    public static void main (String as[])
    {
    	new MyCloudDFReceiver(10);
    }

    public static void CollectFromThing() throws IOException, ClassNotFoundException
    {File myObj=null;
    	try {
    	       myObj = new File("ThingToCloud.txt");
    	     myObj.createNewFile();
    	    } catch (IOException e) {
    	      System.out.println("An error occurred.");
    	      e.printStackTrace();
    	    }
         String str, s1; int i=0;
         do
         {
             str=dis.readUTF();
            FileWriter myWriter = new FileWriter("ThingToCloud.txt", true);
             myWriter.write(str+" \n");
            
                 myWriter.close();
     i++;
 		}while(!str.equals("end"));
         String MAC = Files.readAllLines(Paths.get("ThingToCloud.txt")).get(1);
         String interval= Files.readAllLines(Paths.get("ThingToCloud.txt")).get(3);
         if(interval.contains(" ")) {
        		interval=interval.replace(" ", "");
        	}
         Interval validity=Interval.parse(interval);
         DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");  
         LocalDateTime now = LocalDateTime.now(); 
         String date=dtf.format(now);
         myObj.renameTo(new File("Df_"+MAC+"_"+date+".txt"));
         PrintWriter writer = new PrintWriter("ThingToCloud.txt");
         writer.print("");
         writer.close();
         FlowStructure f= new FlowStructure("Df_"+MAC+"_"+date+".txt",MAC, now, i, validity);
         Flows Myflows =SerializeFlows.serializeDataIn();
         Myflows.getCurrentflows().add(f);
         SerializeFlows.serializeDataOut(Myflows);

    }
}