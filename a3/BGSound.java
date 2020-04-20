package com.mycompany.a3;

import java.io.InputStream;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Display;

public class BGSound implements Runnable{
	private Media m;
	
	public BGSound(String fileName) {
		if (Display.getInstance().getCurrent() == null){
			System.out.println("Error: Create sound objects after calling show()!");
			System.exit(0);}
		try{
		InputStream is = Display.getInstance().getResourceAsStream(getClass(),
		"/"+fileName);
		//attach a runnable to run when media has finished playing as the last parameter
		m = MediaManager.createMedia(is, "audio/wav", this);}
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

	@Override
	public void run() {
		m.setTime(0);
		m.play();
	}
}
