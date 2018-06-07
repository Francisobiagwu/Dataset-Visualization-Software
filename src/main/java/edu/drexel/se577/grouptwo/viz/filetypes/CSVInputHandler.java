package edu.drexel.se577.grouptwo.viz.filetypes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.opencsv.CSVReader;

import org.apache.commons.lang3.StringUtils;

import edu.drexel.se577.grouptwo.viz.dataset.Attribute;
import edu.drexel.se577.grouptwo.viz.dataset.Sample;
import edu.drexel.se577.grouptwo.viz.dataset.Value;
import edu.drexel.se577.grouptwo.viz.filetypes.CSVFileContents;
import edu.drexel.se577.grouptwo.viz.filetypes.FileContents;
import edu.drexel.se577.grouptwo.viz.filetypes.FileInputHandler;

/**
 * CSVInputHandler This class is responsible for parsing a bytearray of excel
 * sheet into the required format.
 * 
 * @author Francis Obiagwu
 * @version 1
 * @date 5/28/2018
 */

class CSVInputHandler implements FileInputHandler {

    static String EXT_CSV = "application/csv";

    @Override
    public Optional<? extends FileContents> parseFile(String name, byte[] inputBuffer) {

        // first non-empty column is the Key. Strings with '#', a.k.a comment.
        // column 2 with no values afterwards :
        // - parse as float (assume all numbers are floats) if 0
        // - assume arbitrary value store as string
        // column 2..n : enumeration

        CSVReader csvReader;
        csvReader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(inputBuffer)));
        String[] nextLine;
        CSVFileContents contents = new CSVFileContents(name);

        try {
            String sKey = "";
            List<Value> values = new ArrayList<>();
            int maxInts = 0, minInts = 0;
            float maxFloats = 0,minFloats = 0;
            List<Integer> integers = new ArrayList<>();
            List<Float> floats = new ArrayList<>();
            Set<String> enumerated = new HashSet<String>();

            while ((nextLine = csvReader.readNext()) != null) {
                int numColumns = nextLine.length;
                int i = 0;

                // Use first column thats not empty or
                // marked as a comment (#), as the rows key.
                for (i = 0; i < numColumns; i++) {
                    String value = nextLine[i];
                    if (value == null || value.isEmpty() || value.contains("#")) {
                        continue;
                    }

                    sKey = value;
                    break;
                }

                if (sKey.isEmpty())
                    continue;

                StringBuilder sb = new StringBuilder();
                for (; i < numColumns; i++) {
                    String value = nextLine[i];
                    if (value == null || value.trim().isEmpty() || value.contains("#")) {
                        continue;
                    }

                    value = value.trim();

                    if (value.contains(",")) {
                        // dealing with a value thats a list.
                        // replace , with | so enum can be split later./**
                        value = value.replace(",", "|");
                    }

                    // add values, ignore key.
                    if (!value.equalsIgnoreCase(sKey)) {
                        sb.append(nextLine[i]);

                        // avoid last unnessesary comma
                        if (i != (numColumns - 1)) {
                            sb.append(",");
                        }
                    }
                }

                if (sb.length() > 0) {
                    String[] tokens = sb.toString().split(","); // should be no empty tokens
                    for (int t = 0; t < tokens.length; t++) {
                        String token = tokens[t];

                        // isNumeric fails on 25.5... naughty Oracle has a defect
                        String tempStr = token.replace(".", "");
                        if (StringUtils.isNumeric(tempStr)) {
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
                        } else {
                            // Check to see if the value was a comma delimited list./**
                            // commas were replaced with pipes above.
                            if (token.contains("|")) {
                                String[] enums = token.toString().split("|");

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
                        }
                    }
                }
            }

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

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.of(contents);
    }
}
