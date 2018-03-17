package io.zipcoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {

    private Pattern pattern;
    private Matcher matcher;
    int exceptionCount = 0;
    private HashMap<String, ArrayList<Item>> groceries;

    public ItemParser() {
        groceries = new HashMap<String, ArrayList<Item>>();
    }

    public void parse(String aString) {
        ArrayList<String> groceries = parseRawDataIntoStringArray(aString);
        addGroceriesToList(groceries);
        printGroceries();
        System.out.println(printGroceries());
    }

    public String printGroceries() {

        StringBuilder display = new StringBuilder();

        for (Map.Entry<String, ArrayList<Item>> entry : groceries.entrySet()) {
            display.append("\nname:");
            display.append(String.format("%9s", entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1)));
            display.append("          seen:  " + entry.getValue().size() + "  times\n");
            display.append("==============" + "\t\t\t" + "===============\n");

            ArrayList<Double> temp = getUniquePrices(entry);

            for (int i = 0; i < temp.size(); i++) {
                display.append("Price:");
                display.append(String.format("%8s", temp.get(i)));
                display.append("          seen:  " + countPriceOccurences(entry.getValue(), temp.get(i)) + "  times\n");
                display.append("--------------" + "\t\t\t" + "---------------\n");
            }
        }

        display.append("\n\n" + exceptionCount + "  errors\n");

        return display.toString();
    }

    public ArrayList<Double> getUniquePrices(Map.Entry<String, ArrayList<Item>> entry) {
        ArrayList<Double> prices = new ArrayList<Double>();

        for (int i = 0; i < entry.getValue().size(); i++) {
            if (!prices.contains(entry.getValue().get(i).getPrice())) {
                prices.add(entry.getValue().get(i).getPrice());
            }
        }
        return prices;
    }

    public int countPriceOccurences(ArrayList<Item> aList, Double aPrice) {
        int count = 0;

        for (int i = 0; i < aList.size(); i++) {
            if (aList.get(i).getPrice().equals(aPrice)) {
                count++;
            }
        }
        return count;
    }

    public void addGroceriesToList(ArrayList<String> jerkList) {
        for (int i = 0; i < jerkList.size(); i++) {
            try {
                Item temp = (parseStringIntoItem(jerkList.get(i)));
                incrementItem(this.groceries, temp);
            } catch (ItemParseException ipe) {
                exceptionCount++;
            }
        }
    }

    private void incrementItem(Map<String, ArrayList<Item>> aMap, Item anItem) {
        if (aMap.keySet().contains(anItem.getName())) {
            aMap.get(anItem.getName()).add(anItem);
        } else {
            aMap.put(anItem.getName(), new ArrayList<Item>());
            aMap.get(anItem.getName()).add(anItem);
        }
    }

    public ArrayList<String> parseRawDataIntoStringArray(String rawData) {
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern, rawData);
        return response;
    }

    public Item parseStringIntoItem(String rawItem) throws ItemParseException {
        String aName = findName(rawItem);
        Double aPrice = findPrice(rawItem);
        String aType = findType(rawItem);
        String anExpiration = findExpiration(rawItem);

        if (findName(rawItem) == null || findPrice(rawItem) == null) {
            throw new ItemParseException();
        }

        return new Item(aName, aPrice, aType, anExpiration);
    }

    private String findName(String aString) {
        String search = "(?<=([n|N][a|A][m|M][e|E][^a-zA-z])).*?(?=[^a-zA-z0])";
        pattern = Pattern.compile(search);
        matcher = pattern.matcher(aString);

        if (matcher.find()) {
            if (matcher.group().length() > 0) {
                return replaceZeros(matcher.group().toLowerCase());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private String replaceZeros(String aString) {
        pattern = Pattern.compile("[0]");
        matcher = pattern.matcher(aString);
        return matcher.replaceAll("o");
    }

    private Double findPrice(String aString) {
        pattern = Pattern.compile("(?<=([p|P][r|R][i|I][c|C][e|E][^a-zA-Z])).*?(?=[^0-9.])");
        matcher = pattern.matcher(aString);

        if (matcher.find()) {
            if (matcher.group().length() > 0) {
                return Double.parseDouble(matcher.group());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private String findType(String aString) {
        pattern = Pattern.compile("(?<=([t|T][y|Y][p|P][e|E][^a-zA-z])).*?(?=[^a-zA-Z0])");
        matcher = pattern.matcher(aString);

        if (matcher.find()) {
            if (matcher.group().length() > 0) {
                return matcher.group().toLowerCase();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private String findExpiration(String aString) {
        String search = "(?<=([e|E][x|X][p|P][i|I][r|R][a|A][t|T][i|I][o|O][n|N][^a-zA-z]))(.)+[^#]";
        pattern = Pattern.compile(search);
        matcher = pattern.matcher(aString);

        if (matcher.find()) {
            if (matcher.group().length() > 0) {
                return matcher.group();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem) {
        String stringPattern = "[;|^]";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern, rawItem);
        return response;
    }

    private ArrayList<String> splitStringWithRegexPattern(String stringPattern, String inputString) {
        return new ArrayList<String>(Arrays.asList(inputString.split(stringPattern)));
    }

    public HashMap<String, ArrayList<Item>> getGroceries() {
        return groceries;
    }
}
