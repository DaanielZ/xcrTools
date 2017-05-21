package pl.xcrafters.xcrtools.listeners;

import com.google.gson.Gson;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.json.simple.JSONObject;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class ServerListPingListener implements Listener {

    ToolsPlugin plugin;

    public ServerListPingListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    Gson gson = new Gson();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onServerListPing(ServerListPingEvent event) {
        JSONObject object = new JSONObject();

        object.put("tps", MinecraftServer.getServer().recentTps[0]);

        event.setMotd(object.toJSONString());
    }

}
