package TableExporter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Creates a HTML file with a table from a given set of 2d array of Strings.
 * 
 * @author Christopher Birkbeck
 *
 */
public class HTMLGenerator extends ExportFileBuilder implements TableFileGenerator {
	
	/* Variables */
	
	// Title shown in the window.
	private String pageTitle = "New Table";
	// Title shown on the page.
	private String textTitle = "New Table";
	// Language of the page.
	private String language = "EN";
	// Character encoding of the page.
	private String charset = "UTF-8";
	// Choose single page to print.
	private boolean printSinglePage = false;
	// Index of single page to print.
	private int pageToPrint = 0;
	// Table for the item.
	private PrintableTable table;
	// Counter for the table.
	private int counter = 0;
	
	
	/* Constuctor */
	
	/**
	 * Sets the title of the page.
	 * 
	 * @param title
	 */
	public HTMLGenerator(PrintableTable table) {
		this.table = table;
	}
	
	/* Accessors */
	
	/**
	 * @return the pageTitle
	 */
	public String getPageTitle() {
		return pageTitle;
	}

	/**
	 * @param pageTitle the pageTitle to set
	 */
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * @param textTitle the textTitle to set
	 */
	public void setTextTitle(String textTitle) {
		this.textTitle = textTitle;
	}

	/**
	 * @return the textTitle
	 */
	public String getTextTitle() {
		return textTitle;
	}

	public boolean isPrintSinglePage() {
		return printSinglePage;
	}

	public void setPrintSinglePageOn(int index) {
		printSinglePage = true;
		
		if (index > 0 && index < table.getData().length) {
			pageToPrint = index;
		}
	}
	
	public void setPrintSinglePageOff(int index) {
		printSinglePage = false;
	}

	public int getPageToPrint() {
		return pageToPrint;
	}

	public void setPageToPrint(int pageToPrint) {
		if (pageToPrint > 0 && pageToPrint < table.getData().length) {
			this.pageToPrint = pageToPrint;
		}
	}

	/**
	 * Take a String a data and generates an entire string of web page.
	 * @param data
	 * @return
	 * @throws IOException 
	 */
	public void generateTableFile(String fileName) throws IOException {
		Queue<String> elements = new LinkedList<String>();
		
		pageTitle = fileName;
		textTitle = fileName;
		
		generateStart(elements, 0);
		
		if(this.table.getData().length == 1){printSinglePage = true;}
		
		// Create a navigation bar for multiple pages.
		if (table.getData().length > 1 || !printSinglePage) {
			generateTOC(elements);
			generateLegend(elements);
		}
		
		if (printSinglePage) {
			
			String[][] data = table.getData()[pageToPrint];
			generateTable(elements, data, table.getRows(), table.getColumns());
			
		} else {
			for (int i = 0; i < table.getData().length; i++) {
				String[][] data = table.getData()[i];
				
				generateTable(elements, data, table.getRows(), table.getColumns());
			}
		}
		
		generateEnd(elements);
		
		generateFile(elements, fileName, "html");
	}
	
	/**
	 * Make the first elements of the HTML page.
	 * 
	 * @param html
	 * @param index TODO
	 */
	private void generateStart(Queue<String> html, int index) {
		html.add("<!DOCTYPE html>\n");
		html.add("<html lang=" + language + ">\n");
		html.add("\t<head>\n");
		
		if (printSinglePage) {
			html.add("\t\t<title>" + table.getPageCategories().get(index) + "</title>\n");
		} else {
			html.add("\t\t<title>" + pageTitle + "</title>\n");
		}
		
		
		html.add("\t\t<meta charset=" + charset + ">\n");
		
		// Call method to generate CSS file.
		
		generateCSS(html);
		
		// End of internal style sheet.
		
		html.add("\t</head>\n");
		html.add("\t<body>\n");
		if (printSinglePage) {
			html.add("\t\t<center><h1>" + table.getPageCategories().get(index) + "</h1></center>\n");
		} else {
			html.add("\t\t<center><h1>" + textTitle + "</h1></center>\n");
		}
	}
	
	/**
	 * Helps generate the CSS portion of the HTML file.
	 * 
	 * @param html
	 */
	private void generateCSS(Queue<String> html) {
		// The following is an internal style sheet that cannot altered by the user.
		// This will be changed in future iterations.
				
		html.add("\t\t<style>\n");
		
		html.add("\t\t\tth, td, ul, caption, p {\n");
		html.add("\t\t\t\tfont-family: \"Arial\", Arial, sans-serif;\n");
		html.add("\t\t\t}\n");

		// Makes table heading green and the text white.
		html.add("\t\t\tth {\n");
		html.add("\t\t\t\tbackground-color: #4CAF50;\n");
		html.add("\t\t\t\tcolor: white;\n");
		html.add("\t\t\t}\n");

		// Adds a gray border at the bottom of the table rows.
		html.add("\t\t\ttd {\n");
		html.add("\t\t\t\tborder-bottom: 1px solid #ddd;\n");
		html.add("\t\t\t}\n");
		
		html.add("\t\t\t#scrollable {\n");
		html.add("\t\t\t\toverflow-x:auto;\n");
		html.add("\t\t\t}\n");
		
		html.add("\t\t\ttd#rowHeader {\n");
		html.add("\t\t\t\tbackground-color: #4CAF50;\n");
		html.add("\t\t\t\tcolor: white;\n");
		html.add("\t\t\t}\n");
		
		html.add("\t\t\tth#cornerCells {\n");
		html.add("\t\t\t\tbackground-color: #FFFFFF;\n");
		html.add("\t\t\t\tcolor: white;\n");
		html.add("\t\t\t}\n");
		
		html.add("\t\t\tul#contents {\n");
		html.add("\t\t\t\tlist-style-type: none;\n");
		html.add("\t\t\t\tmargin: 0;\n");
		html.add("\t\t\t\tpadding: 0;\n");
		html.add("\t\t\t\toverflow-x: auto;\n");
		html.add("\t\t\t\tbackground-color: #333;\n");
		html.add("\t\t\t\tposition: fixed;\n");
		html.add("\t\t\t\ttop: 0;\n");
		html.add("\t\t\t}\n");
		
		html.add("\t\t\tul#legend {\n");
		html.add("\t\t\t\tlist-style-type: none;\n");
		html.add("\t\t\t\tmargin: 0;\n");
		html.add("\t\t\t\tpadding: 0;\n");
		html.add("\t\t\t\toverflow: hidden;\n");
		html.add("\t\t\t\tbackground-color: #333;\n");
		html.add("\t\t\t\tposition: fixed;\n");
		html.add("\t\t\t\tbottom: 0;\n");
		html.add("\t\t\t\tright: 0;\n");
		html.add("\t\t\t}\n");
		
		html.add("\t\t\tli {\n");
		html.add("\t\t\t\tfloat: left;\n");
		html.add("\t\t\t}\n");
		
		html.add("\t\t\tli a {\n");
		html.add("\t\t\t\tdisplay: block;\n");
		html.add("\t\t\t\tcolor: white;\n");
		html.add("\t\t\t\ttext-align: center;\n");
		html.add("\t\t\t\tpadding: 14px 16px;\n");
		html.add("\t\t\t\ttext-decoration: none;\n");
		html.add("\t\t\t}\n");
		
		html.add("\t\t\tli#legendCategory {\n");
		html.add("\t\t\t\tdisplay: block;\n");
		html.add("\t\t\t\tcolor: white;\n");
		html.add("\t\t\t\ttext-align: center;\n");
		html.add("\t\t\t\tpadding: 14px 16px;\n");
		html.add("\t\t\t\ttext-decoration: none;\n");
		html.add("\t\t\t}\n");

		// Creates "zebra-striped" tables for each even row (not counting the header.
		html.add("\t\t\ttr:nth-child(even){background-color: #f2f2f2}\n");
		
		// Creates the CSS rules for making the file printable.
		html.add("\t\t\t@media print {\n");
		html.add("\t\t\t\tth, td, ul, caption, p {\n");
		html.add("\t\t\t\t\tfont-family: \"Times New Roman\", Times, serif;\n");
		html.add("\t\t\t\t}\n");
		html.add("\t\t\t\t#scrollable {\n");
		html.add("\t\t\t\t\toverflow-x:hidden;\n");
		html.add("\t\t\t\t}\n");
		html.add("\t\t\t\t#contents { visibility: hidden;}\n");
		html.add("\t\t\t\t#legend { visibility: hidden;}\n");
		html.add("\t\t\t}\n");

		// End of internal style sheet.
		html.add("\t\t</style>\n");
	}
	
	private void generateLegend(Queue<String> html) {
		html.add("\t\t<ul id=\"legend\">\n");
		html.add("\t\t\t<li id=\"legendCategory\">Column Categories:</li>\n");
		for (int i = 0; i < table.getColumnLabels().size(); i++) {
			html.add("\t\t\t<li id=\"legendCategory\">" + table.getColumnLabels().get(i) +  "</li>\n");
		}
		
		html.add("\t\t\t<li id=\"legendCategory\">Rows Categories:</li>\n");
		for (int i = 0; i < table.getRowLabels().size(); i++) {
			html.add("\t\t\t<li id=\"legendCategory\">" + table.getRowLabels().get(i) +  "</li>\n");
		}
		
		html.add("\t\t</ul>\n");
	}
	
	/**
	 * Generates the Table of contents.
	 * 
	 * @param html
	 */
	private void generateTOC(Queue<String> html) {
		html.add("\t\t<ul id=\"contents\">\n");
		
		LinkedList<String> pageNames = table.getPageCategories();
		
		for (int i = 0; i < pageNames.size(); i++) {
			html.add("\t\t\t<li><a href=\"#table" + i + "\">" + pageNames.get(i) + "</a></li>\n");
		}
		
		html.add("\t\t</ul>\n");
	}
	
	/**
	 * Take the base data and put into table with the proper HTML tags.
	 * 
	 * @param html
	 * @param source
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void generateTable(Queue<String> html, String[][] source, int rows, int columns) throws FileNotFoundException, IOException {
		// Makes an internal horizontal scroll bar if the page overflows.
		html.add("\t\t\t<div id=\"scrollable\">\n");
		// The table id is "table" + counter. Used for the anchor tags.
		html.add("\t\t\t<table id=\"table" + counter + "\">\n");
		
		
		
		if (!printSinglePage) {
			
			html.add("\t\t\t\t<caption>Table " + (counter + 1) + ". " + table.getPageCategories().get(counter) + "</caption>\n");
		
			
		}
		
		
		// Generate the table
		for (int i = 0; i < source.length; i++) {
			html.add("\t\t\t\t<tr>\n");
			for (int j = 0; j < source[i].length; j++) {
				// Makes the first row the header with the HTML tags <th>... </th>.
				// Otherwise, uses the regular <tr>...</tr>.
				// Additionally, this will look for repeated strings in columns and rows
				// and not print them.
				if (i < columns) {
					// Add nothing in case there is a null string.
					if (source[i][j] == null) {
						html.add("\t\t\t\t\t<th id=\"cornerCells\"></th>\n");
					} else {
						if (j > 0) {
							// If the label is a duplicate, do not add it.
							if (source[i][j - 1] != null && source[i][j - 1].equals(source[i][j])) {
								html.add("\t\t\t\t\t<th></th>\n");
							} else {
								html.add("\t\t\t\t\t<th>" + source[i][j] + "</th>\n");
							}
						} else {
							html.add("\t\t\t\t\t<th>" + source[i][j] + "</th>\n");
						}
					}
				} else {
					// Add nothing in case the column part of the second column.
					if (j < rows) {
						if (i > 0) {
							// If the label is a duplicate, do not add it.
							if (source[i - 1][j] != null && source[i - 1][j].equals(source[i][j])) {
								html.add("\t\t\t\t\t<td id=\"rowHeader\"></td>\n");
							} else {
								html.add("\t\t\t\t\t<td id=\"rowHeader\">" + source[i][j] + "</td>\n");
							}
						}
					} else {
						html.add("\t\t\t\t\t<td>" + source[i][j] + "</td>\n");
					}
				}
			}
			html.add("\t\t\t\t</tr>\n");
		}
		html.add("\t\t\t</table>\n");
		html.add("\t\t\t</div>\n");
		counter++;
	}
	
	/**
	 * Generates the final elements of the HTML page.
	 * 
	 * @param html
	 */
	private void generateEnd(Queue<String> html) {
		html.add("\t\t<p>Table generated on " + LocalDate.now() + " at " + LocalTime.now() + ".</p>\n");
		html.add("\t</body>\n");
		html.add("</html>\n");
	}


	
}
