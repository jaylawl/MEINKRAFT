package de.jaylawl.meinkraft.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class GodListener implements Listener {

    public GodListener() {
    }

    @EventHandler
    public void damageEvent(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && event.getCause() != EntityDamageEvent.DamageCause.VOID) {
            if (
                    event.getEntity().hasMetadata("GodMode") &&
                    event.getEntity().getMetadata("GodMode").get(0).asBoolean()
            ) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void hungerEvent(FoodLevelChangeEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            if (
                    event.getEntity().hasMetadata("GodMode") &&
                    event.getEntity().getMetadata("GodMode").get(0).asBoolean() &&
                    event.getFoodLevel() < ((Player) event.getEntity()).getFoodLevel()
            ) {
                event.setCancelled(true);
            }
        }
    }

}
