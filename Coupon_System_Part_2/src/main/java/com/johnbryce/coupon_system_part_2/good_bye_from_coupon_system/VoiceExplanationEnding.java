package com.johnbryce.coupon_system_part_2.good_bye_from_coupon_system;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class VoiceExplanationEnding {

    @Value("classpath:goodBye.wav")
    private Resource audioFileEnding;

    public void playEndingGoodBye() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    audioFileEnding.getInputStream());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.start();

        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            System.out.println("Error during audio loading file");
        }
    }


}
