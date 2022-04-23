package com.fabricka;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fabricka.FileSizeFinder;
import com.fabricka.FileSizeFinder.FileInfo;

public class MainTest {
    final String PARENT_PATH = "src/test/resources/";

    final File[] files = new File[]{
            getTestFile("com/fabricka/somefile1.txt"),
            getTestFile("directory1"),
            getTestFile("directory3"),
            getTestFile("directory1/somefile3.txt"),
    };
    final long[] fileSizes = new long[]{
            50,
            4228846,
            0,
            830834,
    };

    // {(true, 1024), (true, 1000), (false, 1024), (false, 1000)}
    final Map<Long, String[]> byteSizeStringAnswers = new HashMap<>() {{
        put(fileSizes[0], new String[]{"50b", "50b", "0.05", "0.05"});
        put(fileSizes[1], new String[]{"4.03Mb", "4.22Mb", "4129.73", "4228.85"});
        put(fileSizes[2], new String[]{"0b", "0b", "0.00", "0.00"});
        put(fileSizes[3], new String[]{"811.36Kb", "830.83Kb", "811.36", "830.83"});
    }};

    // доступ к ресурсам
    private File getTestFile(String pathname) {
        return new File(getClass().getResource(pathname).getFile());
//        return new File(PARENT_PATH + pathname);
    }

    @Test
    void someTest() {
        System.out.println(getTestFile("somefile1.txt"));
    }

    @Test
    void testGettingFileSize() {
        for (int i = 0; i < files.length; i++) {
            final long calculatedFileSize = FileSizeFinder.getFileSize(files[i]);
            final long actualFileSize = fileSizes[i];
            assertEquals(calculatedFileSize, actualFileSize);
        }
    }

    private void testOneSizeConverse(long size, String[] answers) {
        int i = 0;
        for (boolean isHumanReadable: new boolean[]{true, false}) {
            for (int base: new int[]{1024, 1000}) {
                final String calculatedAnswer = FileSizeFinder.byteSizeToString(size, isHumanReadable, base);
                assertEquals(calculatedAnswer, answers[i++]);
            }
        }
    }

    @Test
    void testSizeConverse() {
        for (long size: fileSizes) {
            testOneSizeConverse(size, byteSizeStringAnswers.get(size));
        }
    }

    @Test
    void testAllFilesSizeConverse() {
        final String[] answers = new String[]{"4.82Mb", "5.05Mb", "4941.14", "5059.73"};
        int i = 0;
        for (boolean isHumanReadable: new boolean[]{true, false}) {
            for (int base: new int[]{1024, 1000}) {
                final String totalSize = FileSizeFinder.getTotalSize(files, isHumanReadable, base);
                assertEquals(totalSize, answers[i++]);
            }
        }
    }

    @Test
    void testGetFilesInfo() {
        int i = 0;
        for (boolean isHumanReadable: new boolean[]{true, false}) {
            for (int base: new int[]{1024, 1000}) {
                FileInfo[] filesInfo = FileSizeFinder.getFilesInfo(files, isHumanReadable, base);
                for (int j = 0; j < filesInfo.length; j++) {
                    final FileInfo currentFileInfo = filesInfo[j];
                    final File currentFile = files[j];
                    final String correctByteSizeString = byteSizeStringAnswers.get(fileSizes[j])[i];
                    assertEquals(new FileInfo(currentFile, correctByteSizeString), currentFileInfo);
                }
                i++;
            }
        }
    }

}
