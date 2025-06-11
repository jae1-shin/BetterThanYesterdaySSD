public class SsdWriter {

    public static final String ERROR = "ERROR";
    public static final int ADDRESS_MIN_RANGE = 0;
    public static final int ADDRESS_MAX_RANGE = 99;

    public String write(String addrStr, String data) {
        if (!isValidAddress(addrStr)) {
            return ERROR;
        }

        if (!isValidData(data)) {
            return ERROR;
        }

        return data;
    }

    private boolean isValidData(String writeData) {
        if (writeData == null) return false;
        return writeData.matches("^0x[0-9A-Fa-f]{8}$");
    }

    private boolean isValidAddress(String addrStr) {
        int addr = -1;
        try {
            addr = Integer.valueOf(addrStr);
        } catch (NumberFormatException e) {
            return false;
        }
        if (addr < ADDRESS_MIN_RANGE || addr >= ADDRESS_MAX_RANGE) {
            return false;
        }
        return true;
    }
}
