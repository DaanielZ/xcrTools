package pl.xcrafters.xcrtools.listeners;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.messaging.PluginMessageListener;
import pl.xcrafters.xcrtools.ToolsPlugin;
import pl.xcrafters.xcrtools.data.DataInventory;
import pl.xcrafters.xcrtools.data.DataManager;
import pl.xcrafters.xcrtools.data.DataOpened;

import java.io.*;
import java.util.UUID;

public class MessageListener implements PluginMessageListener {

    ToolsPlugin plugin;

    public MessageListener(ToolsPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
    }

    public void onPluginMessageReceived(String tag, Player receiver, byte[] bytes){
        if(!tag.equals("BungeeCord")){
            return;
        }
        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
            String channel = dis.readUTF();
            if(channel.equals("TeleportToPlayer")){
                String from = dis.readUTF();
                String to = dis.readUTF();
                int delay = dis.readInt();
                String afterTp = dis.readUTF();
                Player player = Bukkit.getPlayerExact(to.split(":")[0]);
                String location = player != null ? (player.getWorld().getName() + " " + player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ()) : "null";

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                dos.writeUTF("TeleportToPlayer");
                dos.writeUTF(from.split(":")[0]);
                dos.writeUTF(to.split(":")[0]);
                dos.writeUTF(location);
                dos.writeInt(delay);
                dos.writeUTF(afterTp);
                dos.flush();
                receiver.sendPluginMessage(plugin, "BungeeCord", baos.toByteArray());
                dos.close();
            } else if(channel.equals("TeleportToPosition")){
                final Player player = Bukkit.getPlayerExact(dis.readUTF());
                String position = dis.readUTF();
                final int delay = dis.readInt();
                final String afterTp = dis.readUTF();
                if (position.equals("null")) {
                    player.sendMessage(ChatColor.RED + "Wystapil blad podczas teleportacji!");
                } else {
                    String[] location = position.split(" ");
                    final Location loc = new Location(Bukkit.getWorld(location[0]), Integer.parseInt(location[1]), Integer.parseInt(location[2]), Integer.parseInt(location[3]));
                    loc.setX(loc.getBlockX() + 0.5);
                    loc.setZ(loc.getBlockZ() + 0.5);
                    if (loc.getWorld() != null) {
                        if (delay == 0) {
                            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                public void run() {
                                    player.teleport(loc);
                                }
                            });
                            if (afterTp != null && !afterTp.equals("")) {
                                player.sendMessage(plugin.color(afterTp));
                            }
                        } else {
                            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                public void run() {
                                    ToolsPlugin.handleDelayTeleportation(player, loc, delay, afterTp);
                                }
                            });
                        }
                    }
                }
            } else if(channel.equals("TeleportToServer")){
                final Player player = Bukkit.getPlayerExact(dis.readUTF());
                final String server = dis.readUTF();
                String position = dis.readUTF();
                final int delay = dis.readInt();
                final String afterTp = dis.readUTF();
                if (position.equals("null")) {
                    player.sendMessage(ChatColor.RED + "Wystapil blad podczas teleportacji!");
                } else {
                    String[] location = position.split(" ");
                    final Location loc = new Location(Bukkit.getWorld(location[0]), Integer.parseInt(location[1]), Integer.parseInt(location[2]), Integer.parseInt(location[3]));
                    loc.setX(loc.getBlockX() + 0.5);
                    loc.setZ(loc.getBlockZ() + 0.5);
                    if (loc.getWorld() != null) {
                        if (delay == 0) {
                            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                public void run() {
//                                    SektoryAPI.teleport(player, loc, server); //TODO
                                    player.teleport(loc);
                                }
                            });
                            if (!afterTp.equals("")) {
                                player.sendMessage(plugin.color(afterTp));
                            }
                        } else {
                            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                public void run() {
                                    ToolsPlugin.handleDelayTeleportation(player, loc, delay, afterTp);
                                }
                            });
                        }
                    }
                }
            } else if(channel.equals("SetPlayerHome")){
                Player player = Bukkit.getPlayer(dis.readUTF());
                if (player != null) {
                    String location = player.getWorld().getName() + " " + player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);
                    dos.writeUTF("SetPlayerHome");
                    dos.writeUTF(player.getName());
                    dos.writeUTF(location);
                    dos.flush();
                    receiver.sendPluginMessage(plugin, "BungeeCord", baos.toByteArray());
                    dos.close();
                }
            } else if(channel.equals("SetServerSpawn")){
                Player player = Bukkit.getPlayer(dis.readUTF());
                if (player != null) {
                    String location = player.getWorld().getName() + " " + player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);
                    dos.writeUTF("SetServerSpawn");
                    dos.writeUTF(location);
                    dos.flush();
                    receiver.sendPluginMessage(plugin, "BungeeCord", baos.toByteArray());
                    dos.close();
                }
            } else if(channel.equals("SetGamemode")){
                final Player player = Bukkit.getPlayer(dis.readUTF());
                final int gamemode = dis.readInt();
                if (player != null) {
                    Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        public void run() {
                            player.setGameMode(GameMode.getByValue(gamemode));
                        }
                    });
                }
            } else if(channel.equals("SetFly")){
                final Player player = Bukkit.getPlayer(dis.readUTF());
                final boolean fly = dis.readBoolean();
                if (player != null) {
                    Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        public void run() {
                            player.setAllowFlight(fly);
                        }
                    });
                }
            } else if(channel.equals("SetGod")){
                final Player player = Bukkit.getPlayer(dis.readUTF());
                final boolean god = dis.readBoolean();
                if (player != null) {
                    if (god) {
                        plugin.gods.add(player.getName());
                    } else {
                        plugin.gods.remove(player.getName());
                    }
                }
            } else if(channel.equals("VanishPlayer")){
                final Player player = Bukkit.getPlayer(dis.readUTF());
                final boolean vanish = dis.readBoolean();
                if (player != null) {
                    Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        public void run() {
                            plugin.vanishPlayer(player, vanish);
                        }
                    });
                }
            } else if(channel.equals("OpenInventory")){
                DataInventory inv = new DataInventory(dis);
                plugin.openInventory(inv);
                plugin.dataManager.inventories.put(inv.player.getName(), inv);
            } else if(channel.equals("OpenEnderchest")){
                String nick = dis.readUTF();
                Player player = Bukkit.getPlayer(nick);
                if (player != null) {
                    String other = dis.readUTF();
                    byte[] enderchest = ToolsPlugin.serializeItemStacks(player.getEnderChest().getContents());
                    UUID uuid = UUID.fromString(dis.readUTF());

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);
                    dos.writeUTF("OpenInventory");
                    dos.writeUTF("ENDERCHEST");
                    dos.writeUTF(other);
                    dos.writeUTF(nick);
                    dos.writeUTF(uuid.toString());
                    dos.writeInt(enderchest.length);
                    dos.write(enderchest);
                    dos.flush();
                    receiver.sendPluginMessage(plugin, "BungeeCord", baos.toByteArray());
                    dos.close();
                    DataOpened opened = new DataOpened();
                    opened.player = player;
                    opened.type = DataManager.InventoryType.ENDERCHEST;
                    opened.uuid = uuid;
                    plugin.dataManager.openedInventory.put(uuid, opened);
                }
            } else if(channel.equals("OpenInventoryAndArmor")){
                String nick = dis.readUTF();
                Player player = Bukkit.getPlayer(nick);
                if (player != null) {
                    String other = dis.readUTF();
                    byte[] inventory = ToolsPlugin.serializeItemStacks(player.getInventory().getContents());
                    byte[] armor = ToolsPlugin.serializeItemStacks(player.getInventory().getArmorContents());
                    UUID uuid = UUID.fromString(dis.readUTF());

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);
                    dos.writeUTF("OpenInventory");
                    dos.writeUTF("INVENTORY");
                    dos.writeUTF(other);
                    dos.writeUTF(nick);
                    dos.writeUTF(uuid.toString());
                    dos.writeInt(inventory.length);
                    dos.write(inventory);
                    dos.writeInt(armor.length);
                    dos.write(armor);
                    dos.flush();
                    receiver.sendPluginMessage(plugin, "BungeeCord", baos.toByteArray());
                    dos.close();
                    DataOpened opened = new DataOpened();
                    opened.player = player;
                    opened.type = DataManager.InventoryType.INVENTORY;
                    opened.uuid = uuid;
                    plugin.dataManager.openedInventory.put(uuid, opened);
                }
            } else if(channel.equals("UpdateInventory")){
                DataManager.InventoryType type = DataManager.InventoryType.valueOf(dis.readUTF());
                final Player player = Bukkit.getPlayerExact(dis.readUTF());
                String other = dis.readUTF();
                final UUID uuid = UUID.fromString(dis.readUTF());
                if (type.equals(DataManager.InventoryType.ENDERCHEST)) {
                    int enderchest = dis.readInt();
                    final byte[] ender = new byte[enderchest];
                    for (int i = 0; i < enderchest; i++) {
                        ender[i] = dis.readByte();
                    }
                    if (plugin.dataManager.openedInventory.get(uuid) != null && plugin.dataManager.openedInventory.get(uuid).player.getName().equals(other)) {
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                plugin.dataManager.openedInventory.get(uuid).player.getEnderChest().setContents(ToolsPlugin.deserializeItemStacks(ender));
                                plugin.dataManager.openedInventory.get(uuid).player.updateInventory();
                            }
                        });
                    }
                    if (plugin.dataManager.inventories.get(player.getName()) != null) {
                        final DataInventory data = plugin.dataManager.inventories.get(player.getName());
                        if (data.type.equals(type)) {
                            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                public void run() {
                                    data.inventory.setContents(ToolsPlugin.deserializeItemStacks(ender));
                                    data.contents = data.inventory.getContents();
                                    player.updateInventory();
                                }
                            });
                        }
                    }
                } else {
                    int inventory = dis.readInt();
                    final byte[] inv = new byte[inventory];
                    for (int i = 0; i < inventory; i++) {
                        inv[i] = dis.readByte();
                    }
                    int armor = dis.readInt();
                    final byte[] arm = new byte[armor];
                    for (int i = 0; i < armor; i++) {
                        arm[i] = dis.readByte();
                    }
                    if (plugin.dataManager.openedInventory.get(uuid) != null && plugin.dataManager.openedInventory.get(uuid).player.getName().equals(other)) {
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                plugin.dataManager.openedInventory.get(uuid).player.getInventory().setArmorContents(ToolsPlugin.deserializeItemStacks(arm));
                                plugin.dataManager.openedInventory.get(uuid).player.getInventory().setContents(ToolsPlugin.deserializeItemStacks(inv));
                                plugin.dataManager.openedInventory.get(uuid).player.updateInventory();
                            }
                        });
                    }
                    if (plugin.dataManager.inventories.get(player.getName()) != null) {
                        final DataInventory data = plugin.dataManager.inventories.get(player.getName());
                        ItemStack[] armors = ToolsPlugin.deserializeItemStacks(arm);
                        ItemStack[] invi = ToolsPlugin.deserializeItemStacks(inv);
                        final ItemStack[] items = new ItemStack[invi.length + armors.length];
                        for (int i = 0; i < items.length; i++) {
                            items[i] = i >= invi.length ? armors[i - invi.length] : invi[i];
                        }
                        if (data.type.equals(type)) {
                            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                public void run() {
                                    data.inventory.setContents(items);
                                    data.contents = data.inventory.getContents();
                                    player.updateInventory();
                                }
                            });
                        }
                    }
                }
            } else if(channel.equals("CloseInventory")){
                Player player = Bukkit.getPlayerExact(dis.readUTF());
                UUID uuid = UUID.fromString(dis.readUTF());
                if (plugin.dataManager.openedInventory.get(uuid) != null && plugin.dataManager.openedInventory.get(uuid).player.equals(player)) {
                    plugin.dataManager.openedInventory.remove(uuid);
                }
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

}
