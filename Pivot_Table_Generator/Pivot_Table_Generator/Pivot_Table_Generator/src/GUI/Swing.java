package GUI;

import static GUI.pivotDataSource.locaklInputFileName;
import TableExporter.ExportExecuter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import TableMaker.RawDataReader;
import TableMaker.TableMaker;
import TableExporter.PrintableTable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
// @Author: GroupC, Apr.16.2017
public class Swing extends javax.swing.JFrame {

    public static String[][][] threeDimArr = {{{"1", "2"}, {"3", "4"}}, {{"5", "6"}, {"7", "8"}}};
    public static String[][] pivotTable2D_Data = new String[0][0];
    public static String[] tableColTitle  = new String[0];
    public String[] pageTitle = {"-select page-"};
    public static String[] fileType = {"html", "csv"};
    public static String[] aggregation = {"Sum", "Count", "Average"};
    public static String rowDisplay = "";
    public static String colDisplay = "";
    public static String userNameDisplay = pivotLogin.userNameOK;
    public String outputName = "";
    public String rowSchema = "";
    public String columnSchema = "";
    public String pageSchema = "";
    public String[] pageOptions;
    public static String dataSource = "";
    public static String inputName = "";
    static int[] fieldSelection = new int[0];
    static int[] rowSelection = new int[0];
    static int[] colSelection = new int[0];
    static int[] summableFields = new int[0];
    static int pageSelection = -1;
    static int fileTypeSelection = 0;
    static int sumSelection = -1;
    static int avgSelection = -1;
    static JCheckBox cb_row[] = new JCheckBox[0];
    static JCheckBox cb_col[] = new JCheckBox[0];
    static JRadioButton rb_page[] =new JRadioButton[0];
    List<Integer> rowArray = new ArrayList<Integer>();
    List<Integer> colArray = new ArrayList<Integer>();
    List<String> pageTitleArray = new ArrayList<String>();
    
    /**
     * Creates new form pivotUI
     */
    public Swing() {
        initComponents();
        inputName = pivotDataSource.inputFileName;
        // if there is no input file, default input file here:
        if (inputName == null) {
            inputName = "MOCK_TXT.csv";
        }
        // create page titles
        tableColTitle = RawDataReader.readFields(inputName);
        //passing initial data into this UI
        fetchData();
    }

    //passing initial data into this UI
    public void fetchData() {
        // denote User
        jLabel15.setText(userNameDisplay);
        // denote data source by dataSource UI
        jLabel3.setText(dataSource);
        // create pages, row and column tabs
        jTabbedPane1.addTab("Rows", jScrollPane3);
        jTabbedPane1.addTab("Columns", jScrollPane4);
        // dynamically generate check boxes in the page,row/column tabs        
        JCheckBox cb_row[] = new JCheckBox[tableColTitle.length];
        JCheckBox cb_col[] = new JCheckBox[tableColTitle.length];
        // create check boxes for row fields tabs
        for (int i = 0; i < tableColTitle.length; i++) {
            cb_row[i] = new JCheckBox(tableColTitle[i]);
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cb_row[i])
            );
        }
        // create check boxes for column fields tabs
        for (int i = 0; i < tableColTitle.length; i++) {
            cb_col[i] = new JCheckBox(tableColTitle[i]);
            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3Layout.setHorizontalGroup(
                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cb_col[i])
            );
        }
        JRadioButton rb_page[] = new JRadioButton[tableColTitle.length];
        // create JRadioButton for page tabs
        for (int i = 0; i < tableColTitle.length; i++) {
            rb_page[i] = new JRadioButton(tableColTitle[i]);
            buttonGroup1.add(rb_page[i]);
            jPanel8.add(rb_page[i]);
        }
        // jCheckBox event for page, row and column fields displaying
        for (int i = 0; i < tableColTitle.length; i++) {

            cb_row[i].addActionListener(new ActionListener() {
                @Override

                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < tableColTitle.length; i++) {
                        if (cb_row[i].isSelected() && !rowArray.contains(i)) {
                            rowArray.add(i);
                        } else if (!cb_row[i].isSelected() && rowArray.contains(i)) {
                            rowArray.remove(Integer.valueOf(i));
                        }
                    }
                    rowSelection = new int[rowArray.size()];

                    for (int j = 0; j < rowArray.size(); j++) {
                        rowSelection[j] = rowArray.get(j).intValue();
                    }
                }
            });

            cb_col[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < tableColTitle.length; i++) {
                        if (cb_col[i].isSelected() && !colArray.contains(i)) {
                            colArray.add(i);
                        } else if (!cb_col[i].isSelected() && colArray.contains(i)) {
                            colArray.remove(Integer.valueOf(i));
                        }
                    }
                    colSelection = new int[colArray.size()];
                    for (int j = 0; j < colArray.size(); j++) {
                        colSelection[j] = colArray.get(j).intValue();
                    }
                }
            });
            // create mutually exclusice button
            rb_page[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < tableColTitle.length; i++) {
                        if (rb_page[i].isSelected()) {
                            pageTitleArray.clear();
                            pageSelection = i;
                            // update pageSchema
                            pageSchema = tableColTitle[i];
                            TableMaker pivotPageTree = new TableMaker(RawDataReader.loadRawRecords(inputName), rowSelection, colSelection, sumSelection, avgSelection, pageSelection);
                            pageOptions = pivotPageTree.getPageNames();

                            if (pageOptions != null) {
                                for (int j = 0; j < pageOptions.length; j++) {
                                    pageTitleArray.add(pageOptions[j]);
                                }
                                String[] pageNameArray = new String[pageTitleArray.size()];
                                for (int j = 0; j < pageTitleArray.size(); j++) {
                                    pageNameArray[j] = pageTitleArray.get(j);
                                }
                                // Initialize pages function in the drop-down box
                                DefaultComboBoxModel model = new DefaultComboBoxModel(pageNameArray);
                                jComboBox1.setModel(model);
                            }
                        }

                    }
                }
            });
        }
        // Initialize aggregation function
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(aggregation));
        // Initialize output file type selection function
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(fileType));
        // Set output file type selection function
        jComboBox3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                fileTypeSelection = jComboBox3.getSelectedIndex();
            }
        });
        // Initialize summable fields for aggregation function
        int[] test_row = {0};
        int[] test_col = {0};
        TableMaker pivotPageTree = new TableMaker(RawDataReader.loadRawRecords(inputName), test_row, test_col, -1, -1, -1);
        summableFields = pivotPageTree.getNumberFields();
        String[] summableFieldNames = new String[summableFields.length];
        for (int i = 0; i < summableFields.length; i++) {
            summableFieldNames[i] = tableColTitle[summableFields[i]];
        }
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(summableFieldNames));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton4 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pivot Table Viewer");
        setLocation(new java.awt.Point(0, 0));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
                },
                new String[]{
                    "", "", "", ""
                }
        ));
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.setDragEnabled(true);
        jScrollPane1.setViewportView(jTable1);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("Selected rows:");

        jLabel8.setText("Selected columns:");

        jLabel9.setText("row fields");

        jLabel10.setText("column fields");

        jLabel13.setText("Table Schema Name:");

        jTextField2.setText("Type a schema name you want to save");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel14.setText("Hi!");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel15.setText("User");

        jLabel12.setText("Save table schema:");

        jButton5.setText("Save");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Load");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel18.setText("Load table schema:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel13)
                                .addComponent(jLabel8)
                                .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton6)
                                        .addContainerGap())
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel15)
                                        .addGap(48, 48, 48))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                        .addComponent(jLabel10)
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        .addComponent(jLabel12)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap())))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{jButton5, jButton6});

        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel1)
                                                .addComponent(jLabel9)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel14)
                                        .addComponent(jLabel15)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(jLabel10))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel12)
                                .addComponent(jButton5))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton6)
                                .addComponent(jLabel18))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("View Table");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("File Export");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    jButton2ActionPerformed(evt);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Swing.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Choose output file type:");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Press button to preview:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Type name for output:");

        jTextField1.setText("type here");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText(":Pages:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboBox1.setPreferredSize(new java.awt.Dimension(65, 21));

        jButton4.setText("Clear");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel17.setText("De-select page:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton1)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{jButton1, jButton2, jComboBox1, jComboBox3, jTextField1});

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{jLabel2, jLabel4, jLabel5});

        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton4)
                                .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[]{jButton1, jButton2, jButton4, jComboBox1, jComboBox3, jTextField1});

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setPreferredSize(new java.awt.Dimension(160, 70));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboBox4.setPreferredSize(new java.awt.Dimension(83, 23));

        jLabel6.setText("Aggregation:");

        jLabel16.setText("Aggregation Fields:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel16)
                                .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setPreferredSize(new java.awt.Dimension(160, 70));

        jLabel7.setText("Data Source:");

        jButton3.setText("Change data source");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addGap(49, 49, 49))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addContainerGap(14, Short.MAX_VALUE))
        );

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTabbedPane1.setToolTipText("select row fields");
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setFocusTraversalPolicy(jPanel1.getFocusTraversalPolicy());
        jTabbedPane1.setNextFocusableComponent(jPanel8);
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(60, 71));

        jPanel1.setFocusCycleRoot(true);
        jPanel1.setNextFocusableComponent(jPanel3);
        jScrollPane3.setViewportView(jPanel1);

        jTabbedPane1.addTab("Rows", jScrollPane3);

        jPanel8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.addTab("Page", jPanel8);

        jScrollPane4.setViewportView(jPanel3);

        jTabbedPane1.addTab("Columns", jScrollPane4);

        jTabbedPane1.setSelectedIndex(1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) throws FileNotFoundException {
        // Set "file export" button
        // create 3D data array for displaying on the screen
        TableMaker pivotPageTree = new TableMaker(RawDataReader.loadRawRecords(inputName), rowSelection, colSelection, sumSelection, avgSelection, pageSelection);
        String[][][] pivotPageTree_3D_array = pivotPageTree.getOutArray();
        String[] pageNames = pivotPageTree.getPageNames();
        LinkedList<String> rowNames = new LinkedList<String>();
        for (int i = 0; i < rowSelection.length; i++) {
            rowNames.add(tableColTitle[rowSelection[i]]);
        }

        LinkedList<String> columnNames = new LinkedList<String>();
        for (int i = 0; i < colSelection.length; i++) {
            columnNames.add(tableColTitle[colSelection[i]]);
        }
        LinkedList<String> pageCategories = new LinkedList<String>();
        if (pageNames != null) {
            for (int i = 0; i < pageNames.length; i++) {
                pageCategories.add(pageNames[i]);
            }
        } else {
            pageCategories.add("city");
        }

        PrintableTable printable = new PrintableTable(pivotPageTree_3D_array, rowNames, columnNames, pageCategories);

        ExportExecuter exporter = new ExportExecuter(fileTypeSelection, jTextField1.getText(), printable);

        try {
            exporter.export();
        } catch (IOException e) {
            System.out.println("Export has failed to occuer!");
            e.printStackTrace();
        }
    }

    // Set "Change data source" button
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame dataSourceFrame = new pivotDataSource();
        dataSourceFrame.pack();
        dataSourceFrame.setLocationRelativeTo(null);
        dataSourceFrame.setVisible(true);
        //new pivotDataSource().setVisible(true);
        dispose();
    }

    // Set "view table" button
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {

        // Check row selection along with jCheckBox
        List<Integer> fieldArray = new ArrayList<Integer>();
        StringBuilder rowBuilder = new StringBuilder();
        for (int i = 0; i < rowSelection.length; i++) {
            fieldArray.add(rowSelection[i]);
            rowBuilder.append(tableColTitle[rowSelection[i]]);
            rowBuilder.append(", ");
        }
        // Display row selection on the upper part of table
        jLabel9.setText(rowBuilder.toString());
        rowSchema = rowBuilder.toString();
        // Check column selection along with jCheckBox
        StringBuilder colBuilder = new StringBuilder();
        for (int i = 0; i < colSelection.length; i++) {
            fieldArray.add(colSelection[i]);
            colBuilder.append(tableColTitle[colSelection[i]]);
            colBuilder.append(", ");
        }
        // Display row selection on the upper part of table
        jLabel10.setText(colBuilder.toString());
        columnSchema = colBuilder.toString();

        // Set "fieldSelection" for generating 2D string
        fieldSelection = new int[fieldArray.size()];
        for (int j = 0; j < fieldArray.size(); j++) {
            fieldSelection[j] = fieldArray.get(j);
        }
        // Set aggregation function from user slecltion 
        // aggregation-Sum
        if (jComboBox4.getSelectedIndex() == 0) {
            sumSelection = summableFields[jComboBox2.getSelectedIndex()];
            avgSelection = -1;
            // aggregation-Count
        } else if (jComboBox4.getSelectedIndex() == 1) {
            sumSelection = -1;
            avgSelection = -1;
            // aggregation-Average
        } else if (jComboBox4.getSelectedIndex() == 2) {
            sumSelection = -1;
            avgSelection = summableFields[jComboBox2.getSelectedIndex()];
        }
        // create 3D data array for displaying on the screen
        TableMaker pivotPageTree = new TableMaker(RawDataReader.loadRawRecords(inputName), rowSelection, colSelection, sumSelection, avgSelection, pageSelection);
        String[][][] pivotPageTree_3D_array = pivotPageTree.getOutArray();
        // if there is no pageSelection, pageSelection defaults to -1
        if (pageSelection < 0) {
            pivotTable2D_Data = pivotPageTree_3D_array[0];
        } else {
            // if there is a pageSelection, pageSelection is set to user selection
            pivotTable2D_Data = pivotPageTree_3D_array[jComboBox1.getSelectedIndex()];
        }
        // "pivotTable2D_Data" is the comman varible for displaying 2D string array data
        String[] tableColTitle3 = new String[pivotTable2D_Data[0].length];
        jTable1.setModel(new javax.swing.table.DefaultTableModel(pivotTable2D_Data, tableColTitle3));
        // set data of pivot table cells editable
        jTable1.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent evt) {
                // here goes the code "on cell update"
                pivotTable2D_Data[jTable1.getSelectedRow()][jTable1.getSelectedColumn()] = jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()).toString();
            }
        });
    }

    // Save table schema
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String userHome = System.getProperty("user.home");  //locate user root
            File file = new File(userHome + "/Desktop", jTextField2.getText() + ".txt");    //save file in Desktop folder
            FileWriter writer1 = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(writer1);
            writer.write(LocalDate.now() + ", Table schema: " + jTextField2.getText());
            writer.newLine();
            if (dataSource.equals("database")) {
                writer.write("data source: " + dataSource);
                writer.newLine();
            } else {
                writer.write("Source file:" + locaklInputFileName);
                writer.newLine();
            }
            writer.write("Selected rows," + rowSchema);
            writer.newLine();
            writer.write("Row index," + Arrays.toString(rowSelection).replace("[", "").replace("]", "").replaceAll("\\s", ""));
            writer.newLine();
            writer.write("Selected columns," + columnSchema);
            writer.newLine();
            writer.write("Column index," + Arrays.toString(colSelection).replace("[", "").replace("]", "").replaceAll("\\s", ""));
            writer.newLine();
            writer.write("Selected page," + pageSchema);
            writer.newLine();
            writer.write("Page index," + pageSelection);
            writer.newLine();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(pivotRegister.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // "De-select page" button
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {

        buttonGroup1.clearSelection();
        pageSelection = -1;
        String[] pageTitle1 = {"-select page-"};
        jComboBox1.removeAll();
        DefaultComboBoxModel model = new DefaultComboBoxModel(pageTitle1);
        jComboBox1.setModel(model);
    }

    // "Load table schema" button
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {

        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        // import saved table schema file
        String tableSchemaFileName = f.getAbsolutePath();

        // parsing table schema file
        Scanner inputStream = null;
        try {
            inputStream = new Scanner(new FileInputStream(tableSchemaFileName));
        } catch (FileNotFoundException e) {
            System.out.println("Error opening the file");
            System.exit(0);
        }
        // skip the first line data of table schema file
        inputStream.nextLine();
        // parse the second line data of table schema file for data source file
        String sourceFileName1 = inputStream.nextLine();
        sourceFileName1 = sourceFileName1.trim();
        String[] sourceFileName0 = sourceFileName1.split(":");
        String sourceFileName = sourceFileName0[1];
        if (sourceFileName.equals(locaklInputFileName)) {
            // skip the third line data of table schema file
            inputStream.nextLine();
            // the forth line data is selected row index
            String rowIndexLine = inputStream.nextLine();
            rowIndexLine = rowIndexLine.trim();
            String[] rowData = rowIndexLine.split(",");
            // skip the fifth line data of table schema file
            inputStream.nextLine();
            // the sixth line data is selected col index
            String colIndexLine = inputStream.nextLine();
            colIndexLine = colIndexLine.trim();
            String[] colData = colIndexLine.split(",");
            // skip the seventh line data of table schema file
            inputStream.nextLine();
            // the eighth line data is selected page index
            String pageIndexLine = inputStream.nextLine();
            pageIndexLine = pageIndexLine.trim();
            String[] pageData = pageIndexLine.split(",");
            // clear all selected page
            buttonGroup1.clearSelection();
            // parsing selected row schema into UI(row checkbox)
            rowSelection = new int[rowData.length - 1];
            for (int i = 1; i < rowData.length; i++) {
                rowData[i].replaceAll("\\s", "");
                rowSelection[i - 1] = Integer.parseInt(rowData[i]);
                //cb_row[Integer.parseInt(rowData[i])].setSelected(true);
            }
            StringBuilder rowBuilder = new StringBuilder();
            for (int i = 0; i < rowSelection.length; i++) {
                rowBuilder.append(tableColTitle[rowSelection[i]]);
                rowBuilder.append(", ");
            }
            // Display row selection on the upper part of table
            jLabel9.setText(rowBuilder.toString());
            // parsing selected col schema into UI(col checkbox)
            colSelection = new int[colData.length - 1];
            for (int i = 1; i < rowData.length; i++) {
                //colData[i].replaceAll("\\s","");
                colSelection[i - 1] = Integer.parseInt(colData[i]);
                //cb_col[Integer.parseInt(rowData[i])].setSelected(true);
            }
            // Check column selection along with jCheckBox
            StringBuilder colBuilder = new StringBuilder();
            for (int i = 0; i < colSelection.length; i++) {
                colBuilder.append(tableColTitle[colSelection[i]]);
                colBuilder.append(", ");
            }
            // Display row selection on the upper part of table
            jLabel10.setText(colBuilder.toString());
            // parsing selected page schema into UI(page radioButton)
            if (pageData.length > 1) {
                //pageData[1].replaceAll("\\s","");
                pageSelection = Integer.parseInt(pageData[1]);
                //rb_page[pageSelection].setSelected(true);
            } else {
                pageSelection = -1;
            }
            pageTitleArray.clear();
            // create 3D data array for displaying on the screen
            TableMaker pivotPageTree = new TableMaker(RawDataReader.loadRawRecords(inputName), rowSelection, colSelection, sumSelection, avgSelection, pageSelection);
            String[][][] pivotPageTree_3D_array = pivotPageTree.getOutArray();
            pageOptions = pivotPageTree.getPageNames();
            if (pageOptions != null) {
                for (int j = 0; j < pageOptions.length; j++) {
                    pageTitleArray.add(pageOptions[j]);
                }
                String[] pageNameArray = new String[pageTitleArray.size()];
                for (int j = 0; j < pageTitleArray.size(); j++) {
                    pageNameArray[j] = pageTitleArray.get(j);
                }
                // Initialize pages function in the drop-down box
                DefaultComboBoxModel model = new DefaultComboBoxModel(pageNameArray);
                jComboBox1.setModel(model);
            }

            // if there is no pageSelection, pageSelection defaults to -1
            if (pageSelection < 0) {
                pivotTable2D_Data = pivotPageTree_3D_array[0];
            } else {
                // if there is a pageSelection, pageSelection is set to user selection
                pivotTable2D_Data = pivotPageTree_3D_array[jComboBox1.getSelectedIndex()];
            }
            // "pivotTable2D_Data" is the comman varible for displaying 2D string array data
            String[] tableColTitle3 = new String[pivotTable2D_Data[0].length];
            jTable1.setModel(new javax.swing.table.DefaultTableModel(pivotTable2D_Data, tableColTitle3));
            // set data of pivot table cells editable
            jTable1.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent evt) {
                    // here goes the code "on cell update"
                    pivotTable2D_Data[jTable1.getSelectedRow()][jTable1.getSelectedColumn()] = jTable1.getValueAt(jTable1.getSelectedRow(), jTable1.getSelectedColumn()).toString();
                }
            });
        } else {
            JFrame frame = new JFrame();
            JButton returnLogin = new JButton("OK");
            JOptionPane.showMessageDialog(frame,
                    "Data source is not matched. Please make sure you are parcing the same data source.",
                    "Invalid table schema",
                    JOptionPane.WARNING_MESSAGE);
            //dispose();
        }
    }

//==========================Swing UI ==============================================================
    // Variables declaration - do not modify                     
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    public javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration                   
}