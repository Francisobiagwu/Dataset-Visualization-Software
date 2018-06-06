package edu.drexel.se577.grouptwo.viz.parsers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import edu.drexel.se577.grouptwo.viz.dataset.Attribute;
import edu.drexel.se577.grouptwo.viz.dataset.Definition;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.dataset.Value;
import edu.drexel.se577.grouptwo.viz.filetypes.FileContents;
import edu.drexel.se577.grouptwo.viz.filetypes.FileInputHandler;
import edu.drexel.se577.grouptwo.viz.filetypes.XLSFileContents;

/**
 * XLSInputHandler This class is responsible for parsing a bytearray of excel
 * sheet into the required format.
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
        XLSFileContents contents = new XLSFileContents(name);
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(inputBuffer);

            // Use an InputStream, needs more memory
            Workbook wb = WorkbookFactory.create(stream);
            Sheet sheet = wb.getSheetAt(0);

            Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();

            String sKey = "";
            List<Value> values = new ArrayList<>();
            int maxInts = 0, minInts = 0;
            float maxFloats = 0,minFloats = 0;
            List<Integer> integers = new ArrayList<>();
            List<Float> floats = new ArrayList<>();
            Set<String> enumerated = new HashSet<String>();
            
            while (rowIterator.hasNext()) { // navigate the excel sheet one row at a time
                org.apache.poi.ss.usermodel.Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                                    
                while (cellIterator.hasNext()) { // for each row, go through each column
                    Cell cell = cellIterator.next();

                    if(cell.getCellTypeEnum() != CellType.STRING)
                        continue;

                    String value = cell.getStringCellValue();
                    if (value == null || value.isEmpty() || value.contains("#")) {
                        continue;
                    }

                    sKey = value;
                    break;
                }

                if (sKey.isEmpty())
                    continue;
                
                while (cellIterator.hasNext()) { // for each row, go through each column
                    Cell cell = cellIterator.next();

                    // ignore any cells of type CellType.ERROR, CellType.BLANK or CellType.FORMULA                    
                    if(cell.getCellTypeEnum() == CellType.BOOLEAN) {
                        // Only unique values for enum
                        if(!enumerated.contains("True_False")){
                            edu.drexel.se577.grouptwo.viz.dataset.Value.Enumerated fp = new edu.drexel.se577.grouptwo.viz.dataset.Value.Enumerated(
                                "True");
                            values.add(fp);
                            fp = new edu.drexel.se577.grouptwo.viz.dataset.Value.Enumerated(
                                "False");
                            values.add(fp);
                            enumerated.add("True");
                            enumerated.add("False");
                        }
                    } else if(cell.getCellTypeEnum() == CellType.STRING) {
                        String token = cell.getStringCellValue();

                        if (token.isEmpty())
                            continue;

                        // Check to see if the value was a comma delimited list./**
                        // commas were replaced with pipes above.
                        if (token.contains(",")) {                            
                            String[] enums = token.toString().split(",");

                            for (int enm = 0; enm < enums.length; enm++) {
                                edu.drexel.se577.grouptwo.viz.dataset.Value.Enumerated fp = new edu.drexel.se577.grouptwo.viz.dataset.Value.Enumerated(
                                    enums[enm]);
                                values.add(fp);

                                // Only unique values for enum
                                if(!enumerated.contains(enums[enm])){
                                    enumerated.add(enums[enm]);
                                }
                            }
                        } else {
                            // look to see if strings a known enumeration (enum must be previously defined)
                            if(enumerated.contains(token)){
                                edu.drexel.se577.grouptwo.viz.dataset.Value.Enumerated fp = new edu.drexel.se577.grouptwo.viz.dataset.Value.Enumerated(
                                    token);
                                values.add(fp);
                            } else {
                                // Alright, all thats left is that the value is an arbitrary string.
                                edu.drexel.se577.grouptwo.viz.dataset.Value.Arbitrary ap = new edu.drexel.se577.grouptwo.viz.dataset.Value.Arbitrary(
                                        token);
                                values.add(ap);
                            }
                        }

                    } else if(cell.getCellTypeEnum() == CellType.NUMERIC) {                        
                        Double value = cell.getNumericCellValue();

                        // Extra work but we want o classify the number as an integer if possible.
                        String token = value.toString();
                        try {
                            // falls though when value has a mantissa.
                            int val = Integer.parseInt(token);
                            edu.drexel.se577.grouptwo.viz.dataset.Value.Int fp = new edu.drexel.se577.grouptwo.viz.dataset.Value.Int(
                                    val);
                            values.add(fp);
                            integers.add(val);
                            continue;
                        } catch (NumberFormatException ex) {
                            // Not an integer... thats fine.
                        }

                        try {
                            // Apprently were dealing with a decimal
                            float val = Float.parseFloat(token);
                            edu.drexel.se577.grouptwo.viz.dataset.Value.FloatingPoint fp = new edu.drexel.se577.grouptwo.viz.dataset.Value.FloatingPoint(
                                    val);
                            values.add(fp);
                            floats.add(val);
                            continue;
                        } catch (NumberFormatException ex) {
                            // Not a float? really?. add as arbitrary
                            edu.drexel.se577.grouptwo.viz.dataset.Value.Arbitrary ap = new edu.drexel.se577.grouptwo.viz.dataset.Value.Arbitrary(
                                    token);
                            values.add(ap);
                        }
                    } 
                }
            }            
            stream.close();
            wb.close();
           
            // Determine min/max integers and floats, then add attributes.            
            if(integers.size() > 0){
                maxInts = Collections.max(integers);
                minInts = Collections.min(integers);
            }

            if(floats.size() > 0){
                maxFloats = Collections.max(floats);            
                minFloats = Collections.min(floats); 
            }

            contents.getDefinition().put(new Attribute.Int("integer", maxInts, minInts));
            contents.getDefinition().put(new Attribute.FloatingPoint("floating", maxFloats, minFloats));

            // Add arbitrary type
            contents.getDefinition().put(new Attribute.Arbitrary("comment"));

            // Add enumerated type, given found enumerations
            contents.getDefinition().put(new Attribute.Enumerated("color", enumerated));

            int count = 1;
            Sample s = new Sample();
            contents.getSamples().add(s);
            for (Value v : values) {
                s.put(sKey + Integer.toString(count), v);
                contents.getSamples().add(s);
            }

        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.of(contents);
    }

}