package pl.xcrafters.xcrtools.redis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import pl.xcrafters.xcrtools.ToolsPlugin;
import pl.xcrafters.xcrtools.data.DataManager;
import pl.xcrafters.xcrtools.data.OpenedInfo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class RedisManager {

    ToolsPlugin plugin;

    public RedisManager(ToolsPlugin plugin) {
        this.plugin = plugin;
        this.pool = new JedisPool(new JedisPoolConfig(), plugin.configManager.redisHost);

        this.instance = plugin.generateRandomString();
    }

    JedisPool pool;

    private String instance;

    public String getInstance() {
        return this.instance;
    }

    private Gson gson = new Gson();

    public void subscribe(final JedisPubSub pubSub, final String... channels) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            public void run() {
                Jedis jedis = pool.getResource();
                try {
                    jedis.select(0);
                    jedis.subscribe(pubSub, channels);
                } catch (JedisConnectionException ex) {
                    pool.returnBrokenResource(jedis);
                } finally {
                    pool.returnResource(jedis);
                }
            }
        });
    }

    public void sendMessage(String channel, String message) {
        Jedis jedis = pool.getResource();
        try {
            jedis.publish(channel, message);
        } catch (JedisConnectionException ex) {
            pool.returnBrokenResource(jedis);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void uploadInventory(UUID uuid, byte[] inventory, DataManager.InventoryType type) {
        Jedis jedis = pool.getResource();
        try {
            jedis.set((("tools:" + type.name().toLowerCase() + ":" + uuid.toString()).getBytes("UTF-8")), inventory);
        } catch (JedisConnectionException ex) {
            pool.returnBrokenResource(jedis);
        } catch (UnsupportedEncodingException e) {
            pool.returnResource(jedis);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public byte[] getOpenedInventory(UUID uuid, DataManager.InventoryType type) {
        byte[] inventory = null;

        Jedis jedis = pool.getResource();
        try {
            if(jedis.exists(("tools:" + type.name().toLowerCase() + ":" + uuid.toString()).getBytes("UTF-8"))) {
                inventory = jedis.get(("tools:" + type.name().toLowerCase() + ":" + uuid.toString()).getBytes("UTF-8"));
            }
        } catch (JedisConnectionException ex) {
            pool.returnBrokenResource(jedis);
        } catch (UnsupportedEncodingException ex) {
            pool.returnResource(jedis);
        } finally {
            pool.returnResource(jedis);
        }

        return inventory;
    }

    public void updateInventory(UUID uuid, DataManager.InventoryType type, byte[] inventory) {
        Jedis jedis = pool.getResource();
        try {
            if(jedis.exists(("tools:" + type.name().toLowerCase() + ":" + uuid.toString()).getBytes("UTF-8"))) {
                jedis.set(("tools:" + type.name().toLowerCase() + ":" + uuid.toString()).getBytes("UTF-8"), inventory);

                JsonObject object = new JsonObject();
                object.addProperty("inventory_owner_uuid", uuid.toString());
                object.addProperty("type", type.name());
                object.addProperty("instance", plugin.redisManager.getInstance());

                jedis.publish("UpdateInventory", gson.toJson(object));
            }
        } catch (JedisConnectionException ex) {
            pool.returnBrokenResource(jedis);
        } catch (UnsupportedEncodingException ex) {
            pool.returnResource(jedis);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void closeInventory(OpenedInfo info) {
        UUID uuid = info.getPlayer().getUniqueId();
        UUID ownerUUID = info.getUUID();
        DataManager.InventoryType type = info.getInventoryType();

        Jedis jedis = pool.getResource();
        try {
            jedis.srem("tools:" + type.name().toLowerCase() + ":" + ownerUUID.toString() + ":opened", uuid.toString());

            if(!jedis.exists("tools:" + type.name().toLowerCase() + ":" + ownerUUID.toString() + ":opened")) {
                jedis.del(("tools:" + type.name().toLowerCase() + ":" + ownerUUID.toString()).getBytes("UTF-8"));
            }
        } catch (JedisConnectionException ex) {
            pool.returnBrokenResource(jedis);
        } catch (UnsupportedEncodingException ex) {
            pool.returnResource(jedis);
        } finally {
            pool.returnResource(jedis);
        }
    }

    public void publishTps() {
        Jedis jedis = pool.getResource();
        try {
            jedis.select(2);
            String key = "tools:tps:" + Bukkit.getIp() + ":" + Bukkit.getPort();
            jedis.set(key, String.valueOf(MinecraftServer.getServer().recentTps[0]));
            jedis.expire(key, 5);
        } catch (JedisConnectionException ex) {
            pool.returnBrokenResource(jedis);
        } finally {
            pool.returnResource(jedis);
        }
    }

}
