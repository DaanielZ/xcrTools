package pl.xcrafters.xcrtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class AfkCommand implements CommandExecutor {

    ToolsPlugin plugin;

    public AfkCommand(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("afk").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            return false;
        }
        String nick = args[0];
        Player player = Bukkit.getPlayer(nick);
        if(player == null) {
            sender.sendMessage(plugin.color("&cGracz o podanym nicku nie jest online!"));
            return true;
        }
        if(plugin.afkBlocks.get(player.getName()) == null) {
            sender.sendMessage(plugin.color("&cGracz o podanym nicku nie jest AFK!"));
            return true;
        }
        sender.sendMessage(plugin.color("&9Gracz &b" + player.getName() + " &9wykopal &b" + plugin.afkBlocks.get(player.getName()) + " &9bedac AFK."));
        return true;
    }

}
