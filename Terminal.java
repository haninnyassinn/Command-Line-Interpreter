import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Terminal {
    Parser parser;
    Path path;

    Terminal(Parser p) {
        parser = p;
        path = Paths.get("/home");
    }

    public void chooseCommandAction() {
        // conditions to choose the appropriate function
        if (parser.getCommandName().equals("pwd")) {
            pwd();
        } else if (parser.getCommandName().equals("cd")) {
            cd();
        } else if (parser.getCommandName().equals("cp -r")) {
            cp_r(parser.args[0], parser.args[1]);
        }

        // ......
    }


    // commands
    public void pwd() {
        System.out.println("path");
    }

    public void cd() {
        if (parser.args.length == 0) {
            path = Paths.get("/home");
        } else if (parser.args[0].equals("..")) {
            path = path.getParent();
        }
        //else

    }

    public void cp_r(String sourceDir, String targetDir) {
        File src = new File(sourceDir);
        File dest = new File(targetDir, src.getName());
        try {
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied from " + sourceDir + " to " + targetDir);
        } catch (Exception e) {
            System.out.println("Error copying file: " + e.getMessage());
        }
    }

    // ......

    public void path() {
        System.out.println(path);
    }
}