package io.zipcoder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ItemParserTest {

    private String rawSingleItem = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##";

    private String rawSingleItemIrregularSeperatorSample = "naMe:MiLK;price:3.23;type:Food^expiration:1/11/2016##";

    private String rawBrokenSingleItem = "naMe:;price:3.23;type:Food;expiration:1/25/2016##";

    private String rawMultipleItems = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##"
            + "naME:BreaD;price:1.23;type:Food;expiration:1/02/2016##"
            + "NAMe:BrEAD;price:1.23;type:Food;expiration:2/25/2016##";

    private String rawMultipleItemsMore = "naMe:;price:3.23;type:Food;expiration:1/25/2016##"
            + "naME:MILK;price:2.75;type:Food;expiration:1/02/2016##"
            + "NAMe:miLK;price:2.75;type:Food;expiration:2/25/2016##"
            + "naME:c00kIEs;price:1.23;type:Food;expiration:1/02/2013##"
            + "naME:BreaD;price:1.23;type:Food;expiration:1/02/2016##"
            + "naME:milk;price:1.50;type:Food;expiration:3/02/2012##"
            + "naME:BreaD;price:1.23;type:Food;expiration:1/02/2016##"
            + "naME:COOKIES;price:1.75;type:Food;expiration:1/02/2014##"
            + "naME:BreaD;price:1.50;type:Food;expiration:1/02/2016##";

    private ItemParser itemParser;


    @Before
    public void setUp() {
        itemParser = new ItemParser();
    }

    @Test
    public void parseRawDataIntoStringArrayTest() {
        Integer expectedArraySize = 3;
        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(rawMultipleItems);
        Integer actualArraySize = items.size();
        assertEquals(expectedArraySize, actualArraySize);
    }

    @Test
    public void parseStringIntoItemTest() throws ItemParseException {
        Item expected = new Item("milk", 3.23, "food", "1/25/2016");
        Item actual = itemParser.parseStringIntoItem(rawSingleItem);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = ItemParseException.class)
    public void parseBrokenStringIntoItemTest() throws ItemParseException {
        itemParser.parseStringIntoItem(rawBrokenSingleItem);
    }

    @Test
    public void findKeyValuePairsInRawItemDataTest() {
        Integer expected = 4;
        Integer actual = itemParser.findKeyValuePairsInRawItemData(rawSingleItem).size();
        assertEquals(expected, actual);
    }

    @Test
    public void findKeyValuePairsInRawItemDataTestIrregular() {
        Integer expected = 4;
        Integer actual = itemParser.findKeyValuePairsInRawItemData(rawSingleItemIrregularSeperatorSample).size();
        assertEquals(expected, actual);
    }

    @Test
    public void addGroceriesToListTest1() {
        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(rawMultipleItems);
        itemParser.addGroceriesToList(items);
        int expected = 2;
        int actual = itemParser.getGroceries().size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addGroceriesToListTest2() {
        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(rawMultipleItems);
        itemParser.addGroceriesToList(items);
        int expected = 2;
        int actual = itemParser.getGroceries().get("bread").size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addGroceriesToListTest3() {
        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(rawMultipleItems);
        itemParser.addGroceriesToList(items);
        String expected = "[bread, milk]";
        String actual = itemParser.getGroceries().keySet().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void parseTest1() {
        String expected = "\nname:    Bread          seen:  3  times\n" +
                "==============\t\t\t===============\n" +
                "Price:    1.23          seen:  2  times\n" +
                "--------------\t\t\t---------------\n" +
                "Price:     1.5          seen:  1  times\n" +
                "--------------\t\t\t---------------\n" +
                "\n" +
                "name:     Milk          seen:  3  times\n" +
                "==============\t\t\t===============\n" +
                "Price:    2.75          seen:  2  times\n" +
                "--------------\t\t\t---------------\n" +
                "Price:     1.5          seen:  1  times\n" +
                "--------------\t\t\t---------------\n" +
                "\n" +
                "name:  Cookies          seen:  2  times\n" +
                "==============\t\t\t===============\n" +
                "Price:    1.23          seen:  1  times\n" +
                "--------------\t\t\t---------------\n" +
                "Price:    1.75          seen:  1  times\n" +
                "--------------\t\t\t---------------\n" +
                "\n" +
                "\n" +
                "1  errors\n";
        String actual = itemParser.parse(rawMultipleItemsMore);
        Assert.assertEquals(expected, actual);
    }

}