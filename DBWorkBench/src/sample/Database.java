package sample;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;

public class Database
{
    public static String specificErrorStr = ""; //static string variable that contains information about why a method invocation may have failed.
                                                //because it is static, it can be called from DBCommands class which has the specific information
    /*Name: executeCommand(String rawCmdStr, TextArea selectScreen, TextArea statusTA, TextArea errorTA)
   * Author: Chanhee Lee & Joshua Kim
   * Purpose: Takes in parameters from Controller class and executes the SQL commands. This can be seen as the main algorithm
   * of the program
   * Parameters:   String rawCmdStr - Takes in a string variable that contains the unedited text from the SQL command editor
   *               TextArea selectScreen - Takes in the component that shows the select rows screen in the GUI
   *               TextArea statusTA - Takes in the text area that shows the status log in the GUI
   *               TextArea errorTA - Takes in the text area that shows the error log in the GUI
   */
    public void executeCommand(String rawCmdStr, TextArea selectScreen, TextArea statusTA, TextArea errorTA) throws Exception
    {
        try{ //try-catch that catches any kind of exceptions in the overall program
            boolean comSuccess = false; 		//variable to keep track of whether methods have been correctly execute
            StringWriter sWriteStatus = new StringWriter();
            PrintWriter pWriteStatus = new PrintWriter(new BufferedWriter(sWriteStatus));
            StringWriter sWriteError = new StringWriter();
            PrintWriter pWriteError = new PrintWriter(new BufferedWriter(sWriteError));
            StringWriter sWriteSS = new StringWriter();
            PrintWriter pWriteSS = new PrintWriter(new BufferedWriter(sWriteSS));
            String statusStr = "";
            String errorStr = "";

            String selectScreenStr = "";
            //clears previous status, error, and select screen
            pWriteStatus.flush();
            sWriteStatus.getBuffer().setLength(0);
            pWriteError.flush();
            sWriteError.getBuffer().setLength(0);
            pWriteSS.flush();
            sWriteSS.getBuffer().setLength(0);
            //instantiates an object of controller so that Database has access to those methods
            try									//try-catch statement that catches any IOExceptions in
            {
                String line = "";				//keeps track of the whole text file that is being read and converts it into  a string file
                String lineNow = "";			//keeps track of current line that is  being read and executed
                String lineCommand = "";
                String cmdStr = "";             //variable that will have organized commands from rawCmdStr
                Boolean ready = true;
                rawCmdStr = rawCmdStr.replaceAll("\n","");    //removes all line breaks
                //Organizes commands from rawCmdStr and stores it to cmdStr
                String temp = "";
                if (!rawCmdStr.contains(";")){          //checks for presence of ';' which indicates the end of an SQL command
                    pWriteError.append("Failed: No Commands Found");
                    ready = false;
                 }
                while (true){                           //loop processes all the commands and removes all trailing white spaces
                                                        //of the the string. This format is necessary for the next part of this program to work
                    if (rawCmdStr.contains(";")){
                        String temp1 = rawCmdStr.substring(0, rawCmdStr.indexOf(';'));
                        temp1 = temp1.trim();
                        temp1 += ";\n";
                        temp1 = temp1.replaceAll("\n","");
                        temp += temp1;//remove ;
                        rawCmdStr = rawCmdStr.substring(rawCmdStr.indexOf(';')+1);
                    }
                    else{
                        temp += rawCmdStr;
                        temp.trim();
                        break;
                    }
                }
                cmdStr = temp;                              //stores result into cmdStr variable which will be used to be read in a buffered reader
                cmdStr = cmdStr.replaceAll(";",";\n");      //adds in a line break after each semi colon

                //Starts reading and executing the commands
                BufferedReader br = new BufferedReader(new StringReader(cmdStr));
                while (br.ready() && ready){			//loop continues to read next line until cmdStr has more commands to read
                    lineNow = br.readLine();	        //lineNow is a variable with the current command line that will be manipulated
                    if (lineNow == null){               //skips loop if line is non-existent
                        break;
                    }
                    else if (lineNow.equals("")){       //skips loop if line is empty
                        continue;
                    }
                    lineCommand = lineNow; 		//lineCommand is a variable that keeps track of the current SQL like command
                    String firstWord = lineNow.substring(0,lineNow.indexOf(" "));		//substrings first word of current line the program is reading
                    lineNow = lineNow.substring(lineNow.indexOf(" ")+1);				//removes first word from lineNow
                    switch(firstWord) {            //allows program to read one word at a time and continue to act based on each word read
                        //continues program based on first word
                        case "CREATE":    //next: determines whether it is CREATE DATABASE or CREATE TABLE
                            String secondWord = lineNow.substring(0, lineNow.indexOf(" "));    //substrings next word by getting word between the first and second occurrence of a space
                            lineNow = lineNow.substring(lineNow.indexOf(" ") + 1);            //removes second word from lineNow
                            DBCommands dbc = new DBCommands();
                            if (secondWord.equals("DATABASE")) {                                //if the second word is "DATABASE" then it invokes the createDatabase() method
                                comSuccess = dbc.createDatabase(lineNow, pWriteStatus, pWriteError, specificErrorStr);        //invokes method and determines whether it was run successfully
                                if (comSuccess == true) {                            //writes success or failure of method to status or error text area
                                    pWriteStatus.append("Initiating Command CREATE..." + "\n");
                                    pWriteStatus.append("(" + lineCommand + ")\n");
                                    pWriteStatus.append("Successfully Created Database." + "\n\n");
                                } else {
                                    pWriteError.append("Initiating Command CREATE..." + "\n");
                                    pWriteError.append("(" + lineCommand + ")\n");
                                    pWriteError.append("Failed to execute command. \n");
                                    pWriteError.append(specificErrorStr +"\n");
                                }
                            } else if (secondWord.equals("TABLE")) {                            //if the second word is "TABLE" then it invokes the createTable() method
                                comSuccess = dbc.createTable(lineNow, pWriteStatus, pWriteError, specificErrorStr);
                                if (comSuccess == true) {                                      //writes success or failure of method to status or error text area
                                    pWriteStatus.append("Initiating Command CREATE..." + "\n");
                                    pWriteStatus.append("(" + lineCommand + ")\n");
                                    pWriteStatus.append("Successfully Created Table." + "\n\n");
                                } else {
                                    pWriteError.append("Initiating Command CREATE..." + "\n");
                                    pWriteError.append("(" + lineCommand + ")\n");
                                    pWriteError.append("Failed to execute command. \n");
                                    pWriteError.append(specificErrorStr +"\n");
                                }
                            } else {
                                pWriteError.append("Failed to execute command. Entered Command Does Not Exist.\n");
                            }
                            break;

                        case "DROP": //next: determines whether it is DROP DATABASE or DROP TABLE
                            String secondWord1 = lineNow.substring(0, lineNow.indexOf(" "));        //substrings next word by getting word between the first and second occurrence of a space
                            lineNow = lineNow.substring(lineNow.indexOf(" ") + 1);                //removes second word from lineNow
                            DBCommands dbc1 = new DBCommands();
                            if (secondWord1.equals("DATABASE")) {                                //if the second word is "DATABASE" then it invokes dropDatabase()
                                comSuccess = dbc1.dropDatabase(lineNow, pWriteStatus, pWriteError, specificErrorStr);            //invokes method and determines whether it was run successfully
                                if (comSuccess == true) {
                                    pWriteStatus.append("Initiating Command DROP..." + "\n");
                                    pWriteStatus.append("(" + lineCommand + ")\n");
                                    pWriteStatus.append("Successfully Dropped Database." + "\n\n");
                                } else {
                                    pWriteError.append("Initiating Command DROP..." + "\n");
                                    pWriteError.append("(" + lineCommand + ")\n");
                                    pWriteError.append("Failed to execute command. \n");
                                    pWriteError.append(specificErrorStr +"\n");
                                }
                            }
                            else if (secondWord1.equals("TABLE")){								//if the second word is "TABLE" then it invokes droptable()
                                comSuccess = dbc1.dropTable(lineNow, pWriteStatus, pWriteError, specificErrorStr);
                                if (comSuccess == true) {                    //writes success or failure of method to status or error text area
                                    pWriteStatus.append("Initiating Command DROP..." + "\n");
                                    pWriteStatus.append("(" + lineCommand + ")\n");
                                    pWriteStatus.append("Successfully Dropped Table." + "\n\n");
                                }
                                else {
                                    pWriteError.append("Initiating Command DROP..." + "\n");
                                    pWriteError.append("(" + lineCommand + ")\n");
                                    pWriteError.append("Failed to execute command. \n");
                                    pWriteError.append(specificErrorStr +"\n");
                                }
                            }
                            else {
                                pWriteError.append("Failed to execute command. Entered Command Does Not Exist \n\n");		//appends an error message to error  file if command second word does not match either of these possible second words
                            }
                            break;

                        case "INSERT": //next: determines whether there is an INTO after the INSERT and parses  the rest of the command
                            //get record to insert
                            String record = lineNow.substring(0, lineNow.indexOf("\"",1)+1);	//substrings the record to be inserted
                            lineNow = lineNow.substring(lineNow.indexOf("\"",1)+2);				//removes record from lineNow
                            DBCommands dbc2 = new DBCommands();
                            //confirm existence of INTO
                            String into = lineNow.substring(0, lineNow.indexOf(" "));			//stores part of line that should be INTO for command to work
                            lineNow = lineNow.substring(lineNow.indexOf(" ")+1);				//removes INTO from lineNow
                            if (!into.equals("INTO"))		{
                                pWriteError.append("Initiating Command INSERT..." + "\n");
                                pWriteError.append("(" + lineCommand + ")\n");
                                pWriteError.append("Failed to execute command. Command Error - Location Not Specified - INTO not found.\n");
                                pWriteError.append("(" + lineCommand + ")\n\n");
                            }
                            else if (into.equals("INTO")) {										//checks for INTO as the next word in the line
                                comSuccess = dbc2.insertInto(lineNow, record, pWriteStatus, pWriteError, specificErrorStr);		//invokes method and determines whether it was run successfully
                                if (comSuccess == true) {                                //writes success or failure of method to status or error text area
                                    pWriteStatus.append("Initiating Command INSERT..." + "\n");
                                    pWriteStatus.append("(" + lineCommand + ")\n");
                                    pWriteStatus.append("Successfully Inserted Row Into Table." + "\n\n");
                                }
                                else {
                                    pWriteError.append("Initiating Command INSERT..." + "\n");
                                    pWriteError.append("(" + lineCommand + ")\n");
                                    pWriteError.append("Failed to execute command. \n");
                                    pWriteError.append(specificErrorStr +"\n");
                                }
                            }
                            else {
                                pWriteError.append("Failed to execute command. Entered Command Does Not Exist. \n\n"); //appends an error message to error file
                            }
                            break;

                        case "SELECT": //next: determines whether this is a SELECT FROM command  or SELECT FROM WHERE COLUMN command
                            DBCommands dbc3 = new DBCommands();	//just select from
                            //clears printWriter and stringWriter that writes to the select screen
                            pWriteSS.flush();
                            sWriteSS.getBuffer().setLength(0);

                            if (!lineNow.matches("(.*)WHERE COLUMN =(.*)")){	//if there is not a "WHERE COLUMN = " then the program invokes the selectAllFrom
                                comSuccess = dbc3.selectAllFrom(lineNow, pWriteSS, pWriteStatus, pWriteError, specificErrorStr);
                                if (comSuccess == true){
                                    //writes success or failure of method to status or error text area
                                    pWriteStatus.append("Initiating Command SELECT..." + "\n");
                                    pWriteStatus.append("(" + lineCommand + ")\n");
                                    pWriteStatus.append("Successfully Selected All Records From Table"+"\n\n");
                                }
                                else  {
                                    pWriteError.append("Initiating Command SELECT..." + "\n");
                                    pWriteError.append("(" + lineCommand + ")\n");
                                    pWriteError.append("Failed to execute command. \n");
                                    pWriteError.append(specificErrorStr +"\n");
                                }
                            }
                            else {                                    //if there is a "WHERE COLUMN = " then the program invokes selectFromWhere() method
                                comSuccess = dbc3.selectFromWhere(lineNow, pWriteSS, pWriteStatus, pWriteError, specificErrorStr);
                                if (comSuccess == true)    {                                        //writes success or failure of method to status or error text area
                                    pWriteStatus.append("Initiating Command SELECT..." + "\n");
                                    pWriteStatus.append("(" + lineCommand + ")\n");
                                    pWriteStatus.append("Successfully Selected All Matching Records From Table" + "\n\n");
                            }
                                else {
                                    pWriteError.append("Initiating Command SELECT..." + "\n");
                                    pWriteError.append("(" + lineCommand + ")\n");
                                    pWriteError.append("Failed to execute command. \n");
                                    pWriteError.append(specificErrorStr +"\n");
                                }
                            }

                            break;

                        case "DELETE": //next: determines if there is a "WHERE COLUMN = " or not in the command in order to determine whether to execute deleteFrom() or deleteFromWhere()
                            DBCommands dbc4 = new DBCommands();

                            if (!lineNow.matches("(.*)WHERE COLUMN =(.*)")){		//if "WHERE COLUMN = " is not found, then program invokes deleteFrom() method
                                comSuccess = dbc4.deleteFrom(lineNow, pWriteStatus, pWriteError, specificErrorStr);
                                if (comSuccess == true) {                                        //writes success or failure of method to status or error text area
                                    pWriteStatus.append("Initiating Command DELETE..." + "\n");
                                    pWriteStatus.append("(" + lineCommand + ")\n");
                                    pWriteStatus.append("Successfully Deleted All Records From Table" + "\n\n");
                                }
                                else {
                                    pWriteError.append("Initiating Command DELETE..." + "\n");
                                    pWriteError.append("(" + lineCommand + ")\n");
                                    pWriteError.append("Failed to execute command. \n");
                                    pWriteError.append(specificErrorStr +"\n");
                                }
                            }
                            else {			//if "WHERE COLUMN = " is not found, then program invokes deleteFromWhere() method
                                comSuccess = dbc4.deleteFromWhere(lineNow, pWriteStatus, pWriteError, specificErrorStr);
                                if (comSuccess == true) {                //writes success or failure of method to status or error text area
                                    pWriteStatus.append("Initiating Command DELETE..." + "\n");
                                    pWriteStatus.append("(" + lineCommand + ")\n");
                                    pWriteStatus.append("Successfully Deleted All Matching Records From Table" + "\n\n");
                                }
                                else {
                                    pWriteError.append("Initiating Command DELETE..." + "\n");
                                    pWriteError.append("(" + lineCommand + ")\n");
                                    pWriteError.append("Failed to execute command. \n");
                                    pWriteError.append(specificErrorStr +"\n");
                                }
                            }
                            break;

                        case "UPDATE": //response: locates a certain record and replaces it with another
                            DBCommands dbc5 = new DBCommands();
                            comSuccess = dbc5.updateSetWhere(lineNow, pWriteStatus, pWriteError, specificErrorStr);
                            if (comSuccess == true) {                    //writes success or failure of method to status or error text area
                                pWriteStatus.append("Initiating Command UPDATE..." + "\n");
                                pWriteStatus.append("(" + lineCommand + ")\n");
                                pWriteStatus.append("Successfully Updated All Matching Records From Table" + "\n\n");
                            }
                            else {
                                pWriteError.append("Initiating Command UDPATE..." + "\n");
                                pWriteError.append("(" + lineCommand + ")\n");
                                pWriteError.append("Failed to execute command. \n");
                                pWriteError.append(specificErrorStr +"\n");
                            }
                            break;

                        default: 		//if first word does not match any words, program appends an error
                            pWriteError.append("Command Entered: " + lineCommand + "\n");
                            pWriteError.append("Failed: Tried To Execute Command That Does Not Exist."+"\n\n");
                    }
                }
                //flushes writers into string, sets the text in the status text area as the string, then closes the readers
                pWriteStatus.flush();
                statusStr = sWriteStatus.toString();
                statusTA.setText(statusStr);
                pWriteStatus.close();
                sWriteStatus.close();
                //flushes writers into string, sets the text in the error text area as the string, then closes the readers
                pWriteError.flush();
                errorStr = sWriteError.toString();
                errorTA.setText(errorStr);
                pWriteError.close();
                sWriteError.close();
                //flushes writers into string, sets the text in the select rows screen text area as the string, then closes the readers
                pWriteSS.flush();
                selectScreenStr = sWriteSS.toString();
                selectScreen.setText(selectScreenStr);
                pWriteSS.close();
                sWriteSS.close();

                br.close();
            }
            catch(Exception e)		//if there is an exception, the program appends the error text area
            {
                pWriteError.append("Exception Found in File:  "+ e.getMessage() + "\n\n");
                pWriteError.flush();
                e.printStackTrace();
                errorStr = sWriteError.toString();
                errorTA.setText(errorStr);
                pWriteError.close();
                pWriteStatus.close();
                pWriteSS.close();
            }
        }
        catch (Exception e){			//catches any overall Exceptions in the program including the part of the program before the parsing of the command lines
            System.out.println("Exception found: " + e.getMessage() + "\n");	//prints messages as this catch statement catches any exceptions that occur in the program including
            //the part of the program before status and error files have been created
        }
    }
}