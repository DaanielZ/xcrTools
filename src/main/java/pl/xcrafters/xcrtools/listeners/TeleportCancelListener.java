package pl.xcrafters.xcrtools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.xcrafters.xcrtools.ToolsPlugin;
import pl.xcrafters.xcrtools.events.PlayerTeleportCancelEvent;

public class TeleportCancelListener implements Listener{

    ToolsPlugin plugin;
    
    public TeleportCancelListener(ToolsPlugin plugin){
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(plugin.teleportTask.get(player.getName()) != null){
            Location from = event.getFrom();
            Location to = event.getTo();
            if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ() || from.getWorld() != to.getWorld()) {
                plugin.teleportTask.remove(player.getName()).cancel();
                player.sendMessage(ChatColor.RED + "Teleportacja anulowana!");
                PlayerTeleportCancelEvent cancelEvent = new PlayerTeleportCancelEvent(player);
                Bukkit.getPluginManager().callEvent(cancelEvent);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if (plugin.teleportTask.get(player.getName()) != null) {
                plugin.teleportTask.remove(player.getName()).cancel();
                player.sendMessage(ChatColor.RED + "Teleportacja anulowana!");
                PlayerTeleportCancelEvent cancelEvent = new PlayerTeleportCancelEvent(player);
                Bukkit.getPluginManager().callEvent(cancelEvent);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.teleportTask.get(player.getName()) != null) {
            plugin.teleportTask.remove(player.getName()).cancel();
            player.sendMessage(ChatColor.RED + "Teleportacja anulowana!");
            PlayerTeleportCancelEvent cancelEvent = new PlayerTeleportCancelEvent(player);
            Bukkit.getPluginManager().callEvent(cancelEvent);
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if (plugin.teleportTask.get(player.getName()) != null) {
            plugin.teleportTask.remove(player.getName()).cancel();
            player.sendMessage(ChatColor.RED + "Teleportacja anulowana!");
            PlayerTeleportCancelEvent cancelEvent = new PlayerTeleportCancelEvent(player);
            Bukkit.getPluginManager().callEvent(cancelEvent);
        }
    }
    
}
