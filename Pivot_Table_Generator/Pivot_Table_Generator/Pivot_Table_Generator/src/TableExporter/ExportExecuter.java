package TableExporter;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExportExecuter {
	
	TableFileGenerator strategy;
	String fileName;
	PrintableTable table;
	int selection;
	

	public ExportExecuter(int selection, String fileName, PrintableTable table){
		
		this.selection = selection;
		this.fileName = fileName;
		this.table = table;
		
		if(selection == 1){
			strategy = new CSVGenerator(table);
		}else if(selection == 0){
			strategy = new HTMLGenerator(table);
		}
		
		
		
		
	}
	
	
	public void export() throws FileNotFoundException, IOException{
		
		strategy.generateTableFile(fileName);
		
		if(selection == 0){
		 String pass = fileName+".html";
         try {
             Open.openSesame(pass);
         } catch (IOException e1) {
             // TODO Auto-generated catch block
             e1.printStackTrace();
         }
		}else if(selection == 1){
			String pass = fileName+".csv";
	         try {
	             Open.openSesame(pass);
	         } catch (IOException e1) {
	             // TODO Auto-generated catch block
	             e1.printStackTrace();
	         }
		}
		
	}
	
	
	
	static class Open {


	    public static void openSesame(String filePath) throws IOException {
	        //text file, should be opening in default text editor

	        File file = new File(filePath);

	        //first check if Desktop is supported by Platform or not
	        if (!Desktop.isDesktopSupported()) {
	            System.out.println("Desktop is not supported");
	            return;
	        }
	        Desktop desktop = Desktop.getDesktop();
	        if (file.exists()) {
	            desktop.open(file);
	        }
	        //done.
	    }// END OF OPENSESAME
	
	
	
	}
	
}
