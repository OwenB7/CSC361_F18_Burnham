package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
import com.packtpub.libgdx.ghostrunner.GhostRunnerMain;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.
Settings;

/**
 * This is the main class for the GhostRunner-Desktop project 
 */
public class DesktopLauncher
{
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;
	
	/**
	 * @param arg
	 */
	public static void main (String[] arg) 
	{
		if (rebuildAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			
			/* settings, input, output, packfilename */
			TexturePacker.process(settings, "assets-raw/images", "../core/assets/images", "canyonbunny.pack");
		}
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Ghost Runner";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new GhostRunnerMain(), config);
	}
}

