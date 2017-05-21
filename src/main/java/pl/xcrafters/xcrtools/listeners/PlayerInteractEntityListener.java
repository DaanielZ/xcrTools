package pl.xcrafters.xcrtools.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class PlayerInteractEntityListener implements Listener {

    ToolsPlugin plugin;

    public PlayerInteractEntityListener(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if(!player.hasPermission("tools.openinventory")) {
            return;
        }

        if(event.getRightClicked() instanceof Player && player.getItemInHand() != null && player.getInventory().getItemInHand().getType() == Material.STICK) {
            Player other = (Player) event.getRightClicked();

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("OpenPlayerInventory");
            out.writeUTF(player.getName());
            out.writeUTF(other.getName());
            player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        }
    }

}
