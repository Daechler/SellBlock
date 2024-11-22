package net.daechler.sellBlock;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SellBlock extends JavaPlugin implements Listener {

    private static final Material SELL_BLOCK_MATERIAL = Material.BEDROCK;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("SellBlock plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SellBlock plugin disabled.");
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        // Check if the player is within 3 blocks of the sell block and looking at it
        if (isLookingAtSellBlock(player)) {
            event.setCancelled(true); // Cancel the item drop
            getLogger().info("Player " + player.getName() + " attempted to sell an item.");

            // Add a slight delay before executing the sell command
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.getInventory().getItemInMainHand() != null && !player.getInventory().getItemInMainHand().getType().isAir()) {
                        player.performCommand("sell hand 1");
                        player.sendMessage("Your item has been sold!");
                        getLogger().info("Player " + player.getName() + " successfully sold an item.");
                    } else {
                        player.sendMessage("You cannot sell air!");
                        getLogger().warning("Player " + player.getName() + " attempted to sell air.");
                    }
                }
            }.runTaskLater(this, 2L); // Delay of 2 ticks (0.1 seconds)
        }
    }

    /**
     * Checks if the player is within 3 blocks of the sell block and looking at it.
     *
     * @param player The player to check.
     * @return True if the player is looking at a sell block within 3 blocks, otherwise false.
     */
    private boolean isLookingAtSellBlock(Player player) {
        // Get the block the player is looking at within 3 blocks
        return player.getTargetBlockExact(3) != null &&
                player.getTargetBlockExact(3).getType() == SELL_BLOCK_MATERIAL;
    }
}
