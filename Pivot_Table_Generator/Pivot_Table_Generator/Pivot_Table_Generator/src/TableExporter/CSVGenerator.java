package TableExporter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class CSVGenerator extends ExportFileBuilder implements TableFileGenerator {
	
	/* Variables */

	// Table for the item.
	private PrintableTable table;
	// Schema for the item.

	
	public CSVGenerator(PrintableTable table) {
		this.table = table;
	}
	
	/**
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void generateTableFile(String fileName) throws FileNotFoundException {
		if (table == null) {
			return;
		}
		
		Queue<String> elements = new LinkedList<String>();
		
		String[][][] csv = table.getData();
		
		for (int i = 0; i < csv.length; i++) {
			for (int j = 0; j < csv[i].length; j++) {
				for (int k = 0; k < csv[i][j].length; k++) {
					String next = csv[i][j][k];
					
					if (next == null) {
						next = "";
					} else {
						// Replace any 'naked' commas with escaped commas.
						next = next.replaceAll(",", "\",\"");
					}
					
					elements.add(next + ",");
				}
				
				// End each row with end of line character.
				elements.add("\n");
			}
			
			// End each table with two end of line characters.
			elements.add("\n\n");
		}
		
		generateFile(elements, fileName, "csv");
	}

	public void generateSinglePage(String fileName) throws FileNotFoundException, IOException {
		if (table == null) {
			return;
		}
		
		Queue<String> elements = new LinkedList<String>();
		
		String[][][] csv = table.getData();
		
		for (int j = 0; j < csv[0].length; j++) {
			for (int k = 0; k < csv[0][j].length; k++) {
				String next = csv[0][j][k];
				
				if (next == null) {
					next = "";
				} else {
					// Replace any 'naked' commas with escaped commas.
					next = next.replaceAll(",", "\",\"");
				}
				
				elements.add(next + ",");
			}
			
			// End each row with end of line character.
			elements.add("\n");
		}
		
		generateFile(elements, fileName, "csv");
	}

}
