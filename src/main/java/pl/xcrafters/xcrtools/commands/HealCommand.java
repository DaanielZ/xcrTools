package pl.xcrafters.xcrtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class HealCommand implements CommandExecutor {

    ToolsPlugin plugin;

    public HealCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("heal").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                player.setHealth(20.0);
                player.setFoodLevel(20);
                for (PotionEffect potioneffect : player.getActivePotionEffects()) {
                    player.removePotionEffect(potioneffect.getType());
                }
                player.setFireTicks(0);
                player.sendMessage(plugin.color("&9Zostales uleczony!"));
                return true;
            } else {
                if (!sender.hasPermission("tools.heal.others")) {
                    sender.sendMessage(plugin.color("&cNie masz uprawnien do uleczania innych graczy!"));
                    return true;
                }
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    player.sendMessage(plugin.color("&cGracz o nicku &6" + args[0] + " &cnie jest online!"));
                    return true;
                }
                player.setHealth(20.0);
                player.setFoodLevel(20);
                for (PotionEffect potioneffect : player.getActivePotionEffects()) {
                    player.removePotionEffect(potioneffect.getType());
                }
                player.setFireTicks(0);
                sender.sendMessage(plugin.color("&9Gracz &b" + player.getName() + " &9zostal uleczony."));
                player.sendMessage(plugin.color("&9Zostales uleczony!"));
                return true;
            }
        } else {
            if (args.length == 0) {
                return false;
            }
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                player.sendMessage(plugin.color("&cGracz o nicku &6" + args[0] + " &cnie jest online!"));
                return true;
            }
            player.setHealth(20.0);
            player.setFoodLevel(20);
            for (PotionEffect potioneffect : player.getActivePotionEffects()) {
                    player.removePotionEffect(potioneffect.getType());
                }
            player.setFireTicks(0);
            sender.sendMessage(plugin.color("&9Gracz &b" + player.getName() + " &9zostal uleczony."));
            player.sendMessage(plugin.color("&9Zostales uleczony!"));
            return true;
        }
    }

}
