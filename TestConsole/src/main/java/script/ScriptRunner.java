package script;

import command.CommandInvoker;
import command.ConsoleService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ScriptRunner {

    public void run(String filePath) {
        if(!fileExists(filePath)){
            System.out.println("There's no File !");
            return;
        }

        ConsoleService service = new ConsoleService();
        CommandInvoker invoker = new CommandInvoker();

        invoker.register("1_FullWriteAndReadCompare",  new Script1(service));
        invoker.register("1_",  new Script1(service));
        invoker.register("2_PartialLBAWrite",  new Script2(service));
        invoker.register("2_",  new Script2(service));
        invoker.register("3_WriteReadAging",  new Script3(service));
        invoker.register("3_",  new Script3(service));
        invoker.register("4_EraseAndWriteAging",  new Script4(service));
        invoker.register("4_",  new Script4(service));

        List<String> scriptNames;
        try {
            scriptNames = Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!isValidScriptNames(scriptNames, invoker)){
            System.out.println("Invalid Script Names!");
        }

        for(String script : scriptNames){
            System.out.print(script + "  ___  Run...");
            if(!invoker.execute(script)){
                return;
            }
        }
    }

    private boolean isValidScriptNames(List<String> scriptNames, CommandInvoker invoker) {
        if(scriptNames.isEmpty()) return false;
        for(String script : scriptNames){
            if(invoker.hasCommand(script)){
                return false;
            }
        }

        return true;
    }

    private boolean fileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }
}
