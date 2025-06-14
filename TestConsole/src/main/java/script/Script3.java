package script;

import command.Command;
import command.ConsoleService;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Script3 extends Command {
    public static final int LOOP_COUNT = 200;
    public static final int[] targetLBA = new int[]{0, 99};

    public Script3(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean execute(String[] args) {
        for (int i = 0; i < LOOP_COUNT; i++) {
            Map<Integer, String> writeData = createWriteData();

            writeAtTargetLBA(writeData);

            if (!readCompareTargetLBA(writeData)) {
                System.out.println("FAIL");
                return false;
            }
        }

        System.out.println("PASS");
        return true;
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

