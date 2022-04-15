package com.fabricka;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;

public class CommandLineArguments {
    private int base;

    @Argument(usage="Files or/and directories to analyse")
    private File[] files;

    @Option(name="-h", usage="Flag for human readable format")
    private boolean humanReadable = false;

    @Option(name="-c", usage="Flag to summarise size of all files/directories")
    private boolean summarise = false;

    @Option(name="-si", usage="Flag to change base size from 1024 to 1000")
    private boolean base1000 = false;

    public File[] getFiles() {
        return files;
    }

    public int getBase() {
        return base;
    }

    public boolean isHumanReadable() {
        return humanReadable;
    }

    public boolean isSummarise() {
        return summarise;
    }

    public CommandLineArguments(String[] args) throws CmdLineException {
        CmdLineParser parser = new CmdLineParser(this);
        parser.parseArgument(args);
        if (files == null) throw new CmdLineException("No files");
        this.base = this.base1000 ? 1000 : 1024;
    }
}
