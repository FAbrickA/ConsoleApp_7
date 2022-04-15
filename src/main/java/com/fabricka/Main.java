package com.fabricka;

import org.kohsuke.args4j.CmdLineException;

import java.io.File;

// long может хранить до 8388607 Тб, этого более чем достаточно


public class Main {
    public static void main(String[] args) {
        CommandLineArguments cmdArgs;
        try {
            cmdArgs = new CommandLineArguments(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return;
        }
        final File[] files = cmdArgs.getFiles();
        final boolean humanReadable = cmdArgs.isHumanReadable();
        final int base = cmdArgs.getBase();

        if (cmdArgs.isSummarise()) {
            final String totalSize =
                    FileSizeFinder.getTotalSize(files, humanReadable, base);
            System.out.println("Total size: " + totalSize);
        } else {
            final FileSizeFinder.FileInfo[] filesInfo =
                    FileSizeFinder.getFilesInfo(files, humanReadable, base);
            for (FileSizeFinder.FileInfo fileInfo: filesInfo) {
                System.out.println(
                        (fileInfo.getFile().isFile() ? "File:       " : "Directory:  ") +
                        fileInfo.getFile().getPath() +
                        "  \t" + fileInfo.getSize());
            }
        }
    }
}
