public class ReadCompare{
    public boolean execute(int LBA, String value) {
        ConsoleService consoleService = new ConsoleService();
        return value.equals(consoleService.read(LBA));
    }
}