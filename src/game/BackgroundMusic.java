package game;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class BackgroundMusic {
	private static BackgroundMusic instance = new BackgroundMusic();
	
	private BackgroundMusic() {}
	
	public static BackgroundMusic getInstance() {
		return instance;
	}
	
	public void playBGM(String BGMpath) {
		{
			try
			{
				File f = new File(BGMpath);
				
				if(f.exists())
				{
					AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);
					Clip clip = AudioSystem.getClip();
					clip.open(audioIn);
					clip.start();
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				}
				else
				{
			    	System.out.println("BGM File Not Found");
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
}
