package pl.xcrafters.xcrtools.listeners;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class AnvilListener implements Listener{

    ToolsPlugin plugin;
    
    public AnvilListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        HumanEntity entity = event.getWhoClicked();
        Player player = (Player) entity;
        Inventory inventory = event.getInventory();
        if (inventory instanceof AnvilInventory) {
            AnvilInventory anvil = (AnvilInventory) inventory;
            InventoryView view = event.getView();
            int rawSlot = event.getRawSlot();
            if (rawSlot == view.convertSlot(rawSlot)) {
                if (rawSlot == 2) {
                    ItemStack items[] = anvil.getContents();
                    if (items[0] != null && items[1] != null && event.getCurrentItem() != null && items[1].getType() == Material.ENCHANTED_BOOK) {
                        List<String> lore = null;
                        boolean sword = items[0].getItemMeta() != null && items[0].getItemMeta().getLore() != null && items[0].getItemMeta().getLore().get(0).startsWith(plugin.color("&6[ADMIN]"));
                        if(!sword){
                            if (items[1].getItemMeta() != null && items[1].getItemMeta().getLore() != null) {
                                if (items[1].getItemMeta().getLore().get(0).startsWith(plugin.color("&6[ADMIN]"))) {
                                    lore = items[1].getItemMeta().getLore();
                                }
                            }
                            if (lore != null) {
                                ItemStack item = event.getCurrentItem();
                                ItemMeta meta = item.getItemMeta();
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                                event.setCursor(item);
                                anvil.clear();
                            }
                        }
                    }
                }
            }
        }
    }
    
}
