package pl.xcrafters.xcrtools.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class PunchListener implements Listener {

    ToolsPlugin plugin;

    public PunchListener(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!plugin.configManager.punchDisabled) {
            return;
        }
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            Player player = (Player) event.getEntity();
            Arrow arrow = (Arrow) event.getDamager();
            if(arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                if(player.equals(shooter)) {
                    event.setCancelled(true);
                    player.damage(event.getFinalDamage());
                }
            }
        }
    }

}
