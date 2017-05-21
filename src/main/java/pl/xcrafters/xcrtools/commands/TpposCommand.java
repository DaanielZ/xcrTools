package pl.xcrafters.xcrtools.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class TpposCommand implements CommandExecutor{

    ToolsPlugin plugin;
    
    public TpposCommand(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getCommand("tppos").setExecutor(this);
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length < 2){
            return false;
        }
        if(sender instanceof Player){
            Player player = (Player) sender;
            int x;
            int y;
            int z;
            try {
                x = Integer.parseInt(args[0]);
                if(args.length == 3){
                    y = Integer.parseInt(args[1]);
                    z = Integer.parseInt(args[2]);
                } else {
                    z = Integer.parseInt(args[1]);
                    y = player.getWorld().getHighestBlockYAt(x, z);
                }
            } catch (NumberFormatException ex){
                player.sendMessage(plugin.color("&cJeden z podanych argumentow nie jest liczba!"));
                return true;
            }
            Location loc = new Location(player.getWorld(), x, y, z);
//            SektoryAPI.teleport(player, loc, (args.length == 3 ? DataManager.TeleportType.SAFE : DataManager.TeleportType.TOP)); //TODO
            player.teleport(loc);
            player.sendMessage(plugin.color("&9Przeteleportowano na podane koordynaty!"));
            return true;
        } else {
            sender.sendMessage(plugin.color("&cTa komenda nie moze zostac wywolana z konsoli!"));
            return true;
        }
    }
    
}
