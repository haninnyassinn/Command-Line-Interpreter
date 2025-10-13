
package com.example.os.commands.ass1;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Parser {
    String commandName;
    String[] args = null;
    
    public boolean parse(String input) {
        String[] words = input.split(" ");
        args = new String[words.length-1];  // size of arge is number of words - command name
        commandName = words[0];
        
        for(int i=1; i<words.length; i++){
            args[i-1] = words[i];
        }
        // two special case(the commands >, >> is not the first word)
        // case that the command is not true
        
        return false;
    } 
    
    public String getCommandName() {return commandName;}
    // get args function
}

public class Terminal {
    Parser parser;
    Path path;
    
    
    Terminal(Parser p) {
        parser = p;
        path = Paths.get(System.getProperty("user.home"));
    }
    
    public void chooseCommandAction() {
        // conditions to choose the appropriate function
        if(parser.getCommandName().equals("pwd")) {
            pwd();
        }
        
        else if(parser.getCommandName().equals("cd")) {
            cd();
        }
        // ......
    }
    
    // commands
    public void pwd() {
        System.out.println("path");
    }
    
    public void cd() {
        if(parser.args.length == 0) {
            path = Paths.get(System.getProperty("user.home"));
        }
        else if(parser.args[0].equals("..")) {
            path = path.getParent();
        }
        else if(parser.args.length == 1) {
            Path inputPath = path.resolve(Paths.get(parser.args[0])).normalize();
            if(!Files.exists(inputPath)) {
                System.out.print("bash: cd: " + inputPath + ": No such file or directory\n");
            }
            else {
                if(inputPath.isAbsolute()) {
                   path = inputPath.normalize();
            }
               else {
                   path = inputPath;
            }
           }   
        }
    }
    // ......
    
    public void path() { System.out.println(path);}
    
    
    public static void main(String[] args) {
        
        Parser parser = new Parser();
        Terminal terminal = new Terminal(parser);
        
        parser.parse("cd");
        terminal.chooseCommandAction();
        terminal.path();
        
        System.out.println("\n");
        
        parser.parse("cd ..");
        terminal.chooseCommandAction();
        terminal.path();
        
        System.out.println("\n");
        
        parser.parse("cd c:/Users/hp/Arduino/project");   // full path
        terminal.chooseCommandAction();
        terminal.path();
        
        System.out.println("\n");
        
        parser.parse("cd");
        terminal.chooseCommandAction();
        terminal.path();
        
        System.out.println("\n");
        
        parser.parse("cd Documents");   // relative path
        terminal.chooseCommandAction();
        terminal.path();
        
        System.out.println("\n");
        
        
        
    }
}
