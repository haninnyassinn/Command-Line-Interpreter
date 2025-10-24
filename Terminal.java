/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.terminal;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class Parser {

    private String commandName;
    private String[] args = null;

    //function to specerate the command line from the arguments
    public boolean parse(String input) {
        if (input.isEmpty()) {
            return false; // making sure if its empty nothing happens
        }
        String[] words = input.trim().split("\\s+"); // used trim to make sure no spaces before or after, used (\\s+) to make sure if there is more than space doesnt consider an arg

        commandName = words[0];
        args = new String[words.length - 1];  // size of arge is number of words - command name

        for (int i = 1; i < words.length; i++) {
            args[i - 1] = words[i];
        }

        return true;
    }

    //getters
    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }
}

public class Terminal {

    private Parser parser; //object from class Parser so that we get command and args
    private Path path;

    //constructor
    Terminal(Parser p) {
        parser = p;
        path = Paths.get(System.getProperty("user.dir"));
    }

    // Choosing the right command
    public void chooseCommandAction() {
        String cmd = parser.getCommandName(); // a string that save the command needed
        if (cmd.equals("pwd")) {
            pwd();
        } else if (cmd.equals("cd")) {
            cd();
        } else if (cmd.equals("ls")) {
            ls();
        } else if (cmd.equals("mkdir")) {
            mkdir();
        } else if (cmd.equals("rmdir")) {
            rmdir();
        } else if (cmd.equals("touch")) {
            touch();
        } else if (cmd.equals("rm")) {
            rm();
        } else if (cmd.equals("cat")) {
            cat();
        } else if (cmd.equals("wc")) {
            WC();
        } else if (cmd.equals("cp") && parser.getArgs().length >= 3 && parser.getArgs()[0].equals("-r")) {
            cp_r(parser.getArgs()[1], parser.getArgs()[2]);
        } else if (cmd.equals("cp")) {
            cp();
        } else if (cmd.equals("zip")) {
            zip();
        } else if (cmd.equals("unzip")) {
            unzip();
        } else if (cmd.equals("exit")) {
            System.exit(0);
        } else {
            System.out.println("this is an unknown command : " + cmd);
        }

    }

    // commands

    //pwd command which gets the current path we are working in
    public void pwd() {

        String data=path.toAbsolutePath().toString();
        boolean flag = false;

        // this checks that the argument contain > so it goes to redirect function also the i+1 checks that there is an argument after >
        for (int i = 0; i < parser.getArgs().length; i++) {
            if (parser.getArgs()[i].equals(">")  && (i + 1) < parser.getArgs().length) {

                redirect(data);
                flag = true;}
        }
        for (int i = 0; i < parser.getArgs().length; i++) {
            if (parser.getArgs()[i].equals(">>")  && (i + 1) < parser.getArgs().length) {

                append(data);
                flag = true;}
        }
        if (flag == false) {
            System.out.println(path.toAbsolutePath());}

    }

    // cd command
    public void cd() {
        if (parser.getArgs().length == 0) {
            path = Paths.get(System.getProperty("user.home"));
        } else if (parser.getArgs()[0].equals("..")) {
            path = path.getParent();
        } else if (parser.getArgs().length == 1) {
            Path inputPath = path.resolve(Paths.get(parser.getArgs()[0])).normalize();
            if (!Files.exists(inputPath)) {
                System.out.print("bash: cd: " + inputPath + ": No such file or directory\n");
            } else {
                if (inputPath.isAbsolute()) {
                    path = inputPath.normalize();
                } else {
                    path = inputPath;
                }
            }
        }
        append (path.toString());
    }
    //ls command
    public void swap(File[]files,int i,int j){
        File temp=files[i];
        files[i]=files[j];
        files[j]=temp;
    }

    public void selectionSort(File[] file){
        for(int i=0;i<file.length-1;i++){
            for(int j=i+1;j<file.length;j++){
                String file1=file[i].getName().toUpperCase();
                String file2=file[j].getName().toUpperCase();
                if(file1.compareTo(file2) > 0){// file1 > file2 -> >0 ,file1<fil2 -> <0 ,file1=file2 -> =0
                    swap(file,i,j);
                }
            }
        }

    }

    public void ls(){
        String output=" ";
        boolean flag = false;
        String[] args = parser.getArgs();

        if (args.length > 0 && !args[0].equals(">") && !args[0].equals(">>")) {
            System.out.println("'ls' does not take any arguments.");
            return;
        }

        for (int i = 0; i < parser.getArgs().length; i++) {
            if (parser.getArgs()[i].equals(">") || parser.getArgs()[i].equals(">>") ){
                flag = true;
                break;}
        }
        File current=new File(path.toString());//obj from file
        if(!current.isDirectory()||!current.exists()){
            System.out.println("Not a directory");
            return;
        }


        if (current != null) {// not empty
            File[] file = current.listFiles();//list the files
            selectionSort(file);//sort
            for (int i = 0; i < file.length; i++) {
                File files = file[i];
                if (files.isDirectory()) {// make sure if its folder or not
                    output += files.getName() + "(folder)" + "\n";
                    if (flag==false){ System.out.println(files.getName() + "(folder)");}
                } else {
                    output += files.getName() + "\n";
                    if (flag==false){System.out.println(files.getName());}
                }
            }
        }
        else{
            System.out.println("Directory is emty");
        }
        for (int i = 0; i < parser.getArgs().length; i++) {
            if (parser.getArgs()[i].equals(">") && (i + 1) < parser.getArgs().length) {
                redirect(output);
                break;
            }
        }

        append(output);
    }
    //mkdir command
    //mkdir command
    public void mkdir() {
        String data=" ";
        if (parser.getArgs().length == 0) {
            System.out.println("mkdir: missing argument");
            return;
        }

        for (String arg : parser.getArgs()) {
            Path dir = Paths.get(arg);

            if (Arrays.asList(parser.getArgs()).contains(">>")) {
                append(Paths.get(arg).toString());
            }
            if (!dir.isAbsolute()) {
                dir = path.resolve(dir);
            }

            if (Files.exists(dir)) {
                data="Directory already exists: " + dir;

                System.out.println(data);
            } else {
                try {
                    Files.createDirectories(dir);

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
        }


    }

    //rmdir command
    public void rmdir() {
        try {
            if (parser.getArgs().length == 0) {
                System.out.println("rmdir: missing argument");
                return;
            }
            if (parser.getArgs().length > 1) {
                System.out.println("rmdir: too many arguments");
                return;
            }
            if (parser.getArgs()[0].equals("*")) {
                File[] files = new File(path.toString()).listFiles();
                if (files == null) {
                    System.out.println("rmdir: cannot read directory contents");
                    return;
                }
                for (File file : files) {
                    if (Files.isDirectory(file.toPath())) {
                        String[] contents = file.list();
                        if (contents != null && contents.length == 0) {
                            Files.delete(file.toPath());
                            System.out.println("Removed empty directory: " + file.getName());
                        }
                        else {
                            System.out.println("The directory is not empty.");
                        }
                    }
                }
            } else if (parser.getArgs().length == 1) {
                Path dir = Paths.get(parser.getArgs()[0]);
                if (!dir.isAbsolute()) {
                    dir = path.resolve(dir);
                }

                File file = new File(dir.toString());

                if (!file.exists()) {
                    System.out.println("error: the file not exist");
                    return;
                }

                if (Files.isDirectory(file.toPath())) {
                    String[] contents = file.list();
                    if (contents != null && contents.length == 0) {
                        Files.delete(file.toPath());
                        System.out.println("Removed empty directory: " + file.getName());
                    }
                    else {
                        System.out.println("The directory is not empty.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("rmdir: error deleting directory");
        }
    }

    //touch command
    public void touch(){
        File newfile = null;
        try {
            String[] args = parser.getArgs();

            if (args.length < 1) {
                System.out.println("touch <filename>");
                return;
            }
            String fileName = String.join(" ", args);
            File newfiles =new File( fileName);
            if (newfiles.isAbsolute()) {
                newfile=newfiles;
            }
            else{
                newfile = new File(path.toFile(), fileName);}
            File parent=newfile.getParentFile();
            if (parent != null && !parent.exists()) {
                System.out.println(" Parent folder does not exist: " + newfile.getParentFile().getAbsolutePath());
                return;
            }

            if (newfile.createNewFile()) {           // create file
                System.out.println("File created: " + newfile.getAbsolutePath());
            } else {
                System.out.println("File already exists"+ newfile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace(); // Print error details
            return;
        }
        String files = newfile.getAbsolutePath();

        append(files);
    }

    //cp commands
    public void cp() {
        boolean found = false;

        // Check if there is >>
        if (parser.getArgs().length > 2 && parser.getArgs()[2].equals(">>")) {
            found = true;
        }
        if (parser.getArgs().length < 2) {
            System.out.println("cp command requires 2 arguments");
            return;
        }
        Path source = path.resolve(parser.getArgs()[0]);
        Path destination = path.resolve(parser.getArgs()[1]);
        if(found){
            append(source +"->" + destination);}
        try {
            if (!Files.exists(source)) { //checks if the source exists
                if(!found)  System.out.println("source file does not exist");
                return;
            }

            if (Files.isDirectory(source)) { //checks if the file is directory
                if(!found) System.out.println("cannot copy a directory");
                return;
            }

            // copy and overwrite if destination exists
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

            if(!found) System.out.println("File copied successfully.");


        } catch (IOException e) {
            System.out.println("Error copying file: " + e.getMessage());
        }

    }

    //cp_r command
    public void cp_r(String sourcedir, String destdir) {
        boolean found = false;


        // Check if there is >>
        for (int i = 0; i < parser.getArgs().length; i++) {
            if (parser.getArgs()[i].equals(">>")) {
                found = true;
                break;
            }
        }
        Path source = path.resolve(sourcedir);
        Path destination = path.resolve(destdir);
        try {
            if (!Files.exists(source)) {
                System.out.println("cp: cannot stat '" + sourcedir + "': No such file or directory");
                return;
            }

            if (Files.isDirectory(source)) {
                // if the destination dir is not found
                Files.createDirectories(destination.resolve(source.getFileName()));

                File[] files = source.toFile().listFiles();
                if (files != null) {
                    for (File f : files) {
                        Path target = destination.resolve(source.getFileName()).resolve(f.getName());
                        if (f.isDirectory()) {
                            // Call the function again if there is a folder inside another folder
                            cp_r(f.getPath(), destination.resolve(source.getFileName()).toString());
                        } else {
                            Files.copy(f.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            } else {
                Path target = destination.resolve(source.getFileName());
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            }

            if(!found ) { System.out.println("Copied successfully!");}
            if(found){
                append(source +"->" + destination);}
        } catch (IOException e) {
            System.out.println("Error while copying: " + e.getMessage());
        }
    }
    //rm file.txt this take the argument which is file.txt and see if it exits and delete it
    public void rm() {
        String[] args = parser.getArgs();
        // we are taking only one argument if less or more we will return

        String fileName ;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(">>")) {
                fileName = args[i - 1];
                Path filePath = path.resolve(fileName);

                if (Files.exists(filePath)) {
                    try {
                        Files.delete(filePath);
                        System.out.println("Deleted: " + fileName);
                        append( "Deleted"+fileName);
                    } catch (IOException e) {
                        System.out.println("Error deleting file: " + e.getMessage());
                    }
                } else {
                    System.out.println(fileName + " not found");
                }
                return;
            }
        }
        if (args.length != 1) {
            System.out.println("write the file");
            return;
        }

        fileName = args[0];
        Path filePath = path.resolve(fileName); //getting fall path of the file in the computer

        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath); // deleting the file
                System.out.println("Deleted: " + fileName);
            } catch (IOException e) {
                System.out.println("there is a problem in deleting the file :" + fileName);
            } // the file might be locked
        } else {
            System.out.println(fileName + " cant be deleted as it is not found");
        }

    }
    //cat command
    public void cat() {
        String[] args = parser.getArgs();
        String content="";
        Path filePath;
        String data;
        Path srcFile;
        boolean found=false;


        // if no arguments
        if (args.length == 0) {
            System.out.println("cat should contain at least one argument");
            return;
        }

        // > , >>in cat function
        for (int i = 0; i < args.length; i++) {
            if ((args[i].equals(">")|| args[i].equals(">>"))&& (i + 1) < args.length) {

                String info = ""; // store the arguments before >
                for (int j = 0; j < i; j++)  // this loop to get the info before >
                {
                    Path file = path.resolve(args[j]); // get the path for the agrument
                    if (!Files.exists(file)) {
                        System.out.println("File not found: " + args[j]);
                        return;
                    }
                    try {
                        info += Files.readString(file); // read file/files info
                    } catch (IOException e) {
                        System.out.println("Error reading file: " + args[j]);
                        return;
                    }
                }

                if(args[i].equals(">")){redirect(info);}// writing all the content of file/files in the file after >
                else if (args[i].equals(">>")){append(info);}
                return; // stop function
            }
        }


        // One argument- print file content

        if (args.length == 1) {
            filePath = path.resolve(args[0]);
            if (!Files.exists(filePath)) { //chacks if file exists
                System.out.println("File not found: " + args[0]);
                return;
            }

            try {
                // Read and print file content line by line
                Files.lines(filePath).forEach(System.out::println);
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
            return;
        }

        // Multiple arguments (print all contents to screen)
for (int i = 0; i < args.length; i++) {
    Path currentFile = path.resolve(args[i]);
    if (!Files.exists(currentFile)) {
        System.out.println("File not found: " + args[i]);
        continue;
    }

    try {
        System.out.println("----- " + args[i] + " -----");
        Files.lines(currentFile).forEach(System.out::println);
    } catch (IOException e) {
        System.out.println("Error reading file: " + args[i]);
    }
}

    }

    //WC command
    public void WC() {
        String[] args = parser.getArgs();
        String characters;
        boolean found = false;
        // the flag is to print it only in the file so if there isnt > then print it normally if there is the flag will be true
        boolean flag = false;

        // Check if there is >>
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(">>")) {
                found = true;
                break;
            }
        }


        if (args.length < 1) {
            if(!found)  System.out.println("WC command needs one argument");
            return;
        }

        Path filePath = path.resolve(args[0]);

        if (!Files.exists(filePath)) {
            if(!found)  System.out.println("File not found: " + args[0]);
            return;
        }

        try {
            int lineCount = 0;
            int wordCount = 0;
            int charCount = 0;

            // Read all lines from the file
            for (String line : Files.readAllLines(filePath)) {
                lineCount++;
                charCount += line.length();
                String[] words = line.trim().split("\\s+");
                if (words.length == 1 && words[0].isEmpty()) {
                    continue; // skip empty lines
                }
                wordCount += words.length;
            }

            characters = lineCount + " " + wordCount + " " + charCount + " " + args[0];

            if (found) {
                append(characters);
            }
            // this checks that the argument contain > so it goes to redirect function also the i+1 checks that there is an argument after >
            for (int i = 0; i < parser.getArgs().length; i++) {
                if (parser.getArgs()[i].equals(">") && (i + 1) < parser.getArgs().length) {
                    redirect(characters);
                    flag = true;}
            }

            if(flag==false&&found==false  ){
                System.out.println(characters);}

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

    }
    //> command which overwrite a file by replacing the content and if it doesnt exist create new file
    public void redirect(String data) {
        String fileName="";
        for (int i = 0; i < parser.getArgs().length; i++) {
            if (parser.getArgs()[i].equals(">") ) {
                fileName=parser.getArgs()[i+1];
                break;}
        }
        //this means if the file exist then overwrite it
        try (FileWriter writer = new FileWriter(fileName, false)) {
            writer.write(data); // here we are writing data that are output of a command into the file
            System.out.println("Output written to " + fileName);
        } catch (IOException e) {
            System.out.println("we couldn't write to " + fileName );
        }
    }

    // >> command
    public void append(String data){
        String[]args=parser.getArgs();
        for(int i=0;i<args.length;i++){
            if(args[i].equals(">>")&& (i+1) < args.length){
                String filename=args[i+1];
                File file=new File(filename);
                if(!file.exists()){
                    try{
                        file.createNewFile();
                    }
                    catch (IOException e){
                        return;
                    }
                }
                try( FileWriter write=new FileWriter(args[i+1],true)){


                    write.write(data);

                }
                catch (IOException e){
                    return;
                }
            }
        }}
    // zip command
    public void zip() {
        try {
            String[] args = parser.getArgs();
            if (args.length >= 3 && args[0].equals("-r")) { //Making sure there are 3 arguments and if the first argument is -r then the whole directory will be zipped
                Path zipPath = path.resolve(args[1]);
                Path dirToZip = path.resolve(args[2]);

                if (!Files.exists(dirToZip) || !Files.isDirectory(dirToZip)) { //Check that the directory is already existed on the device
                    System.out.println("Directory not found: " + dirToZip);
                    return;
                }

                try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) { //zip the file
                    zipDirectoryRecursive(dirToZip, dirToZip, zos);
                    System.out.println("Directory compressed successfully!");
                } catch (IOException e) {
                    System.out.println("Error creating ZIP: " + e.getMessage());
                }
                return;
            }
            //Check that there are 2 argumenets
            if (args.length < 2) {
                System.out.println("Usage: zip archive_name.zip file1 file2 ...");
                return;
            }

            Path zipPath = path.resolve(args[0]);
            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {

                // From the first file to the last
                for (int i = 1; i < args.length; i++) {
                    Path filePath = path.resolve(args[i]);

                    if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                        // Add the file to the ZIP archive
                        ZipEntry entry = new ZipEntry(filePath.getFileName().toString());
                        zos.putNextEntry(entry);

                        // Copy the file content into the ZIP archive
                        Files.copy(filePath, zos);

                        zos.closeEntry();
                    } else {
                        System.out.println("File not found or is a directory: " + args[i]);
                    }
                }

                System.out.println("Files compressed successfully!");

            }

        } catch (IOException e) {
            System.out.println("Error creating ZIP: " + e.getMessage());
        }
    }


    private void zipDirectoryRecursive(Path baseDir, Path current, ZipOutputStream zos) throws IOException { //Function the zip the whole directory with its files
        for (File file : current.toFile().listFiles()) {
            Path filePath = file.toPath();
            String entryName = baseDir.relativize(filePath).toString().replace("\\", "/");

            if (file.isDirectory()) {
                zipDirectoryRecursive(baseDir, filePath, zos);
            } else {
                zos.putNextEntry(new ZipEntry(entryName));
                Files.copy(filePath, zos);
                zos.closeEntry();
            }
        }
    }


        //unzip command
    public void unzip() {
            String[] args = parser.getArgs();

            if (args.length < 1) { //make sure that the user typed in a path and if not an error message will be displayed
                System.out.println("You should write a zip file name for this command");
                return;
            }

            Path zipFilePath = path.resolve(args[0]);
            Path destDir = path;

            for (int i = 1; i < args.length - 1; i++) {
                if (args[i].equals("-d")) {
                    destDir = Paths.get(args[i + 1]);
                    if (!destDir.isAbsolute()) {
                        destDir = path.resolve(destDir);
                    }
                    break;
                }
            }

            if (!Files.exists(zipFilePath)) {
                System.out.println("Zip file not found: " + zipFilePath);
                return;
            }

            try (java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(Files.newInputStream(zipFilePath))) {
                java.util.zip.ZipEntry entry;

                while ((entry = zis.getNextEntry()) != null) {
                    Path newFilePath = destDir.resolve(entry.getName()).normalize();
                    if (entry.isDirectory()) {
                        Files.createDirectories(newFilePath);
                    } else {
                        Files.createDirectories(newFilePath.getParent());
                        Files.copy(zis, newFilePath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }

                System.out.println("Unzipped successfully to: " + destDir);
            } catch (IOException e) {
                System.out.println("Error during unzip process: " + e.getMessage());
            }
        }


        // exit command stops the programm
    public void exit() {
        System.exit(0); // Ends the program
    }

    public void path() {
        System.out.println(path);
    }
    // Main cladd
    public static void main(String[] args) {
        // getting the command from the user
        Scanner sc = new Scanner(System.in);
        Parser parser = new Parser();
        Terminal terminal = new Terminal(parser);
        // it will loop until the input is exit and it will go to  terminal.chooseCommandAction(); where exit will stop the program
        while (true) {
            System.out.print("> ");
            String input = sc.nextLine();
            if (parser.parse(input)) {
                terminal.chooseCommandAction();
            }
        }

    }
}