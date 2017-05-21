package pl.xcrafters.xcrtools.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class HeadCommand implements CommandExecutor {

    ToolsPlugin plugin;

    public HeadCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("head").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setOwner(player.getName());
                item.setItemMeta(meta);
                player.getInventory().addItem(item);
                player.sendMessage(plugin.color("&9Glowa gracza &b" + player.getName() + " &9zostala dodana do twojego ekwipunku."));
                return true;
            } else if (args.length > 0) {
                String otherplayer = args[0];
                ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setOwner(otherplayer);
                item.setItemMeta(meta);
                player.getInventory().addItem(item);
                player.sendMessage(plugin.color("&9Glowa gracza &b" + otherplayer + " &9zostala dodana do twojego ekwipunku."));
                return true;
            }
        } else {
            sender.sendMessage(plugin.color("&cTa komenda nie moze zostac wywolana z konsoli!"));
        }
        return false;
    }

}
