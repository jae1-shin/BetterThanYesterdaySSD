public class Script1 implements Command {
    public static final String TEST_VALUE = "0xFFFFFFFF";
    public static final int LAST_LBA = 100;
    public static final int DIV_NUM = 5;
    private ConsoleService consoleService;
    public Script1(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    @Override
    public void execute(String commandStr) {
        int currentLBA = 0;
        while(currentLBA < LAST_LBA) {
            for(int LBA = currentLBA; LBA< currentLBA + DIV_NUM;LBA++){
                consoleService.write((LBA), TEST_VALUE);
            }

            for(int LBA = currentLBA; LBA < currentLBA + DIV_NUM;LBA++){
                if (!consoleService.readCompare((LBA), TEST_VALUE)) {
                    System.out.println("FAIL");
                    return;
                }
            }

            currentLBA += DIV_NUM;
        }
        
        
        System.out.println("PASS");
    }

}

