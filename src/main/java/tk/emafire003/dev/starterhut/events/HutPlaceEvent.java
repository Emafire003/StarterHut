package tk.emafire003.dev.starterhut.events;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import tk.emafire003.dev.starterhut.GenerateHut;
import tk.emafire003.dev.starterhut.Main;
import tk.emafire003.dev.starterhut.commands.Hut;

public class HutPlaceEvent implements Listener {
	
	private FileConfiguration lang = Main.getLang();
	
	@EventHandler
	public void onHutItemPlaceEvent(BlockPlaceEvent event) {
		
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		
		if(Main.getGriefPrevention()) {
			//Checks weather or not the territory belongs to another player or not
			//if it does canbreak is a player's name string, so if
			//canbreak is null or the name of the player it's good to go
			//otherwise the generation and the event are stopped
			String canbreak = GriefPrevention.instance.allowBreak(player, loc.getBlock(), loc);
			if(canbreak != null && !canbreak.equals(player.getName())) {
				player.sendMessage(Main.getPrefix() + Main.color(lang.getString("already_claimed_territory")));
				event.setCancelled(true);
				return;
			}
		}else {
			player.sendMessage(Main.color(lang.getString("general_generation_error")));
			System.out.println(Main.color(lang.getString("griefprevention_not_installed")));
		}
		//checks if the player can build in a WG region
		if(Main.getWorldGuard()) {
			LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
			com.sk89q.worldedit.util.Location locA = BukkitAdapter.adapt(player.getLocation());
			RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
			RegionQuery query = container.createQuery();
			if(!query.testBuild(locA, localPlayer, Flags.BUILD)) { //TODO claimed territory -> WG
				player.sendMessage(Main.getPrefix() + Main.color(lang.getString("already_claimed_territory")));
			}
		}else {
			System.out.println(Main.color(lang.getString("worldguard_not_installed")));
		}
		
		NamespacedKey key = Hut.getKey();
		ItemStack item = event.getItemInHand();
		ItemStack right_item = Hut.getHutItem();
		PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
		if(data.has(key, PersistentDataType.STRING) || item.equals(right_item)) {
			event.setCancelled(true);
			
			if(!player.hasPermission("starterhut.create")) {
				player.sendMessage(Main.getPrefix() + Main.color(lang.getString("no_permission")));
				return;
			}
			player.getInventory().remove(item);
			
			new GenerateHut(player);
		}
	}

}
