package pl.xcrafters.xcrtools.data;

import org.bukkit.entity.Player;

import java.util.UUID;

public class OpenedInfo {

    public OpenedInfo(Player player, UUID uuid, DataManager.InventoryType type) {
        this.player = player;
        this.uuid = uuid;
        this.type = type;
    }

    private Player player;
    private UUID uuid;
    private DataManager.InventoryType type;

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public DataManager.InventoryType getInventoryType() {
        return this.type;
    }

}
