import java.util.Random;

public class Script3 extends Command{

    protected Script3(ConsoleService service) {
        super(service);
    }

    @Override
    public void execute(String[] args) {
        for (int i = 0; i < 200; i++) {
            String randomHexFor0 = getRandomHexString();
            String randomHexFor99 = getRandomHexString();

            service.write(0, randomHexFor0);
            service.write(99, randomHexFor99);

            if (!service.readCompare(0, randomHexFor0)) {
                System.out.println("FAIL");
                return;
            }

            if (!service.readCompare(99, randomHexFor99)) {
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

