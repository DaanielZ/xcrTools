package pl.xcrafters.xcrtools.listeners;

import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import pl.xcrafters.xcrtools.ToolsPlugin;
import pl.xcrafters.xcrtools.data.DataManager;
import pl.xcrafters.xcrtools.data.DataOpened;

public class BlockBreakListener implements Listener{

    ToolsPlugin plugin;
    
    public BlockBreakListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
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

        if(plugin.afk.get(player.getName()) == null) {
            return;
        }
        if ((plugin.afk.get(player.getName()).getPitch() == player.getLocation().getPitch() && plugin.afk.get(player.getName()).getYaw() == player.getLocation().getYaw()) || (plugin.afk.get(player.getName()).getX() == player.getLocation().getX() || plugin.afk.get(player.getName()).getZ() == player.getLocation().getZ())) {
            int blocks = 0;
            if(plugin.afkBlocks.get(player.getName()) != null) {
                blocks = plugin.afkBlocks.get(player.getName());
            }
            blocks++;
            plugin.afkBlocks.put(player.getName(), blocks);
        }
    }
}
