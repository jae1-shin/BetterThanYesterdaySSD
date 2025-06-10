public class ReadCompare{
    public boolean execute(int LBA, String value) {
        TestConsole testConsole = new TestConsole();

        String commandStr="";
        String result = testConsole.read(LBA);

        return result.equals(value);
    }
}