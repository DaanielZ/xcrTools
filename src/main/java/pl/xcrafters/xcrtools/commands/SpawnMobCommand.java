package pl.xcrafters.xcrtools.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class SpawnMobCommand implements CommandExecutor, TabExecutor{

    ToolsPlugin plugin;
    
    public SpawnMobCommand(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getCommand("spawnmob").setExecutor(this);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args){
        List<String> materials = new ArrayList();
        for(EntityType type : EntityType.values()){
            if(args[0] == null || type.name().toLowerCase().startsWith(args[args.length - 1].toLowerCase())){
                materials.add(type.name());
            }
        }
        return materials;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage(plugin.color("&cTa komenda nie moze zostac wywolana z konsoli!"));
            return true;
        }
        if(args.length == 0){
            return false;
        }
        Player player = (Player) sender;
        String mob = args[0];
        EntityType type = getEntityTypeByName(mob);
        if(type == null){
            player.sendMessage(plugin.color("&cNie znaleziono moba o podanej nazwie!"));
            return true;
        }
        Block block = player.getTargetBlock(Collections.<Material>emptySet(), 10).getRelative(BlockFace.UP);
        Entity entity = player.getWorld().spawnEntity(block.getLocation(), type);
        if(!player.hasPermission("tools.mobs.without-lore") && entity instanceof LivingEntity){
            LivingEntity living = (LivingEntity) entity;
            living.setCustomName(plugin.color("&6[ADMIN] &7" + player.getName()));
            living.setCustomNameVisible(true);
        }
        player.sendMessage(plugin.color("&9Pomyslnie przywolano moba!"));
        return true;
    }
    
    public EntityType getEntityTypeByName(String name){
        for(EntityType type : EntityType.values()){
            if(name.replaceAll("_", "").equalsIgnoreCase(type.name().replaceAll("_", ""))){
                return type;
            }
        }
        return null;
    }
    
}
