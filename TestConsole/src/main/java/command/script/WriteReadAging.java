package command.script;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;

//3_WriteReadAging
public class WriteReadAging extends TestScript {
    public static final int LOOP_COUNT = 200;
    public static final int[] targetLBA = new int[]{0, 99};

    public WriteReadAging(ConsoleService service) {
        super(service);
    }

    @Override
    public CommandResult doExecute(String[] args) {
        for (int i = 0; i < LOOP_COUNT; i++) {
            Map<Integer, String> writeData = createWriteData();

            writeAtTargetLBA(writeData);

            if (!readCompareTargetLBA(writeData)) {
                return CommandResult.scriptFail(FAIL_FLAG);
            }
        }

        return CommandResult.pass("PASS");
    }

    private Map<Integer, String> createWriteData() {
        Map<Integer, String> writeData = new HashMap<>();
        for (int lba : targetLBA) {
            writeData.put(lba, getRandomHexString());
        }
        return writeData;
    }

    private void writeAtTargetLBA(Map<Integer, String> writeData) {
        for (int lba : targetLBA) {
            String value = writeData.get(lba);
            service.write(lba, value);
        }
    }

    private boolean readCompareTargetLBA(Map<Integer, String> writeData) {
        for (int lba : targetLBA) {
            String value = writeData.get(lba);
            if (!service.readCompare(lba, value)) {
                return false;
            }
        }
        return true;
    }

    private String getRandomHexString() {
        Random random = new Random();
        long randomUnsignedInt = random.nextLong() & 0xFFFFFFFFL;

        return "0x" + String.format("%08X", randomUnsignedInt);
    }
}

