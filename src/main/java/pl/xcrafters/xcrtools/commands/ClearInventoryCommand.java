package pl.xcrafters.xcrtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class ClearInventoryCommand implements CommandExecutor{

    ToolsPlugin plugin;
    
    public ClearInventoryCommand(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getCommand("clear").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(args.length == 0){
            if(sender instanceof Player){
                Player player = (Player) sender;
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.getInventory().setHeldItemSlot(0);
                player.updateInventory();
                player.sendMessage(plugin.color("&9Twoj ekwipunek zostal wyczyszczony."));
                return true;
            } else {
                sender.sendMessage(plugin.color("&cTa komenda nie moze zostac wywolana z konsoli!"));
                return true;
            }
        } else if(args.length == 1){
            if(!sender.hasPermission("tools.clear.others")){
                sender.sendMessage(plugin.color("&cNie masz uprawnien do czyszczenia ekwipunku innym graczom!"));
                return true;
            }
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null){
                sender.sendMessage(plugin.color("&cGracz o nicku &6" + args[0] + " &cnie jest online!"));
                return true;
            }
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getInventory().setHeldItemSlot(0);
            player.updateInventory();
            sender.sendMessage(plugin.color("&9Wyczyszczono ekwipunek gracza &b" + player.getName() + "&9!"));
            player.sendMessage(plugin.color("&9Twoj ekwipunek zostal wyczyszczony."));
            return true;
        }
        return false;
    }
    
}
