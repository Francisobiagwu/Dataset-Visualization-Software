package edu.drexel.se577.grouptwo.viz.test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import edu.drexel.se577.grouptwo.viz.parsers.XLSInputHandler;

public class XLSInputHandlerTest 
{
    public static void main( String[] args )
    {
        try {
            Path path = Paths.get("src\\main\\java\\edu\\drexel\\se577\\grouptwo\\viz\\test\\test.xlsx");
            byte[] data = Files.readAllBytes(path);
            new XLSInputHandler().parseFile("test.xlsx",data);
        }catch(Exception e){
            e.printStackTrace();    
        }

    }
}