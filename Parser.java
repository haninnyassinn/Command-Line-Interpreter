import java.io.Serializable;

class Parser {
    String commandName;
    String[] args;

    public Serializable parse(String input) {
        String[] words = input.split(" ");
        args = new String[words.length - 1];  // size of arge is nuImber of words - command name

        commandName = words[0];
        if (commandName.equals("cp") && words.length > 1 && words[1].equals("-r")) {
            commandName = "cp -r";
        }

        for (int i = 1; i < words.length; i++) {
            args[i - 1] = words[i];
        }
        // two special case(the commands >, >> is not the first word)
        // case that the command is not true

        if (commandName.equals("pwd") ||
                commandName.equals("cd") ||
                commandName.equals("ls") ||
                commandName.equals("mkdir") ||
                commandName.equals("rmdir") ||
                commandName.equals("touch") ||
                commandName.equals("cp") ||
                commandName.equals("cp -r") ||
                commandName.equals("rm") ||
                commandName.equals("cat") ||
                commandName.equals("wc") ||
                commandName.equals("zip") ||
                commandName.equals("unzip") ||
                commandName.equals("exit") ||
                commandName.equals(">") ||
                commandName.equals(">>")) {

            return true;
        } else {
            return false;
        }
    }

    public String getCommandName() {
            return commandName;
        }
    // get args function
    }

