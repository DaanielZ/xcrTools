package pl.xcrafters.xcrtools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import pl.xcrafters.xcrtools.data.DataManager;
import pl.xcrafters.xcrtools.data.DataInventory;
import pl.xcrafters.xcrtools.redis.RedisManager;
import pl.xcrafters.xcrtools.commands.*;
import pl.xcrafters.xcrtools.listeners.*;

public class ToolsPlugin extends JavaPlugin {

    public ConfigManager configManager;
    public DataManager dataManager;
    public RedisManager redisManager;

    Enchantments enchantments;

    GodListener godListener;
    TeleportCancelListener teleportCancelListener;
    VanishListener vanishListener;
    DispenserListener dispenserListener;
    RemoteServerCommandListener remoteServerCommandListener;
    WeatherListener weatherListener;
    SignListener signListener;
    InventoryListener inventoryListener;
    SpeedListener speedListener;
    ChestListener chestListener;
    PlayerPrefixSuffixListener playerPrefixSuffixListener;
    CraftingListener craftingListener;
    PlayerDeathListener playerDeathListener;
    EntityDamageByEntityListener entityDamageByEntityListener;
    FurnaceSmeltListener furnaceSmeltListener;
    AnvilListener anvilListener;
    AfkListener afkListener;
    PlayerDropItemListener playerDropItemListener;
    PlayerPickupItemListener playerPickupItemListener;
    BlockBreakListener blockBreakListener;
    BlockPlaceListener blockPlaceListener;
    EntityExplodeListener entityExplodeListener;
    BeaconListener beaconListener;
    BookListener bookListener;
    ServerListPingListener serverListPingListener;
    MessageListener messageListener;
    LagListener lagListener;
    BedListener bedListener;
    PadListener padListener;
    PunchListener punchListener;
    PacketListener packetListener;
    PlayerInteractEntityListener playerInteractEntityListener;
    RedisListener redisListener;

    ClearInventoryCommand clearInventoryCommand;
    SpawnerCommand spawnerCommand;
    ItemCommand itemCommand;
    RenameCommand renameCommand;
    GiveCommand giveCommand;
    HealCommand healCommand;
    RepairCommand repairCommand;
    HatCommand hatCommand;
    HeadCommand headCommand;
    TpposCommand tpposCommand;
    TopCommand topCommand;
    EnchantCommand enchantCommand;
    SpeedCommand speedCommand;
    CompassCommand compassCommand;
    KnockCommand knockCommand;
    ExpCommand expCommand;
    SpawnMobCommand spawnMobCommand;
    AfkCommand afkCommand;

    BukkitTask afkTask;

    static ToolsPlugin plugin;

    public HashMap<String, BukkitTask> teleportTask = new HashMap();
    public List<String> inventories = new ArrayList();

    public List<String> gods = new ArrayList();
    public List<String> vanished = new ArrayList();

    public HashMap<String, Location> afk = new HashMap();
    public HashMap<String, Long> lastMove = new HashMap();
    public HashMap<String, Integer> afkBlocks = new HashMap();

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.dataManager = new DataManager(this);
        this.redisManager = new RedisManager(this);

        this.enchantments = new Enchantments(this);

        this.godListener = new GodListener(this);
        this.teleportCancelListener = new TeleportCancelListener(this);
        this.vanishListener = new VanishListener(this);
        this.dispenserListener = new DispenserListener(this);
        this.remoteServerCommandListener = new RemoteServerCommandListener(this);
        this.weatherListener = new WeatherListener(this);
        this.signListener = new SignListener(this);
        this.inventoryListener = new InventoryListener(this);
        this.speedListener = new SpeedListener(this);
        this.chestListener = new ChestListener(this);
        this.playerPrefixSuffixListener = new PlayerPrefixSuffixListener(this);
        this.craftingListener = new CraftingListener(this);
        this.playerDeathListener = new PlayerDeathListener(this);
        this.entityDamageByEntityListener = new EntityDamageByEntityListener(this);
        this.furnaceSmeltListener = new FurnaceSmeltListener(this);
        this.anvilListener = new AnvilListener(this);
        this.afkListener = new AfkListener(this);
        this.playerDropItemListener = new PlayerDropItemListener(this);
        this.playerPickupItemListener = new PlayerPickupItemListener(this);
        this.blockBreakListener = new BlockBreakListener(this);
        this.blockPlaceListener = new BlockPlaceListener(this);
        this.entityExplodeListener = new EntityExplodeListener(this);
        this.beaconListener = new BeaconListener(this);
        this.bookListener = new BookListener(this);
        this.serverListPingListener = new ServerListPingListener(this);
        this.messageListener = new MessageListener(this);
        this.lagListener = new LagListener(this);
        this.bedListener = new BedListener(this);
        this.padListener = new PadListener(this);
        this.punchListener = new PunchListener(this);
        this.packetListener = new PacketListener(this);
        this.playerInteractEntityListener = new PlayerInteractEntityListener(this);
        this.redisListener = new RedisListener(this);

        this.clearInventoryCommand = new ClearInventoryCommand(this);
        this.spawnerCommand = new SpawnerCommand(this);
        this.itemCommand = new ItemCommand(this);
        this.renameCommand = new RenameCommand(this);
        this.giveCommand = new GiveCommand(this);
        this.healCommand = new HealCommand(this);
        this.repairCommand = new RepairCommand(this);
        this.hatCommand = new HatCommand(this);
        this.headCommand = new HeadCommand(this);
        this.tpposCommand = new TpposCommand(this);
        this.topCommand = new TopCommand(this);
        this.enchantCommand = new EnchantCommand(this);
        this.speedCommand = new SpeedCommand(this);
        this.compassCommand = new CompassCommand(this);
        this.knockCommand = new KnockCommand(this);
        this.expCommand = new ExpCommand(this);
        this.spawnMobCommand = new SpawnMobCommand(this);
        this.afkCommand = new AfkCommand(this);

        this.afkTask = new AfkTask(this).runTaskTimer(this, 20L, 20L);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            public void run() {
                redisManager.publishTps();
            }
        }, 60L, 60L);

        plugin = this;
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    public enum WeatherType {

        SKY, RAIN, THUNDER
    };

    public static void handleDelayTeleportation(final Player player, final Location loc, int delay, final String afterTp) {
        if (plugin.teleportTask.get(player.getName()) != null) {
            plugin.teleportTask.remove(player.getName()).cancel();
        }
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            public void run() {
//                SektoryAPI.teleport(player, loc); //TODO
                player.teleport(loc);
                plugin.teleportTask.remove(player.getName());
                if (afterTp != null && !afterTp.equals("")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', afterTp));
                }
            }
        }, 20L * delay);
        plugin.teleportTask.put(player.getName(), task);
    }

    public static void handleDelayTeleportation(final Player player, final String server, final Location loc, int delay, final String afterTp) {
        if (plugin.teleportTask.get(player.getName()) != null) {
            plugin.teleportTask.remove(player.getName()).cancel();
        }
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            public void run() {
//                SektoryAPI.teleport(player, loc, server);
                player.teleport(loc); //TODO
                plugin.teleportTask.remove(player.getName());
                if (afterTp != null && !afterTp.equals("")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', afterTp));
                }
            }
        }, 20L * delay);
        plugin.teleportTask.put(player.getName(), task);
    }

    public String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public Material getMaterial(String materialName) {
        Material returnMaterial = null;
        if (materialName.matches("-?[0-9]+")) {
            int id = Integer.parseInt(materialName);
            returnMaterial = Material.getMaterial(id);
        } else {
            if (Material.getMaterial(materialName) == null) {
                for (Material material : Material.values()) {
                    String sourceName = material.name();
                    if (materialName.equalsIgnoreCase(sourceName.replaceAll("_", ""))) {
                        returnMaterial = material;
                    }
                }
            } else {
                returnMaterial = Material.matchMaterial(materialName);
            }
        }
        return returnMaterial;
    }

    public void vanishPlayer(final Player player, boolean vanish) {
        if (vanish) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!online.hasPermission("tools.vanish.see") && online.canSee(player)) {
                    online.hidePlayer(player);
                }
            }
            if (!vanished.contains(player.getName())) {
                vanished.add(player.getName());
            }
        } else {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if(!online.canSee(player)) {
                    online.showPlayer(player);
                }
            }
            vanished.remove(player.getName());
        }
    }

    public void openInventory(final DataInventory inventory) {
        final Inventory inv = Bukkit.createInventory(inventory.player, inventory.type == DataManager.InventoryType.INVENTORY ? 45 : 27, plugin.color("&9[" + (inventory.type == DataManager.InventoryType.INVENTORY ? "INV" : "EC") + "] &8" + inventory.opened));
        inv.setContents(inventory.contents);
        inventory.inventory = inv;
        Bukkit.getScheduler().runTask(plugin, new Runnable(){
            public void run(){
                inventory.player.openInventory(inv);
            }
        });
    }

    public void openInventory(final Player player, DataManager.InventoryType type, byte[] inventory, String nick) {
        final Inventory inv = Bukkit.createInventory(player, type == DataManager.InventoryType.INVENTORY ? 36 : 27, plugin.color("&9[" + (type == DataManager.InventoryType.INVENTORY ? "INV" : "EC") + "] &8" + nick));
        inv.setContents(ToolsPlugin.deserializeItemStacks(inventory));
        Bukkit.getScheduler().runTask(plugin, new Runnable(){
            public void run(){
                player.openInventory(inv);
            }
        });
    }

    public void updateInventory(Player player, byte[] inventory) {
        Inventory inv = player.getOpenInventory().getTopInventory();

        inv.setContents(ToolsPlugin.deserializeItemStacks(inventory));

        player.updateInventory();
    }

    public static byte[] serializeItemStacks(ItemStack[] inv) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BukkitObjectOutputStream bos = new BukkitObjectOutputStream(os);
            bos.writeObject(inv);
            return os.toByteArray();
        } catch (IOException ex) {
            plugin.getLogger().log(Level.WARNING, ex.toString());
        }
        return null;
    }

    public static ItemStack[] deserializeItemStacks(byte[] b) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            BukkitObjectInputStream bois = new BukkitObjectInputStream(bais);
            return (ItemStack[]) bois.readObject();
        } catch (Exception ex) {
            plugin.getLogger().log(Level.WARNING, ex.toString());
        }
        return null;
    }

    public ItemStack parseItemStack(String string) {
        String[] board = string.split(" ");
        Material material = Material.matchMaterial(board[0].split(":")[0]);
        short data = 0;
        if (board[0].split(":").length > 1) {
            data = Short.valueOf(board[0].split(":")[1]);
        }
        int amount = Integer.parseInt(board[1]);
        return new ItemStack(material, amount, data);
    }

    public void updateInventory(final UUID uuid, final ItemStack[] contents, final DataManager.InventoryType type, final String player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            public void run() {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);
                    dos.writeUTF("UpdateInventory");
                    dos.writeUTF(type.toString());
                    dos.writeUTF(player);
                    dos.writeUTF(uuid.toString());
                    if (type.equals(DataManager.InventoryType.ENDERCHEST)) {
                        byte[] enderchest = ToolsPlugin.serializeItemStacks(contents);
                        dos.writeInt(enderchest.length);
                        dos.write(enderchest);
                    } else {
                        ItemStack[] inv = new ItemStack[36];
                        for (int i = 0; i < 36; i++) {
                            inv[i] = contents[i];
                        }
                        ItemStack[] arm = new ItemStack[4];
                        if (contents.length <= 36) {
                            arm = Bukkit.getPlayerExact(player).getInventory().getArmorContents();
                        } else {
                            for (int i = 0; i < 4; i++) {
                                arm[i] = contents[36 + i];
                            }
                        }
                        byte[] inventory = ToolsPlugin.serializeItemStacks(inv);
                        byte[] armor = ToolsPlugin.serializeItemStacks(arm);
                        dos.writeInt(inventory.length);
                        dos.write(inventory);
                        dos.writeInt(armor.length);
                        dos.write(armor);
                    }
                    dos.flush();
                    if(Bukkit.getOnlinePlayers().size() > 0) {
                        Player player = Bukkit.getOnlinePlayers().iterator().next();
                        player.sendPluginMessage(plugin, "BungeeCord", baos.toByteArray());
                    }
                    dos.close();
                } catch (IOException ex) {
                    Logger.getLogger(ToolsPlugin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void closeInventory(final DataInventory inv) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            public void run() {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);
                    dos.writeUTF("CloseInventory");
                    dos.writeUTF(inv.player.getName());
                    dos.writeUTF(inv.uuid.toString());
                    dos.flush();
                    if(Bukkit.getOnlinePlayers().size() > 0) {
                        Player player = Bukkit.getOnlinePlayers().iterator().next();
                        player.sendPluginMessage(plugin, "BungeeCord", baos.toByteArray());
                    }
                    dos.close();
                } catch (IOException ex) {
                    Logger.getLogger(ToolsPlugin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void openChest(Block block, Player player) {
        Chest chest = (Chest) block.getState();
        InventoryHolder ih = chest.getInventory().getHolder();
        if (ih instanceof DoubleChest) {
            DoubleChest doublechest = (DoubleChest) ih;
            player.openInventory(doublechest.getInventory());
        } else {
            player.openInventory(chest.getBlockInventory());
        }
    }

    private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public String generateRandomString(){
        StringBuffer randStr = new StringBuffer();
        for(int i=0; i<8; i++){
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    private int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }

}
