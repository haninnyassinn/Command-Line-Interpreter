public static void main(String[] args) {

    com.example.os.commands.ass1.Parser parser = new com.example.os.commands.ass1.Parser();
    parser.parse("pwd");
    com.example.os.commands.ass1.Terminal terminal = new com.example.os.commands.ass1.Terminal(parser);
    terminal.chooseCommandAction();

}