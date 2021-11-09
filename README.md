# [JAVA_GUI_File_Manager](https://github.com/JiaxuanCai/JAVA_GUI_File_Manager)

Spring 2019-2020 JAVA Programming course homeworks -- Chongqing University. Include my source codes and reports.

#### Contents:

1. achieve folder creation, delete, enter.

2. the realization of the current folder under the list of content.

3. Realize file copy and folder copy (folder copy refers to deep copy, including all subdirectories and files).

4. Encrypt and decrypt the specified files.

5. achieve the specified file and folder compression.

6. to achieve the decompression of compressed files.

7. The document manager has a graphical interface.



#### Design of Classes

Using the MVC design pattern, the program has Model, View and Control three parts:

(1) Model

Contains the FileTree class, which is a custom data structure for the formation and display of the left tree file management interface.

(2) the View

Contains the MainFrame class for displaying the main interface and the MyCellRenderer class for rerendering the generated interface.

(3) Control

Contains classes such as AccessFile and AccessIcon for operation of the entire program function. For the functionality of specific classes, see the class functionality analysis below.



##### Description of various functions

###### (1) AccessFile

Gets the filename or folder name of the current path and all subordinate file names under the current path (if the current path is a folder)

GetSingleName: Gets the name of the file or folder in the current path

GetAllName: Gets the names of all subordinate files or folders under the current path (if the current path is a folder)



###### (2) AccessIcon class:

Get the small ICONS corresponding to the file name or folder of the current path, and the small ICONS corresponding to all subordinate files of the current path (if folder) for file tree rendering

GetSingleIcon: Small icon to get a file or folder in the current path

GetAllIcon: Gets a small icon of all subordinate files or folders under the current path (if the current path is a folder)



###### (3) CopyFileAndFolder:

To copy folders or files, click Copy in the function box that is displayed

Init: Initializes a member variable of a class, leaving it in its initial state. The purpose is to reset the member variable after each paste, that is, each copy of the content after a paste after the failure, imitate the Windows file management system.

GenerateDir: Source path used during copy generation based on the selected path, which is convenient for pasting.



###### (4)DeleteFileAndFolder class:

Delete a file or directory

Delete Directly receives the path to be deleted

DeleteFile and deleteFolder are static and can be called separately outside the class, but we use them as utility functions here.



###### (5) DirectoryHelp class:

Iterate over capital letters A-z to get drive letters

FindDisk obtains the drive letter of the path by iterating



###### (6)LockAndUnlock class:

The operation of encrypting and decrypting files

EncryptFile: Gets the path to the current file and the folder where the file is located, along with the secret keys

Functions of the secret key: encryption as an operation, set by the user; Decryption is used to restore computations and can only be decrypted if the user enters the correct secret key



###### (7)PasteFileAndFolder class:

Paste the copied content, calling Either pasteFile or pasteFolder, depending on the type of the copied content

PasteFile: paste a file

PasteFolder: Paste folders

ZipAndUnzip class:

Compress and unzip files

Zip: compressed file

Unzip: Decompresses the file



###### (9)FileTree class:

Custom data result FileTree, used to mimic Windows style file manager, generates a FileTree on the left side

Modified from Github_Kirill Grouchnikov open source project



###### (10) MainFrame class:

The main interface of the file operating system

It belongs to the VIEW part of the MVC design structure

Undertake the task of interface interaction with the user



###### (11)MyCellRenderer class:

Rewrite the re-render class

Used to re-render ICONS

Undertake the task of interface interaction with the user



#### Design of Process

AccessFile, AccessIcon as a tool class, used to get the name and icon of the file, used to get the icon and name in the panel, so that there is a rendering of the material;

CopyFileAndFolder and PasteFileAndFolder are used to copy and paste selected files or folders, respectively;

DeleteFileAndFolder Is used to delete a single file/folder or multiple files/folders that are selected.

DirectoryHelp is used to find the corresponding drive letter in the system root directory for the current path;

LockAndUnlock encrypts or decrypts a selected file.

ZipandUnzip is used to compress the selected folder/file or multiple folders/files and decompress the selected compressed files.



See codes and corresponding lab reports in the folder of each lab project.
