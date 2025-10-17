
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
//import java.nio.file.*;
import java.util.Scanner;
import java.nio.file.StandardCopyOption;
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
        } else if (cmd.equals("WC")) {
            WC();
        } else if (cmd.equals("cp") && parser.getArgs().length == 3 && parser.getArgs()[0].equals("-r")) {
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
        System.out.println(path.toAbsolutePath());
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
    }

    //ls command
    public void ls() {

    }

    //mkdir command
    public void mkdir() {
        for (String arg : parser.getArgs()) {
            Path dir = Paths.get(arg);

            if (!dir.isAbsolute()) {
                dir = path.resolve(dir);
            }

            if (Files.exists(dir)) {
                System.out.println("Directory already exists: " + dir);
            } else {
                try {
                    Files.createDirectories(dir);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
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
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("rmdir: error deleting directory");
        }
    }

    //touch command
    public void touch() {

    }

    //cp commands
    public void cp() {
        if (parser.getArgs().length != 2) {
            System.out.println("cp command requires 2 arguments");
            return;
        }
        Path source = path.resolve(parser.getArgs()[0]);
        Path destination = path.resolve(parser.getArgs()[1]);

        try {
            if (!Files.exists(source)) { //checks if the source exists
                System.out.println("source file does not exist");
                return;
            }

            if (Files.isDirectory(source)) { //checks if the file is directory
                System.out.println("cannot copy a directory");
                return;
            }

            // copy and overwrite if destination exists
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File copied successfully.");

        } catch (IOException e) {
            System.out.println("Error copying file: " + e.getMessage());
        }
    }

    //rm file.txt this take the argument which is file.txt and see if it exits and delete it
    public void cp_r(String sourcedir, String destdir) {
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

            System.out.println("Copied successfully!");
        } catch (IOException e) {
            System.out.println("Error while copying: " + e.getMessage());
        }
    }

    public void rm() {
        String[] args = parser.getArgs();
        // we are taking only one argument if less or more we will return
        if (args.length != 1) {
            System.out.println("write the file");
            return;
        }
        String fileName = args[0];
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

        // if no arguments
        if (args.length == 0) {
            System.out.println("cat should contain at least one argument");
            return;
        }

        // One argument- print file content
        if (args.length == 1) {
            Path filePath = path.resolve(args[0]);
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

        // Two arguments- concatenate files
        if (args.length == 2) {
            Path firstFile = path.resolve(args[0]);
            Path secondFile = path.resolve(args[1]);

            if (!Files.exists(firstFile)) {
                System.out.println("File not found: " + args[0]);
                return;
            }

            if (!Files.exists(secondFile)) {
                System.out.println("File not found: " + args[1]);
                return;
            }

            try {
                // Read from first file and append to second
                String content = Files.readString(firstFile);
                Files.writeString(secondFile, content, java.nio.file.StandardOpenOption.APPEND);
                System.out.println("Contents of " + args[0] + " appended to " + args[1]);
            } catch (IOException e) {
                System.out.println("Error during concatenation: " + e.getMessage());
            }
            return;
        }
    }

    //WC command
    public void WC() {

    }

    // zip command
    public void zip() {
        try {
            String[] args = parser.getArgs();

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

    //unzip command
    public void unzip() {

    }

    // exit command stops the programm
    public void exit() {
        System.exit(0); // Ends the program
    }

    // ......
    public void path() {
        System.out.println(path);
    }

    public static void main(String[] args) {
        // deh al bn read beha al input zy cin keda
        Scanner sc = new Scanner(System.in);
        Parser parser = new Parser();
        Terminal terminal = new Terminal(parser);

        while (true) {
            System.out.print("> ");
            String input = sc.nextLine();
            if (parser.parse(input)) {
                terminal.chooseCommandAction();
            }
        }

    }
}
