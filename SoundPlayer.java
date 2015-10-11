import java.io.*;
import javax.sound.sampled.*;

public class SoundPlayer {	
	static void play(File Sound) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(Sound));
			clip.start();
			Thread.sleep(clip.getMicrosecondLength()/1000);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
