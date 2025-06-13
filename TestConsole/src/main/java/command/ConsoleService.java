package command;

import logger.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ConsoleService {

    public static final int TOTAL_LBA_COUNT = 100;
    public static final int MAX_ERASE_CHUNK = 10;
    public static final int MIN_LBA = 0;

    Logger logger = Logger.getInstance();

    public String read(int address) {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "../JarLibs/ssd.jar", "R", Integer.toString(address));
            pb.inheritIO();
            Process process = null;
            process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            logger.debug("ERROR read service at process " + e.getMessage());
            return "ERROR read service at process";
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("ssd_output.txt"));
        } catch (IOException e) {
            logger.debug("ERROR read service at file " + e.getMessage());
            return "ERROR read service at file";
        }
        return lines.get(0);
    }

    public boolean write(int address, String data) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-jar", "../JarLibs/ssd.jar", "W", Integer.toString(address), data
            );

            pb.inheritIO(); // 콘솔 출력 연결

            Process process = pb.start();
            process.waitFor();
            if (data.equals(read(address))) return true;

            process.destroy(); // 또는 process.destroyForcibly();

        } catch (Exception e) {
            logger.debug("ERROR write service " + e.getMessage());
            return false;
        }

        return false;
    }

    public void fullRead() {
        for (int i = 0; i < TOTAL_LBA_COUNT; i++) {
            System.out.println("LBA " + i + ": " + read(i));
        }
    }

    public boolean fullWrite(String data) {
        boolean eachResult = false;
        for (int i = 0; i < TOTAL_LBA_COUNT; i++) {
            eachResult = write(i, data);
            if (eachResult == false) return false;
        }
        return true;
    }

    public boolean readCompare(int LBA, String value) {
        return value.equals(this.read(LBA));
    }

    public void flush() {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-jar", "../JarLibs/ssd.jar", "F"
            );
            pb.inheritIO(); // 콘솔 출력 연결
            Process process = pb.start();
            process.waitFor();

        } catch (Exception e) {
            logger.debug("ERROR flush service " + e.getMessage());
        }

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

     public void excuteErase(int lba, int size) throws IOException, InterruptedException {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-jar", "../JarLibs/ssd.jar", "E", Integer.toString(lba), Integer.toString(size)
            );

            pb.inheritIO(); // 콘솔 출력 연결
            Process process = pb.start();
            process.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
