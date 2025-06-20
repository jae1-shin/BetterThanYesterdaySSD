package command.script;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;

//4_EraseAndWriteAging

public class EraseAndWriteAging extends TestScript {
    public static final int LOOP_COUNT = 30;
    public static final String WRITE_VALUE = "0x11111111";
    public static final String OVERWRITE_VALUE = "0x22222222";
    public static final String ERASED_VALUE = "0x00000000";
    public static final int BASE_COUNT = 3;
    public static final int EXCEPT_COUNT = 2;

    public EraseAndWriteAging(ConsoleService service) {
        super(service);
    }

    @Override
    public CommandResult doExecute(String[] args) {
        service.erase_range(0, 2);

        for (int i = 0; i < LOOP_COUNT; i++) {
            for (int startLBA = 2; startLBA < 100; startLBA += 2) {
                service.write(startLBA, WRITE_VALUE);
                service.write(startLBA, OVERWRITE_VALUE);

                int count = BASE_COUNT;
                if (startLBA == 98) count = EXCEPT_COUNT;

                for (int LBA = startLBA; LBA < startLBA + count; LBA++) {
                    service.erase(LBA, 1);
                    if (!service.readCompare(LBA, ERASED_VALUE)) {
                        return CommandResult.scriptFail(FAIL_FLAG);
                    }
                }
            }
        }

        return CommandResult.pass(PASS_FLAG);
    }
}

