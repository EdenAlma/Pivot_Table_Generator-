package TableExporter;
import java.util.LinkedList;

public class PrintableTable {
	
	/* Variables */
	
	// The 3d array of strings. The order is pages, rows and columns.
	private String[][][] data;
	// Number of row categories.
	private int rows = 1;
	// Number of column categories.
	private int columns = 1;
	// Labels of rows.
	private LinkedList<String> rowLabels = new LinkedList<String>();
	// Labels of columns.
	private LinkedList<String> columnLabels = new LinkedList<String>();
	// Page categories.
	private LinkedList<String> pageCategories = new LinkedList<String>();

	/* Constuctors */

	public PrintableTable(String[][][] data, LinkedList<String> rowLabels, LinkedList<String> columnLabels, LinkedList<String> pageCategories) {
		this.data = data;
		this.rowLabels = rowLabels;
		rows = rowLabels.size();
		this.columnLabels = columnLabels;
		columns = columnLabels.size();
		this.pageCategories = pageCategories;
	}
	
	/* Accessors */

	/**
	 * @return the data
	 */
	public String[][][] getData() {
		return data;
	}

	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @return the columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * @return the rowLabels
	 */
	public LinkedList<String> getRowLabels() {
		return rowLabels;
	}
	
	/**
	 * 
	 * @param index
	 * @param label
	 * @throws Exception
	 */
	public void changeRowLabel(int index, String label) throws Exception {
		if (index > 0 && index < rowLabels.size()) {
			rowLabels.set(index, label);
		} else {
			throw new Exception();
		}
	}

	/**
	 * @return the columnLabels
	 */
	public LinkedList<String> getColumnLabels() {
		return columnLabels;
	}
	
	/**
	 * 
	 * @param index
	 * @param label
	 * @throws Exception
	 */
	public void changeColumnLabels(int index, String label) throws Exception {
		if (index > 0 && index < columnLabels.size()) {
			columnLabels.set(index, label);
		} else {
			throw new Exception();
		}
	}
	
	/**
	 * @return the pageCategories
	 */
	public LinkedList<String> getPageCategories() {
		return pageCategories;
	}
}
