package pl.xcrafters.xcrtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class HatCommand implements CommandExecutor {

    ToolsPlugin plugin;

    public HatCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("hat").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack item = player.getItemInHand();
            if (!item.getType().isSolid()) {
                player.sendMessage(plugin.color("&cTego przedmiotu nie mozesz zalozyc na glowe!"));
                return true;
            }
            if (args.length == 1) {
                if (!player.hasPermission("tools.hat.others")) {
                    player.sendMessage(plugin.color("&cNie masz uprawnien do ustawiania czapek innym graczom!"));
                    return true;
                }
                Player other = Bukkit.getPlayer(args[0]);
                if (other == null) {
                    player.sendMessage(plugin.color("&cGracz o nicku &6" + args[0] + " &cnie jest online!"));
                    return true;
                }

                player.setItemInHand(null);
                if (other.getInventory().getHelmet() != null) {
                    ItemStack helmet = other.getInventory().getHelmet();
                    other.setItemInHand(helmet);
                }
                other.getInventory().setHelmet(item);
                other.sendMessage(plugin.color("&9Ciesz sie nowa czapka od &b" + player.getName() + " &9;)"));
                return true;
            }
            player.setItemInHand(null);
            if (player.getInventory().getHelmet() != null) {
                ItemStack helmet = player.getInventory().getHelmet();
                player.setItemInHand(helmet);
            }
            player.getInventory().setHelmet(item);
            player.sendMessage(plugin.color("&9Ciesz sie nowa czapka ;)"));
            return true;
        } else {
           sender.sendMessage(plugin.color("&cTa komenda nie moze zostac wywolana z konsoli!"));
           return true;
        }
    }

}
