package pl.xcrafters.xcrtools.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class SpeedListener implements Listener{

    ToolsPlugin plugin;
    
    public SpeedListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.setFlySpeed(0.1F);
        player.setWalkSpeed(0.2F);
    }
    
}
