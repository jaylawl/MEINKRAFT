package de.jaylawl.meinkraft.listener.bukkit;

import de.jaylawl.meinkraft.Meinkraft;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.jetbrains.annotations.NotNull;

public class GodModeListener implements Listener {

    public GodModeListener() {
    }

    //

    @EventHandler
    public void onEntityDamage(@NotNull EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.VOID) {
            if (Meinkraft.getDataCenter().isInGodMode(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(@NotNull FoodLevelChangeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (player.getFoodLevel() > event.getFoodLevel()) {
                if (Meinkraft.getDataCenter().isInGodMode(player.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
