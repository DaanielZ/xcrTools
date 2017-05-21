package pl.xcrafters.xcrtools.commands;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class CompassCommand implements CommandExecutor{

    ToolsPlugin plugin;
    
    public CompassCommand(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getCommand("compass").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            ItemStack item = new ItemStack(Material.COMPASS, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + "Kompas administratora");
            if(!player.hasPermission("tools.items.without-lore")){
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                meta.setLore(Arrays.asList(plugin.color("&6[ADMIN]"), plugin.color("&6Nick : &7&o" + sender.getName()), plugin.color("&6Data: &7&o" + dt.format(date)), plugin.color("&6Item: &7&o" + item.getType().name() + ":" + item.getData().getData() + " x " + item.getAmount())));
            }
            item.setItemMeta(meta);
            player.getInventory().addItem(item);
            player.updateInventory();
            player.sendMessage(plugin.color("&9Dodano kompas do twojego ekwipunku."));
            return true;
        } else {
            sender.sendMessage(plugin.color("&cTa komenda nie moze zostac wywolana z konsoli!"));
            return true;
        }
    }
    
}
