public class SsdWriter {

    public static final String ERROR = "ERROR";

    public String write(String addrStr, String writeData) {
        int addr = -1;
        try {
            addr = Integer.valueOf(addrStr);
        } catch (NumberFormatException e) {
            return ERROR;
        }

        if (!isValidAddress(addr)) {
            return ERROR;
        }

        if (!isValidDataLength(writeData)) {
            return ERROR;
        }

        if (!isStartWithHexPrefix(writeData)) {
            return ERROR;
        }

        if (!isValidData(writeData)) {
            return ERROR;
        }

        return writeData;
    }

    private boolean isValidData(String writeData) {
        for (int i = 2; i < writeData.length(); i++) {
            if (writeData.charAt(i) < '0' || writeData.charAt(i) > '9') {
                return false;
            }
            if (writeData.charAt(i) < 'A' || writeData.charAt(i) > 'Z') {
                return false;
            }
            if (writeData.charAt(i) < 'a' || writeData.charAt(i) > 'z') {
                return false;
            }
        }
        return true;
    }

    private boolean isStartWithHexPrefix(String writeData) {
        return !writeData.startsWith("0x");
    }

    private boolean isValidDataLength(String writeData) {
        return writeData.length() != 10;
    }

    private boolean isValidAddress(int addr) {
        return addr < 0 || addr > 99;
    }
}
