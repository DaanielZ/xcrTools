package pl.xcrafters.xcrtools.listeners;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class PadListener implements Listener {

    ToolsPlugin plugin;

    public PadListener(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(player.getLocation().getBlock().getRelative(BlockFace.DOWN, 2).getType() == Material.ENDER_STONE) {
            if (player.getLocation().getBlock().getType() == Material.STONE_PLATE || player.getLocation().getBlock().getType() == Material.WOOD_PLATE || player.getLocation().getBlock().getType() == Material.IRON_PLATE || player.getLocation().getBlock().getType() == Material.GOLD_PLATE) {
                player.setVelocity(new Vector(0, 1.25, 0));
            }
        }
    }

}
