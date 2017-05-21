package pl.xcrafters.xcrtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class SpeedCommand implements CommandExecutor {

    ToolsPlugin plugin;

    public SpeedCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("speed").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                return false;
            }
            float speed = 0.2F;
            try {
                speed = Float.parseFloat(args[0]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(plugin.color("&cPodany argument nie jest liczba!"));
            }
            if (args.length == 1) {
                if (player.isFlying()) {
                    player.setFlySpeed(speed * 0.1F);
                    player.sendMessage(plugin.color("&9Ustawiono predkosc latania na &b" + speed + "&9!"));
                } else {
                    player.setWalkSpeed(speed * 0.2F);
                    player.sendMessage(plugin.color("&9Ustawiono predkosc chodzenia na &b" + speed + "&9!"));
                }
            } else {
                if(!sender.hasPermission("tools.speed.others")){
                    sender.sendMessage(plugin.color("&cNie masz uprawnien do ustawiania predkosci poruszania sie innym graczom!"));
                    return true;
                }
                player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(plugin.color("&cGracz o nicku &6" + args[1] + " &cnie jest online!"));
                    return true;
                }
                if (player.isFlying()) {
                    player.setFlySpeed(speed * 0.1F);
                    player.sendMessage(plugin.color("&9Ustawiono predkosc latania na &b" + speed + "&9!"));
                    sender.sendMessage(plugin.color("&9Ustawiono predkosc latania na &b" + speed + " &9dla gracza &b" + player.getName() + "&9!"));
                } else {
                    player.setWalkSpeed(speed * 0.2F);
                    player.sendMessage(plugin.color("&9Ustawiono predkosc chodzenia na &b" + speed + "&9!"));
                    sender.sendMessage(plugin.color("&9Ustawiono predkosc chodzenia na &b" + speed + " &9dla gracza &b" + player.getName() + "&9!"));
                }
                return true;
            }
        } else {
            if (args.length != 2) {
                return false;
            }
            float speed = 0.2F;
            try {
                speed = Float.parseFloat(args[0]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(plugin.color("&cPodany argument nie jest liczba!"));
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(plugin.color("&cGracz o nicku &6" + args[1] + " &cnie jest online!"));
                return true;
            }
            if (player.isFlying()) {
                player.setFlySpeed(speed * 0.1F);
                player.sendMessage(plugin.color("&9Ustawiono predkosc latania na &b" + speed + "&9!"));
                sender.sendMessage(plugin.color("&9Ustawiono predkosc latania na &b" + speed + " &9dla gracza &b" + player.getName() + "&9!"));
            } else {
                player.setWalkSpeed(speed * 0.2F);
                player.sendMessage(plugin.color("&9Ustawiono predkosc chodzenia na &b" + speed + "&9!"));
                sender.sendMessage(plugin.color("&9Ustawiono predkosc chodzenia na &b" + speed + " &9dla gracza &b" + player.getName() + "&9!"));
            }
            return true;
        }
        return true;
    }

}
