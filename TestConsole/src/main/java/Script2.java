public class Script2 {
    private TestConsole testConsole;

    public Script2(TestConsole testConsole) {
        this.testConsole = testConsole;
    }

    void execute(){
        int[] lba = {4,0,3,1,2};
        String value = "0x12345678";

        for(int i=0;i<30;i++){
            for(int j=0;j<5;j++){
                testConsole.write(lba[j], value);
            }

            for(int j=0;j<5;j++){
                if(!testConsole.readCompare(j, value)){
                    System.out.println("FAIL");
                    return;
                }
            }
        }

        System.out.println("PASS");
    }

}
