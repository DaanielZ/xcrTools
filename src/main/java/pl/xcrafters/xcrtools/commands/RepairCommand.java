package pl.xcrafters.xcrtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class RepairCommand implements CommandExecutor {

    ToolsPlugin plugin;

    public RepairCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("repair").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String type = args[0];
            if (args.length == 2) {
                if (!sender.hasPermission("tools.repair.others")) {
                    sender.sendMessage(plugin.color("&cNie masz uprawnien do naprawiania ekwipunku innym graczom!"));
                    return true;
                }
                player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    player.sendMessage(plugin.color("&cGracz o nicku &6" + args[0] + " &cnie jest online!"));
                    return true;
                }
                type = args[1];
            }
            if (type.equalsIgnoreCase("hand")) {
                player.getItemInHand().setDurability((short) 0);
                player.sendMessage(plugin.color("&9Naprawiono przedmiot, ktory trzymasz w rece."));
                if (!sender.equals(player)) {
                    sender.sendMessage(plugin.color("&9Naprawiono przedmiot w rece gracza &b" + player.getName() + "&9."));
                }
                return true;
            } else if (type.equalsIgnoreCase("armor")) {
                for (ItemStack item : player.getInventory().getArmorContents()) {
                    if (item != null) {
                        item.setDurability((short) 0);
                    }
                }
                player.sendMessage(plugin.color("&9Naprawiono twoja zbroje."));
                if (!sender.equals(player)) {
                    sender.sendMessage(plugin.color("&9Naprawiono zbroje gracza &b" + player.getName() + "&9."));
                }
                return true;
            } else if (type.equalsIgnoreCase("all")) {
                for (ItemStack item : player.getInventory().getArmorContents()) {
                    if (item != null) {
                        item.setDurability((short) 0);
                    }
                }
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null) {
                        item.setDurability((short) 0);
                    }
                }
                player.sendMessage(plugin.color("&9Naprawiono twoj ekwipunek."));
                if (!sender.equals(player)) {
                    sender.sendMessage(plugin.color("&9Naprawiono ekwipunek gracza &b" + player.getName() + "&9."));
                }
                return true;
            } else {
                return false;
            }
        } else {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                player.sendMessage(plugin.color("&cGracz o nicku &6" + args[0] + " &cnie jest online!"));
                return true;
            }
            String type = args[1];
            if (type.equalsIgnoreCase("hand")) {
                player.getItemInHand().setDurability((short) 0);
                player.sendMessage(plugin.color("&9Naprawiono przedmiot, ktory trzymasz w rece."));
                sender.sendMessage(plugin.color("&9Naprawiono przedmiot w rece gracza &b" + player.getName() + "&9."));
                return true;
            } else if (type.equalsIgnoreCase("armor")) {
                for (ItemStack item : player.getInventory().getArmorContents()) {
                    if (item != null) {
                        item.setDurability((short) 0);
                    }
                }
                player.sendMessage(plugin.color("&9Naprawiono twoja zbroje."));
                sender.sendMessage(plugin.color("&9Naprawiono zbroje gracza &b" + player.getName() + "&9."));
                return true;
            } else if (type.equalsIgnoreCase("all")) {
                for (ItemStack item : player.getInventory().getArmorContents()) {
                    if (item != null) {
                        item.setDurability((short) 0);
                    }
                }
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null) {
                        item.setDurability((short) 0);
                    }
                }
                player.sendMessage(plugin.color("&9Naprawiono twoj ekwipunek."));
                sender.sendMessage(plugin.color("&9Naprawiono ekwipunek gracza &b" + player.getName() + "&9."));
                return true;
            } else {
                return false;
            }
        }
    }

}
