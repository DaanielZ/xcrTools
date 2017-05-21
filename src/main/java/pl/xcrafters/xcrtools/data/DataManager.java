package pl.xcrafters.xcrtools.data;

import java.util.HashMap;
import java.util.UUID;

import pl.xcrafters.xcrtools.ToolsPlugin;

public class DataManager {

    ToolsPlugin plugin;
    
    public DataManager(ToolsPlugin plugin){
        this.plugin = plugin;
    }
    
    public HashMap<String, DataInventory> inventories = new HashMap();
    public HashMap<UUID, DataOpened> openedInventory = new HashMap();

    public HashMap<UUID, OpenedInfo> opened = new HashMap();
    
    public enum InventoryType {INVENTORY, ENDERCHEST};
    
}
