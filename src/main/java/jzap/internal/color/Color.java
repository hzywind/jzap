package jzap.internal.color;

public enum Color {

    BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE;

    private static final String ANSI_RESET = "\u001B[0m";
    private final String code;

    Color() {
        code = "\u001B[" + (30 + ordinal()) + "m";
    }

    public String add(String s) {
        return code + s + ANSI_RESET;
    }

}
