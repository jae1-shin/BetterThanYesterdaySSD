package script;

import command.Command;
import command.ConsoleService;

public class Script1 extends Command {
    public static final String TEST_VALUE = "0xFFFFFFFF";
    public static final int LAST_LBA = 100;
    public static final int DIV_NUM = 5;

    public Script1(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean execute(String[] args) {
        int currentLBA = 0;
        while (currentLBA < LAST_LBA) {
            for (int LBA = currentLBA; LBA < currentLBA + DIV_NUM; LBA++) {
                service.write(LBA, TEST_VALUE);
            }

            for (int LBA = currentLBA; LBA < currentLBA + DIV_NUM; LBA++) {
                if (!service.readCompare(LBA, TEST_VALUE)) {
                    System.out.println("FAIL");
                    return false;
                }
            }

            currentLBA += DIV_NUM;
        }
        
        
        System.out.println("PASS");
        return true;
    }

}

