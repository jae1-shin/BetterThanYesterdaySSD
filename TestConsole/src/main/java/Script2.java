public class Script2 extends Command {

    protected Script2(ConsoleService service) {
        super(service);
    }

    public void execute(String[] args){
        int[] lba = {4,0,3,1,2};
        String value = "0x12345678";

        for(int i=0;i<30;i++){
            for(int j=0;j<5;j++){
                service.write(lba[j], value);
            }

            for(int j=0;j<5;j++){
                if(!service.readCompare(j, value)){
                    System.out.println("FAIL");
                    return;
                }
            }
        }

        System.out.println("PASS");
    }

}
