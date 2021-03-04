package tk.emafire003.dev.starterhut;

import java.io.File;
import java.io.FileInputStream;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;


public class GenerateHut {
	
	private FileConfiguration lang = Main.getLang();
	private FileConfiguration config = Main.getConf();
	private String prefix = Main.getPrefix();
	
	/**With this a new hut is generated at the player's location 
	 * 
	 * @param player The player for which the hut needs generating*/
	public GenerateHut(final Player player) {	
		try {

			Location loc = player.getLocation();
			
			if(config.getBoolean("generate_structure_console_message")) {
				System.out.println(prefix + Main.color(lang.getString("structure_generation"))
				+ " " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + "player: " + player.getName());
			}
			player.sendMessage(prefix + Main.color(lang.getString("structure_generation")));
			
			//getting the right file
			File file;
			if(config.getBoolean("biome_change")) {
				file = this.checkBiome(loc);
			}else {
				file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/schem/oak.schem");
			}
			
			//generating the schematic
			this.pasteSchematic(file, loc, config.getBoolean("use_air"));
			player.setBedSpawnLocation(loc);
		
			if(config.getBoolean("generate_structure_console_message")) {
				System.out.println(prefix + Main.color(lang.getString("structure_generated")));
			}
			player.sendMessage(prefix + Main.color(lang.getString("structure_generated")));
			
			if(config.getBoolean("remove_permession_after_hut_generated")) {
				if(Main.getVault()) {
					Main.getPermissions().playerRemove(player, "starterhut.create");
				}
			}else {
				System.out.println(Main.color(lang.getString("vault_not_installed")));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			player.sendMessage(Main.color(lang.getString("general_generation_error")));
		}
		
	}
	
	/**With this a new hut is generated at a given location
	 * 
	 * @param loc The location where the Hut will be generated*/
	public GenerateHut(Location loc) {	
		
		try {
			if(config.getBoolean("generate_structure_console_message")) {
				System.out.println(prefix + Main.color(lang.getString("structure_generation"))
				+ " " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
			}
			
			
			File file;
			if(config.getBoolean("biome_change")) {
				file = this.checkBiome(loc);
			}else {
				file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/schem/oak.schem");
			}
			this.pasteSchematic(file, loc, config.getBoolean("use_air"));
		
			if(config.getBoolean("generate_structure_console_message")) {
				System.out.println(prefix + Main.color(lang.getString("structure_generated")));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**This method checks the biome in a specific location (usually
	 * where the hut needs to spawn) and returns the corresponding
	 * schemfile for the hut
	 * 
	 * @param loc The location that needs biomechecking*/
	public File checkBiome(Location loc) {
		Biome biome = loc.getWorld().getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		
		//birch biomes = birch
		if(biome.equals(Biome.BIRCH_FOREST) || biome.equals(Biome.BIRCH_FOREST_HILLS) 
				|| biome.equals(Biome.TALL_BIRCH_FOREST) || biome.equals(Biome.TALL_BIRCH_HILLS)) {
			File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/schem/birch.schem");		
			return file;
		
		//cold biomes = Spruce
		}else if(biome.equals(Biome.GIANT_SPRUCE_TAIGA) || biome.equals(Biome.GIANT_SPRUCE_TAIGA_HILLS) 
				|| biome.equals(Biome.GIANT_TREE_TAIGA) || biome.equals(Biome.GIANT_TREE_TAIGA_HILLS)
				|| biome.equals(Biome.TAIGA) || biome.equals(Biome.TAIGA_HILLS) || biome.equals(Biome.TAIGA_MOUNTAINS)
				|| biome.equals(Biome.SNOWY_TAIGA) || biome.equals(Biome.SNOWY_TAIGA_HILLS) || biome.equals(Biome.SNOWY_TAIGA_MOUNTAINS)
				|| biome.equals(Biome.SNOWY_TUNDRA) || biome.equals(Biome.SNOWY_BEACH) || biome.equals(Biome.SNOWY_MOUNTAINS) 
				|| biome.equals(Biome.DEEP_FROZEN_OCEAN) || biome.equals(Biome.ICE_SPIKES) || biome.equals(Biome.FROZEN_RIVER)
				|| biome.equals(Biome.FROZEN_OCEAN) || biome.equals(Biome.GIANT_TREE_TAIGA) || biome.equals(Biome.MOUNTAINS)
				|| biome.equals(Biome.MOUNTAINS) || biome.equals(Biome.WOODED_MOUNTAINS)) {
			File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/schem/spruce.schem");		
			return file;
		//darkoak biomes = darkoak
		}else if(biome.equals(Biome.DARK_FOREST) || biome.equals(Biome.DARK_FOREST_HILLS) 
				|| biome.equals(Biome.WOODED_HILLS)) {
			File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/schem/darkoak.schem");		
			return file;
		
		//savanna biomes (maybe i'll add warm biomes) = acacia
		}else if(biome.equals(Biome.SAVANNA) || biome.equals(Biome.SAVANNA_PLATEAU) 
				|| biome.equals(Biome.SHATTERED_SAVANNA) || biome.equals(Biome.SHATTERED_SAVANNA_PLATEAU)) {
			File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/schem/acacia.schem");		
			return file;
		//Jungle like biomes = jungle wood
		}else if(biome.equals(Biome.JUNGLE) || biome.equals(Biome.JUNGLE_EDGE) 
				|| biome.equals(Biome.JUNGLE_HILLS) || biome.equals(Biome.BAMBOO_JUNGLE)
				|| biome.equals(Biome.BAMBOO_JUNGLE_HILLS) || biome.equals(Biome.MODIFIED_JUNGLE)
				|| biome.equals(Biome.MODIFIED_JUNGLE_EDGE)) {
			File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/schem/jungle.schem");		
			return file;
		}
		//Warped & Soul sand = warped wood
		else if(biome.equals(Biome.WARPED_FOREST) || biome.equals(Biome.SOUL_SAND_VALLEY) ) {
			File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/schem/warped.schem");		
			return file;
		}
		//nether biomes except above = crimson wood
		else if(biome.equals(Biome.CRIMSON_FOREST) || biome.equals(Biome.NETHER_WASTES) 
				|| biome.equals(Biome.BASALT_DELTAS)) {
			File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/schem/crimson.schem");		
			return file;
		}
		File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/schem/oak.schem");		
		return file;
		
	}
	
	/**
	 * Pastes a schematic in the world.
	 * 
	 * @param file The schematic file
	 * @param loc The location at which the structure will spawn
	 * @param withAir Spawn air too or not
	 * */
	public boolean pasteSchematic(File file, Location loc, boolean withAir) {
		try {
	    	//Clipboard section
			Clipboard clipboard;

			ClipboardFormat format = ClipboardFormats.findByFile(file);
			try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
			    clipboard = reader.read();
			}
	        
	        World world = new BukkitWorld(loc.getWorld());
			//Pasting section
	         int x = loc.getBlockX();
	         int y = loc.getBlockY();
	         int z = loc.getBlockZ();
	        try (@SuppressWarnings("deprecation")
			EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
	            Operation operation = new ClipboardHolder(clipboard)
	                    .createPaste(editSession)
	                    .to(BlockVector3.at(x, y, z))
	                    .ignoreAirBlocks(!withAir)
	                    .build();
	            Operations.complete(operation);
	        }
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	        return false;
	    }
		return true;
	}
	

}
