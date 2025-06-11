public class WriteCommand extends Command{
    protected WriteCommand(ConsoleService service) {
        super(service);
    }

    @Override
    public void execute(String[] args)  {
        try {
            if (args.length != 3) {
                System.out.println("ERROR Invalid argument numbers. Usage: read <address>");
                return;
            }
            if (!args[2].startsWith("0x")) {
                System.out.println("ERROR Value must be in hex format (e.g., 0x1234ABCD)");
                return;
            }

            if(service.write(Integer.parseInt(args[1]), args[2]) == false) {
                System.out.println("ERROR Write failed");
                return;
            }else{
                System.out.println("[Write] Done");
            }


        } catch (NumberFormatException e) {
            System.out.println("ERROR NumberFormatException" + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ERROR IndexOutOfBoundsException" + e.getMessage());
        }
    }
}
