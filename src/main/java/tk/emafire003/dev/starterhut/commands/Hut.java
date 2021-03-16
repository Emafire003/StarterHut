package tk.emafire003.dev.starterhut.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_TYPE;
import tk.emafire003.dev.starterhut.GenerateHut;
import tk.emafire003.dev.starterhut.Main;

//Class that "holds" the commands relative to the plugin
public class Hut implements CommandExecutor {
	
	private static FileConfiguration config = Main.getMain().getConfig();
	private static FileConfiguration lang = config;
	private static NamespacedKey key = new NamespacedKey(Main.getMain(), "StarterHut-hutitem");

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender.hasPermission("starterhut.hut")) {
			try {
				if(args == null || args[0] == null) {
					if(sender.hasPermission("starterhut.help")) {
						sender.sendMessage("§bStarterHut made by @Emafire003 \n§aVersion: " + Main.getMain().getDescription().getVersion());
						help(sender);
					}else {
						sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("no_permission")));
					}
					return true;
				}
				if(args[0].equals("help")) {
					if(sender.hasPermission("starterhut.help")) {
						sender.sendMessage("§bStarterHut made by @Emafire003 \n§aVersion: " + Main.getMain().getDescription().getVersion());
						help(sender);
					}else {
						sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("no_permission")));
					}	
					return true;
				}
				
				//needs arg0 as create
				//checks for the subcommand "create" and spawns the structer at the player's location
				else if(args[0].equals("create")) {
					if(config.getString("mode").equals("item") || config.getString("mode").equals("firstjoin")) {
						sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("mode_not_enabled")));
						return true;
					}
					if(sender instanceof Player && sender.hasPermission("starterhut.create")) {
						Player player = (Player) sender;
						player.sendMessage(Main.getPrefix() + Main.color("structure_generation"));
						
						rtpPlayer(player);
						new GenerateHut(player);
						
						player.sendMessage(Main.getPrefix() + Main.color( "structure_generated"));
					}else {
						if(!(sender instanceof Player)) {
							
							sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("error_running_from_console")));
						}
						sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("no_permission")));
					}
				
				}
				
				//needs args0 as create, args1,2,3 as X,Y,Z, args4 as player name, args5 World (if sender is not Entity)
				//same as before but takes coords & wolrd & stuff
				else if(args[0].equals("createat")){
					if(config.getString("mode").equals("item") || config.getString("mode").equals("firstjoin")) {
						sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("mode_not_enabled")));
						return true;
					}
					if(sender.hasPermission("starterhut.createat")) {
						World world;
						if(sender instanceof Entity && args[5] == null) {
							world = ((Entity) sender).getWorld();
						}else {
							world = Bukkit.getWorld(args[5]);
						}					
						Location loc = new Location(world, Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3]));
						sender.sendMessage(Main.getPrefix() + Main.color("structure_generation"));
						new GenerateHut(loc);
						Player player = Bukkit.getPlayer(args[4]);
						player.teleport(loc);
						player.sendMessage(Main.getPrefix() + Main.color("structure_gifted"));
						sender.sendMessage(Main.getPrefix() + Main.color("structure_generated"));
					}else {
						sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("no_permission")));
					}
				}
				
				//needs args[0] as give and args[1] as playername
				//it gives the HutItem to the player
				else if(args[0].equals("item")) {
					if(config.getString("mode").equals("firstjoin") || config.getString("mode").equals("command")) {
						if(Main.getPrefix() == null) {
							Bukkit.broadcastMessage("prefix null");
						}
						else if(lang == null) {
							Bukkit.broadcastMessage("comfig null");
						}else if(config.getString("mode_not_enabled") == null){
							Bukkit.broadcastMessage("get string null");
						}
						sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("mode_not_enabled")));
						return true;
					}
					Player player = Bukkit.getPlayer(args[1]);
					if(player.hasPermission("starterhut.item")) {
						ItemStack item = Hut.getHutItem();
						player.getInventory().addItem(item);
						if(Main.getPrefix() == null) {
							System.out.println("Main null");
						}
						if(lang == null) {
							System.out.println("lang null");
						}
						if(lang.getString("item_recived") == null) {
							System.out.println("string null");
						}
						sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("item_recived")));
					}
					
				//needs args0 as getmode
				//sends a message with the mode setted in the config to the plaeyr
				}else if(args[0].equals("getmode")) {
					if(sender.hasPermission("starterhut.getmode")) {
						sender.sendMessage(Main.getPrefix() + config.getString("mode"));
					}else {
						sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("no_permission")));
					}
				//need args0 as setmode && args1 as the mode to set
				//allows to set the mode from within the game
				}else if(args[0].equals("setmode")) {
					if(sender.hasPermission("starterhut.setmode")) {
						config.set("mode", args[1]);
						sender.sendMessage(Main.getPrefix() + Main.color(config.getString("setmode_to")) + args[1]);
					}else {
						sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("no_permission")));
					}
					
				}
				//needs arsg0 as reload
				else if(args[0].equals("reload") && sender.hasPermission("starterhut.reload")) {
					try{
						sender.sendMessage(Main.getPrefix() + Main.color(config.getString("reloading")));
						Main.getMain().reloadConfig();
						sender.sendMessage(Main.getPrefix() + Main.color(config.getString("reloaded")));
					}catch(Exception e) {
						sender.sendMessage(Main.getPrefix() + "§cCould not reload the plugin!");
						e.printStackTrace();
					}			
					
				}		
				
				
			}catch(Exception e) {
				//e.printStackTrace();
				if(sender.hasPermission("starterhut.help")) {
					sender.sendMessage("§bStarterHut made by @Emafire003 \n§aVersion: " + Main.getMain().getDescription().getVersion());
					help(sender);
				}else {
					sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("provide_subcommand")));
				}
				
				return true;
			}
		}else {
			sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("no_permission")));
		}
		return true;
	}
	
	/**
	 * This method is used to get the ItemStack of the HutItem
	 * It takes name, and theree lines of lore from the config
	 * and adds the persitant key "StarterHut-hutitem" to it
	 * then returns the ItemStack*/
	public static ItemStack getHutItem() {
		ItemStack item = new ItemStack(Material.getMaterial(config.getString("hutitem_type")));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Main.color(config.getString("hutitem_name")));
		String line1 = Main.color(config.getString("hutitem_lore.line1"));
		String line2 = Main.color(config.getString("hutitem_lore.line2"));
		String line3 = Main.color(config.getString("hutitem_lore.line3"));
		List<String> lore = Arrays.asList(line1, line2, line3);
		meta.setLore(lore);
		meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "StarterHut-hutitem");
		
		item.setItemMeta(meta);
		return item;
		
	}
	
	/**
	 * This method returns the NamespacedKey of the HutItem
	 * */
	public static NamespacedKey getKey() {
		return key;
	}
	
	/**
	 * This method will use BetterRTP library to teleport
	 * a player to a random location
	 * 
	 * @param player The player who will get RTPed*/
	public static void rtpPlayer(Player player) {
		//checks if the mode is item. If it's not, RTPs a player
		if(Main.getBetterRTP()) {
			BetterRTP.getInstance().getRTP().start(player, player, player.getWorld().getName(), null, false, RTP_TYPE.ADDON);
		}else {
			player.sendMessage(Main.color(lang.getString("general_generation_error")));
			System.out.println(Main.color(lang.getString("rtp_not_installed")));
		}
	}
	
	//pretty self explanatory
	public static FileConfiguration getConfig() {
		return config;
	}
	
	/**
	 * This method sends a bunch of messages to a CommandSender
	 * that executed the commands to get the help section
	 * (currently either just typed /hut, /hut help or if
	 * there was an error in the command typing)
	 * 
	 * The messages explain the various commands of the plugins
	 * 
	 * @param sender The command sender who executed the help command*/
	public static void help(CommandSender sender) {
		sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("help_line1")));
		sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("help_line2")));
		sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("help_line3")));
		sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("help_line4")));
		sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("help_line5")));
		sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("help_line6")));
		sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("help_line7")));
		sender.sendMessage(Main.getPrefix() + Main.color(lang.getString("help_line8")));
	}

}
