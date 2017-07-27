package TableExporter;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface TableFileGenerator {
	
	public void generateTableFile(String fileName) throws FileNotFoundException, IOException;
	
	
}
