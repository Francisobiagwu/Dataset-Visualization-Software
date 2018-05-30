import au.com.bytecode.opencsv.CSVReader;
import java.io.*;
import java.util.*;

/**
 * @author Francis Obiagwu
 * @version 1
 * @date 5/28/2018
 */

public class CSVInputHandler implements FileInputHandler {
    CSVReader csvReader;
    Attribute.Int attributeInt;
    Attribute.FloatingPoint attributeFloatingPoint;
    Attribute.Arbitrary attributeArbitary;
    Attribute.Enumerated attributeEnumerated;
    Definition definition;
    String key;
    String value;


    @Override
    public FileContents parseFile(byte[] inputBuffer) {

        attributeArbitary = new Attribute.Arbitrary();
        csvReader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(inputBuffer)));
        definition = new Definition();
        String[] nextLine;
        int numColumns = Integer.MAX_VALUE; // just a placeholder for comparison
        Map<String, String> map = new HashMap<>();

        try {
            while((nextLine = csvReader.readNext())!=null){
                if(numColumns == Integer.MAX_VALUE){
                    numColumns = nextLine.length; // set the real value for the number of columns
                }
                if(numColumns != nextLine.length){ //for some csv, the number of columns may decrease/change before, during operation, we want to stop reading once we discover uneven number of columns
                    break;
                }
                else{
                    key = nextLine[0];            //get the key
                    StringBuilder sb = new StringBuilder(); //this is used to keep track of all the values associated with a key
                    for (int i = 1; i < numColumns; i++){
                        sb.append(nextLine[i]);
                        if(i != (numColumns-1)){
                            sb.append(",");
                        }
                    }
                    value = sb.toString();
                    map.put(key, value);
                    System.out.println(key+" :"+value);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();


        }

        List<Integer> keys = new LinkedList<>(); // A List object that will be used to discover the max and min int
        for (String key: map.keySet()
             ) {
            keys.add(Integer.parseInt(key));
        }


        int max = Collections.max(keys);
        int min = Collections.min(keys);
        attributeInt = new Attribute.Int(max, min);
        attributeFloatingPoint = new Attribute.FloatingPoint(max, min);
        definition.put(value, attributeInt);


        return new FileContents() {
            @Override
            public Definition getDefinition() {
                return definition;
            }

            @Override
            public List<Sample> getSamples() { // still needs be implemented, not entirely sure what needs to be done here
                return null;
            }
        };
    }



}