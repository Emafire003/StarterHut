package tk.emafire003.dev.starterhut.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.SuperRonanCraft.BetterRTP.references.customEvents.RTP_TeleportPostEvent;
import tk.emafire003.dev.starterhut.GenerateHut;
import tk.emafire003.dev.starterhut.Main;

public class RTPWentWellEvent implements Listener {
	
	@EventHandler
	public void onPlayerRTPedEvent(RTP_TeleportPostEvent event) {
		Player player = event.getPlayer();
		if(player.hasMetadata("StarterHut-rtping")) {
			new GenerateHut(player);
			player.removeMetadata("StarterHut-rtping", Main.getMain());
		}
		
	}

}
