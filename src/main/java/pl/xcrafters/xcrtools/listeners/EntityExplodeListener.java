package pl.xcrafters.xcrtools.listeners;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class EntityExplodeListener implements Listener{

    ToolsPlugin plugin;
    
    public EntityExplodeListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event){
        for(Block block : event.blockList()){
            if (block.getMetadata("admin") != null && block.getMetadata("admin").size() > 0) {
                List<String> lore = (List<String>) block.getMetadata("admin").get(0).value();
                for (ItemStack drop : block.getDrops()) {
                    ItemMeta meta = drop.getItemMeta();
                    meta.setLore(lore);
                    drop.setItemMeta(meta);
                }
                block.setType(Material.AIR);
            }
        }
    }
    
}
