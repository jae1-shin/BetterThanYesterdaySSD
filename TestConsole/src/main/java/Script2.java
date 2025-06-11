public class Script2 implements Command{
    public static final int LOOP_COUNT = 30;
    public static final String TEST_VALUE = "0x12345678";
    private TestConsole testConsole;

    public Script2(TestConsole testConsole) {
        this.testConsole = testConsole;
    }

    @Override
    public void execute(String commandStr) {
        int[] lbaTestSequence = {4, 0, 3, 1, 2};

        for(int i = 0; i< LOOP_COUNT; i++){
            for(int lba : lbaTestSequence){
                testConsole.write(lba, TEST_VALUE);
            }

            for(int lba : lbaTestSequence){
                if(!testConsole.readCompare(lba, TEST_VALUE)){
                    System.out.println("FAIL");
                    return;
                }
            }
        }

        System.out.println("PASS");
    }

}
