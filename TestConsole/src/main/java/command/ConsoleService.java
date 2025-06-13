package command;

import logger.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsoleService {

    public static final int TOTAL_LBA_COUNT = 100;
    public static final int MAX_ERASE_CHUNK = 10;
    public static final int MIN_LBA = 0;
    public static final String READ = "R";
    public static final String WRITE = "W";
    public static final String FLUSH = "F";
    public static final String ERASE = "E";
    public static final Path SSD_OUTPUT_PATH = Paths.get("ssd_output.txt");
    public static final String SSD_JAR_PATH = "../JarLibs/ssd.jar";

    Logger logger = Logger.getInstance();

    public String read(int address) {
        try {
            List<String> lines = executeSSDAndReadOutput(READ, Integer.toString(address));
            return lines.isEmpty() ? "" : lines.get(0);
        } catch (IOException e) {
            logger.debug("ERROR read service at file: " + e.getMessage());
            return "ERROR read service at file";
        } catch (InterruptedException e) {
            logger.debug("ERROR read service at process: " + e.getMessage());
            return "ERROR read service at process";
        }
    }

    private List<String> executeSSDAndReadOutput(String... args) throws IOException, InterruptedException {
        executeSSD(args);
        return Files.readAllLines(SSD_OUTPUT_PATH);
    }

    public boolean write(int address, String data) {
        try {
            executeSSD(WRITE, Integer.toString(address), data);
            String readResult = read(address);
            if (readResult.startsWith("ERROR")) {
                logger.debug("Failed to verify written data due to read error");
                return false;
            }
            return data.equals(readResult);

        } catch (Exception e) {
            logger.debug("ERROR write service: " + e.getMessage());
            return false;
        }
    }

    public void flush() {
        try {
            executeSSD(FLUSH);
        } catch (Exception e) {
            logger.debug("ERROR flush service " + e.getMessage());
        }

    }

    public void excuteErase(int lba, int size) throws IOException, InterruptedException {
        try {
            executeSSD(ERASE, Integer.toString(lba), Integer.toString(size));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void executeSSD(String... args) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-jar");
        command.add(SSD_JAR_PATH);
        command.addAll(Arrays.asList(args));

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.inheritIO(); // 콘솔 출력 연결
        Process process = pb.start();
        process.waitFor();

    }

    public void fullRead() {
        for (int i = 0; i < TOTAL_LBA_COUNT; i++) {
            System.out.println("LBA " + i + ": " + read(i));
        }
    }

    public boolean fullWrite(String data) {
        for (int i = 0; i < TOTAL_LBA_COUNT; i++) {
            if (!write(i, data)) {
                return false;
            }
        }
        return true;
    }

    public boolean readCompare(int LBA, String value) {
        return value.equals(this.read(LBA));
    }

    public void erase(int lba, int size) {
        if (size == 0) return;

        // 음수 크기 처리 → 방향 바꾸기
        if (size < 0) {
            int newLba = lba + size + 1;
            int newSize = -size;
            lba = newLba;
            size = newSize;
        }

        // 주소가 0보다 작으면, size 보정 후 start를 0으로 올림
        if (lba < 0) {
            size -= (0 - lba);
            lba = 0;
        }

        // 범위 끝(99) 넘어가면 size 보정
        if (lba >= TOTAL_LBA_COUNT) return;
        if (lba + size > TOTAL_LBA_COUNT) {
            size = TOTAL_LBA_COUNT - lba;
        }

        while (size > 0) {
            int chunk = Math.min(MAX_ERASE_CHUNK, size);
            try {
                excuteErase(lba, chunk);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            lba += chunk;
            size -= chunk;
        }

        return;
    }

    public void erase_range(int start, int end) {
        start = clamp(start, MIN_LBA, TOTAL_LBA_COUNT - 1);
        end = clamp(end, MIN_LBA, TOTAL_LBA_COUNT - 1);

        int[] swapped = swapIfNeeded(start, end);
        start = swapped[0];
        end = swapped[1];

        int size = end - start + 1;

        while (size > 0) {
            int chunk = Math.min(MAX_ERASE_CHUNK, size);
            try {
                excuteErase(start, chunk);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            start += chunk;
            size -= chunk;
        }
    }

    private int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    private int[] swapIfNeeded(int a, int b) {
        if (a > b) {
            return new int[]{b, a};
        }
        return new int[]{a, b};
    }

}
