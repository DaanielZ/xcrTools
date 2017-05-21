package pl.xcrafters.xcrtools.commands;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class KnockCommand implements CommandExecutor{

    ToolsPlugin plugin;
    
    public KnockCommand(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getCommand("knock").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            ItemStack item = new ItemStack(Material.STICK, 1);
            item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + "Rozdzka administratora");
            if(!player.hasPermission("tools.items.without-lore")){
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                meta.setLore(Arrays.asList(plugin.color("&6[ADMIN]"), plugin.color("&6Nick : &7&o" + sender.getName()), plugin.color("&6Data: &7&o" + dt.format(date)), plugin.color("&6Item: &7&o" + item.getType().name() + ":" + item.getData().getData() + " x " + item.getAmount())));
            }
            item.setItemMeta(meta);
            player.getInventory().addItem(item);
            player.updateInventory();
            player.sendMessage(plugin.color("&9Dodano patyk z zakleciem knockback do twojego ekwipunku."));
            return true;
        } else {
            sender.sendMessage(plugin.color("&cTa komenda nie moze zostac wywolana z konsoli!"));
            return true;
        }
    }
    
}
