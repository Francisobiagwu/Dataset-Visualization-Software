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


    final class CSVInputHandler implements FileInputHandler {
        CSVReader csvReader;


        @Override
        public FileContents parseFile(byte[] inputBuffer) {
            try {
                csvReader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(inputBuffer)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new FileContents() {
                @Override
                public Definition getDefintion() {
                    return new Definition();
                }

                @Override
                public Sample getSamples() {
                    return new Sample();
                }
            };
        }
    }

    final class XLSXInputHandler implements FileInputHandler {
        HSSFWorkbook workbook;

        @Override
        public FileContents parseFile(byte[] inputBuffer) {
            try {
                workbook = new HSSFWorkbook((new POIFSFileSystem(new ByteArrayInputStream(inputBuffer))));

            } catch (IOException e) {
                e.printStackTrace();
            }


            return new FileContents() {
                @Override
                public Definition getDefintion() {
                    return new Definition();
                }

                @Override
                public Sample getSamples() {
                    return new Sample();
                }
            };
        }
    }

    final class MatInputHandler implements FileInputHandler {
        //not sure what MatInputhandlder input type is

        @Override
        public FileContents parseFile(byte[] inputBuffer) {
            return new FileContents() {
                @Override
                public Definition getDefintion() {
                    return new Definition();
                }

                @Override
                public Sample getSamples() {
                    return new Sample();
                }
            };
        }
    }


}
