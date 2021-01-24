package de.jaylawl.meinkraft.listeners;

import de.jaylawl.meinkraft.util.DataCenter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.jetbrains.annotations.NotNull;

public class GodListener implements Listener {

    private final DataCenter dataCenter;

    public GodListener(@NotNull DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    //

    @EventHandler
    public void damageEvent(@NotNull EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.VOID) {
            if (this.dataCenter.isInGodMode(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void hungerEvent(@NotNull FoodLevelChangeEvent event) {
        if (this.dataCenter.isInGodMode(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }

}
