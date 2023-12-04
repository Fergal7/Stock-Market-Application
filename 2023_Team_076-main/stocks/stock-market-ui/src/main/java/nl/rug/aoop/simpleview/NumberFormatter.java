package nl.rug.aoop.simpleview;

/**
 * Class with some utility methods to format numbers in a nice way.
 */
public class NumberFormatter {

    private static final double TRILLION = 1E+9;
    private static final double BILLION = 1E+9;
    private static final double MILLION = 1E+6;
    private static final double THOUSAND = 1E+3;

    /**
     * Formats large numbers into a readable string.
     *
     * @param number The number to be formatted.
     * @return A string representation of the number.
     */
    public static String largeNumberFormat(double number) {
        if (Math.abs(number) >= TRILLION) {
            return String.format("%.2fT", number / TRILLION);
        }
        if (Math.abs(number) >= BILLION) {
            return String.format("%.2fB", number / BILLION);
        }
        if (Math.abs(number) >= MILLION) {
            return String.format("%.2fM", number / MILLION);
        }
        if (Math.abs(number) >= THOUSAND) {
            return String.format("%.2fK", number / THOUSAND);
        }
        return String.format("%.2f", number);
    }

    /**
     * Formats a number into a percentage by truncating it and adding a percentage symbol.
     *
     * @param number The number to be formatted.
     * @return A percentage representation of the number.
     */
    public static String percentageFormat(double number) {
        return String.format("%.2f%%", number);
    }
}
