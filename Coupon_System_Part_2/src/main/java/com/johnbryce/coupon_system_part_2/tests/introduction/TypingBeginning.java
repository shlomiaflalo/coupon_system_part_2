package com.johnbryce.coupon_system_part_2.tests.introduction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TypingBeginning {

    private String textBeginning() {
        return """
                
                Welcome to the Coupon System Project - Part 2
                Time to test our coupon system! First, we’ll check Admin functionality,
                from valid inputs to handling errors. Then, we’ll test
                Customer interactions and finish with the Company test,
                showing how businesses manage their coupons\s
                Let’s get started.
                
                """;
    }

    public void typingBeginning() {
        String[] textSentences = textBeginning().split("\\.,");
        int counter = 0;
        for (String textSentence : textSentences) {
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
