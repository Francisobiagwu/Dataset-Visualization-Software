package edu.drexel.se577.grouptwo.viz.filetypes;

import java.util.Optional;

public interface FileInputHandler {
    boolean CanParse(String ext);
    Optional<? extends FileContents> parseFile(String name, byte[] buffer);
}
