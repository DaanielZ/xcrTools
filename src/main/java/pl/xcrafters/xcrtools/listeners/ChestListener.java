package pl.xcrafters.xcrtools.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class ChestListener implements Listener {

    ToolsPlugin plugin;

    public ChestListener(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock() != null) {
            final Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if (block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)) {
                Chest chest = (Chest) block.getState();
                Inventory inv;
                InventoryHolder ih = chest.getInventory().getHolder();
                if (ih instanceof DoubleChest) {
                    DoubleChest doublechest = (DoubleChest) ih;
                    inv = doublechest.getInventory();
                } else {
                    inv = chest.getBlockInventory();
                }

                for(int i = 0; i < inv.getSize(); i++) {
                    if(checkItem(inv.getItem(i))) {
                        inv.remove(inv.getItem(i));
                    }
                }

                boolean blocked = block.getWorld().getBlockAt(block.getLocation().getBlockX(), block.getLocation().getBlockY() + 1, block.getLocation().getBlockZ()).getType().isBlock();
                if (blocked && player.hasPermission("tools.chest.blocked")) {
                    plugin.openChest(block, player);
                    event.setCancelled(true);
                }
            }
        }
    }

    boolean checkItem(ItemStack item) {
        return item != null && item.getItemMeta() != null && item.getItemMeta().getLore() != null && item.getItemMeta().getLore().size() > 0 && item.getItemMeta().getLore().get(0).equals(ChatColor.GOLD + "[ADMIN]");
    }

}
