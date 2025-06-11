public class ReadCompare{
    public boolean execute(int LBA, String value) {
        TestConsole testConsole = new TestConsole();
        return value.equals(testConsole.read(LBA));
    }
}