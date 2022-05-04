package calc;

import java.io.IOException;
import java.util.*;

public class Main {
    private static List<String> list = new ArrayList<>();
    private static String type = null;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println(calc(scanner.nextLine()));
    }

    public static String calc(String input) throws IOException {
        StringTokenizer st = new StringTokenizer(input, "+-*/", true);
        Integer result = null;
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }

        symbolValid(list);
        typeEqualsValid(list);
        constraintValueValid(list);
        constraintMathValid(list);

        switch (list.get(1)) {
            case ("+"):
                if (type.equals("Integer"))
                    result = Integer.parseInt(list.get(0)) + Integer.parseInt(list.get(2));
                else result = romanToInt(list.get(0)) + romanToInt(list.get(2));
                break;
            case ("-"):
                if (type.equals("Integer"))
                    result = Integer.parseInt(list.get(0)) - Integer.parseInt(list.get(2));
                else result = romanToInt(list.get(0)) - romanToInt(list.get(2));
                break;
            case ("*"):
                if (type.equals("Integer"))
                    result = Integer.parseInt(list.get(0)) * Integer.parseInt(list.get(2));
                else result = romanToInt(list.get(0)) * romanToInt(list.get(2));
                break;
            case ("/"):
                if (type.equals("Integer"))
                    result = Integer.parseInt(list.get(0)) / Integer.parseInt(list.get(2));
                else result = romanToInt(list.get(0)) / romanToInt(list.get(2));
                break;
            default:
                throw new IOException("Invalid math operation");
        }
        if (type.equals("String")) {
            if (result < 0)
                throw new IOException("throws Exception //т.к. в римской системе нет отрицательных чисел");
            return intToRoman(result);
        } else return result.toString();
    }

    private static String getType(String s) throws IOException {
        try {
            Integer.parseInt(s);
            return "Integer";
        } catch (NumberFormatException e) {
            try {
                Double.parseDouble(s);
                return "Double";
            } catch (NumberFormatException e1) {
                return "String";
            }
        }
    }

    private static String intToRoman(int number) {
        if (number >= 4000 || number < 0)
            return "-1";
        if (number == 0)
            return "nulla";
        StringBuilder result = new StringBuilder();
        for (Integer key : units.descendingKeySet()) {
            while (number >= key) {
                number -= key;
                result.append(units.get(key));
            }
        }
        return result.toString();
    }

    private static Integer romanToInt(String romanNumber) throws IOException {
        int result = 0, prev = 0, actual = 0;
        for (int i = 1; i < romanNumber.length(); i++) {
            prev = search(romanNumber.charAt(i - 1));
            actual = search(romanNumber.charAt(i));
//            if (prev == -1 || actual == -1)
//                throw new IOException("Invalid roman number");
            if (prev >= actual)
                result += prev;
            else result -= prev;
        }
        if (romanNumber.length() > 1)
            result += actual;
        else result += search(romanNumber.charAt(0));
        return result;
    }

    private static int search(char s) throws IOException {
        Set<Map.Entry<Integer, String>> entrySet = units.entrySet();
        for (Map.Entry<Integer, String> entry : entrySet) {
            if (entry.getValue().equals(String.valueOf(s)))
                return entry.getKey();
        }
        return -1;
    }

    private static void typeEqualsValid(List<String> list) throws IOException {
        List<String> tempList = new ArrayList<>(list);
        deleteSign(tempList);
        Iterator<String> iterator = tempList.iterator();
        type = getType(iterator.next());
        while (iterator.hasNext())
            if (!type.equals(getType(iterator.next())))
                throw new IOException("throws Exception //т.к. используются одновременно разные системы счисления");
    }

    private static void deleteSign(List<String> list) throws IOException {
        Iterator<String> iterator = list.iterator();
        String type = null;
        while (iterator.hasNext()) {
            String i = iterator.next();
            type = getType(i);
            if (type.equals("Double"))
                throw new IOException("throws Exception //т.к. формат числа десятичный");
            if (type.equals("String"))
                if (romanToInt(i) == -1) {
                    iterator.remove();
                }
        }
    }

    private static void constraintValueValid(List<String> list) throws IOException {
        List<String> tempList = new ArrayList<>(list);
        if (tempList.get(0).equals("-"))
            throw new IOException("throws Exception //т.к. числа не могут быть отрицательными");
        deleteSign(tempList);
        Iterator<String> iterator = tempList.iterator();
        while (iterator.hasNext()) {
            String i = iterator.next();
            switch (getType(i)) {
                case ("Integer"):
                    if (Integer.parseInt(i) > 10 ||
                            Integer.parseInt(i) < 1)
                        throw new IOException("throws Exception //т.к. числа должны быть в диапазоне от 1 до 10");
                    break;
                case ("String"):
                    if (romanToInt(i) > 10 ||
                            romanToInt(i) < 1)
                        throw new IOException("throws Exception //т.к. числа должны быть в диапазоне от 1 до 10");
                    break;
            }
        }
    }

    private static void constraintMathValid(List<String> list) throws IOException {
        List<String> tempList = new ArrayList<>(list);
        deleteSign(tempList);
        if (tempList.size() > 2)
            throw new IOException("throws Exception //т.к. формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        if (tempList.size() < 2)
            throw new IOException("throws Exception //т.к. строка не является математической операцией");
    }

    private static void symbolValid(List<String> list) throws IOException {
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            for (int i = 0; i < s.length(); i++) {
                try {
                    Integer.parseInt(String.valueOf(s.charAt(i)));
                } catch (NumberFormatException e) {
                    if (!String.valueOf(s.charAt(i)).equals("+") &&
                            !String.valueOf(s.charAt(i)).equals("-") &&
                            !String.valueOf(s.charAt(i)).equals("*") &&
                            !String.valueOf(s.charAt(i)).equals("/") &&
                            !String.valueOf(s.charAt(i)).equals("."))
                        if (romanToInt(String.valueOf(s.charAt(i))) == -1)
                            throw new IOException("throws Exception //т.к. недопустимые символы");
                }
            }
        }
    }

    private static final NavigableMap<Integer, String> units;

    static {
        NavigableMap<Integer, String> initMap = new TreeMap<>();
        initMap.put(1000, "M");
        initMap.put(900, "CM");
        initMap.put(500, "D");
        initMap.put(400, "CD");
        initMap.put(100, "C");
        initMap.put(90, "XC");
        initMap.put(50, "L");
        initMap.put(40, "XL");
        initMap.put(10, "X");
        initMap.put(9, "IX");
        initMap.put(5, "V");
        initMap.put(4, "IV");
        initMap.put(1, "I");
        units = Collections.unmodifiableNavigableMap(initMap);
    }
}
