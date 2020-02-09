
public class Splitter {
    public static String REGEX = "[\\s.,()/\"#;'\\\\\\-:$]+";

    public static String[] split(String string) {
        return string.split(REGEX);
    }
}
