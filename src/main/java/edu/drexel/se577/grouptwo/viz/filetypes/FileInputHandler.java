package edu.drexel.se577.grouptwo.viz.filetypes;

import java.util.Optional;

public interface FileInputHandler {
    Optional<? extends FileContents> parseFile(String name, byte[] buffer);
}
