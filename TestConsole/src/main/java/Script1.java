public class Script1 extends Command {

    protected Script1(ConsoleService service) {
        super(service);
    }

    @Override
    public void execute(String[] args) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 5; j++) {
                service.write((i * 5 + j), "0xFFFFFFFF");
            }
            for (int j = 0; j < 5; j++) {
                boolean result = service.readCompare((i * 5 + j), "0xFFFFFFFF");

                if (!result) {
                    System.out.printf("FAIL");
                    return;
                }
            }
        }
        System.out.printf("PASS");
    }
}

