public class ReadCompare{
    public boolean execute(String CommandStr) {
        TestConsole testConsole = new TestConsole();

        String commandStr="";
        String result = testConsole.read(CommandStr);

        return result.equals(commandStr);
    }
}