public class Script2 extends Command {
    public static final int LOOP_COUNT = 30;
    public static final String TEST_VALUE = "0x12345678";

    protected Script2(ConsoleService service) {
        super(service);
    }

    @Override
    public void execute(String[] args) {
        int[] lbaTestSequence = {4, 0, 3, 1, 2};

        for(int i = 0; i< LOOP_COUNT; i++){
            for(int lba : lbaTestSequence){
                service.write(lba, TEST_VALUE);
            }

            for(int lba : lbaTestSequence){
                if(!service.readCompare(lba, TEST_VALUE)){
                    System.out.println("FAIL");
                    return;
                }
            }
        }

        System.out.println("PASS");
    }
}
