package com.packtpub.libgdx.ghostrunner.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * centralized point of control over the game's audio playback
 */
public class AudioManager 
{
public static final AudioManager instance = new AudioManager();
	
	private Music playingMusic;
	
	// singleton: prevent instantiation from other classes
	private AudioManager() 
	{
		
	}
	
	/**
	 * Overloaded this method to make certain paramters optional
	 * @param sound
	 */
	public void play (Sound sound) 
	{
		play (sound, 1);
	}
	
	/**
	 * Overloaded this method to make certain paramters optional
	 * @param sound
	 * @param volume
	 */
	public void play (Sound sound, float volume) 
	{
		play (sound, volume, 1);
	}
	
	/**
	 * Overloaded this method to make certain paramters optional
	 * @param sound
	 * @param volume
	 * @param pitch
	 */
	public void play (Sound sound, float volume, float pitch)
	{
		play (sound, volume, pitch, 0);
	}
	
	/**
	 * Overloaded this method to make certain paramters optional
	 * @param sound
	 * @param volume
	 * @param pitch
	 * @param pan
	 */
	public void play (Sound sound, float volume, float pitch, float pan) 
	{
		if (!GamePreferences.instance.sound)
			return;
		
		sound.play(GamePreferences.instance.volSound * volume, pitch, pan);
	}
	
	/**
	 * Another overload play method that takes in the Music that will be played
	 * @param music
	 */
	public void play (Music music) 
	{	
		stopMusic();
		playingMusic = music;
		if (GamePreferences.instance.music) 
		{
			music.setLooping(true);
			music.setVolume(GamePreferences.instance.volMusic);
			music.play();
		}
	}
	
	/**
	 * Stops music
	 */
	public void stopMusic()
	{
		if (playingMusic != null)
			playingMusic.stop();
	}
	
	/**
	 * Allows the Options menu to inform AudioManager when settings
	 * have changed to execute appropriate actions.
	 */
	public void onSettingsUpdated()
	{
		if (playingMusic == null)
			return;
		
		playingMusic.setVolume(GamePreferences.instance.volMusic);
		
		if (GamePreferences.instance.music)
		{
			if (!playingMusic.isPlaying())
				playingMusic.play();
		}
		else
		{
			playingMusic.pause();
		}
}
}
