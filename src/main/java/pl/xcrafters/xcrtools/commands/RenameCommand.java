package pl.xcrafters.xcrtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class RenameCommand implements CommandExecutor{

    ToolsPlugin plugin;
    
    public RenameCommand(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getCommand("rename").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 0){
                return false;
            }
            String name = args[0];
            for(int i=1; i<args.length; i++){
                name += " " + args[i];
            }
            ItemStack item = player.getItemInHand();
            if(item == null){
                player.sendMessage(plugin.color("&cNie trzymasz zadnego przedmiotu w swojej rece!"));
                return true;
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(plugin.color(name));
            item.setItemMeta(meta);
            player.sendMessage(plugin.color("&9Nazwa przedmiotu zostala zmieniona: " + name + "&9."));
            return true;
        } else {
            sender.sendMessage(plugin.color("&cTa komenda nie moze zostac wywolana z konsoli!"));
            return true;
        }
    }
    
}
