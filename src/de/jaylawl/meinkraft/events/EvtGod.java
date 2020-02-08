package de.jaylawl.meinkraft.events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EvtGod implements Listener {

    public EvtGod() {
    }

    @EventHandler
    public void event(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && event.getCause() != EntityDamageEvent.DamageCause.VOID) {
            if (
                    event.getEntity().hasMetadata("GodMode") &&
                    event.getEntity().getMetadata("GodMode").get(0).asBoolean()
            ) {
                event.setCancelled(true);
            }
        }
    }

}
