package pl.xcrafters.xcrtools.listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class EntityDamageByEntityListener implements Listener{

    ToolsPlugin plugin;
    
    public EntityDamageByEntityListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player){
            Player player = (Player) event.getDamager();
            if(player.getItemInHand().getType().equals(Material.STICK) && player.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK) == 2){
                event.setDamage(0.0);
            }
        }
    }
    
}
