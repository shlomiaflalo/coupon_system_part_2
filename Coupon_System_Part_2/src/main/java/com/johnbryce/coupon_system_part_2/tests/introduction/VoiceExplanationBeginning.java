package com.johnbryce.coupon_system_part_2.tests.introduction;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class VoiceExplanationBeginning {

    @Value("classpath:IntroToProject.wav")
    private Resource audioFileBeginning;

    //Used concurrently with another function
    public void playBeginningExplanation() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFileBeginning.getInputStream());
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
