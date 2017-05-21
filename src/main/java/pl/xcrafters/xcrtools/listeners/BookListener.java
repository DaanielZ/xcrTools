package pl.xcrafters.xcrtools.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class BookListener implements Listener{

    ToolsPlugin plugin;
    
    public BookListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event){
        BookMeta old = event.getPreviousBookMeta();
        BookMeta meta = event.getNewBookMeta();
        meta.setTitle(meta.getTitle().replaceAll("§", ""));
        meta.setDisplayName(meta.getDisplayName().replaceAll("§", ""));
        event.setNewBookMeta(meta);
        if(old.getEnchants().size() != meta.getEnchants().size()){
            event.setCancelled(true);
        }
    }
    
}
