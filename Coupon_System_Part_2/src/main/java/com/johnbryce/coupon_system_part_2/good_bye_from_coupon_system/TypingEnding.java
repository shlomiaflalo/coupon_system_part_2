package com.johnbryce.coupon_system_part_2.good_bye_from_coupon_system;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TypingEnding {

    private String textEnding() {
        return """
                
                The tests are now officially completed, Let us proceed to Part 3, Thank you, and good bye\s
                """;
    }

    public void typingEnding() {
        String[] text = textEnding().split("\\.,");
        int counter = 0;
        for (String textSentence : text) {
            while (counter < textSentence.length()) {
                System.out.print(textSentence.charAt(counter));
                counter++;
                try {
                    Thread.sleep(49);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println();
            counter = 0;
        }

    }

}
