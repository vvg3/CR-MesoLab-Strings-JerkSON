package io.zipcoder;

import org.apache.commons.io.IOUtils;


public class Main {

    static ItemParser parser = new ItemParser();

    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) throws Exception{
        String output = (new Main()).readRawDataToString();
        // TODO: parse the data in output into items, and display to console.
        System.out.println(parser.parse(output));
    }
}
