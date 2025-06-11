public class Script1 implements ICommand {
    private ConsoleService consoleService;
    private ReadCompare readCompare;

    public Script1(ConsoleService consoleService, ReadCompare readCompare) {
        this.consoleService = consoleService;
        this.readCompare = readCompare;
    }

    @Override
    public void execute(String[] args) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 5; j++) {
                consoleService.write((i * 5 + j), "0xFFFFFFFF");
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

