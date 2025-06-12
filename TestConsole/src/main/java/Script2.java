import static logger.LoggerHolder.logger;

public class Script2 extends Command {
    public static final int LOOP_COUNT = 30;
    public static final String TEST_VALUE = "0x12345678";
    public static final int[] LBA_TEST_SEQUENCE = new int[]{4, 0, 3, 1, 2};

    protected Script2(ConsoleService service) {
        super(service);
    }

    @Override
    public boolean execute(String[] args) {

        for(int i = 0; i< LOOP_COUNT; i++){
            for(int lba : LBA_TEST_SEQUENCE){
                service.write(lba, TEST_VALUE);
            }

            for(int lba : LBA_TEST_SEQUENCE){
                if(!service.readCompare(lba, TEST_VALUE)){
                    logger.result("FAIL");
                    return false;
                }
            }
        }

        logger.result("PASS");
        return true;
    }
}
