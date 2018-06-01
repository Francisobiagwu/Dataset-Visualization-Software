
package edu.drexel.se577.grouptwo.viz.filetypes;

import java.util.List;

public class FileInputFactory {

    List<FileInputHandler> _handlers;

    public FileInputFactory(List<FileInputHandler> handlers){
        _handlers = handlers;
    }

    public FileInputHandler GetFileInputHandler(String ext){
        for(FileInputHandler h: _handlers){
            if(h.CanParse(ext)){
                return h;
            }
         }

         return null;
    }
    
    public boolean IsType(String ext) {
        return false;
    }
}