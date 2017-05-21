package pl.xcrafters.xcrtools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AfkTask extends BukkitRunnable{

    ToolsPlugin plugin;
    
    public AfkTask(ToolsPlugin plugin){
        this.plugin = plugin;
    }
    
    @Override
    public void run(){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(!player.hasPermission("tools.afk.bypass") && player.getLocation().distance(player.getWorld().getSpawnLocation()) >= 50){
                if(plugin.afk.get(player.getName()) == null){
                    plugin.afk.put(player.getName(), player.getLocation());
                } else if (plugin.lastMove.get(player.getName()) == null && plugin.afk.get(player.getName()).equals(player.getLocation())) {
                    plugin.lastMove.put(player.getName(), System.currentTimeMillis());
                } else if (plugin.lastMove.get(player.getName()) != null && plugin.afk.get(player.getName()) != null && plugin.afk.get(player.getName()).getWorld().equals(player.getLocation().getWorld())) {
                    if ((plugin.afk.get(player.getName()).getPitch() == player.getLocation().getPitch() && plugin.afk.get(player.getName()).getYaw() == player.getLocation().getYaw()) || (plugin.afk.get(player.getName()).getX() == player.getLocation().getX() || plugin.afk.get(player.getName()).getZ() == player.getLocation().getZ())) {
                        if ((System.currentTimeMillis() - plugin.lastMove.get(player.getName())) / 1000 >= plugin.configManager.afkKickSeconds) {
                            plugin.lastMove.remove(player.getName());
                            plugin.afk.remove(player.getName());
                            player.kickPlayer(ChatColor.GOLD + "Zostales wyrzucony za bycie AFK przez ponad " + String.valueOf(plugin.configManager.afkKickSeconds / 60) + " minut.");
                        }
                    } else {
                        plugin.lastMove.remove(player.getName());
                        plugin.afk.remove(player.getName());
                        plugin.afkBlocks.remove(player.getName());
                    }
                } else {
                    plugin.lastMove.remove(player.getName());
                    plugin.afk.remove(player.getName());
                    plugin.afkBlocks.remove(player.getName());
                }
            }
        }
    }
    
}
