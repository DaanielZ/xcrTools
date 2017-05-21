package pl.xcrafters.xcrtools.listeners;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.xcrafters.xcrtools.ToolsPlugin;
import pl.xcrafters.xcrtools.data.DataManager;
import pl.xcrafters.xcrtools.data.OpenedInfo;

public class InventoryListener implements Listener {

    ToolsPlugin plugin;

    public InventoryListener(ToolsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inv = event.getClickedInventory();

        if((event.getClick().equals(ClickType.UNKNOWN) || event.getClick().equals(ClickType.MIDDLE)) && event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE) && !event.getWhoClicked().hasPermission("tools.items.without-lore")){
            ItemStack item = event.getCurrentItem();
            if (item != null && item.getItemMeta() != null) {
                item.setAmount(item.getType().getMaxStackSize());
                ItemMeta meta = item.getItemMeta();
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                meta.setLore(Arrays.asList(plugin.color("&6[ADMIN]"), plugin.color("&6Nick : &7&o" + event.getWhoClicked().getName()), plugin.color("&6Data: &7&o" + dt.format(date)), plugin.color("&6Item: &7&o" + item.getType().name() + ":" + item.getData().getData() + " x " + item.getAmount())));
                item.setItemMeta(meta);
                event.setCursor(item);
            }
        }

        if(!updateInventory(player, inv, event.getView())) {
            event.setCancelled(true);
            event.setResult(Result.DENY);
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inv = event.getInventory();

        if(!updateInventory(player, inv, event.getView())) {
            event.setCancelled(true);
            event.setResult(Result.DENY);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        OpenedInfo info = plugin.dataManager.opened.remove(player.getUniqueId());
        if(info != null) {
            plugin.redisManager.closeInventory(info);
        }
    }
    
    @EventHandler
    public void onInventoryCreative(InventoryClickEvent event){
        if (event.getClick().isCreativeAction() && event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE) && !event.getWhoClicked().hasPermission("tools.items.without-lore")) {
            ItemStack item = event.getCursor();
            if (item != null && item.getItemMeta() != null) {
                ItemMeta meta = item.getItemMeta();
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                meta.setLore(Arrays.asList(plugin.color("&6[ADMIN]"), plugin.color("&6Nick : &7&o" + event.getWhoClicked().getName()), plugin.color("&6Data: &7&o" + dt.format(date)), plugin.color("&6Item: &7&o" + item.getType().name() + ":" + item.getData().getData() + " x " + item.getAmount())));
                item.setItemMeta(meta);
                event.setCursor(item);
            }
        }
    }

    private boolean updateInventory(final Player player, final Inventory inv, final InventoryView view) {
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            public void run() {
                if(inv == null) {
                    return;
                }

                final byte[] inventory = inv.getType() == InventoryType.ENDER_CHEST ? ToolsPlugin.serializeItemStacks(player.getEnderChest().getContents()) : ToolsPlugin.serializeItemStacks(player.getInventory().getContents());
                final DataManager.InventoryType type = inv.getType() == InventoryType.ENDER_CHEST ? DataManager.InventoryType.ENDERCHEST : DataManager.InventoryType.INVENTORY;

                Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                    public void run() {
                        plugin.redisManager.updateInventory(player.getUniqueId(), type, inventory);
                    }
                });

                for (Player online : Bukkit.getOnlinePlayers()) {
                    OpenedInfo info = plugin.dataManager.opened.get(online.getUniqueId());

                    if (!player.getUniqueId().equals(online.getUniqueId()) && info != null && info.getUUID().equals(player.getUniqueId()) && info.getInventoryType() == type) {
                        plugin.updateInventory(online, inventory);
                    }
                }
            }
        }, 1L);

        final OpenedInfo info = plugin.dataManager.opened.get(player.getUniqueId());

        if(info != null && inv != null && inv.equals(view.getTopInventory())) {
            if(!player.hasPermission("tools." + info.getInventoryType().name().toLowerCase() + ".edit")) {
                return false;
            }

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                public void run() {
                    final byte[] inventory = ToolsPlugin.serializeItemStacks(inv.getContents());

                    Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                        public void run() {
                            plugin.redisManager.updateInventory(info.getUUID(), info.getInventoryType(), inventory);
                        }
                    });

                    for (Player online : Bukkit.getOnlinePlayers()) {
                        OpenedInfo info1 = plugin.dataManager.opened.get(online.getUniqueId());

                        if (!player.getUniqueId().equals(online.getUniqueId()) && info1 != null && info1.getUUID().equals(info.getUUID()) && info.getInventoryType() == info1.getInventoryType()) {
                            plugin.updateInventory(online, inventory);
                        }
                    }

                    Player owner = Bukkit.getPlayer(info.getUUID());

                    if (owner != null && !owner.getUniqueId().equals(player.getUniqueId())) {
                        if (info.getInventoryType() == DataManager.InventoryType.ENDERCHEST) {
                            owner.getEnderChest().setContents(ToolsPlugin.deserializeItemStacks(inventory));
                        } else {
                            owner.getInventory().setContents(ToolsPlugin.deserializeItemStacks(inventory));
                        }
                    }
                }
            }, 1L);
        }

        return true;
    }

}
