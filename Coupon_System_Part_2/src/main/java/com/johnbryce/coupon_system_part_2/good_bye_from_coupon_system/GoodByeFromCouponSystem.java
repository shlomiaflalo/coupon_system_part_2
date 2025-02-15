package com.johnbryce.coupon_system_part_2.good_bye_from_coupon_system;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(4)
public class GoodByeFromCouponSystem implements CommandLineRunner {

    private final TypingEnding typingEnding;
    private final VoiceExplanationEnding voiceExplanationEnding;

    //Goodbye message
    public void typeAndPlayGoodByeMessage() {
        voiceExplanationEnding.playEndingGoodBye();
        typingEnding.typingEnding(); /*Typing the
                text of the voice using clip.start();*/
    }

    @Override
    public void run(String... args) {
        typeAndPlayGoodByeMessage();
    }
}
