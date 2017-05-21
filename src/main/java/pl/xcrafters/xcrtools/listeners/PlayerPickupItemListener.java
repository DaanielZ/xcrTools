package pl.xcrafters.xcrtools.listeners;

import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import pl.xcrafters.xcrtools.ToolsPlugin;
import pl.xcrafters.xcrtools.data.DataManager;
import pl.xcrafters.xcrtools.data.DataOpened;

public class PlayerPickupItemListener implements Listener{

    ToolsPlugin plugin;
    
    public PlayerPickupItemListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event){
        final Player player = event.getPlayer();
        UUID id = null;
        for (Map.Entry<UUID, DataOpened> en : plugin.dataManager.openedInventory.entrySet()) {
            if (en.getValue().player.equals(player)) {
                id = en.getKey();
                break;
            }
        }
        if (id != null) {
            final UUID uuid = id;
            final DataManager.InventoryType type = DataManager.InventoryType.INVENTORY;
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                public void run() {
                    plugin.updateInventory(uuid, player.getInventory().getContents(), type, player.getName());
                }
            }, 1L);
        }
    }
    
}
