package pl.xcrafters.xcrtools.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class PlayerPrefixSuffixListener implements Listener{

    ToolsPlugin plugin;
    
    public PlayerPrefixSuffixListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
//    @EventHandler(priority = EventPriority.MONITOR)
//    public void onPlayerPrefixSuffix(PlayerPrefixSuffixEvent event){
//        if(getGroupColor(event.getNamedPlayer()) != null){
//            event.setPrefix(getGroupColor(event.getNamedPlayer()));
//        }
//    }
//
//    public String getGroupColor(Player player) {
//        String group = PermsAPI.getPlayerGroup(player);
//        if(group == null) {
//            return null;
//        }
//        String color = plugin.configManager.groupColors.get(group.toLowerCase());
//        if (color == null) {
//            return null;
//        }
//        return ChatColor.translateAlternateColorCodes('&', color);
//    }
    
}
