package com.johnbryce.coupon_system_part_2.console_messages_color;

/**
 * Utility class for printing messages with colors and styles in the console.
 * Provides methods to print messages with specific color formats such as successful and failed messages.
 */
public class ConsoleColorsUtil {

    public static final String RESET = "\033[0m";  // Text Reset

    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN

    public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE BACKGROUND

    public static void printSuccessfulMessage(String message) {
        System.out.println(WHITE_BACKGROUND_BRIGHT + GREEN_BOLD + message + RESET);
    }

    public static void printFailedMessage(String message) {
        System.out.println(WHITE_BACKGROUND_BRIGHT + RED_BOLD + message + RESET);
    }

    public static void printGreenClientLogo(String message) {
        System.out.println("\n" + GREEN_BOLD + message + RESET);
    }
}
