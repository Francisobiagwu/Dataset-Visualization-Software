package edu.drexel.se577.grouptwo.viz.test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import edu.drexel.se577.grouptwo.viz.parsers.XLSInputHandler;

public class XLSInputHandlerTest 
{
    public static void main( String[] args )
    {
        try {        
            Path currentRelativePath = Paths.get("");
            Path path = Paths.get(currentRelativePath + "src\\main\\java\\edu\\drexel\\se577\\grouptwo\\viz\\test\\test.xls");
            byte[] data = Files.readAllBytes(path);
            new XLSInputHandler().parseFile("test.xls",data);
        }catch(Exception e){
            e.printStackTrace();    
        }

    }
}