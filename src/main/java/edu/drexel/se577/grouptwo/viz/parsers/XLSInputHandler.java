package edu.drexel.se577.grouptwo.viz.parsers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import edu.drexel.se577.grouptwo.viz.dataset.Attribute;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.filetypes.FileContents;
import edu.drexel.se577.grouptwo.viz.filetypes.FileInputHandler;
import edu.drexel.se577.grouptwo.viz.filetypes.XLSFileContents;

/**
 * XLSInputHandler This class is responsible for parsing a bytearray of excel
 * sheet into the required format.
 * 
 * @author Francis Obiagwu
 * @version 1
 * @date 5/28/2018
 */

public class XLSInputHandler implements FileInputHandler {

    static String EXT_XLS = "application/xls";
    static String EXT_XLSX = "application/xlsx";

    Attribute.Int attributeInt;
    Attribute.FloatingPoint attributeFloatingPoint;
    Attribute.Arbitrary attributeArbitary;
    Attribute.Enumerated attributeEnumerated;
    Definition definition;
    String value;

    @Override
    public boolean CanParse(String ext) {
        return ext.equalsIgnoreCase(EXT_XLS) || ext.equalsIgnoreCase(EXT_XLSX);
    }

    @Override
    public Optional<? extends FileContents> parseFile(String name, byte[] inputBuffer) {
        try {
            Map<String, String[]> map = new HashMap<>();

            ByteArrayInputStream file = new ByteArrayInputStream(inputBuffer);

            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file); // create and object which will be used to read the
                                                                // excel file
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
            Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) { // navigate the excel sheet one row at a time
                org.apache.poi.ss.usermodel.Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int count = 0; // used to determine the id column
                String id = null;
                int numberOfColumns = row.getPhysicalNumberOfCells();
                String values[] = new String[numberOfColumns - 1];

                while (cellIterator.hasNext()) { // for each row, go through each column
                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_BOOLEAN: // this section currently not used. It is provided for extensibility
                        System.out.print(cell.getBooleanCellValue() + "\t\t");
                        break;
                    case Cell.CELL_TYPE_NUMERIC: // since we are only considering numeric at this time
                        System.out.print(cell.getNumericCellValue() + "\t\t");
                        if (count == 0) {
                            id = String.valueOf(cell.getNumericCellValue());
                        } else {
                            values[count - 1] = String.valueOf(cell.getNumericCellValue());
                        }
                        break;

                    case Cell.CELL_TYPE_STRING: // this section is for extensibility
                        System.out.print(cell.getStringCellValue() + "\t\t");
                        break;
                    }
                    count += 1;
                }
                System.out.println(id + ": " + Arrays.toString(values));
                map.put(id, values);
            }
            file.close();

            List<Integer> keys = new LinkedList<>(); // A List object that will be used to discover the max and min int
            for (String key : map.keySet()) {
                Double d = Double.parseDouble(key);
                Integer i = d.intValue();
                keys.add(i);
            }

            int max = Collections.max(keys);
            int min = Collections.min(keys);
            attributeInt = new Attribute.Int("?", max, min);
            // System.out.println(max +" "+ min);
            attributeFloatingPoint = new Attribute.FloatingPoint("?", max, min);
            // definition.put(value, attributeInt); // not sure what value will should be
            // passed as parameter

            hssfWorkbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.of(new XLSFileContents(name));
    }

}