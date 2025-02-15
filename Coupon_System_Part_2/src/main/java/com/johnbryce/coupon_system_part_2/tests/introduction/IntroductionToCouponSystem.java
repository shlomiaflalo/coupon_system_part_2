package com.johnbryce.coupon_system_part_2.tests.introduction;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(2)
public class IntroductionToCouponSystem implements CommandLineRunner {

    private final TypingBeginning typingBeginning;
    private final VoiceExplanationBeginning voiceExplanationBeginning;

    //Introduction by voice and typing of the project
    public void typeAndPlayExplanation() {
        voiceExplanationBeginning.playBeginningExplanation();
        typingBeginning.typingBeginning(); /*Typing the
                text of the voice using clip.start();*/
    }

    @Override
    public void run(String... args) {
        typeAndPlayExplanation();
    }
}
