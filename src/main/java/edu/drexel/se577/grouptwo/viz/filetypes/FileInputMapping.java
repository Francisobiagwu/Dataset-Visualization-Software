package edu.drexel.se577.grouptwo.viz.filetypes;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

class FileInputMapping {
    private final Map<String, FileInputHandler> handlers;

    private static Optional<FileInputMapping> mapping = Optional.empty();

    private FileInputMapping() {
        Map<String, FileInputHandler> handlers = new HashMap<>();
        // TODO: add mime-type to FileInputHandler Implementations here ala
        // handlers.put("video/ogg", new OggFileInputHandler());
        this.handlers = handlers;
    }

    Optional<FileInputHandler> get(String mimeType) {
        return Optional.ofNullable(handlers.get(mimeType));
    }

    static FileInputMapping getInstance() {
        mapping = Optional.of(mapping.orElseGet(FileInputMapping::new));
        return mapping.get();
    }
}
