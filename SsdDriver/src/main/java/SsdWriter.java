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

        if (!s.startsWith("0x")) {
            return "ERROR";
        }

        for (int i = 2; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') {
                return "ERROR";
            }
            if (s.charAt(i) < 'A' || s.charAt(i) > 'Z') {
                return "ERROR";
            }
            if (s.charAt(i) < 'a' || s.charAt(i) > 'z') {
                return "ERROR";
            }
        }
        return s;
    }
}
