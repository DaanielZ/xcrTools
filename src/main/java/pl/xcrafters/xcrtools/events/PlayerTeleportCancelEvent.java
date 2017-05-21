package pl.xcrafters.xcrtools.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerTeleportCancelEvent extends PlayerEvent {

    public PlayerTeleportCancelEvent(Player player){
        super(player);
    }

    private static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

}
