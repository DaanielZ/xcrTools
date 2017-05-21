package pl.xcrafters.xcrtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import pl.xcrafters.xcrtools.Enchantments;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class EnchantCommand implements CommandExecutor{

    ToolsPlugin plugin;
    
    public EnchantCommand(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getCommand("enchant").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            if(args.length == 0){
                return false;
            }
            Player player = (Player) sender;
            Enchantment enchant = Enchantments.getEnchantment(args[0]);
            if(enchant == null){
                sender.sendMessage(plugin.color("&9Nie znaleziono zaklecia o nazwie &b" + args[0] + "&9!"));
                return true;
            }
            int level = enchant.getMaxLevel();
            if(args.length == 2){
                try {
                    level = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex){
                    player.sendMessage(plugin.color("&cPodany argument nie jest liczba!"));
                    return true;
                }
            }
            if(player.getItemInHand() == null){
                player.sendMessage("&cNie trzymasz zadnego przedmiotu w rece!");
                return true;
            }
            if(player.hasPermission("tools.enchant.unsafe")){
                player.getItemInHand().addUnsafeEnchantment(enchant, level);
            } else {
                if(level > enchant.getMaxLevel()){
                    player.sendMessage(plugin.color("&cNie mozesz nalozyc zaklecia o poziomie &6" + level + " &cna ten przedmiot!"));
                    return true;
                }
                if(!enchant.canEnchantItem(player.getItemInHand())){
                    player.sendMessage(plugin.color("&cNie mozesz nalozyc zaklecia na ten przedmiot!"));
                    return true;
                }
                player.getItemInHand().addEnchantment(enchant, level);
            }
            player.sendMessage(plugin.color("&9Nalozono zaklecie &b" + enchant.getName() + "&9(&b" + level + "&9) na przedmiot, ktory trzymasz w rece."));
            return true;
        } else {
            sender.sendMessage(plugin.color("&cTa komenda nie moze zostac wywolana z konsoli!"));
            return true;
        }
    }
    
}
