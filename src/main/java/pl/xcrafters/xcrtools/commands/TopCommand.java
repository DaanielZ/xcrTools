package pl.xcrafters.xcrtools.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class TopCommand implements CommandExecutor{

    ToolsPlugin plugin;
    
    public TopCommand(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getCommand("top").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            Location location = player.getLocation();
            int y = location.getWorld().getHighestBlockYAt(location);
            if(y == 0){
                y = 256;
            }
            location.setY(y);
            player.teleport(location);
            player.sendMessage(plugin.color("&9Przeteleportowano na najwyzszy blok!"));
            return true;
        } else {
            sender.sendMessage(plugin.color("&cTa komenda nie moze zostac wywolana z konsoli!"));
            return true;
        }
    }
    
}
