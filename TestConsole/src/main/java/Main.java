import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            TestConsole console = new TestConsole();
            console.run();
        }

        if(args.length == 1){
            ScriptRunner scriptRunner = new ScriptRunner();
            scriptRunner.run(args[0]);
        }
    }
}
