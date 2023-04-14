package CloudSim;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyCloudOFReceiver 
{
    static ServerSocket ss;
    static Socket s;
    static DataInputStream dis;
    DataOutputStream dos;
  int port;
    public  MyCloudOFReceiver(int Port){
    	try
        {
			System.out.println("Cloud started");

            ss=new ServerSocket(Port);
            s=ss.accept();
            dis= new DataInputStream(s.getInputStream());
            Collect();
        }
        catch(Exception e)
        {
             System.out.println(e);
        }
    }
    public static void Collect() throws IOException, ClassNotFoundException
    {File myObj =null;
    	try {
    	       myObj = new File("ThingViaCloudToCloud.txt");
    	     myObj.createNewFile();
    	    } catch (IOException e) {
    	      System.out.println("An error occurred.");
    	      e.printStackTrace();
    	    }
         String str, s1; int i=0;
         do
         {
             str=dis.readUTF();
            FileWriter myWriter = new FileWriter("ThingViaCloudToCloud.txt", true);
             myWriter.write(str+" \n");
            
                 myWriter.close();
      i++;

 		} while(!str.contains("end"));
    		
         String MAC = Files.readAllLines(Paths.get("ThingViaCloudToCloud.txt")).get(2);
 		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");  
 		LocalDateTime now = LocalDateTime.now(); 
 		String date=dtf.format(now);
 		myObj.renameTo(new File("Of_"+MAC+"_"+date+".txt"));
 		PrintWriter writer = new PrintWriter("ThingViaCloudToCloud.txt");
 		writer.print("");
 		writer.close();
 		
 		
	}
   
    public static void main (String as[])
    {    	new MyCloudOFReceiver(13);

    }


}