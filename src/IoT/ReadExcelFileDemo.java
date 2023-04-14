package IoT;
import java.io.File;  
import java.io.FileInputStream;  
import java.util.ArrayList;
import java.util.Iterator;  
import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.xssf.usermodel.XSSFSheet;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  
public class ReadExcelFileDemo    
{  
public static ArrayList<Double> GetQoSs()   
{  
	 ArrayList<Double> QoS =new ArrayList<Double>();

try  
{  

File file = new File(".............................");     
FileInputStream fis = new FileInputStream(file);
XSSFWorkbook wb = new XSSFWorkbook(fis);   
XSSFSheet sheet = wb.getSheetAt(0);      
Row row;
String cellValueMaybeNull;
for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
	  row = sheet.getRow(rowIndex);
	  if (row != null) {
	    Cell cell = row.getCell(3);
	    if (cell != null) {
	      cellValueMaybeNull = String.valueOf(cell.getNumericCellValue());
QoS.add(Double.parseDouble(cellValueMaybeNull));

	    }
	  }
	}
}  
catch(Exception e)  
{  
e.printStackTrace();  
}  
return QoS;

}
public static void main(String args[]){
	 ArrayList<Double> QoS =new ArrayList<Double>();
QoS=ReadExcelFileDemo.GetQoSs();
}
}