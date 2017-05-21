package pl.xcrafters.xcrtools.listeners;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class DispenserListener implements Listener {

    ToolsPlugin plugin;

    public DispenserListener(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        Dispenser dispenser = (Dispenser) event.getBlock().getState();
        if (event.getBlock().getRelative(BlockFace.UP).getType().equals(Material.ENDER_STONE) || event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.ENDER_STONE)
                || event.getBlock().getRelative(BlockFace.NORTH).getType().equals(Material.ENDER_STONE) || event.getBlock().getRelative(BlockFace.SOUTH).getType().equals(Material.ENDER_STONE)
                || event.getBlock().getRelative(BlockFace.EAST).getType().equals(Material.ENDER_STONE) || event.getBlock().getRelative(BlockFace.WEST).getType().equals(Material.ENDER_STONE)) {
            if (event.getBlock().getX() > 100 || event.getBlock().getZ() > 100 || event.getBlock().getX() < -100 || event.getBlock().getZ() < -100) {
                dispenser.getInventory().clear();
                event.setCancelled(true);
                return;
            }
            ItemStack item = event.getItem();
            dispenser.getInventory().addItem(item);
        }
    }

}
