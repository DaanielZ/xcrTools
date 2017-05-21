package pl.xcrafters.xcrtools.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class PlayerDeathListener implements Listener {

    ToolsPlugin plugin;

    public PlayerDeathListener(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (plugin.configManager.lightningOnDeath) {
            event.getEntity().getLocation().getWorld().strikeLightningEffect(event.getEntity().getLocation());
        }
        if (plugin.configManager.dropHeadOnDeath && !event.getEntity().hasPermission("tools.no-head-drop")) {
            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwner(event.getEntity().getName());
            item.setItemMeta(meta);
            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), item);
        }
        Player player = event.getEntity();
        ByteArrayDataOutput dos = ByteStreams.newDataOutput();
        dos.writeUTF("RemovePlayerHome");
        dos.writeUTF(player.getName());
        player.sendPluginMessage(plugin, "BungeeCord", dos.toByteArray());
    }

}
