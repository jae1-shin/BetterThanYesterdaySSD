public final class SsdConstants {

    private SsdConstants() {
        // 인스턴스 생성 방지
    }

    public static final String SSD_NAND_FILE = "ssd_nand.txt";
    public static final String OUTPUT_FILE_PATH = "ssd_output.txt";
    public static final int BLOCK_SIZE = 10;
    public static final int BUFFER_SIZE = 5;
    public static final String BUFFER_PATH = "buffer";
    public static final String DEFAULT_DATA = "0x00000000";
    public static final String ERROR = "ERROR";

    public static String getBufferFilePrefix(int bufferNum) {
        return bufferNum + "_";
    }

    public static String getBufferDefaultFileName(int bufferNum) {
        return getBufferFilePrefix(bufferNum) + "empty";
    }
}