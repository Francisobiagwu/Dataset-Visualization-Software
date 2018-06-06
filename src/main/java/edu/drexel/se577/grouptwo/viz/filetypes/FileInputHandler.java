package edu.drexel.se577.grouptwo.viz.filetypes;

import java.util.Optional;

public interface FileInputHandler {
    @Deprecated
    boolean CanParse(String ext);
    Optional<? extends FileContents> parseFile(String name, byte[] buffer);

    public static Optional<? extends FileInputHandler> forType(String mimeType) {
        return FileInputMapping.getInstance().get(mimeType);
    }
}
