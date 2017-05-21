package pl.xcrafters.xcrtools.listeners;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class CraftingListener implements Listener {

    ToolsPlugin plugin;

    public CraftingListener(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemCraft(CraftItemEvent event) {
        if (!event.getWhoClicked().hasPermission("tools.items.without-lore")) {
            List<String> lore = null;
            for (ItemStack item : event.getInventory().getMatrix()) {
                if (item != null && item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
                    if(item.getItemMeta().getLore().get(0).startsWith(plugin.color("&6[ADMIN]"))){
                        lore = item.getItemMeta().getLore();
                    }
                }
            }
            if (lore != null) {
                if(event.isShiftClick()){
                    event.setCancelled(true);
                }
                ItemStack item = event.getCurrentItem();
                ItemMeta meta = item.getItemMeta();
                meta.setLore(lore);
                item.setItemMeta(meta);
                event.setCurrentItem(item);
            }
        }
    }

}
