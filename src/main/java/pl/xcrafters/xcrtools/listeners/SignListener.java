package pl.xcrafters.xcrtools.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class SignListener implements Listener{

    ToolsPlugin plugin;
    
    public SignListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onSignChange(SignChangeEvent event){
        Player player = event.getPlayer();
        if(player.hasPermission("tools.color-signs")){
            for(int i=0; i < event.getLines().length; i++){
                event.setLine(i, ChatColor.translateAlternateColorCodes('&', event.getLine(i)));
            }
        }
    }
    
}
