package logger;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RollingFileLogListener implements LogListener {

    private static final long MAX_FILE_SIZE = 10 * 1024; // 10KB
    private static final String LOG_NAME = "latest.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMdd_HH'h'_mm'm'_ss's'");
    private final File currentLogFile;

    public RollingFileLogListener() {
        this.currentLogFile = new File(LOG_NAME);
    }

    @Override
    public void onLog(String timestamp, LogLevel level, String className, String message) {
        try {
            rotateIfNeeded();

            try (FileWriter writer = new FileWriter(currentLogFile, true)) {
                writer.write(String.format("[%s]%-8s %-30s : %s%n", timestamp, "[" + level + "]", className, message));
            }

        } catch (IOException e) {
            System.err.println("파일 로그 실패: " + e.getMessage());
        }
    }

    private void rotateIfNeeded() throws IOException {
        if (!currentLogFile.exists() || currentLogFile.length() < MAX_FILE_SIZE) return;

        String dateTime = LocalDateTime.now().format(FORMATTER);
        String baseName = "until_" + dateTime;
        File rotatedFile = new File(baseName + ".log");

        // 1. latest.log 제외하고 존재하는 .log 파일을 .zip 으로 변경
        File[] logFiles = new File(".").listFiles((dir, name) -> name.endsWith(".log") && !name.equals(LOG_NAME));
        if (logFiles != null) {
            for (File file : logFiles) {
                String zipName = file.getName().replaceFirst("\\.log$", ".zip");
                File zipFile = new File(file.getParent(), zipName);
                if (zipFile.exists() && !zipFile.delete()) {
                    System.err.println("파일 삭제 실패: " + zipFile.getName());
                }
                Files.move(file.toPath(), zipFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // 2. latest.log -> until_TIMESTAMP.log
        Files.move(currentLogFile.toPath(), rotatedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

    }
}