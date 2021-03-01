package tk.emafire003.dev.starterhut.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import tk.emafire003.dev.starterhut.GenerateHut;
import tk.emafire003.dev.starterhut.Main;
import tk.emafire003.dev.starterhut.commands.Hut;

public class HutJoinEvent implements Listener {
	
	private FileConfiguration config = Main.getMain().getConfig();
	//TODO Hook with worldguard (maybe) and GriefPrevention to prevent placing and generating
	//the structure in some regions
	
	/** 
	 * If the option "firstjoin" or "all" is enabled 
	 * it fires the event. I already checked in the main before
	 * enabling this method but i figured it would be useful to 
	 * check it here too in case of problems. 
	 * 
	 * If the player joined for the first time a new Hut is generated
	 * using new GenerateHut(player) which RTPs a player
	 * and spawns the hut
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if(config.getString("mode").equals("item") || config.getString("mode").equals("command") 
				|| config.getString("mode").equals("notjoin")) {
			return;
		}
		Player player = event.getPlayer();
		if(!player.hasPlayedBefore()) {
			Hut.rtpPlayer(player);
			new GenerateHut(player);
		}
	}

}
