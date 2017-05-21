package pl.xcrafters.xcrtools.data;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.xcrafters.xcrtools.ToolsPlugin;

public class DataInventory {
    
    public DataInventory(DataInputStream in){
        try {
            type = DataManager.InventoryType.valueOf(in.readUTF().toUpperCase());
            player = Bukkit.getPlayerExact(in.readUTF());
            opened = in.readUTF();
            uuid = UUID.fromString(in.readUTF());
            if(type == DataManager.InventoryType.ENDERCHEST){
                int size = in.readInt();
                byte[] data = new byte[size];
                for(int i=0; i<size; i++){
                    data[i] = in.readByte();
                }
                contents = ToolsPlugin.deserializeItemStacks(data);
            } else {
                int inv = in.readInt();
                byte[] invb = new byte[inv];
                for(int i=0; i<inv; i++){
                    invb[i] = in.readByte();
                }
                ItemStack[] invi = ToolsPlugin.deserializeItemStacks(invb);
                int arm = in.readInt();
                byte[] armb = new byte[arm];
                for(int i=0; i<arm; i++){
                    armb[i] = in.readByte();
                }
                ItemStack[] armi = ToolsPlugin.deserializeItemStacks(armb);
                ItemStack[] items = new ItemStack[invi.length+armi.length];
                for(int i=0; i<items.length; i++){
                    items[i] = i>=invi.length ? armi[i - invi.length] : invi[i];
                }
                contents = items;
            }
        } catch (IOException ex) {
            Logger.getLogger(DataInventory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DataManager.InventoryType type;
    public Player player;
    public String opened;
    public ItemStack[] contents;
    public Inventory inventory;
    public UUID uuid;
    
}
