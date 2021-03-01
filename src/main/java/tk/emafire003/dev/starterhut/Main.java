package tk.emafire003.dev.starterhut;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.permission.Permission;
import tk.emafire003.dev.starterhut.commands.Hut;
import tk.emafire003.dev.starterhut.events.HutPlaceEvent;

public class Main extends JavaPlugin {

	private static Plugin main;
	//private File customLangFile;
	private static FileConfiguration config;
	private static boolean betterRTP = true;
	private static Permission perms = null;
	private static boolean vault = true;
	private static boolean griefprevention = true;
	private static boolean worldguard = true;
	
	@Override
	public void onEnable() {
		System.out.println("[StarterHut] Enabling StarterHut plugin... *Made by @Emafire003* ");
		main = this;
		
		/*File langfile = new File(this.getDataFolder().getAbsolutePath() + "/lang.yml");
		langFile = YamlConfiguration.loadConfiguration(langfile);*/
		
		this.getCommand("hut").setExecutor(new Hut());
		config = Main.getMain().getConfig();
		
		if(config.getString("mode").equals("item") || config.getString("mode").equals("all") || config.getString("mode").equals("notjoin")) {
			getServer().getPluginManager().registerEvents(new HutPlaceEvent(), this);
			System.out.println("[StarterHut] Registering HutPlaceEvent");
		}else {
			System.out.println("[StarterHut]  HutPlaceEvent not registered");
		}
		
		try {
			if ( !setupPermissions() ) {
				System.out.println("[StarterHut] WARNING!!! No Vault dependecy found, can't interact with permissions!");
	            vault = false;
	        }
			System.out.println("[StarterHut] Hooking in Vault!");
		}catch(Exception e) {
			System.out.println("[StarterHut] WARNING!!! No Vault dependecy found, can't interact with permissions!");
            vault = false;
			e.printStackTrace();
		}
		
		
		try {
			Plugin worldEditPlugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
			Plugin worldGuardPlugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
			Plugin betterRTPPlugin = Bukkit.getServer().getPluginManager().getPlugin("BetterRTP");
			Plugin griefPreventionPlugin = Bukkit.getServer().getPluginManager().getPlugin("GriefPrevention");
            if(worldEditPlugin == null){
            	System.out.println("[StarterHut] CRITICAL ERROR!!! No WorldEdit dependecy found, can't load plugin!");
    			getServer().getPluginManager().disablePlugin(this);
    			return; 
            }
            if (betterRTPPlugin == null) {
            	System.out.println("[StarterHut] WARNING!!! No BetterRTP dependecy found, can't rtp players!");
	            betterRTP = false;
			}else {
				System.out.println("[StarterHut] Hooking in BetterRTP!");
			}
            
            if (worldGuardPlugin == null) {
            	System.out.println("[StarterHut] WARNING!!! No WorldGuard dependecy found, can't check if the hut generates in wg region!");
	            worldguard = false;
			}else {
				System.out.println("[StarterHut] Hooking in WorldGuard!");
			}
            
            if (griefPreventionPlugin == null) {
				System.out.println("[StarterHut] WARNING!!! No GriefPrevention dependecy found, can't check if the hut generates in someone's claim!");
				griefprevention = false;
			}else {
				System.out.println("[StarterHut] Hooking in GriefPrevention!");
			}
			System.out.println("[StarterHut] Hooking in WorldEdit!");
		}catch(Exception e) {
			System.out.println("[StarterHut] CRITICAL ERROR!!! No WorldEdit dependecy found, can't load plugin!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		this.saveDefaultConfig();
		this.getConfig();
		
		//createLangConfig();		
		createSchemFolder();
		
		//this.saveResource("lang.yml", false);

		System.out.println("§a[StarterHut] Enabled! ");
	}
	
	@Override
	public void onDisable() {
		System.out.println("§c[StarterHut] Disabling StarterHut plugin... §9*Made by @Emafire003* ");
		System.out.println("§c[StarterHut] Disabled! ");
	}
	
	private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
	}
	
	public static Permission getPermissions() {
        return perms;
    }
	
	public static Plugin getMain(){
		return main;
	}
	
	public static boolean getBetterRTP(){
		return betterRTP;
	}
	
	public static boolean getGriefPrevention(){
		return griefprevention;
	}
	
	public static boolean getWorldGuard(){
		return worldguard;
	}
	
	public static boolean getVault(){
		return vault;
	}
	
	
	
	/*@SuppressWarnings("unused")
	private void createLangConfig() {
        customLangFile = new File(getDataFolder(), "lang.yml");
        if (!customLangFile.exists()) {
            customLangFile.getParentFile().mkdirs();
            saveResource("lang.yml", false);
         }

        langFile = new YamlConfiguration();
        try {
            langFile.load(customLangFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }*/
	
	private void createSchemFolder() {
		File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/schem");
		file.mkdir();
    }
	
	//Simply didn't want to spend all my time fixing the lang file not working for now
	public static FileConfiguration getLang() {
		return Main.getMain().getConfig();
	}
	
	public static String color(String text) {
		 return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	public static String getPrefix() {
		String prefix = Main.getMain().getConfig().getString("prefix")+" ";
		return Main.color(prefix);
	}
	
}
