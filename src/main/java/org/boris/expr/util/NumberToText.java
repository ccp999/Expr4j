package org.boris.expr.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class NumberToText {

    private static BigDecimal MAX_VALUE = new BigDecimal("999999999999999.99");
    private static BigDecimal ONE_THOUSAND = new BigDecimal("1000");
    private static BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static BigDecimal TWENTY = new BigDecimal("20");
    
    private static final String[] specialNames = { "", " thousand", " million", " billion", " trillion" };

    private static final String[] tensNames = { "", " ten", " twenty", " thirty", " fourty", " fifty", " sixty",
            " seventy", " eighty", " ninety" };

    private static final String[] numNames = { "", " one", " two", " three", " four", " five", " six", " seven",
            " eight", " nine", " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen",
            " seventeen", " eighteen", " nineteen" };

    private static final List<String> numNamesList = Arrays.asList(numNames);
    private static final List<String> tenNamesList = Arrays.asList(tensNames);
    private static final List<String> specialNamesList = Arrays.asList(specialNames);
    private static final Set<String> validWords = new TreeSet<>();
    
    static {
        for (int i=1; i<specialNames.length; i++) {
            validWords.add(specialNames[i].substring(1));
        }
        for (int i=1; i<tensNames.length; i++) {
            validWords.add(tensNames[i].substring(1));
        }
        for (int i=1; i<numNames.length; i++) {
            validWords.add(numNames[i].substring(1));
        }
        validWords.add("hundred");
        validWords.add("and");
        validWords.add("dollars");
        validWords.add("cents");
        validWords.add("negative");
        validWords.add("zero");
        validWords.add("point");
    }

    private static String convertLessThanOneThousand(BigDecimal number) {
        String current;

        // Handles numbers between 1 and 19
        if (number.remainder(ONE_HUNDRED).compareTo(TWENTY) < 0) {
            current = numNames[number.remainder(ONE_HUNDRED).intValue()];
            number = number.divide(ONE_HUNDRED, new MathContext(50)).setScale(0, RoundingMode.DOWN);
        //Handles numbers between 20 through 99 by concatenating two parts together
        //i.e. 95 = "ninety" + "five"
        } else {            
            current = numNames[number.remainder(BigDecimal.TEN).intValue()];
            number = number.divide(BigDecimal.TEN, new MathContext(50)).setScale(0, RoundingMode.DOWN);

            current = tensNames[number.remainder(BigDecimal.TEN).intValue()] + (current.equals("") ? "" : "-") + current.replace(" ", "");
            number = number.divide(BigDecimal.TEN, new MathContext(50)).setScale(0, RoundingMode.DOWN);
        }
        if (number.compareTo(BigDecimal.ZERO) == 0) return current;
        
        // Handles numbers > 99
        return numNames[number.intValue()] + " hundred" + current;
    }

    /**
     * Converts a string of words back to a double value
     * @param words
     * @return
     */
    public static double convert(String words) {
        words = words.replaceAll("-", " ");
        
        List<String> wordList = Arrays.asList(words.split(" "));
        double convertedValue = 0;
        double multiplierValue = 0;
        float decimalFactor = 1;
        boolean isNegative = false;
        
        Iterator<String> wordIterator = wordList.iterator();
        while (wordIterator.hasNext()) {
            String word = wordIterator.next();

            // If the next word is "and" or "point" we need to 
            // switch to decimal mode because numbers are after period
            if (word.equals("and") || word.equals("point")) {
                decimalFactor = .01f;
                continue;                
            }
            // This is a negative number, make a note of it
            else if (word.equals("negative")) {
                isNegative = true;
                continue;
            }           
                    
            int numIndex = numNamesList.indexOf(" " + word);
            int tenIndex = tenNamesList.indexOf(" " + word);
        
            // Word represents number between 1 and 19
            if (numIndex > 0) {
                multiplierValue += (numIndex * decimalFactor);
            }
            // Word represents a ten number (i.e. twenty, thirty, etc)
            else if (tenIndex > 0) {
                multiplierValue += (tenIndex * 10 * decimalFactor);
            } else {
                if (word.equals("hundred")) {
                    multiplierValue *= 100;
                }
                else {
                    // Multiply the value by 1000 the appropriate number of times
                    multiplierValue *= convertSpecialName(word);
                    convertedValue += multiplierValue;
                    
                    // We need to reset after thousand, million, etc
                    multiplierValue = 0;                    
                }
            }
        }

        convertedValue += multiplierValue;
        
        if (isNegative) {
            return (double) Math.round(convertedValue * -100) / 100;
        } else {
            return (double) Math.round(convertedValue * 100) / 100;
        }
    }

    private static double convertSpecialName(String specialName) {        
        int index = specialNamesList.indexOf(" " + specialName);
        double value = 1;
        if (index >= 0) {
            while (index-- > 0) {
                value *= 1000;
            }
        }
        
        return value;
    }
    
    /**
     * Converts a double to a sentence of words. If number is >= 1 Quadrillion, it will
     * be returned as scientific notation.
     * @param number
     * @return
     */
    public static String convert(BigDecimal number) {
        // Let's stop at 999 trillion
        if (number.compareTo(MAX_VALUE) >= 0) {
            return number.toString();
        }
       
        // Parse decimal portion off as an integer so we can convert
        // it the same way
        //int decimalAsInt = (int) Math.round((number % 1) * 100);
        BigDecimal scaleValue = number.remainder(BigDecimal.ONE).multiply(ONE_HUNDRED);
        if (scaleValue.compareTo(BigDecimal.ZERO) < 0) {
            scaleValue = scaleValue.negate();
        }

        number = number.setScale(0, RoundingMode.DOWN);
        String convertedNumber = convertNumber(number);
        
        // If there is a decimal, convert that too
        if (scaleValue.compareTo(BigDecimal.ZERO) > 0) {
            String convertedDecimal = convertNumber(scaleValue);
            
            // If scale value is less than 10 we need to add point 
            // i.e. 5.02 should be point zero two because point two would imply 5.20
            if (scaleValue.compareTo(BigDecimal.TEN) < 0) {
                return convertedNumber + " point zero " +  convertedDecimal;
            }
            else {
                return convertedNumber + " point " +  convertedDecimal;
            }
        } else {
            return convertedNumber;
        }

    }

    /**
     * Convert a number to a currency value
     * i.e. one hundred point fifty three becomes one hundred dollars and fifty three cents
     * @param text
     * @return
     */
    public static String convertToCurrency(String text, BigDecimal value) {
        BigDecimal scaleValue = value.remainder(BigDecimal.ONE).multiply(new BigDecimal("100"));
        boolean singleDollar = value.compareTo(BigDecimal.ONE) >=0 && value.compareTo(new BigDecimal("2")) < 0;
        boolean singleCent = scaleValue.compareTo(BigDecimal.ONE) >=0 && scaleValue.compareTo(new BigDecimal("2")) < 0;
        boolean lessThanDime = scaleValue.compareTo(BigDecimal.TEN) <0;
        
        if (text.indexOf("point") > 0) {       
            String wordsToReplace = "point";

            // If scale value is less than 10 we need to undo the zero before point 
            // i.e. 5.02 will be point zero two because point two would imply 5.20            
            if (lessThanDime) {
                wordsToReplace = "point zero";
            }
            return text.replaceAll(wordsToReplace, "dollar" + (singleDollar == false ? "s" : "") + " and") + " cent"
                    + (singleCent == false ? "s" : "");
        }
        else {
            return text + " dollar" + (singleDollar == false ? "s" : "");
        }
    }
    
    /**
     * Determines if the provided text is eligible to be converted to a double
     * @param text
     * @return
     */
    public static boolean isParsable(String text) {
        if (text == null) {
            return false;
        }
        else {
            String[] words = text.split(" ");
            for (String word : words) {
                if (!validWords.contains(word)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Converts a double value to a number
     * @param number
     * @return
     */
    private static String convertNumber(BigDecimal number) {
        if (number.compareTo(BigDecimal.ZERO) == 0) {
            return "zero";
        }

        String prefix = "";

        if (number.compareTo(BigDecimal.ZERO) < 0) {
            number = number.negate();
            prefix = "negative";
        }

        String current = "";
        int place = 0;

        do {
            //int n = (int) (number % 1000);
            BigDecimal remainder = number.remainder(ONE_THOUSAND);
            if (remainder.compareTo(BigDecimal.ZERO)  != 0) {
                String s = convertLessThanOneThousand(remainder);
                current = s + specialNames[place] + current;
            }
            place++;
            number = number.divide(ONE_THOUSAND, new MathContext(50)).setScale(0, RoundingMode.DOWN) ;
        } while (number.compareTo(BigDecimal.ZERO) >= 1);

        return (prefix + current).trim();
    }
    
/*    public static void main (String[] args) throws UnparsableNumberTextException {
        NumberToText n = new NumberToText();
        System.out.println(n.convert("fifty one billion four hundred eight million eight hundred fourty eight thousand four hundred eighty six point ten"));
        n.convert(95);
    }*/
}
