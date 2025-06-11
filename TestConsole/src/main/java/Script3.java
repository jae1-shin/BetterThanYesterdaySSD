import java.util.Random;

public class Script3 implements Command{
    public static final int LOOP_COUNT = 200;
    private ConsoleService consoleService;

    public Script3(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    @Override
    public void execute(String commandStr) {
        for (int i = 0; i < LOOP_COUNT; i++) {
            String randomHexFor0 = getRandomHexString();
            String randomHexFor99 = getRandomHexString();

            consoleService.write(0, randomHexFor0);
            consoleService.write(99, randomHexFor99);

            if (!consoleService.readCompare(0, randomHexFor0)) {
                System.out.println("FAIL");
                return;
            }

            if (!consoleService.readCompare(99, randomHexFor99)) {
                System.out.println("FAIL");
                return;
            }
        }

        System.out.println("PASS");
    }

    private String getRandomHexString() {
        Random random = new Random();
        long randomUnsignedInt = random.nextLong() & 0xFFFFFFFFL;

        return "0x" + String.format("%08X", randomUnsignedInt);
    }
}

