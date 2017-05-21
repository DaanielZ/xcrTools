package pl.xcrafters.xcrtools.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class GiveCommand implements CommandExecutor, TabCompleter {

    ToolsPlugin plugin;

    public GiveCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("give").setExecutor(this);
        plugin.getCommand("give").setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> players = new ArrayList();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (args[0] == null || (player.getName().toLowerCase().startsWith(args[0].toLowerCase()) && !player.getName().equalsIgnoreCase(args[0]))) {
                players.add(player.getName());
            }
        }
        if (!players.isEmpty()) {
            return players;
        }
        List<String> materials = new ArrayList();
        for(Material material : Material.values()){
            if((args.length == 1 || material.name().toLowerCase().startsWith(args[args.length - 1].toLowerCase())) && args.length <= 2){
                materials.add(material.name());
            }
        }
        return materials;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 2) {
            return false;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(plugin.color("&cGracz o nicku &6" + args[0] + " &cnie jest online!"));
            return true;
        }
        String[] type = args[1].split(":");
        Material material = plugin.getMaterial(type[0]);
        if(material == null) {
            return false;
        }
        int amount = 64;
        if (args.length > 2) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        short data = 0;
        if (type.length > 1) {
            data = Short.valueOf(type[1]);
        }
        ItemStack item = new ItemStack(material, amount, data);
        if (!sender.hasPermission("tools.items.without-lore")) {
            ItemMeta meta = item.getItemMeta();
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            meta.setLore(Arrays.asList(plugin.color("&6[ADMIN]"), plugin.color("&6Nick : &7&o" + sender.getName()), plugin.color("&6Data: &7&o" + dt.format(date)), plugin.color("&6Item: &7&o" + item.getType().name() + ":" + item.getData().getData() + " x " + item.getAmount())));
            item.setItemMeta(meta);
        }
        player.getInventory().addItem(item);
        player.updateInventory();
        player.sendMessage(plugin.color("&9Otrzymano &b" + material.name() + (type.length > 1 ? ":" + type[1] : "") + " (" + amount + ")&9."));
        sender.sendMessage(plugin.color("&9Gracz &b" + player.getName() + " &9otrzymal &b" + material.name() + (type.length > 1 ? ":" + type[1] : "") + " (" + amount + ")&9."));
        return true;
    }

}
