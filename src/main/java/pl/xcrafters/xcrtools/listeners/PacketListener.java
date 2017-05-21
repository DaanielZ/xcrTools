package pl.xcrafters.xcrtools.listeners;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot;
import net.minecraft.server.v1_8_R3.PacketPlayOutWindowItems;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.xcrafters.xcrtools.ToolsPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class PacketListener implements Listener {

    ToolsPlugin plugin;

    public PacketListener(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

//    @EventHandler
//    public void onPacketSending(PacketSendEvent event) {
//        if(event.getPacket() == null) {
//            return;
//        }
//        Player player = event.getConnection().getPlayer();
//
//        if(player.hasPermission("tools.items.see-lore")) {
//            return;
//        }
//
//        Packet rawPacket = event.getPacket();
//
//        if(rawPacket instanceof PacketPlayOutSetSlot) {
//            PacketPlayOutSetSlot packet = (PacketPlayOutSetSlot) rawPacket;
//            Field c = null;
//            try {
//                c = PacketPlayOutSetSlot.class.getDeclaredField("c");
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            }
//            c.setAccessible(true);
//
//            ItemStack item = null;
//            try {
//                item = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_8_R3.ItemStack) c.get(packet));
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            if(item != null) {
//                ItemMeta meta = item.getItemMeta();
//                if (meta != null && meta.getLore() != null && meta.getLore().get(0).startsWith(plugin.color("&6[ADMIN]"))) {
//                    meta.setLore(new ArrayList());
//                }
//                item.setItemMeta(meta);
//                try {
//                    c.set(packet, CraftItemStack.asNMSCopy(item));
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        if(rawPacket instanceof PacketPlayOutWindowItems) {
//            PacketPlayOutWindowItems packet = (PacketPlayOutWindowItems) rawPacket;
//            Field b = null;
//            try {
//                b = PacketPlayOutWindowItems.class.getDeclaredField("b");
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            }
//            b.setAccessible(true);
//            net.minecraft.server.v1_8_R3.ItemStack[] items = new net.minecraft.server.v1_8_R3.ItemStack[0];
//            try {
//                items = (net.minecraft.server.v1_8_R3.ItemStack[]) b.get(packet);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            for(int i=0; i<items.length; i++) {
//                ItemStack item = CraftItemStack.asBukkitCopy(items[i]);
//                if(item != null) {
//                    ItemMeta meta = item.getItemMeta();
//                    if (meta != null && meta.getLore() != null && meta.getLore().get(0).startsWith(plugin.color("&6[ADMIN]"))) {
//                        meta.setLore(new ArrayList());
//                    }
//                    item.setItemMeta(meta);
//                    items[i] = CraftItemStack.asNMSCopy(item);
//                }
//            }
//            try {
//                b.set(packet, items);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
