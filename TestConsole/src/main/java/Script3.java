import java.util.Random;

public class Script3 implements Command{
    private TestConsole testConsole;
    private ReadCompare readCompare;

    public Script3(TestConsole testConsole, ReadCompare readCompare) {
        this.testConsole = testConsole;
        this.readCompare = readCompare;
    }

    @Override
    public void execute(String commandStr) {
        for (int i = 0; i < 200; i++) {
            String randomHexFor0 = getRandomHexString();
            String randomHexFor99 = getRandomHexString();

            testConsole.write(0, randomHexFor0);
            testConsole.write(99, randomHexFor99);

            if (!readCompare.execute(0, randomHexFor0)) {
                System.out.println("FAIL");
                return;
            }

            if (!readCompare.execute(99, randomHexFor99)) {
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

