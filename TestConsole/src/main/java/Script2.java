public class Script2 {
    void execute(){
        TestConsole testConsole = new TestConsole();
        ReadCompare readCompare = new ReadCompare();
        int lba[] = {4,0,3,1,2};
        String value = "0x12345678";

        for(int i=0;i<30;i++){
            for(int j=0;j<5;j++){
                testConsole.write(lba[j], value);
            }

            for(int j=0;j<5;j++){
                if(!readCompare.execute(j, value)){
                    System.out.println("FAIL");
                    return;
                }
            }
        }

        System.out.println("PASS");
    }

}
