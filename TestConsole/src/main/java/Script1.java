public class Script1 implements Command {
    private TestConsole testConsole;
    private ReadCompare readCompare;

    public Script1(TestConsole testConsole, ReadCompare readCompare) {
        this.testConsole = testConsole;
        this.readCompare = readCompare;
    }

    @Override
    public void execute(String commandStr) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 5; j++) {
                testConsole.write((i * 5 + j), "0xFFFFFFFF");
            }
            for (int j = 0; j < 5; j++) {
                boolean result = readCompare.execute((i * 5 + j), "0xFFFFFFFF");

                if (!result) {
                    System.out.printf("FAIL");
                    return;
                }
            }
        }
        System.out.printf("PASS");
    }
}

