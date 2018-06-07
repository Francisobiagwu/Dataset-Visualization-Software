package edu.drexel.se577.grouptwo.viz.test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import edu.drexel.se577.grouptwo.viz.parsers.CSVInputHandler;

public class CSVInputHandlerTest 
{
    public static void main( String[] args )
    {
        try {
            Path path = Paths.get("src\\main\\java\\edu\\drexel\\se577\\grouptwo\\viz\\test\\test.csv");
            byte[] data = Files.readAllBytes(path);
            new CSVInputHandler().parseFile("test.csv",data);
        }catch(Exception e){
            e.printStackTrace();    
        }

    }
}