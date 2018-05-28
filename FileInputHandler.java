import au.com.bytecode.opencsv.CSVReader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;


/**
 * FileInputHandler
 * @author Francis Obiagwu
 * @version 1
 * @date 5/27/2018
 */
public interface FileInputHandler {
    FileContents parseFile(byte[] inputBuffer);


}
