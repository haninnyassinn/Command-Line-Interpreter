
package com.example.os.commands.ass1;
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
        path = Paths.get("/home");
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
            path = Paths.get("/home");
        }
        else if(parser.args[0].equals("..")) {
            path = path.getParent();
        }
        //else 
    }
    // ......
    
    public void path() { System.out.println(path);}
    
    
    public static void main(String[] args) {
        
        Parser parser = new Parser();
        Terminal terminal = new Terminal(parser);
        
        parser.parse("cd");
        //System.out.print(parser.getCommandName());
        terminal.chooseCommandAction();
        terminal.path();
        
        parser.parse("cd ..");
        terminal.chooseCommandAction();
        terminal.path();
        
        
        parser.parse("cd");
        terminal.chooseCommandAction();
        terminal.path();
        
        
        
        
        
        
        
    }
}
