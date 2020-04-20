package com.mycompany.a3;

import java.io.InputStream;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Display;

public class Sound {
	private Media m;
	
	public Sound(String fileName) {
		if (Display.getInstance().getCurrent() == null){
			System.out.println("Error: Create sound objects after calling show()!");
			System.exit(0);}
		try{
		InputStream is = Display.getInstance().getResourceAsStream(getClass(),
		"/"+fileName);
		//attach a runnable to run when media has finished playing as the last parameter
		m = MediaManager.createMedia(is, "audio/wav");}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void pause() {
		m.pause();
	}
	
	public void play() {
		m.play();
	}
}
