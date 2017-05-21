package pl.xcrafters.xcrtools.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class AfkListener implements Listener{

    ToolsPlugin plugin;
    
    public AfkListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        plugin.afk.remove(event.getPlayer().getName());
        plugin.lastMove.remove(event.getPlayer().getName());
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event){
        plugin.afk.remove(event.getPlayer().getName());
        plugin.lastMove.remove(event.getPlayer().getName());
    }
    
//    @EventHandler
//    public void onBlockDamage(BlockDamageEvent event){
//        if((plugin.lastMove.get(event.getPlayer().getName()) != null && System.currentTimeMillis() - plugin.lastMove.get(event.getPlayer().getName()) >= 60000) || (plugin.lastMove.get(event.getPlayer().getName()) != null && System.currentTimeMillis() - plugin.lastMove.get(event.getPlayer().getName()) >= 10000 && plugin.afk.get(event.getPlayer().getName()).getPitch() == event.getPlayer().getLocation().getPitch() && plugin.afk.get(event.getPlayer().getName()).getYaw() == event.getPlayer().getLocation().getYaw())){
//            event.setCancelled(true);
//        }
//    }
    
}
