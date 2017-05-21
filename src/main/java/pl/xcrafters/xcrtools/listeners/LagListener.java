package pl.xcrafters.xcrtools.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.ItemStack;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class LagListener implements Listener {

    ToolsPlugin plugin;

    public LagListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        ItemStack item = event.getItem();
        if(item.getType() == Material.LAVA_BUCKET || item.getType() == Material.WATER_BUCKET) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        if(block.getType() == Material.REDSTONE_LAMP_OFF || block.getType() == Material.REDSTONE_LAMP_ON) {
            boolean redstoneBlock = false;
            for(BlockFace blockFace : BlockFace.values()) {
                if(block.getRelative(blockFace).getType() == Material.REDSTONE_BLOCK) {
                    redstoneBlock = true;
                    break;
                }
            }
            if(!redstoneBlock) {
                event.setNewCurrent(0);
            }
        }
    }

}
