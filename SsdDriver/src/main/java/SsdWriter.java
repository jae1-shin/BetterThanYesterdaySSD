public class SsdWriter {
    public String write(String number, String s) {
        int addr = -1;
        try {
            addr = Integer.valueOf(number);
        } catch (NumberFormatException e) {
            return "ERROR";
        }

        if (addr < 0 || addr > 99) {
            return "ERROR";
        }

        if (s.length() != 10) {
            return "ERROR";
        }
        return s;
    }
}
