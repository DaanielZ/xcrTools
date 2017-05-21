package pl.xcrafters.xcrtools.listeners;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.RemoteServerCommandEvent;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class RemoteServerCommandListener implements Listener{

    ToolsPlugin plugin;
    
    public RemoteServerCommandListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onRemoteServerCommand(RemoteServerCommandEvent event){
        try {
            Socket socket = new Socket("127.0.0.1", 50000);
            socket.setTcpNoDelay(true);
            DataOutputStream ds = new DataOutputStream(socket.getOutputStream());
            ds.writeUTF("RCON");
            ds.writeUTF(event.getCommand());
            ds.flush();
            ds.close();
            socket.close();
            event.setCommand("");
        } catch (IOException ex) {
            Logger.getLogger(RemoteServerCommandListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
