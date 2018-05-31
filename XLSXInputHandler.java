import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;



import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * XLSXInputHandler
 * This class is responsible for parsing a bytearray of excel sheet into the required format
 * @author Francis Obiagwu
 * @version 1
 * @date 5/28/2018
 */

final class XLSXInputHandler implements FileInputHandler {
    Attribute.Int attributeInt;
    Attribute.FloatingPoint attributeFloatingPoint;
    Attribute.Arbitrary attributeArbitary;
    Attribute.Enumerated attributeEnumerated;
    Definition definition;
    String value;



    @Override
    public FileContents parseFile(byte[] inputBuffer) {
        try {
            Map<String, String[]> map = new HashMap<>();
            String path = "C:\\Users\\Francis\\group-2-project-backend\\src\\OUTFile.xls"; //path where to store the excel file. We are storing it first before reading because we cannot read excel file directly from bytes. (the current path is for testing purposes only)
            FileInputStream file = new FileInputStream(path);
            Path pathPath = Paths.get(path);
            Files.write(pathPath, inputBuffer);  // take the bytes received and then write them out in the specified path.

            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file); //create and object which will be used to read the excel file
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
            Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();

            while(rowIterator.hasNext()){ // navigate the excel sheet one row at a time
                org.apache.poi.ss.usermodel.Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int count = 0; // used to determine the id column
                String id = null;
                int numberOfColumns = row.getPhysicalNumberOfCells();
                String values[] = new String[numberOfColumns-1];

                while(cellIterator.hasNext()){ // for each row, go through each column
                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()){
                        case Cell.CELL_TYPE_BOOLEAN:
//                            System.out.print(cell.getBooleanCellValue() + "\t\t"); // this section currently not used. It is provided for extensibility
                            break;
                        case Cell.CELL_TYPE_NUMERIC: //since we are only considering numeric at this time
//                            System.out.print(cell.getNumericCellValue() + "\t\t");
                            if(count == 0){
                                id = String.valueOf(cell.getNumericCellValue()); // this section is for extensibility
                            }
                            else {
                                values[count-1] = String.valueOf(cell.getNumericCellValue());
                            }
                            break;

                        case Cell.CELL_TYPE_STRING:
//                            System.out.print(cell.getStringCellValue() + "\t\t");
                            break;
                    }
                    count += 1;
                }
                System.out.println(id +": "+Arrays.toString(values));
                map.put(id, values);
            }
            file.close();

            List<Integer> keys = new LinkedList<>(); // A List object that will be used to discover the max and min int
            for (String key: map.keySet()
                    ) {
                Double d = Double.parseDouble(key);
                Integer i = d.intValue();
                keys.add(i);
            }


            int max = Collections.max(keys);
            int min = Collections.min(keys);
            attributeInt = new Attribute.Int(max, min);
//            System.out.println(max +" "+ min);
            attributeFloatingPoint = new Attribute.FloatingPoint(max, min);
//            definition.put(value, attributeInt); // not sure what value will should be passed as parameter






        } catch (IOException e) {
            e.printStackTrace();}


        return new FileContents() {
                @Override
                public Definition getDefinition() {
                    return definition;
                }

                @Override
                public List<Sample> getSamples() {
                    return null;
                }
            };
        }


    }