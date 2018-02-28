Project 2

Chanhee Lee
811 Number: 811774216
Joshua Kim
811 Number: 8116750930

How To Run The Program: 
Program was not designed
to be runned on Nike. In order to run this program, please run it locally. If possible please install IntelliJ. Have the DBWorkBench
folder be placed inside another folder called project2. Then open IntelliJ and import the project2 folder wherever it was saved. 
Then open the Main.java inside /DBWorkBench/src/sample/ and run the program. Every time the program is closed and runned again, the 
files that were created from previous program run will be deleted. This will reset the directory and provide a clean slate for the 
user to create new databases and tables. Only files/directories that are needed for the program to run will be kept.

Project Overview:
The purpose behind this program is to allow the user to create, delete, and edit directories and files using an 
SQL Command Editor. The program will read data from a command file, parse each command, and perform a certain function 
based on each command. A command file can be opened from the File menu button with the Open SQL Script button. The commands within
the file can create and delete database, tables, and data using the commands create, drop, insert, select, and update. In order
to execute the commands, the Run SQL Script button in the File menu or the lightning bolt icon button can run all the commands 
within the SQL Command Editor. If the user wants to execute a specific command line, then the user must highlight the command 
that he/she wants to execute and click on the lightning bolt icon button with an I in it. To delete everything inside the SQL
Command Editor, the sweep icon button can be used. The user can also save all of his/her edits of a command file using the
Save SQL Script button in the File menu. The program will have a dedicated textbox that will output all of data of a 
specific file using the SELECT command. If there are multiple SELECT commands, then the textbox will display the data of 
the most recent SELECT command. The program will also have two other dedicated textboxes that will output whether a command 
succeeded or failed to execute. One textbox will hold the successfully executed commands. The other will hold the failed
commands. 

Project Instructions:
In order to call a command, the user must follow this specific syntax. Please be sure to end the command with a semicolon.
Below are the lists of commands that can be called.
CREATE DATABASE dbName;
DROP DATABASE dbName;
CREATE TABLE dbName.tableName;
DROP TABLE dbName.tableName;
INSERT "text" INTO dbName.tableName;
SELECT * FROM dbName.tableName WHERE COLUMN = "text";
SELECT * FROM dbName.tableName;
DELETE FROM dbName.tableName;
DELETE FROM dbName.tableName WHERE COLUMN = "text";
UPDATE dbName.tableName SET COLUMN = "newText" WHERE COLUMN = "text";
All of these commands are opened for the user to use. The create database command will create a directory with the name
given by the user. The drop database command will delete the directory specified by the user. The create table command 
will create a file within that directory. Drop table will then delete the file inside of the directory that was specified
by the user. The insert command allows the user to insert any text or information inside of a file creating records. The 
select * from command will take all the content within the the file that was specified by the user and input that 
information into a textbox area in the program where it will be displayed. The select * from where column command will take 
a specific text within a file and inputs that specific text into the textbox area holding the select command information. 
The delete from command deletes all of the content in a file and the delete from where column command deletes a specific line 
of text within a file. The update command will change a specific line in a file and replace with another line of text that the 
user provides. 

There will be two textboxes within the program that will display an output of whether the commands executed 
have succeeded or failed. The textbox with the status icon above it will hold all the status messages. The textbox
with the error icon above it will hold all the error messages.If the user tries to run all commands when the SQL 
Command Editor is empty, the program will do nothing. If the user does not highlight a specific command to run, 
the program will also do nothing. If the user tries to save without opening a SQL Script, then the program will have 
a dialog box pop up stating an error and that it cannot save because no file was opened. The preferences menu button 
will open another dialog box that gives the user options to select from. However, these buttons have no functionality 
and will do nothing when the user interacts with it. The about menu button will open a dialog that explains the purpose 
of the program. 

Additional Notes:
The SQL script should follow the correct syntax in order for the program to run. A semicolon (;) cannot be used anywhere in the
SQL script such as the database name or file name unless it is being used at the delimiter character.  The WorkBench will not 
automatically save the contents of the opened file unless the Save MenuItem is pressed while a file has been opened. The 
WorkBench will open empty files so that users can modify its contents in the SQL Commmand Editor and save the text to the file. 




