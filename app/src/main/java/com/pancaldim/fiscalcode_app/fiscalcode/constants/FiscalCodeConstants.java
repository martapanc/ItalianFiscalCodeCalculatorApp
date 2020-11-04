package com.pancaldim.fiscalcode_app.fiscalcode.constants;

import java.util.HashMap;
import java.util.Map;

public class FiscalCodeConstants {

    public static Map<Character, Integer> getEvenPositionMap() {
        Map<Character, Integer> evenPositionMap = new HashMap<>();
        evenPositionMap.put('0', 0);
        evenPositionMap.put('1', 1);
        evenPositionMap.put('2', 2);
        evenPositionMap.put('3', 3);
        evenPositionMap.put('4', 4);
        evenPositionMap.put('5', 5);
        evenPositionMap.put('6', 6);
        evenPositionMap.put('7', 7);
        evenPositionMap.put('8', 8);
        evenPositionMap.put('9', 9);
        evenPositionMap.put('A', 0);
        evenPositionMap.put('B', 1);
        evenPositionMap.put('C', 2);
        evenPositionMap.put('D', 3);
        evenPositionMap.put('E', 4);
        evenPositionMap.put('F', 5);
        evenPositionMap.put('G', 6);
        evenPositionMap.put('H', 7);
        evenPositionMap.put('I', 8);
        evenPositionMap.put('J', 9);
        evenPositionMap.put('K', 10);
        evenPositionMap.put('L', 11);
        evenPositionMap.put('M', 12);
        evenPositionMap.put('N', 13);
        evenPositionMap.put('O', 14);
        evenPositionMap.put('P', 15);
        evenPositionMap.put('Q', 16);
        evenPositionMap.put('R', 17);
        evenPositionMap.put('S', 18);
        evenPositionMap.put('T', 19);
        evenPositionMap.put('U', 20);
        evenPositionMap.put('V', 21);
        evenPositionMap.put('W', 22);
        evenPositionMap.put('X', 23);
        evenPositionMap.put('Y', 24);
        evenPositionMap.put('Z', 25);
        return evenPositionMap;
    }

    public static Map<Character, Integer> getOddPositionMap() {
        Map<Character, Integer> oddPositionMap = new HashMap<>();
        oddPositionMap.put('0', 1);
        oddPositionMap.put('1', 0);
        oddPositionMap.put('2', 5);
        oddPositionMap.put('3', 7);
        oddPositionMap.put('4', 9);
        oddPositionMap.put('5', 13);
        oddPositionMap.put('6', 15);
        oddPositionMap.put('7', 17);
        oddPositionMap.put('8', 19);
        oddPositionMap.put('9', 21);
        oddPositionMap.put('A', 1);
        oddPositionMap.put('B', 0);
        oddPositionMap.put('C', 5);
        oddPositionMap.put('D', 7);
        oddPositionMap.put('E', 9);
        oddPositionMap.put('F', 13);
        oddPositionMap.put('G', 15);
        oddPositionMap.put('H', 17);
        oddPositionMap.put('I', 19);
        oddPositionMap.put('J', 21);
        oddPositionMap.put('K', 2);
        oddPositionMap.put('L', 4);
        oddPositionMap.put('M', 18);
        oddPositionMap.put('N', 20);
        oddPositionMap.put('O', 11);
        oddPositionMap.put('P', 3);
        oddPositionMap.put('Q', 6);
        oddPositionMap.put('R', 8);
        oddPositionMap.put('S', 12);
        oddPositionMap.put('T', 14);
        oddPositionMap.put('U', 16);
        oddPositionMap.put('V', 10);
        oddPositionMap.put('W', 22);
        oddPositionMap.put('X', 25);
        oddPositionMap.put('Y', 24);
        oddPositionMap.put('Z', 23);
        return oddPositionMap;
    }

    public static Map<Integer, String> getControlCharMap() {
        Map<Integer, String> controlCharMap = new HashMap<>();
        controlCharMap.put(0, "A");
        controlCharMap.put(1, "B");
        controlCharMap.put(2, "C");
        controlCharMap.put(3, "D");
        controlCharMap.put(4, "E");
        controlCharMap.put(5, "F");
        controlCharMap.put(6, "G");
        controlCharMap.put(7, "H");
        controlCharMap.put(8, "I");
        controlCharMap.put(9, "J");
        controlCharMap.put(10, "K");
        controlCharMap.put(11, "L");
        controlCharMap.put(12, "M");
        controlCharMap.put(13, "N");
        controlCharMap.put(14, "O");
        controlCharMap.put(15, "P");
        controlCharMap.put(16, "Q");
        controlCharMap.put(17, "R");
        controlCharMap.put(18, "S");
        controlCharMap.put(19, "T");
        controlCharMap.put(20, "U");
        controlCharMap.put(21, "V");
        controlCharMap.put(22, "W");
        controlCharMap.put(23, "X");
        controlCharMap.put(24, "Y");
        controlCharMap.put(25, "Z");
        return controlCharMap;
    }
}
