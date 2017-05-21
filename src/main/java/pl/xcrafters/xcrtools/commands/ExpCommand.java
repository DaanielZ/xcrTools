package pl.xcrafters.xcrtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class ExpCommand implements CommandExecutor{
    
    ToolsPlugin plugin;
    
    public ExpCommand(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getCommand("exp").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length < 2){
                return false;
            }
            if(args.length == 3){
                if(!player.hasPermission("tools.exp.others")){
                    player.sendMessage(plugin.color("&cNie masz uprawnien do ustawiania doswiadczenia innym graczom!"));
                    return true;
                }
                player = Bukkit.getPlayer(args[1]);
                if(player == null){
                    sender.sendMessage(plugin.color("&cGracz o nicku &6" + args[1] + " &cnie jest online!"));
                    return true;
                }
            }
            if(args.length > 3){
                return false;
            }
            if(args[args.length - 1].toLowerCase().endsWith("l")){
                int level;
                try {
                    level = Integer.parseInt(args[args.length - 1].substring(0, args[args.length - 1].length() - 1));
                } catch(NumberFormatException ex){
                    sender.sendMessage(plugin.color("&cPodany argument nie jest liczba!"));
                    return true;
                }
                if(args[0].equalsIgnoreCase("set")){
                    player.setLevel(level);
                } else if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")){
                    player.setLevel(player.getLevel() + level);
                } else if(args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")){
                    player.setLevel(player.getLevel() - level < 0 ? 0 : player.getLevel() - level);
                } 
                if (sender.getName().equals(player.getName())) {
                    sender.sendMessage(plugin.color("&9Ustawiono twoj poziom doswiadczenia na &b" + level + "&9."));
                    return true;
                } else {
                    sender.sendMessage(plugin.color("&9Ustawiono poziom doswiadczenia gracza &b" + player.getName() + "&9 na &b" + player.getLevel() + "."));
                    return true;
                }
            } else {
                int exp;
                try {
                    exp = Integer.parseInt(args[args.length - 1]);
                } catch(NumberFormatException ex){
                    sender.sendMessage(plugin.color("&cPodany argument nie jest liczba!"));
                    return true;
                }
                if(args[0].equalsIgnoreCase("set")){
                    player.setTotalExperience(exp);
                } else if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")){
                    player.giveExp(exp);
                } else if(args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")){
                    player.giveExp(exp * -1);
                }
                if (sender.getName().equals(player.getName())) {
                    sender.sendMessage(plugin.color("&9Ustawiono twoja liczbe punktow doswiadczenia na &b" + exp + "&9."));
                    return true;
                } else {
                    sender.sendMessage(plugin.color("&9Ustawiono liczbe punktow doswiadczenia gracza &b" + player.getName() + "&9 na &b" + player.getTotalExperience()+ "."));
                    return true;
                }
            }
        } else {
            if(args.length < 3 || args.length > 3){
                return false;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if(player == null){
                sender.sendMessage(plugin.color("&cGracz o nicku &6" + args[1] + " &cnie jest online!"));
                return true;
            }
            if(args[2].toLowerCase().endsWith("l")){
                int level;
                try {
                    level = Integer.parseInt(args[args.length - 1].substring(0, args[args.length - 1].length() - 1));
                } catch(NumberFormatException ex){
                    sender.sendMessage(plugin.color("&cPodany argument nie jest liczba!"));
                    return true;
                }
                if(args[0].equalsIgnoreCase("set")){
                    player.setLevel(level);
                } else if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")){
                    player.setLevel(player.getLevel() + level);
                } else if(args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")){
                    player.setLevel(player.getLevel() - level < 0 ? 0 : player.getLevel() - level);
                } 
                sender.sendMessage(plugin.color("&9Ustawiono poziom doswiadczenia gracza &b" + player.getName() + "&9 na &b" + player.getLevel() + "."));
                return true;
            } else {
                int exp;
                try {
                    exp = Integer.parseInt(args[2]);
                } catch(NumberFormatException ex){
                    sender.sendMessage(plugin.color("&cPodany argument nie jest liczba!"));
                    return true;
                }
                if(args[0].equalsIgnoreCase("set")){
                    player.setTotalExperience(exp);
                } else if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")){
                    player.giveExp(exp);
                } else if(args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")){
                    player.giveExp(exp * -1);
                }
                sender.sendMessage(plugin.color("&9Ustawiono liczbe punktow doswiadczenia gracza &b" + player.getName() + "&9 na &b" + player.getTotalExperience() + "."));
                return true;
            }
        }
    }

}
