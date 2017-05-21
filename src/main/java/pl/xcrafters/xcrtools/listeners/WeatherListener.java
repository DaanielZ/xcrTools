package pl.xcrafters.xcrtools.listeners;

import org.bukkit.event.Listener;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class WeatherListener implements Listener{

    ToolsPlugin plugin;
    
    public WeatherListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
//    @EventHandler
//    public void onWeatherChange(WeatherChangeEvent event){
//        if(event.toWeatherState()){
//            event.setCancelled(true);
//        }
//    }
//    
//    @EventHandler
//    public void onThunderChange(ThunderChangeEvent event){
//        if(event.toThunderState()){
//            event.setCancelled(true);
//        }
//    }
    
}
