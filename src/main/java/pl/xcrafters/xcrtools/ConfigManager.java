package pl.xcrafters.xcrtools;

import java.util.HashMap;

public class ConfigManager {

    ToolsPlugin plugin;
    
    public ConfigManager(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        load();
    }

    public String redisHost;
    
    public boolean dropHeadOnDeath;
    public boolean lightningOnDeath;
    public boolean punchDisabled;
    public boolean netherPvpDisabled;
    
    public int afkKickSeconds;

    public HashMap<String, String> groupColors = new HashMap();
    
    public void load(){
        redisHost = plugin.getConfig().getString("config.redis.host");

        dropHeadOnDeath = plugin.getConfig().getBoolean("config.death.head");
        lightningOnDeath = plugin.getConfig().getBoolean("config.death.lightning");
        punchDisabled = plugin.getConfig().getBoolean("config.punch-disabled");
        netherPvpDisabled = plugin.getConfig().getBoolean("config.nether-pvp-disabled");
        
        afkKickSeconds = plugin.getConfig().getInt("config.afk.kick-seconds");

        for(String group : plugin.getConfig().getConfigurationSection("config.group-colors").getKeys(false)){
            groupColors.put(group.toLowerCase(), plugin.getConfig().getString("config.group-colors." + group));
        }
    }
    
}
