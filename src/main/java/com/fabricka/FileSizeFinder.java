package com.fabricka;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

public class FileSizeFinder {
    private static final String[] SIZE_NAMES = {"b", "Kb", "Mb", "Gb", "Tb"};
    private static final boolean DEFAULT_HUMAN_READABLE = false;
    private static final int DEFAULT_BASE = 1024;

    public static class FileInfo { // private done !!!
        private final File file;
        private final String size;
        public FileInfo(File file, String size) {
            this.file = file;
            this.size = size;
        }

        public File getFile() {
            return file;
        }

        public String getSize() {
            return size;
        }
    }


    private static void checkFileIsCorrect(File file) {
        if (!file.exists()) throw new IllegalArgumentException("File '" + file.getPath() + "' not found");
    }

    public static long getFileSize(File file) {
        /* we think that file is correct */

        // return size if it's just a file
        if (file.isFile()) return file.length();

        // count total size if it's a directory
        long totalBytes = 0;
        for (File childFile: Objects.requireNonNull(file.listFiles())) {
            totalBytes += getFileSize(childFile);
        }
        return totalBytes;
    }

    public static String byteSizeToString(long byteSize, boolean humanReadable, int base) {
        if (!humanReadable) {
            return String.format(Locale.ROOT, "%.2f", (double) byteSize / base);
        }

        long currentBlock = byteSize; // 1536 => 1Kb
        long lastBlock = 0; // 1536 => 512b
        int blockNumber = 0; // 1536 => 1
        while (currentBlock >= base && blockNumber < SIZE_NAMES.length - 1) {
            lastBlock = currentBlock % base;
            currentBlock /= base;
            blockNumber++;
        }
        // if only bytes - without float.
        if (blockNumber == 0) return currentBlock + SIZE_NAMES[blockNumber];
        return currentBlock + "." +
                String.format("%02d", (int) Math.floor((double) lastBlock / base * 100)) +
                SIZE_NAMES[blockNumber];
    }

    public static String byteSizeToString(long byteSize, boolean humanReadable) {
        return byteSizeToString(byteSize, humanReadable, DEFAULT_BASE);
    }

    public static String byteSizeToString(long byteSize, int base) {
        return byteSizeToString(byteSize, DEFAULT_HUMAN_READABLE, base);
    }

    public static String byteSizeToString(long byteSize) {
        return byteSizeToString(byteSize, DEFAULT_HUMAN_READABLE, DEFAULT_BASE);
    }

    public static String getTotalSize(File[] files, boolean humanReadable, int base) {
        long totalSize = 0;
        for (File file: files) {
            checkFileIsCorrect(file);
            totalSize += getFileSize(file);
        }
        return byteSizeToString(totalSize, humanReadable, base);
    }

    public static String getTotalSize(File[] files, boolean humanReadable) {
        return getTotalSize(files, humanReadable, DEFAULT_BASE);
    }

    public static String getTotalSize(File[] files, int base) {
        return getTotalSize(files, DEFAULT_HUMAN_READABLE, base);
    }

    public static String getTotalSize(File[] files) {
        return getTotalSize(files, DEFAULT_HUMAN_READABLE, DEFAULT_BASE);
    }

    public static FileInfo[] getFilesInfo(File[] files, boolean humanReadable, int base) {
        FileInfo[] result = new FileInfo[files.length]; // tests done !!!
        File file;
        for (int i = 0; i < files.length; i++) {
            file = files[i];
            checkFileIsCorrect(file);
            final String byteSizeString = byteSizeToString(getFileSize(file), humanReadable, base);
            result[i] = new FileInfo(file, byteSizeString);
        }
        return result;
    }

    public static FileInfo[] getFilesInfo(File[] files, boolean humanReadable) {
        return getFilesInfo(files, humanReadable, DEFAULT_BASE);
    }

    public static FileInfo[] getFilesInfo(File[] files, int base) {
        return getFilesInfo(files, DEFAULT_HUMAN_READABLE, base);
    }

    public static FileInfo[] getFilesInfo(File[] files) {
        return getFilesInfo(files, DEFAULT_HUMAN_READABLE, DEFAULT_BASE);
    }
}
