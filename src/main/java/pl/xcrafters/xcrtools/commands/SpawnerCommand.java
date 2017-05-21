package pl.xcrafters.xcrtools.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.xcrafters.xcrtools.ToolsPlugin;

import java.util.Collections;

public class SpawnerCommand implements CommandExecutor{

    ToolsPlugin plugin;
    
    public SpawnerCommand(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getCommand("spawner").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length == 0){
            return false;
        }
        if(sender instanceof Player){
            Player player = (Player) sender;
            String type = args[0].toLowerCase();
            Block block = player.getTargetBlock(Collections.<Material>emptySet(), 10);
            if(!block.getType().equals(Material.MOB_SPAWNER)){
                sender.sendMessage(plugin.color("&cBlok, w ktory celujesz nie jest spawnerem"));
                return true;
            }
            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            spawner.setCreatureTypeByName(type);
            player.sendMessage(plugin.color("&9Zmieniono typ spawnera na &b" + spawner.getCreatureTypeName() + "&9."));
            return true;
        } else {
            sender.sendMessage(plugin.color("&cTa komenda nie moze zostac wywolana z konsoli!"));
            return true;
        }
    }
    
}
