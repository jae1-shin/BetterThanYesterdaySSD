package command.script;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;

//1_FullWriteAndReadCompare
public class FullWriteAndReadCompare extends Command {
    public static final String TEST_VALUE = "0xFFFFFFFF";
    public static final int LAST_LBA = 100;
    public static final int DIV_NUM = 5;

    public FullWriteAndReadCompare(ConsoleService service) {
        super(service);
    }

    @Override
    public String isValidArguments(String[] args) {
        return "";
    }

    @Override
    public CommandResult doExecute(String[] args) {
        int currentLBA = 0;
        while (currentLBA < LAST_LBA) {
            for (int LBA = currentLBA; LBA < currentLBA + DIV_NUM; LBA++) {
                service.write(LBA, TEST_VALUE);
            }

            for (int LBA = currentLBA; LBA < currentLBA + DIV_NUM; LBA++) {
                if (!service.readCompare(LBA, TEST_VALUE)) {
                    return CommandResult.scriptFail("FAIL");
                }
            }

            currentLBA += DIV_NUM;
        }


        logger.result("PASS");
        return CommandResult.PASS;
    }

}

