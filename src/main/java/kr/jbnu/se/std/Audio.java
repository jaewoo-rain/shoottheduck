package kr.jbnu.se.std;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {
    /**
     * play audio
     */
    private Clip clip;
    private AudioInputStream audioInputStream;
    private File audioFile;
    private boolean isLoop;


    public Audio(String pathName, boolean isLoop) {
        try{
            clip = AudioSystem.getClip();
            audioFile = new File(pathName);
            audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip.open(audioInputStream);

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        clip.setFramePosition(0);
        clip.start();
        if (!isLoop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    public void stop() {
        clip.stop();
    }
}

