import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;




class Parser {
   private  String commandName;
   private  String[] args = null;
//function to specerate the command line from the arguments 
    public boolean parse(String input) {
        if (input.isEmpty()) return false; // making sure if its empty nothing happens 
      String[] words = input.trim().split("\\s+"); // used trim to make sure no spaces before or after, used (\\s+) to make sure if there is more than space doesnt consider an arg 

        
        commandName = words[0];
        args = new String[words.length-1];  // size of arge is number of words - command name

        for(int i=1; i<words.length; i++){
            args[i-1] = words[i];
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
     private  Parser parser; //object from class Parser so that we get command and args 
     private Path path;

//constructor
    Terminal(Parser p) {
        parser = p;
        path = Paths.get("/home");
    }
// Choosing the right command 
    public void chooseCommandAction() {
   String cmd = parser.getCommandName(); // a string that save the command needed 
    if (cmd.equals("pwd")) {
        pwd();}
    else if (cmd.equals("cd")) {
        cd();}
         
    else if (cmd.equals("ls")) {
        ls();}
    else if (cmd.equals("mkdir")) {
        mkdir();}
    else if (cmd.equals("rmdir")) {
        rmdir();}
    else if (cmd.equals("touch")) {
        touch();}
    else if (cmd.equals("rm")) {
        rm();}
    else if (cmd.equals("cat")) {
        cat();}
    else if (cmd.equals("WC")) {
        WC();}
    else if (cmd.equals("cp")) {
        cp();}
    else if (cmd.equals("zip")) {
        zip();}
    else if (cmd.equals("unzip")) {
        unzip();}
    else if (cmd.equals("exit")) {
        System.exit(0);}
    else {
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
        if(parser.getArgs().length == 0) {
            path = Paths.get("/home");
        }
        else if(parser.getArgs()[0].equals("..")) {
            path = path.getParent();
        }
        //else
      }
      //ls command 
     public void ls(){

     }
     //mkdir command
     public void mkdir(){

     }
     //rmdir command
     public void rmdir(){

     }
     //touch command 
     public void touch(){

     }
     //cp,cp-r commands
     public void cp(){

     }
     //rm file.txt this take the argument which is file.txt and see if it exits and delete it
     public void rm() {
         String[] args = parser.getArgs();
         // we are taking only one argument if less or more we will return
           if (args.length != 1) {
              System.out.println("write the file");
             return;}
         String fileName = args[0];
         Path filePath = path.resolve(fileName); //getting fall path of the file in the computer
         
          if (Files.exists(filePath)) {
              try {
            Files.delete(filePath); // deleting the file 
            System.out.println("Deleted: " + fileName);}
         catch (IOException e) {
            System.out.println("there is a problem in deleting the file :" + fileName );} // the file might be locked 
        }else{
             System.out.println( fileName + " cant be deleted as it is not found");
        }
     }
     //cat command
      public void cat(){

     }
     //WC command 
      public void WC(){

     }
     // zip command 
      public void zip(){

     }
     //unzip command 
     public void unzip(){

     }
     // exit command stops the programm
     public void exit() {
         System.exit(0); // Ends the program
      }



  
    // ......

    public void path() { System.out.println(path);}


    public static void main(String[] args)
    {
        // deh al bn read beha al input zy cin keda 
       Scanner sc = new Scanner(System.in);
        Parser parser = new Parser();
        Terminal terminal = new Terminal(parser);

         while (true) {
            System.out.print("> ");
            String input = sc.nextLine();
            if (parser.parse(input))
                terminal.chooseCommandAction();
        }


    }
}