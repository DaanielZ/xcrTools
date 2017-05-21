package pl.xcrafters.xcrtools.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.*;
import org.bukkit.entity.Player;
import pl.xcrafters.xcrtools.ToolsPlugin;
import pl.xcrafters.xcrtools.data.DataManager;
import pl.xcrafters.xcrtools.data.OpenedInfo;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;

public class RedisListener extends JedisPubSub {

    ToolsPlugin plugin;

    public RedisListener(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.redisManager.subscribe(this, "SetTime", "SetWeather", "ExecuteCommand", "ExecuteCommandAsPlayer", "OpPlayer", "DeopPlayer", "SetRespawn", "UploadInventory", "OpenInventory", "UpdateInventory", "TeleportToPlayer", "TeleportToPosition", "SetFly", "SetGod", "SetGamemode", "VanishPlayer");
    }

    private Gson gson = new Gson();

    public void onMessage(String channel, final String message) {
        try {
            if (channel.equals("SetTime")) {
                final long time = Long.parseLong(message);
                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    public void run() {
                        for (World world : Bukkit.getWorlds()) {
                            world.setTime(time);
                        }
                    }
                });
            } else if (channel.equals("SetWeather")) {
                final ToolsPlugin.WeatherType type = ToolsPlugin.WeatherType.valueOf(message);
                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    public void run() {
                        if (type.equals(ToolsPlugin.WeatherType.SKY)) {
                            Bukkit.getWorld("world").setThundering(false);
                            Bukkit.getWorld("world").setStorm(false);
                        } else if (type.equals(ToolsPlugin.WeatherType.RAIN)) {
                            Bukkit.getWorld("world").setStorm(true);
                        } else if (type.equals(ToolsPlugin.WeatherType.THUNDER)) {
                            Bukkit.getWorld("world").setThundering(true);
                        }
                    }
                });
            } else if (channel.equals("ExecuteCommand")) {
                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message);
                    }
                });
            } else if (channel.equals("ExecuteCommandAsPlayer")) {
                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    public void run() {
                        String nick = message.split(" ")[0];
                        Player player = Bukkit.getPlayer(nick);
                        String cmd = message.split(" ")[1];
                        for(int i=2; i<message.split(" ").length; i++) {
                            cmd += " " + message.split(" ")[i];
                        }
                        if (player != null) {
                            Bukkit.dispatchCommand(player, cmd);
                        }
                    }
                });
            } else if (channel.equals("OpPlayer")) {
                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    public void run() {
                        OfflinePlayer player = Bukkit.getOfflinePlayer(message);
                        if (player != null) {
                            player.setOp(true);
                        }
                    }
                });
            } else if (channel.equals("DeopPlayer")) {
                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    public void run() {
                        OfflinePlayer player = Bukkit.getOfflinePlayer(message);
                        if (player != null) {
                            player.setOp(false);
                        }
                    }
                });
            } else if (channel.equals("SetRespawn")) {
                JsonObject object = gson.fromJson(message, JsonObject.class);

                String world = object.get("world").getAsString();
                int x = object.get("x").getAsInt();
                int y = object.get("y").getAsInt();
                int z = object.get("z").getAsInt();

                final Location loc = new Location(Bukkit.getWorld(world), x, y, z);

                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    public void run() {
                        loc.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                    }
                }, 1L);
            } else if (channel.equals("UploadInventory")) {
                JsonObject object = gson.fromJson(message, JsonObject.class);

                UUID uuid = UUID.fromString(object.get("uuid").getAsString());
                UUID ownerUUID = UUID.fromString(object.get("inventory_owner_uuid").getAsString());
                String ownerNick = object.get("inventory_owner_nick").getAsString();

                DataManager.InventoryType type = DataManager.InventoryType.valueOf(object.get("type").getAsString());

                Player player = Bukkit.getPlayer(uuid);
                Player owner = Bukkit.getPlayer(ownerUUID);

                byte[] inventory = null;

                if(owner != null) {
                    inventory = ToolsPlugin.serializeItemStacks(type == DataManager.InventoryType.INVENTORY ? owner.getInventory().getContents() : owner.getEnderChest().getContents());

                    plugin.redisManager.uploadInventory(ownerUUID, inventory, type);
                }

                if(owner != null && player != null) {
                    OpenedInfo info = new OpenedInfo(player, ownerUUID, type);
                    plugin.dataManager.opened.put(player.getUniqueId(), info);

                    plugin.openInventory(player, type, inventory, ownerNick);
                    return;
                }

                if(owner != null) {
                    plugin.redisManager.sendMessage("OpenInventory", gson.toJson(object));
                }
            } else if (channel.equals("OpenInventory")) {
                JsonObject object = gson.fromJson(message, JsonObject.class);

                UUID uuid = UUID.fromString(object.get("uuid").getAsString());
                UUID ownerUUID = UUID.fromString(object.get("inventory_owner_uuid").getAsString());
                String ownerNick = object.get("inventory_owner_nick").getAsString();

                DataManager.InventoryType type = DataManager.InventoryType.valueOf(object.get("type").getAsString());

                Player player = Bukkit.getPlayer(uuid);

                if(player == null) {
                    return;
                }

                byte[] inventory = plugin.redisManager.getOpenedInventory(ownerUUID, type);

                OpenedInfo info = new OpenedInfo(player, ownerUUID, type);
                plugin.dataManager.opened.put(player.getUniqueId(), info);

                plugin.openInventory(player, type, inventory, ownerNick);
            } else if (channel.equals("UpdateInventory")) {
                JsonObject object = gson.fromJson(message, JsonObject.class);

                final UUID ownerUUID = UUID.fromString(object.get("inventory_owner_uuid").getAsString());
                final DataManager.InventoryType type = DataManager.InventoryType.valueOf(object.get("type").getAsString());
                String instance = object.get("instance").getAsString();

                if(instance.equals(plugin.redisManager.getInstance())) {
                    return;
                }

                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            OpenedInfo info = plugin.dataManager.opened.get(player.getUniqueId());

                            if (info != null && info.getUUID().equals(ownerUUID) && type == info.getInventoryType()) {
                                byte[] inventory = plugin.redisManager.getOpenedInventory(ownerUUID, type);

                                plugin.updateInventory(player, inventory);
                            }
                        }

                        Player player = Bukkit.getPlayer(ownerUUID);

                        if (player != null) {
                            byte[] inventory = plugin.redisManager.getOpenedInventory(ownerUUID, type);

                            if (type == DataManager.InventoryType.ENDERCHEST) {
                                player.getEnderChest().setContents(ToolsPlugin.deserializeItemStacks(inventory));
                            } else {
                                player.getInventory().setContents(ToolsPlugin.deserializeItemStacks(inventory));
                            }
                        }
                    }
                }, 1L);
            } else if(channel.equals("TeleportToPlayer")) {
                JsonObject object = gson.fromJson(message, JsonObject.class);

                JsonObject from = object.get("from").getAsJsonObject();
                String nickFrom = from.get("nick").getAsString();
                String serverFrom = from.get("server").getAsString();

                JsonObject to = object.get("to").getAsJsonObject();
                String nickTo = to.get("nick").getAsString();
                String serverTo = to.get("server").getAsString();

                final int delay = object.get("delay").getAsInt();
                final String afterTeleport = object.get("afterTeleport") != null ? object.get("afterTeleport").getAsString() : null;

                final Player playerFrom = Bukkit.getPlayer(nickFrom);
                final Player playerTo = Bukkit.getPlayer(nickTo);

                if(playerFrom != null && playerTo != null) {
                    if (delay == 0) {
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                playerFrom.teleport(playerTo.getLocation());
                            }
                        });
                        if (afterTeleport != null && !afterTeleport.equals("")) {
                            playerFrom.sendMessage(plugin.color(afterTeleport));
                        }
                    } else {
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                ToolsPlugin.handleDelayTeleportation(playerFrom, playerTo.getLocation(), delay, afterTeleport);
                            }
                        });
                    }
                    return;
                }

                if(playerTo == null) {
                    return;
                }

                Location loc = playerTo.getLocation();

                object = new JsonObject();

                from = new JsonObject();
                from.addProperty("nick", nickFrom);
                from.addProperty("server", serverFrom);
                object.add("from", from);

                to = new JsonObject();
                to.addProperty("world", loc.getWorld().getName());
                to.addProperty("x", loc.getX());
                to.addProperty("y", loc.getY());
                to.addProperty("z", loc.getZ());
                to.addProperty("server", serverTo);
                object.add("to", to);

                object.addProperty("delay", delay);
                object.addProperty("afterTeleport", afterTeleport);

                plugin.redisManager.sendMessage("TeleportToPosition", gson.toJson(object));
            } else if(channel.equals("TeleportToPosition")) {
                JsonObject object = gson.fromJson(message, JsonObject.class);

                JsonObject from = object.get("from").getAsJsonObject();
                String nickFrom = from.get("nick").getAsString();

                JsonObject to = object.get("to").getAsJsonObject();
                final Location loc = new Location(Bukkit.getWorld(to.get("world").getAsString()), to.get("x").getAsDouble(), to.get("y").getAsDouble(), to.get("z").getAsDouble());
                final String serverTo = to.get("server") != null ? to.get("server").getAsString() : null;

                final int delay = object.get("delay").getAsInt();
                final String afterTeleport = object.get("afterTeleport") != null ? object.get("afterTeleport").getAsString() : null;

                final Player playerFrom = Bukkit.getPlayer(nickFrom);

                if(playerFrom == null) {
                    return;
                }

                if (loc.getWorld() != null) {
                    if (delay == 0) {
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                if(serverTo == null) {
                                    playerFrom.teleport(loc);
                                } else {
//                                    SektoryAPI.teleport(playerFrom, loc, serverTo); //TODO
                                    playerFrom.teleport(loc);
                                }
                            }
                        });
                        if (afterTeleport != null && !afterTeleport.equals("")) {
                            playerFrom.sendMessage(plugin.color(afterTeleport));
                        }
                    } else {
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                ToolsPlugin.handleDelayTeleportation(playerFrom, loc, delay, afterTeleport);
                            }
                        });
                    }
                }
            } else if(channel.equals("SetGamemode")) {
                JsonObject object = gson.fromJson(message, JsonObject.class);

                final Player player = Bukkit.getPlayer(object.get("nick").getAsString());
                final int gamemode = object.get("gamemode").getAsInt();
                if (player != null) {
                    Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        public void run() {
                            player.setGameMode(GameMode.getByValue(gamemode));
                        }
                    });
                }
            } else if(channel.equals("SetFly")) {
                JsonObject object = gson.fromJson(message, JsonObject.class);

                final Player player = Bukkit.getPlayer(object.get("nick").getAsString());
                final boolean fly = object.get("fly").getAsBoolean();
                if (player != null) {
                    Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        public void run() {
                            player.setAllowFlight(fly);
                        }
                    });
                }
            } else if(channel.equals("SetGod")) {
                JsonObject object = gson.fromJson(message, JsonObject.class);

                final Player player = Bukkit.getPlayer(object.get("nick").getAsString());
                final boolean god = object.get("god").getAsBoolean();
                if (player != null) {
                    if (god) {
                        plugin.gods.add(player.getName());
                    } else {
                        plugin.gods.remove(player.getName());
                    }
                }
            } else if(channel.equals("VanishPlayer")) {
                JsonObject object = gson.fromJson(message, JsonObject.class);

                String nick = object.get("nick").getAsString();
                final boolean vanish = object.get("vanish").getAsBoolean();

                final Player player = Bukkit.getPlayer(nick);

                if (player != null) {
                    Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        public void run() {
                            plugin.vanishPlayer(player, vanish);
                        }
                    });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onSubscribe(String channel, int subscribedChannels) {
    }

    public void onUnsubscribe(String channel, int subscribedChannels) {
    }

    public void onPSubscribe(String pattern, int subscribedChannels) {
    }

    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    }

    public void onPMessage(String pattern, String channel, String message) {
    }

}
