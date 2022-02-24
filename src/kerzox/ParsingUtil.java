package kerzox;

public class ParsingUtil {

    public static String TryParseString(Object value, String defV) {
        try {
            return String.valueOf(value);
        } catch (NumberFormatException e) {
            return defV;
        }
    }

    public static int TryParseInt(String value, int defV) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defV;
        }
    }

    public static boolean IsInt(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static double TryParseDouble(String value, double defV) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defV;
        }
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
