import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fabricka.FileSizeFinder;

public class MainTest {
    final String PARENT_PATH = "src/test/resources/";

    final File[] files = new File[]{
            getTestFile("somefile1.txt"),
            getTestFile("directory1"),
            getTestFile("directory3"),
            getTestFile("directory1\\somefile3.txt"),
    };
    final long[] fileSizes = new long[]{
            50,
            4228846,
            0,
            830834,
    };

    private File getTestFile(String pathname) {
        return new File(PARENT_PATH + pathname);
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
        final Map<Long, String[]> answers = new HashMap<>();
        answers.put(fileSizes[0], new String[]{"50b", "50b", "0.05", "0.05"});
        answers.put(fileSizes[1], new String[]{"4.03Mb", "4.22Mb", "4129.73", "4228.85"});
        answers.put(fileSizes[2], new String[]{"0b", "0b", "0.00", "0.00"});
        answers.put(fileSizes[3], new String[]{"811.36Kb", "830.83Kb", "811.36", "830.83"});

        for (long size: fileSizes) {
            testOneSizeConverse(size, answers.get(size));
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

}
