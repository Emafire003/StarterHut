# StarterHut
 A minecraft spigot plugin to create a little schematic (hut) when the player joins or places and item.
 
 INFO: Requires BetterRTP(https://github.com/SuperRonanCraft/BetterRTP) to be in the Build Path.
 
 Per installare posizionare il plugin nella cartella /plugins/ del server ed avviare il server. Inserire poi le schematiche desiderate nella cartella /schems/
 del plugin, con il nome secondo il legno (anche in accordo col bioma) (oak.schem spruce.schem jungle.schem birch.schem darkoak.schem acacia.schem warped.schem crimson.schem).
 
 softdepend:
   - Vault
   - GriefPrevention
   - BetterRTP
   #TO DO WorldGuard
depend:
   - WorldEdit
commands:
   hut:
      description: Used to manage hut commands, like create ad createat and item
      usage: /hut <subcommand>
permissions:
   starterhut.create:
      description: Ability to create an hut using /hut create
      default: op
   starterhut.createat:
      description: Ability to create an hut at a specifc location using /hut create at x y z player worldname
      default: op
   starterhut.item:
      description: Ability to give the HutItem at a player using /hut item <playername>
      default: op
   starterhut.help:
      description: Ability to use the /hut help command
      default: op
   starterhut.reload:
      description: Ability to use the /hut reload command to reload the plugin
      default: op
   starterhut.getmode:
      description: Ability to use the /hut getmode command to get the spawning mode used
      default: op
   starterhut.setmode:
      description: Ability to use the /hut setmode command to set the spawning mode used
      default: op
