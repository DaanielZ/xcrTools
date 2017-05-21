package pl.xcrafters.xcrtools.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class GodListener implements Listener{

    ToolsPlugin plugin;
    
    public GodListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(plugin.gods.contains(player.getName())){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(plugin.gods.contains(player.getName())){
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(plugin.gods.contains(player.getName())){
            plugin.gods.remove(player.getName());
        }
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event){
        Player player = event.getPlayer();
        if(plugin.gods.contains(player.getName())){
            plugin.gods.remove(player.getName());
        }
    }
    
}
