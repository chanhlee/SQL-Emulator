    package sample;

    import javafx.scene.control.TextArea;

    import java.io.IOException;
    import java.util.Scanner;
    import java.io.File;
    import java.io.FileReader;
    import java.io.BufferedReader;
    import java.io.BufferedWriter;
    import java.io.PrintWriter;
    import java.io.FileWriter;

    public class DBCommands
    {
        //  CREATE DATABASE method
        //-------------------------------------------------------------------------------------------
        // Author: 		Chanhee Lee
        // Date: 		02/06/17
        // Purpose: 	Creates a database (directory) with the given abstract path
        // Parameters: 	String lineNow - the string containing part of the command that the method
        // 				takes in, in order to parse the it and determine the name of the database
        //				then create a database with that name
        //				PrintWriter pWriteError - Takes in a PrintWriter that allows this method to append
        //				to the errorStr from this class if there is an exception or error
        //              String specificErrorStr - allows this class to store specific error messages for the
        //              Database class to use to write to the error text area
        // Example Command:	CREATE DATABASE colleges;
        //------------------------------------------------------------------------------------------
        public boolean createDatabase(String lineNow, PrintWriter pWriteStatus, PrintWriter pWriteError, String specificErrorStr) throws IOException{
            try {
                if (!lineNow.contains(";")){
                    Database.specificErrorStr = "Failed: Incorrect Syntax - Could not find ';'\n";
                    return false;
                }
                lineNow = lineNow.replace(";","");
                String dbName = lineNow;	//gets database name from remaining line
                File f = new File(dbName);
                if (!f.exists()){			//checks to see if the file does not  already exist
                    f.mkdirs();
                    return true;
                }
                else {						//error message for when database trying to be created already exists
                    Database.specificErrorStr = "Failed: Database already exists.\n";
                    return false;
                }
            }
            catch (Exception e){
                Database.specificErrorStr = "Exception: " + e.getMessage() +"\n";
                return false;
            }
        }

        //  CREATE TABLE method
        //-------------------------------------------------------------------------------------------
        // Author: 		Chanhee Lee
        // Date: 		02/06/17
        // Purpose: 	Creates a table (file) with the given abstract path
        // Parameters: 	String lineNow - the string containing part of the command that the method
        // 				takes in, in order to parse it and determine the name of the table and database
        //				then create a table with that name within the specified database
        //				PrintWriter pWriteError - Takes in a PrintWriter that allows this method to append
        //				to the errorStr from this class if there is an exception or error
        // Example Command:	CREATE TABLE colleges.students;
        //------------------------------------------------------------------------------------------
        public boolean createTable(String lineNow, PrintWriter pWriteStatus, PrintWriter pWriteError, String specificErrorStr) throws IOException{

            try {
                if (!lineNow.contains(";")){
                    Database.specificErrorStr = "Failed: Incorrect Syntax - Could not find ';'\n";
                    return false;
                }
                lineNow = lineNow.replace(";","");
                String dbName = lineNow.substring(0,lineNow.indexOf("."));
                lineNow = lineNow.substring(lineNow.indexOf(".")+1);			//removes dbName from lineNow
                String tabName = lineNow;										//remaining lineNow should be dbName
                File db = new File(dbName);
                File tab = new File(dbName + "\\" + tabName);					//sets up file's path to be under dbName
                if (db.exists() && !tab.exists()){								//only creates file if database to create file in exists and file to be created does not exist yet
                    tab.createNewFile();
                    return true;
                }
                else if (!db.exists()){											//error if database does not exist
                    Database.specificErrorStr = "Failed: Database \"" + dbName + "\" does not exists.\n";
                    return false;
                }
                else if (tab.exists()){											//error if table does not exists
                    Database.specificErrorStr = "Failed: Table \"" + tab + "\" already exists.\n";
                    return false;
                }
                else {
                    Database.specificErrorStr = "Failed: Other Error."+"\n";					//catches any errors that may have not been caught, though this should usually not becalled
                    return false;
                }
            }
            catch (IOException e){												//catches any exceptions
                Database.specificErrorStr = "Exception: " + e.getMessage() +"\n";
                return false;
            }
        }

        //  DROP DATABASE method
        //-------------------------------------------------------------------------------------------
        // Author: 		Chanhee Lee
        // Date: 		02/06/17
        // Purpose: 	Deletes a database (directory) with the given abstract path
        // Parameters: 	String lineNow - the string containing part of the command that the method
        // 				takes in, in order to parse the it and determine the name of the database
        //				then delete a database with that name
        //				PrintWriter pWriteError - Takes in a PrintWriter that allows this method to append
        //				to the errorStr from this class if there is an exception or error
        //              String specificErrorStr - allows this class to store specific error messages for the
        //              Database class to use to write to the error text area
        // Example Command:	DROP DATABASE colleges;
        //------------------------------------------------------------------------------------------
        public boolean dropDatabase(String lineNow, PrintWriter pWriteStatus, PrintWriter pWriteError, String specificErrorStr) throws Exception{
            try{
                if (!lineNow.contains(";")){
                    Database.specificErrorStr = "Failed: Incorrect Syntax - Could not find ';'\n";
                    return false;
                }
                lineNow = lineNow.replace(";","");
                String dbName = lineNow;							//gets database name from remaining line
                File db = new File(dbName);
                if (db.exists()){									//checks if database that is to be dropped exists
                    String[]content = db.list();
                    for(String s: content){							//deletes every file within db directory because directory cannot
                        File updatedDB = new File(db.getPath(),s);	//be deleted without deleting the contents first
                        updatedDB.delete();
                    }
                    db.delete(); 									//deletes the remaining empty database
                    return true;
                }
                else {
                    Database.specificErrorStr = "Failed: Database \"" + dbName + "\" does not exist.\n";
                    return false;
                }
            }
            catch (Exception e){									//catches general exceptions because IOException could not be thrown from this method
                Database.specificErrorStr = "Exception: " + e.getMessage() +"\n";
                return false;
            }
        }

        //  DROP TABLE method
        //-------------------------------------------------------------------------------------------
        // Author: 		Chanhee Lee
        // Date: 		02/06/17
        // Purpose: 	Deletes a table (file) with the given abstract path
        // Parameters: 	String lineNow - the string containing the command that the method takes in,
        // 				in order to parse it and determine the name of the table and database then
        //				delete a table with that name within the specified database
        //				PrintWriter pWriteError - Takes in a PrintWriter that allows this method to append
        //				to the errorStr from this class if there is an exception or error
        //              String specificErrorStr - allows this class to store specific error messages for the
        //              Database class to use to write to the error text area
        // Example Command:	DROP TABLE colleges;
        //------------------------------------------------------------------------------------------
        public boolean dropTable(String lineNow, PrintWriter pWriteStatus, PrintWriter pWriteError, String specificErrorStr)throws IOException{
            try {
                if (!lineNow.contains(";")){
                    Database.specificErrorStr = "Failed: Incorrect Syntax - Could not find ';'\n";
                    return false;
                }
                lineNow = lineNow.replace(";","");
                String dbName = lineNow.substring(0,lineNow.indexOf("."));
                lineNow = lineNow.substring(lineNow.indexOf(".")+1);			//removes dbName from lineNow
                String tabName = lineNow;
                File db = new File(dbName);										//sets up File object for database
                File tab = new File(dbName + "\\" + tabName);					//sets up File object for table with file's path to be under dbName
                //if database with dbName exists: 								//these objects allow the program to use methods such  as .exists()
                //to check if they exist before trying to drop them
                if(!db.exists()){								//if database doesn't exist, appends error to error file
                    Database.specificErrorStr = "Failed: Database, " + dbName + " does not exist.\n";
                    return false;
                }
                else if(!tab.exists()){							//if table doesn't exist, appends error to error file
                    Database.specificErrorStr = "Failed: File, " + tabName + " does not exist.\n";
                    return false;
                }
                else if(db.exists() && tab.exists()){			//only drops table if the file and the database it is in exists
                    tab.delete();								//otherwise there is an error
                    return true;
                }
                else{
                    Database.specificErrorStr = "Error: Other Error."+"\n"; 	//catches any errors that may have not been caught, though this should usually not becalled
                    return false;
                }
            }
            catch (Exception e){
                Database.specificErrorStr = "Exception: " + e.getMessage() +"\n";
                return false;
            }
        }

        //  INSERT INTO method
        //-------------------------------------------------------------------------------------------
        // Author: 		Chanhee Lee
        // Date: 		02/06/17
        // Purpose: 	Inserts a record into a table (file) with the given abstract path
        // Parameter: 	String lineNow - the string containing part of the command that the method
        // 				takes in, in order to parse it and determine the name of the table and database
        //				then delete a table with that name within the specified database
        //				String record - contains the 'record' that needs to be appended to the table (file)
        //				PrintWriter pWriteError - Takes in a PrintWriter that allows this method to append
        //				to the errorStr from this class if there is an exception or error
        //              String specificErrorStr - allows this class to store specific error messages for the
        //              Database class to use to write to the error text area
        // Example Command:	INSERT "John Smith" INTO colleges.students;
        //------------------------------------------------------------------------------------------
        public boolean insertInto(String lineNow, String record, PrintWriter pWriteStatus, PrintWriter pWriteError, String specificErrorStr) throws IOException{
            try {
                if (!lineNow.contains(";")){
                    Database.specificErrorStr = "Failed: Incorrect Syntax - Could not find ';'\n";
                    return false;
                }
                lineNow = lineNow.replace(";","");
                System.out.println("insertInto invoked");
                String recordPath = lineNow.substring(0);
                record = record.substring(1, record.length()-1);		//removes quotations from record
                recordPath = recordPath.replace('.', '\\');				//replaces . with / to get a valid abstract path
                //Check file if it exists
                File file = new File(recordPath);

                if (file.exists() && file.isFile()){ 					//if file in directory already exists, the program inserts the record
                    //write to recordPath
                    FileWriter fWriter = new FileWriter(file, true);	//instantiates a FileWriter that allows program to append
                    PrintWriter pWriter = new PrintWriter(fWriter);		//extend FileWriter for more methods
                    pWriter.append(record+"\n");
                    pWriter.flush();
                    pWriter.close();
                    return true;
                }
                else {													//if file does not exist or the path leads to a directory, method appends an error
                    //to the error file
                    Database.specificErrorStr = "File you are trying to insert to does not exist.\n";
                    return false;
                }
            }
            catch (IOException e){
                Database.specificErrorStr = "IOException: " + e.getMessage() +"\n";
                return false;
            }
        }

        //  SELECT * FROM method
        //-------------------------------------------------------------------------------------------
        // Author: 		Chanhee Lee
        // Date: 		02/06/17
        // Purpose: 	Selects all records (rows) from a table (file) with the given abstract path
        // Parameters: 	String lineNow - the string containing part of the command that the method
        // 				takes in, in order to parse it and determine the name of the table and database
        //				then appends the records in the file to a log file (e.g. status.txt)
        //				PrintWriter pWriteLog - Takes in an object of the PrintWriter class that allows
        //				this class to append the selected records to the statusStr in the Database class
        //				PrintWriter pWriteError - Takes in a PrintWriter that allows this method to append
        //				to the errorStr from this class if there is an exception or error
        //              String specificErrorStr - allows this class to store specific error messages for the
        //              Database class to use to write to the error text area
        // Example Command:	SELECT * FROM colleges.students;
        //------------------------------------------------------------------------------------------
        public boolean selectAllFrom(String lineNow, PrintWriter pWriteSS, PrintWriter pWriteStatus, PrintWriter pWriteError, String specificErrorStr) throws IOException{
            try {
                if (!lineNow.contains(";")){
                    Database.specificErrorStr = "Failed: Incorrect Syntax - Could not find ';'\n";
                    return false;
                }
                lineNow = lineNow.replace(";","");
                String astericksFrom = lineNow.substring(0, lineNow.indexOf("M")+1);	//substrings "* FROM"
                lineNow = lineNow.substring(lineNow.indexOf(" ", 6)+1);					//removes "* FROM " from lineNow
                if (!astericksFrom.equals("* FROM")){									//error appended to error file if the "* FROM" is not in the command with the appropriate syntax
                    Database.specificErrorStr = "Failed: Command Error - \'*\' or \"From\" not found.\n";
                    return false;
                }
                else {	//if the "* FROM" exists in the command with the appropriate syntax then the program continues
                    String selectFilePath = lineNow;
                    selectFilePath =  selectFilePath.replace('.', '\\'); 				//changes format to suit file path in linux
                    File f = new File(selectFilePath);
                    BufferedReader bReadSelect = new BufferedReader(new FileReader(f));	//made to check if file has contents
                    if (!bReadSelect.ready()){											//error if path is not ready to be read (which means it is empty)
                        Database.specificErrorStr = "Failed: 0 Records Found. Cannot Select From Empty File.\n";
                        bReadSelect.close();
                        return false;
                    }
                    else{				//if the file is not empty, program continues
                        pWriteSS.append("Selected: \n");
                        String nextLine = "";
                        while(bReadSelect.ready()){					//reads and appends ("selects") each line of the file to the statusStr
                            nextLine = bReadSelect.readLine();		//until the file runs out of lines
                            pWriteSS.append(nextLine + "\n");
                        }
                        bReadSelect.close();
                        return true;
                    }
                }
            }
            catch (IOException e){
                Database.specificErrorStr = "IOException: " + e.getMessage() +"\n";
                return false;
            }
        }

        //  SELECT * FROM WHERE method
        //-------------------------------------------------------------------------------------------
        // Author: 		Chanhee Lee
        // Date: 		02/06/17
        // Purpose: 	Selects all records (rows) from a table (file) with the given abstract path that
        //				matches the column record that the command has specified
        // Parameters: 	String lineNow - the string that contains part of the command that the method
        // 				takes in, in order to parse it and determine the name of the table and database
        //				then appends the records that match the given column record in the file, to a
        //				statusStr in the Database class
        //				PrintWriter pWriteLog - Takes in an object of the PrintWriter class that allows
        //				this class to append the selected records to the statusStr
        //				PrintWriter pWriteError - Takes in a PrintWriter that allows this method to append
        //				to the errorStr from this class if there is an exception or error
        //              String specificErrorStr - allows this class to store specific error messages for the
        //              Database class to use to write to the error text area
        // Example Command:	SELECT * FROM colleges.students WHERE COLUMN = "John Smith";
        //------------------------------------------------------------------------------------------
        public boolean selectFromWhere(String lineNow, PrintWriter pWriteSS, PrintWriter pWriteStatus, PrintWriter pWriteError, String specificErrorStr)throws IOException{
            try {
                if (!lineNow.contains(";")){
                    Database.specificErrorStr = "Failed: Incorrect Syntax - Could not find ';'\n";
                    return false;
                }
                lineNow = lineNow.replace(";","");
                String astericksFrom = lineNow.substring(0, lineNow.indexOf("M")+1);	//substrings *
                lineNow = lineNow.substring(lineNow.indexOf(" ", 6)+1);					//removes "* FROM " from lineNow
                if (!astericksFrom.equals("* FROM")){
                    Database.specificErrorStr = "Failed: Command Error - \'*\' or \"From\" not found.\n";
                    return false;
                }
                else {
                    String dbName = lineNow.substring(0, lineNow.indexOf('.'));			//gets dbName
                    lineNow = lineNow.substring(lineNow.indexOf('.')+1);				//removes dbName and .
                    String tabName = lineNow.substring(0, lineNow.indexOf(' '));		//gets tabName
                    lineNow = lineNow.substring(lineNow.indexOf(' ')+1);				//removes tabName and space
                    if (lineNow.matches("WHERE COLUMN = (.*)")){						//checks that there is a "WHERE COLUMN =" where it should be  in the command
                        lineNow = lineNow.substring(15);								//removes "WHERE COLUMN = "
                        String idValue = lineNow.substring(1, lineNow.indexOf('"', 1));	//gets idValue
                        //program reads the file until (line == idvalue) then appends idvalue to the log each time it is encountered
                        File f = new File(dbName + "\\" + tabName);
                        BufferedReader bReadSelect = new BufferedReader(new FileReader(f));
                        if (!bReadSelect.ready()){										//error if path is not ready to be read (which means it is empty)
                            Database.specificErrorStr = "Failed: 0 Records Found. Cannot Select From Empty File.\n";
                            bReadSelect.close();
                            return false;
                        }
                        else{															//if the file is not empty, program proceeds to select records that match the idValue
                            pWriteSS.append("Selected: \n");
                            String nextLine = "";
                            boolean found = false;
                            while(bReadSelect.ready()){			//while the file has lines to read the BufferedReader object, bReadSelect, continues to read each line
                                nextLine = bReadSelect.readLine();	//each line is then compared to the idValue that was specified to be selected and if it is, it is appended
                                //to the statusStr
                                if (nextLine.equals(idValue)){
                                    pWriteSS.append(nextLine + "\n");	//only writes to log when record matches idValue
                                    found = true;
                                }
                            }
                            bReadSelect.close();
                            if (found){									//if something was selected, then return true
                                return true;							//if something was not selected, return false because the record to be selected did not exist
                            }
                            else {
                                Database.specificErrorStr = "Failed: " + idValue + " Not Found.\n";
                                return false;
                            }
                        }
                    }
                    else {					//error message to error file if "WHERE COLUMN = " is not located where it is supposed to be
                        //e.g. SELECT * FROM college.students    WHERE COLUMN ="John Smith"
                        //this command has too many spaces between the file path and does not have a space between = and the idValue
                        Database.specificErrorStr = "Failed: Command Error - \'WHERE COLUMN =\' could not be found."+"\n";
                        return false;
                    }
                }
            }								//if an IOException is caught, method appends the exception mesasge to the error file
            catch (IOException e){
                Database.specificErrorStr = "IOException: " + e.getMessage() +"\n";
                return false;
            }
        }

        //  DELETE FROM method
        //-------------------------------------------------------------------------------------------
        // Author: 		Chanhee Lee
        // Date: 		02/06/17
        // Purpose: 	Deletes all records (rows) from a table (file) with the given abstract path
        // Parameters: 	String lineNow - the string containing part of the command that the method
        // 				takes in, in order to parse it and determine the name of the table and database
        //				then deletes the records in the file
        //				PrintWriter pWriteError - Takes in a PrintWriter that allows this method to append
        //				to the errorStr from this class if there is an exception or error
        //              String specificErrorStr - allows this class to store specific error messages for the
        //              Database class to use to write to the error text area
        // Example Command:	DELETE FROM colleges.students;
        //------------------------------------------------------------------------------------------
        public boolean deleteFrom(String lineNow, PrintWriter pWriteStatus, PrintWriter pWriteError, String specificErrorStr) throws IOException{
            try {
                if (!lineNow.contains(";")){
                    Database.specificErrorStr = "Failed: Incorrect Syntax - Could not find ';'\n";
                    return false;
                }
                lineNow = lineNow.replace(";","");
                String from = lineNow.substring(0, lineNow.indexOf("M")+1);		//substrings FROM
                lineNow = lineNow.substring(lineNow.indexOf(" ", 3)+1);			//removes "FROM "
                if (!from.equals("FROM")){										//if from variable is not FROM, it means that there is an issue with the command's syntax
                    Database.specificErrorStr = "Command Error: FROM not found.\n";		//which will result in an error that will be caught and appended into the error file
                    return false;
                }
                else {															//if there is a FROM, the command has the appropriate syntax and the program continues
                    String dbName = lineNow.substring(0,lineNow.indexOf("."));
                    lineNow = lineNow.substring(lineNow.indexOf(".")+1);		//removes dbName from lineNow
                    String tabName = lineNow;
                    File db = new File(dbName);									//sets up File object for database
                    File tab = new File(dbName + "\\" + tabName);				//sets up File object for table with file's path to be under dbName
                    //if database with dbName exists:
                    if(!db.exists()){											//error caught if the database to delete from does not exist
                        Database.specificErrorStr = "Failed: Database, " + dbName + " does not exist.\n";
                        return false;
                    }
                    else if(!tab.exists()){										//error caught if the table to delete from does not exist
                        Database.specificErrorStr = "Failed: File, " + tabName + " does not exist.\n";
                        return false;
                    }
                    else if(db.exists() && tab.exists()){						//file deleted if both the table and respective database (directory) exists
                        tab.delete();
                        tab.createNewFile();									//creates new empty file which is the equivalent of deleting all contents of a file
                        return true;
                    }
                    else{
                        Database.specificErrorStr = "Other Error.";
                        return false;
                    }
                }
            }
            catch (IOException e){
                Database.specificErrorStr = "IOException: " + e.getMessage() +"\n";
                return false;
            }
        }

        //  DELETE FROM WHERE method
        //-------------------------------------------------------------------------------------------
        // Author: 		Chanhee Lee
        // Date: 		02/06/17
        // Purpose: 	Deletes all records (rows) from a table (file) with the given abstract path that
        //				matches the column record that the command has specified
        // Parameter: 	String lineNow - the string that contains part of the command that the method
        // 				takes in, in order to parse it and determine the name of the table and database
        //				then deletes the records that match the given column record in the file, to a
        //				log file (e.g. status.txt)
        //				PrintWriter pWriteError - Takes in a PrintWriter that allows this method to append
        //				to the errorStr from this class if there is an exception or error
        //              String specificErrorStr - allows this class to store specific error messages for the
        //              Database class to use to write to the error text area
        // Example Command:	DELETE FROM colleges.students WHERE COLUMN = "John Smith";
        //------------------------------------------------------------------------------------------
        public boolean deleteFromWhere(String lineNow, PrintWriter pWriteStatus, PrintWriter pWriteError, String specificErrorStr) throws Exception{
            try {
                if (!lineNow.contains(";")){
                    Database.specificErrorStr = "Failed: Incorrect Syntax - Could not find ';'\n";
                    return false;
                }
                lineNow = lineNow.replace(";","");
                String from = lineNow.substring(0, lineNow.indexOf("M")+1);			//substrings FROM
                lineNow = lineNow.substring(lineNow.indexOf(" ", 3)+1);				//removes "FROM " from lineNow
                String dbName = lineNow.substring(0, lineNow.indexOf('.'));			//gets dbName
                lineNow = lineNow.substring(lineNow.indexOf('.')+1);				//removes dbName and .
                String tabName = lineNow.substring(0, lineNow.indexOf(' '));		//gets tabName
                lineNow = lineNow.substring(lineNow.indexOf(' ')+1);				//removes tabName and space
                String lineNow2 = dbName + '.' + tabName + ";";							//path of the file that will later be passed into the insertInto() method as a parameter

                if (lineNow.matches("WHERE COLUMN = (.*)")){						//checks that there is a "WHERE COLUMN =" where it should be  in the command
                    lineNow = lineNow.substring(15);								//removes "WHERE COLUMN = "
                    String idValue = lineNow.substring(1, lineNow.indexOf('"', 1));	//gets idValue and stores it in a variable
                    //program reads the file until (line == idvalue) then appends idvalue to the log each time it is encountered
                    File f = new File(dbName + "\\" + tabName);
                    BufferedReader bReadSelect = new BufferedReader(new FileReader(f));
                    if (!bReadSelect.ready()){										//error if path is not ready to be read (which means it is empty)
                        Database.specificErrorStr = "Failed: 0 Records Found. Cannot Delete From Empty File.\n";
                        bReadSelect.close();
                        return false;
                    }
                    else{		//if the file is not empty, program proceeds to select records that match the idValue
                        String nextLine = "";
                        String tempLine = "";
                        while(bReadSelect.ready()){		//reads and then adds it to a new line of string surrounded by "-"
                            tempLine = tempLine + "-" + bReadSelect.readLine() + "-" + "\n";	//the "-" is where the " character would usually be for a record entered
                        }																		//in the insertInto() command
                        bReadSelect.close();
                        f.delete();				//deletes the whole file
                        f.createNewFile();		//creates a new empty file with the same path
                        String tempLine2 = "";
                        Scanner sReader1 = new Scanner(tempLine);
                        while (sReader1.hasNextLine()){							//compares each record of the file to the specified current file
                            String nextLine1 = sReader1.nextLine();
                            if (nextLine1.equals('-'+idValue+'-'))	{		//if the line equals specified idValue, deletes that idValue
                                nextLine1 = nextLine1.replace(idValue, "") + "\n";
                                tempLine2 = tempLine2 + nextLine1;
                            }
                            else{											//else the line does not equal the specified idValue, it just adds the unmodified line from tempLine
                                nextLine1 = nextLine1 + "\n";
                                tempLine2 = tempLine2 + nextLine1;
                            }
                        }
                        sReader1.close();
                        Scanner sReader2 = new  Scanner(tempLine2);	//instantiates a Scanner object to read the tempLine with the modified contents of the original file as a string
                        boolean deleted = false; 					//keeps track of whether whatever was supposed to be deleted, existed
                        while(sReader2.hasNextLine()){				//reads the string as long as it has a next line
                            nextLine = sReader2.nextLine();			//sets nextLine variable to the next line of the tempLine string
                            if (!nextLine.equals("--")){			//"--"  indicates an empty line, if the line is not empty, insertInto() method is invoked to insert the line in tempLine to the file
                                this.insertInto(lineNow2, nextLine, pWriteStatus, pWriteError, specificErrorStr);////
                            }
                            else {
                                deleted = true;
                            }
                        }
                        sReader2.close();
                        if (deleted){								//if something was deleted, then return true
                            return true;							//if something was not deleted, return false because the record to be deleted did not exist
                        }
                        else {
                            Database.specificErrorStr = "Failed: " + idValue + " Not Found.\n";
                            return false;
                        }
                    }
                }
                else{		//error is appended to error file if "WHERE COLUMN = " does not exist in the right syntax or position of the command
                    Database.specificErrorStr = "Failed: Command Error - \'WHERE COLUMN =\' could not be found."+"\n";
                    return false;
                }
            }
            catch (Exception e) {		//any exceptions appended to error file
                Database.specificErrorStr = "Exception: " + e.getMessage() +"\n";
                return false;
            }
        }

        //  UPDATE SET WHERE FROM WHERE method
        //-------------------------------------------------------------------------------------------
        // Author: 		Chanhee Lee
        // Date: 		02/06/17
        // Purpose: 	Updates all records (rows) from a table (file) with the given abstract path that
        //				matches the column record that the command has specified, with a new column record
        //				that thAe program has specified
        // Parameters: 	String lineNow - the string that contains part of the command that the method
        // 				takes in, in order to parse it and determine the name of the table and database
        //				then deletes the records that match the given column record in the file, to different
        //				column record that has been specified
        //				PrintWriter pWriteError - Takes in a PrintWriter that allows this method to append
        //				to the errorStr from this class if there is an exception or error
        //              String specificErrorStr - allows this class to store specific error messages for the
        //              Database class to use to write to the error text area
        // Example Command:	UPDATE college.students SET COLUMN = "The Martian" WHERE COLUMN = "John Smith";
        //------------------------------------------------------------------------------------------
        public boolean updateSetWhere(String lineNow, PrintWriter pWriteStatus, PrintWriter pWriteError, String specificErrorStr) throws Exception {
            try{
                if (!lineNow.contains(";")){
                    Database.specificErrorStr = "Failed: Incorrect Syntax - Could not find ';'\n";
                    return false;
                }
                lineNow = lineNow.replace(";","");
                String dbName = lineNow.substring(0, lineNow.indexOf('.'));			//gets dbName
                lineNow = lineNow.substring(lineNow.indexOf('.')+1);				//removes dbName and .
                String tabName = lineNow.substring(0, lineNow.indexOf(' '));		//gets tabName
                lineNow = lineNow.substring(lineNow.indexOf(' ')+1);				//removes tabName and space
                if (!lineNow.substring(0, 13).equals("SET COLUMN = ")){		//checks for "SET COLUMN = " as a part of the command line being read
                    Database.specificErrorStr = "Failed: Command Error - \"SET COLUMN =\" not found. "+"\n"; //error appended to error file if it cannot be found
                    return false;
                }
                lineNow = lineNow.substring(13);									//removes "SET COLUMN = "
                String newVal = lineNow.substring(1, lineNow.indexOf('"',1));		//stores  newVal to replace currentVal w ith
                lineNow = lineNow.substring(lineNow.indexOf('"',1)+2);				//removes ""newvalue" "
                if(!lineNow.substring(0,15).equals("WHERE COLUMN = ")){				//checks for "WHERE COLUMN = " as a part of the command line being read
                    Database.specificErrorStr = "Failed: Command Error - \"WHERE COLUMN =\" not found. "+"\n"; //error appended to error file if it cannot be found
                    return false;
                }
                lineNow = lineNow.substring(15);
                String currentVal = lineNow.substring(1,lineNow.indexOf('\"',1));	//stores currentVal
                File f = new File(dbName + "\\" + tabName);
                BufferedReader bReadUpdate = new BufferedReader(new FileReader(f));

                if (!f.exists()){		//error appended to error file if the table or the database that it is supposed to be in does not exist
                    Database.specificErrorStr = "Failed: File," + tabName + "does not exist.\n";
                    bReadUpdate.close();
                    return false;
                }
                else if (!bReadUpdate.ready()){		//error if path is not ready to be read (which means it is empty)
                    Database.specificErrorStr = "Failed: 0 Records Found. Cannot Update From Empty File.\n";
                    bReadUpdate.close();
                    return false;
                }
                else{											//if there are none of the errors checked above are caught, the program continues
                    String tempLine = "";
                    String lineNow2 = dbName + '.' + tabName + ";";	//makes a line that will be passed into the insertInto() method as a parameter
                    boolean found = false;                          //keeps track of whether the text to be updated has been found
                    while(bReadUpdate.ready()){												//adds '-' before and after every line
                        tempLine = tempLine + "-" + bReadUpdate.readLine() + "-" + "\n";	//the "-" is where the " character would usually be for a record entered
                        //in the insertInto() command
                    }
                    bReadUpdate.close();
                    f.delete();			//deletes file
                    f.createNewFile();	//creates new empty file
                    Scanner sReader1 = new Scanner(tempLine);				//first scanner to replace each line of the file
                    String tempLine2 = "";
                    while (sReader1.hasNextLine()){							//compares each record of the file to the specified current file
                        String nextLine1 = sReader1.nextLine();
                        if (nextLine1.equals('-'+currentVal+'-'))	{		//if the line equals specified currentVal, replace that currentVal with the newVal
                            nextLine1 = nextLine1.replace(currentVal, newVal) + "\n";
                            tempLine2 = tempLine2 + nextLine1;
                            found = true;
                        }
                        else{												//else the line does not equal the specified currentVal, it just adds the unmodified line from tempLine
                            nextLine1 = nextLine1 + "\n";
                            tempLine2 = tempLine2 + nextLine1;
                        }
                    }														//this loop adds modified or same lines from tempFile to tempFile2
                    sReader1.close();
                    Scanner sReader2 = new Scanner(tempLine2);				//the tempLine which contains the updated contents of the file as a string is read by a scanner

                    while(sReader2.hasNextLine()){							//while the file has lines to read, the method invokes the insertInto() method to add each line of the tempLine into the file
                        this.insertInto(lineNow2, sReader2.nextLine(), pWriteStatus, pWriteError, specificErrorStr);
                    }
                    sReader2.close();
                    if (found)
                        return true;
                    else {
                        Database.specificErrorStr = "Row, \"" + currentVal + "\", you are trying to replace cannot be found.\n";
                        return false;
                    }
                }

            }
            catch (IOException e){			//any exceptions caught are appended into the error file
                Database.specificErrorStr = "IOException: " + e.getMessage() +"\n";
                return false;
            }

        }
        //These methods return boolean variables so that the main method can use the booleans to determine whether it has been executed successfully
        //this will help to determine whether the program needs to append a success or failure to the statusStr and/or the errorStr
    }