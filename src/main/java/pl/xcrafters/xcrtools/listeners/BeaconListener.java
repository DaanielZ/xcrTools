package pl.xcrafters.xcrtools.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class BeaconListener implements Listener{

    ToolsPlugin plugin;
    
    public BeaconListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerBeacon(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)){
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
        }
    }
    
}
