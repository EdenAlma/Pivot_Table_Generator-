package TableExporter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Queue;

/**
 * Class implements the writing of text files from a Queue of Strings.
 * 
 * @author Christopher Birkbeck
 *
 */
public class ExportFileBuilder {
	
	/**
	 * Outputs a file of a given type.
	 * 
	 * @param elements A Queue of strings that will make up the document.
	 * @param fileName The name of file as selected by the user.
	 * @param fileType The file extension name (e.g. csv, html, xlxs, etc.)
	 * @throws FileNotFoundException
	 */
	protected void generateFile(Queue<String> elements, String fileName, String fileType) throws FileNotFoundException {
		
                PrintWriter stream = new PrintWriter(new FileOutputStream(fileName + "." + fileType));
		
		while (!elements.isEmpty()) {
			String element = elements.remove();
			stream.print(element);
		}
		
		stream.flush();
		stream.close();
	}
	

}
