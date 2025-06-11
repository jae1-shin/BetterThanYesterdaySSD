public class Script1 implements Command {
    private TestConsole testConsole;

    public Script1(TestConsole testConsole) {
        this.testConsole = testConsole;
    }

    @Override
    public void execute(String commandStr) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 5; j++) {
                testConsole.write((i * 5 + j), "0xFFFFFFFF");
            }
            for (int j = 0; j < 5; j++) {
                boolean result = testConsole.readCompare((i * 5 + j), "0xFFFFFFFF");

                if (!result) {
                    System.out.printf("FAIL");
                    return;
                }
            }
        }
        System.out.printf("PASS");
    }
}

