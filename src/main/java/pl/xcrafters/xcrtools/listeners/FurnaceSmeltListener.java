package pl.xcrafters.xcrtools.listeners;

import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class FurnaceSmeltListener implements Listener{

    ToolsPlugin plugin;
    
    public FurnaceSmeltListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event){
        List<String> lore = null;
        if (event.getSource() != null && event.getSource().getItemMeta() != null && event.getSource().getItemMeta().getLore() != null) {
            if (event.getSource().getItemMeta().getLore().get(0).startsWith(plugin.color("&6[ADMIN]"))) {
                lore = event.getSource().getItemMeta().getLore();
            }
        }
        if(lore != null){
            ItemStack item = event.getResult();
            ItemMeta meta = item.getItemMeta();
            meta.setLore(lore);
            item.setItemMeta(meta);
            event.setResult(item);
        }
    }
    
}
