package ThingSim;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;


public class MyThing {
	
    static Socket s;
    static DataOutputStream dout;
   
    public MyThing(String MAC, String IP, int Port)
    {
         try
         {
        	 

     		System.out.println("Thing started");

             s=new Socket(IP,Port);
             System.out.println(s);
             dout= new DataOutputStream(s.getOutputStream());
             SentFromThing(MAC, IP, Port);

         }
         catch(Exception e)
         {
             System.out.println(e);
         }
     }
     public static void SentFromThing(String MAC, String IP, int Port) throws IOException
     {
    	 String s1;
    	 DateTime startDate = new DateTime();
         DateTime endDate = startDate.plus(Days.days(2));
    	 Interval t= new Interval(startDate,endDate);
    	 dout.writeUTF("Metadata: source=");
    	 dout.writeUTF(MAC);
    	 dout.writeUTF("validity interval=");
    	 dout.writeUTF(t.toString());
 		s1=readFile(".................");
 		
 		dout.writeUTF(s1);
 		dout.flush();
           dout.writeUTF("end");
           dout.flush();
    }
     private static String readFile(String file) throws IOException {
 	    BufferedReader reader = new BufferedReader(new FileReader (file));
 	    String         line = null;
 	    StringBuilder  stringBuilder = new StringBuilder();
 	    String         ls = System.getProperty("line.separator");

 	    try {
 	        while((line = reader.readLine()) != null) {
 	            stringBuilder.append(line);
 	            stringBuilder.append(ls);
 	        }

 	        return stringBuilder.toString();
 	    } finally {
 	        reader.close();
 	    }
 	}
   
}