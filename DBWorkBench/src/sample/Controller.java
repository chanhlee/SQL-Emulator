package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.text.html.ImageView;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    //Declares all variables including those linked to  the fx:id in the fxml files
    public TextArea cmdEdit, selectScreen, status, error;
    public MenuItem openMI, runMI, saveMI, copyMI, pasteMI, preferencesMI, aboutMI;
    public Button runAllButton, runCursorButton, sweepButton;
    public TreeView dirStructure;
    public File openedFile;     //keeps track of the command file that has been
    public BorderPane mainLayout;
    public String msgSEStr;

    /*Name: openMIHandler()
    * Author: Chanhee Lee & Joshua Kim
    * Purpose: The purpose of this method is to handle the program when the menu button "open" is pressed. Once
    * the open menu button is invoked, it will open the script file and reads into the SQL Command Editor.
    */
    public void openMIHandler() throws Exception {
        try {
            System.out.println("openMIHandler Method Invoked");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("."));
            File selectFile = fileChooser.showOpenDialog(null);
            openedFile = selectFile;
            String text = "";
            FileReader fr = new FileReader(selectFile);
            BufferedReader br = new BufferedReader(fr);                 //sets up buffered reader that will be used to process input stream from file
                text += br.readLine();
                while(br.ready()) {                                     //for every next line as long as there is a next line, \n and new line are added to text variable
                    text += "\n" + br.readLine() ;
                }
                cmdEdit.setText(text);                                  //sets the text for SQL Command Edit Area by referencing the fx:id in the sample.fxml file
                br.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /*Name: runMIHandler()
   * Author: Chanhee Lee & Joshua Kim
   * Purpose: The purpose of this method is to handle the program when the menu button "run" is pressed.
   * Once the run menu button is invoked, all the commands that the user is currently working on
   * display will be executed in the SQL editor.
   */
    public void runMIHandler() throws Exception {
        try {
            System.out.println("runMIHandler Method Invoked");
            String rawCmdStr = "";
            rawCmdStr = cmdEdit.getText();                                              //stores whatever text is in the SQL command editor into a string variable
            Database db = new Database();                                               //instantiates Database to use its method
            db.executeCommand(rawCmdStr, selectScreen, status, error);
            displayTreeView(".");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /*Name: saveMIHandler()
    * Author: Chanhee Lee & Joshua Kim
    * Purpose: The purpose of this method is to handle the program when the menu button "save" is pressed.
    * Once the save menu button is invoked, all the commands that the user is currently working on
    * display will be saved from the editor to an SQL file.
    */
    public void saveMIHandler() throws Exception{
        try{
            System.out.println("saveMIHandler Method Invoked");
            if (openedFile != null){                                                             //if opened file exists, a buffered writer writes the text that is on the SQL command editor
                BufferedWriter br = new BufferedWriter(new FileWriter(openedFile, false));  	//to the  file that is currently opened
                br.write(cmdEdit.getText());
                br.flush();
                br.close();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error...");
                alert.setHeaderText("Error");
                alert.setContentText("Cannot save because no file has been opened.");
                alert.showAndWait();
            }
            displayTreeView(".");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /*Name: copyMIHandler()
    * Author: Chanhee Lee & Joshua Kim
    * Purpose: The purpose of this method is to handle the program when the menu button "copy" is pressed.
    * Once the copy menu button is invoked, it will copy the text that the user highlighted. If nothing was
    * highlighted, the previously copied text will not be overridden.
    */
    public void copyMIHandler() throws Exception{
        try {
            System.out.println("copyMIHandler Method Invoked");
            String text = cmdEdit.getSelectedText();       					//gets text that user highlights
            System.out.println(text);
            final Clipboard clipboard = Clipboard.getSystemClipboard();     //creates variable that acts as clipboard
            final ClipboardContent content = new ClipboardContent();        //creates variable that acts as content for clipboard
            content.putString(text);          							    //puts the highlighted text into variable content
            clipboard.setContent(content);     							    //puts the content that holds the highlighted text into the clipboard
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        displayTreeView(".");
    }

    /*Name: pasteMIHandler()
    * Author: Chanhee Lee & Joshua Kim
    * Purpose: The purpose of this method is to handle the program when the menu button "paste" is pressed.
    * Once the paste menu button is invoked, it will paste the text that the user had previously copied. If
    * nothing was copied, it will paste nothing.
    */
    public void pasteMIHandler() {
        try {
            System.out.println("pasteMIHandler Method Invoked");
            Toolkit toolkit = Toolkit.getDefaultToolkit();         						 	//creates a toolkit variable
            java.awt.datatransfer.Clipboard clipboard = toolkit.getSystemClipboard();   	//creates clipboard variable
            Transferable contents = clipboard.getContents(null);           					//creates transferable variable
            if (contents != null) {         												//goes through if there is content inside the clipboard
                int cursor = cmdEdit.getCaretPosition();    								//gets blinking cursor position
                String pastedText = (String) clipboard.getData(DataFlavor.stringFlavor);    //gets clipboard content and converts it into a String
                System.out.println(pastedText);
                cmdEdit.insertText(cursor, pastedText);     								//inserts text from clipboard to where the blinking cursor is
            }
        }
        catch (UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavorExceptions was found");
            ufe.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("IOException was found");
            e.printStackTrace();
        }
    }

    /*Name: preferencesMIHandler()
    * Author: Chanhee Lee & Joshua Kim
    * Purpose: The purpose of this method is to handle the program when the menu button "preferences" is pressed.
    * Once the preferences menu button is invoked, the program will provide additional settings for the window.
    */
    public void preferencesMIHandler() throws Exception{
        try {
            System.out.println("preferencesMIHandler Method Invoked");
            Stage prefStage = new Stage();     												 //makes new stage for preferences
            Parent root = FXMLLoader.load(getClass().getResource("/sample/PreferencesGUI.fxml"));  //make new fxml
            prefStage.setTitle("Preferences...");      										//titles the preferences window
            prefStage.setScene(new Scene(root, 600, 400));     								//sets size of preferences window
            prefStage.show();
        }
        catch (IOException  e){
            e.printStackTrace();
        }
    }

    /*Name: aboutMIHandler()
    * Author: Chanhee Lee & Joshua Kim
    * Purpose: The purpose of this method is to handle the program when the menu button "about" is pressed.
    * Once the about menu button is invoked, the program will explain the purpose of the GUI-DB "SQL WorkBench"
    * is.
    */
    public void aboutMIHandler() {
        try {
            System.out.println("aboutMIHandler Method Invoked");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);   //creates an Alert type dialog box
            alert.setTitle("About...");         					//sets title of dialog box
            alert.setHeaderText("Purpose of Program");      		//set header of dialog box
            alert.setContentText("The purpose behind this program is to allow the user to create, delete, and edit directories " +      //sets content inside the dialog box
                    "and files using an SQL Command Editor. The program will read data from a command file, parse each " +
                    "command, and perform a certain function based on each command. A command file can be opened from the" +
                    " File menu button with the Open SQL Script button. The commands within the file can create and delete" +
                    " database, tables, and data using the commands create, drop, insert, select, and update. In order to " +
                    "execute the commands, the Run SQL Script button in the File menu or the lightning bolt icon button can " +
                    "run all the commands within the SQL Command Editor. If the user wants to execute a specific command line, " +
                    "then the user must highlight the command that he/she wants to execute and click on the lightning bolt icon" +
                    " button with an I in it. To delete everything inside the SQL Command Editor, the sweep icon button can be used." +
                    " The user can also save all of his/her edits of a command file using the Save SQL Script button in the File menu." +
                    " The program will have a dedicated textbox that will output all of data of a specific file using the SELECT " +
                    "command. If there are multiple SELECT commands, then the textbox will display the data of the most recent SELECT" +
                    " command. The program will also have two other dedicated textboxes that will output whether a command succeeded or" +
                    " failed to execute. One textbox will hold the successfully executed commands. The other will hold the failed commands.");
            alert.showAndWait();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Name: runAllButtonHandler()
    * Author: Chanhee Lee & Joshua Kim
    * Purpose: The purpose of this method is to handle the program when the icon button "run all" is pressed which
    * is replaced with a lightning icon. Once the run all button is invoked, the program will run all the commands
    * in the SQL Command Editor.
    */
    public void runAllButtonHandler() throws Exception{   //action listener that reads text into string, invoke  executeCommand(cmdStr)
        try {
            System.out.println("runMIHandler Method Invoked");
            String rawCmdStr = "";
            rawCmdStr = cmdEdit.getText();                                              //stores whatever text is in the SQL command editor into a string variable
            Database db = new Database();                                               //instantiates Database to use its methods
            db.executeCommand(rawCmdStr, selectScreen, status, error);
            displayTreeView(".");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /*Name: runCursorButtonHandler()
    * Author: Chanhee Lee & Joshua Kim
    * Purpose: The purpose of this method is to handle the program when the icon button "run 1" is pressed which
    * is replaced with a lightning icon with a letter I in it. Once the run 1 button is invoked, the program will
    * run the 1 command that is under the cursor in the SQL Command Editor.
    */
    public void runCursorButtonHandler() throws Exception{  //action listeners that  track position of mouse, read only that line, invoke excuteCommand(cmdStr)
        try {
            System.out.println("runCursorButtonHandler Method Invoked");
            String rawSingleCmdStr = "";
            rawSingleCmdStr = cmdEdit.getSelectedText();								//stores whatever selected text is in the SQL command editor into a string variable
            Database db = new Database();												//instantiates Database to use its methods
            db.executeCommand(rawSingleCmdStr, selectScreen, status, error);
            displayTreeView(".");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /*Name: sweepButtonHandler()
    * Author: Chanhee Lee & Joshua Kim
    * Purpose: The purpose of this method is to handle the program when the icon button "sweep" is pressed which
    * is replaced with a broom icon. Once the sweep all button is invoked, the program will remove all the commands
    * from the SQL command editor.
    */
    public void sweepButtonHandler() throws Exception{  //action that uses a method that should already exist
        System.out.println("sweepButtonHandler Method Invoked");
        cmdEdit.setText("");															//clears the SQL command editor
        displayTreeView(".");
    }

    /*Name: resetDirectory()
     * Author: Chanhee Lee & Joshua Kim
     * Purpose: The purpose of this method is to have the program start with a clean slate. The program will delete
     * any remaining files & directories that were created from the previous run. It will only hold the files/directories
     * that are needed to run the program.
      */
    public void resetDirectory() {
        File fileInputDirectoryLocation = new File(".");
        File fileList[] = fileInputDirectoryLocation.listFiles();       //lists all files into an array
        File ideaFile = new File(".idea");                    //folder needed to run program
        File outFile = new File("out");                       //folder needed to run program
        File srcFile = new File("src");                       //folder needed to run program
        for (File file : fileList) {                                    //checks each file if its the file needed to run program. If it isn't then it deletes. If it is, it doesn't delete it.
            String name = file.getName();
            if (file.getName().contains("DBWorkBench") || file.getName().equals(ideaFile.getName()) || file.getName().equals(outFile.getName()) || file.getName().equals(srcFile.getName()) || file.getName().contains("iml") || file.getName().contains("README")) {
            }
            else {
                if (file.exists() && file.isDirectory()) {
                    String[] content = file.list();
                    for (String s : content) {
                        File updatedDB = new File(file.getPath(), s);       //deletes every file within the directory because a
                        updatedDB.delete();                                 //directory cannot be deleted without deleting the contents first
                    }
                    file.delete();
                }
                else if (file.exists() && file.isFile()){
                    file.delete();
                }

            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {                    //method that is run when the program is started
        try {
            resetDirectory();
            displayTreeView(".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Name: displayTreeView()
    * Author: Chanhee Lee & Joshua Kim
    * Purpose: The purpose of this method is to display the directory structure in a TreeView component. The
    * directory structure shows the directories and files within the specified parameters. The root created
    * with these directories and files are then set as the root to the dirStructure component.
    * Parameters: String inputDirectoryLocation - holds the string value of the path to the input directory for
    *              the root of the treeview object
    */
    public void displayTreeView(String inputDirectoryLocation) throws Exception{
        System.out.println("displayTreeViewInvoked");
        CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<>(inputDirectoryLocation);///creates root of the tree
        dirStructure.setShowRoot(false); //hides root
        dirStructure.setCellFactory(CheckBoxTreeCell.<String>forTreeView());		//sets a cell factory for the treeview component

        File fileInputDirectoryLocation = new File(inputDirectoryLocation);			//using inputDirectoryLocation parameter, creates a list of files in a file list
        File fileList[] = fileInputDirectoryLocation.listFiles();
        // create tree
        for (File file : fileList) {					//adds file from file list into the tree with the set root
            createTree(file, rootItem);
            System.out.println(file);
        }
        dirStructure.setRoot(rootItem);
        dirStructure.refresh();
    }

    /*Name: createTree()
    * Author: Chanhee Lee & Joshua Kim
    * Purpose: The purpose of this method is to add all the files in a directory to the root
    * the open menu button is invoked, it will open the script file and reads into the SQL Command Editor.
    * Parameters:   File file - the current file that the method is trying to add the to the root
    *               CheckBoxTreeItem<String> parent - the parent of the file that the method is trying to add
    *               to the root. It may be the root itself if the file is directly  under the root
    */
    public static void createTree(File file, CheckBoxTreeItem<String> parent) throws Exception{
        if (file.isDirectory()) {														//if the 'file' variable is a directory, new checkboxtreemitem is created and calls itself for the  files in that directory
            CheckBoxTreeItem<String> treeItem = new CheckBoxTreeItem<>(file.getName());
            parent.getChildren().add(treeItem);
            for (File f : file.listFiles()) {
                createTree(f, treeItem);
            }
        }
        else if (file.isFile()) {														//if the 'file' variable is a file, the file's name is added to the parent checkboxtreeitem that was passed in as a paramter
            parent.getChildren().add(new CheckBoxTreeItem<>(file.getName()));
        }
        else
            System.out.println("File neither file nor directory.");
    }
}
