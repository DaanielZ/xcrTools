package pl.xcrafters.xcrtools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.*;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class VanishListener implements Listener {

    ToolsPlugin plugin;

    public VanishListener(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.vanished.contains(event.getPlayer().getName())) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.showPlayer(player);
            }
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if (plugin.vanished.contains(event.getPlayer().getName())) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.showPlayer(player);
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (plugin.vanished.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (plugin.vanished.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("tools.vanish.on-join")){
            plugin.vanishPlayer(player, true);
        }
        if (!player.hasPermission("tools.vanish.see")) {
            for (String nick : plugin.vanished) {
                Player online = Bukkit.getPlayerExact(nick);
                if(online != null) {
                    player.hidePlayer(online);
                }
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event){
        if(event.getTarget() instanceof Player){
            Player player = (Player) event.getTarget();
            if (plugin.vanished.contains(player.getName())) {
                event.setCancelled(true);
            }
        }
    }

}
