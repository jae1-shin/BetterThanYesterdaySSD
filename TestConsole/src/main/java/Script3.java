import java.util.Random;

public class Script3 implements Command{
    private TestConsole testConsole;

    public Script3(TestConsole testConsole) {
        this.testConsole = testConsole;
    }

    @Override
    public void execute(String commandStr) {
        for (int i = 0; i < 200; i++) {
            String randomHexFor0 = getRandomHexString();
            String randomHexFor99 = getRandomHexString();

            testConsole.write(0, randomHexFor0);
            testConsole.write(99, randomHexFor99);

            if (!testConsole.readCompare(0, randomHexFor0)) {
                System.out.println("FAIL");
                return;
            }

            if (!testConsole.readCompare(99, randomHexFor99)) {
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

