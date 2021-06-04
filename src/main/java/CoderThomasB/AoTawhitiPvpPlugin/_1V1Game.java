package CoderThomasB.AoTawhitiPvpPlugin;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.time.Instant;
import java.util.Objects;

public class _1V1Game extends AdvancedGame implements Listener {
    public Player FirstPlayer;
    public Player SecondPlayer;

    public _1V1Game(AoTawhitiPvpPlugin Plugin) {
        super(Plugin);
    }

    @Override
    public void StartCountdown() {
        if (Owner.getServer().getOnlinePlayers().size() < 2) {
            throw new RuntimeException("There are not enough players to start a new game");
        }

        super.StartCountdown();
    }

    public static void SetupInventory(PlayerInventory TheInventory) {
        TheInventory.clear();
        TheInventory.setHelmet(new ItemStack(Material.NETHERITE_HELMET));
        TheInventory.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
        TheInventory.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        TheInventory.setBoots(new ItemStack(Material.NETHERITE_BOOTS));

        TheInventory.setItemInOffHand(new ItemStack(Material.SHIELD));
        TheInventory.setHeldItemSlot(0);
        TheInventory.setItem(0, new ItemStack(Material.COOKED_BEEF));
        Objects.requireNonNull(TheInventory.getItem(0)).setAmount(64);
        TheInventory.setItem(1, new ItemStack(Material.NETHERITE_SWORD));
        TheInventory.setItem(2, new ItemStack(Material.NETHERITE_AXE));
        TheInventory.setItem(3, new ItemStack(Material.BOW));
        TheInventory.setItem(4, new ItemStack(Material.ARROW));
        Objects.requireNonNull(TheInventory.getItem(4)).setAmount(64);
    }

    @Override
    public void StartNow() {
        super.StartNow();

        if (Owner.getServer().getOnlinePlayers().size() < 2) {
            throw new RuntimeException("There are not enough players to start a new game");
        }

        FirstPlayer = GetNextPlayer();
        Owner.LastPlayed.put(FirstPlayer, Instant.now());

        SecondPlayer = GetNextPlayer();
        Owner.LastPlayed.put(SecondPlayer, Instant.now());

        FirstPlayer.setHealth(Objects.requireNonNull(FirstPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        SecondPlayer.setHealth(Objects.requireNonNull(SecondPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());

        FirstPlayer.teleport(new Location(FirstPlayer.getWorld(), 0, 4, 0, -45, 0));
        SecondPlayer.teleport(new Location(FirstPlayer.getWorld(), 20, 4, 20, 135, 0));

        FirstPlayer.setFoodLevel(20);
        FirstPlayer.setSaturation(5);
        SecondPlayer.setFoodLevel(20);
        SecondPlayer.setSaturation(5);

        SetupInventory(FirstPlayer.getInventory());
        SetupInventory(SecondPlayer.getInventory());

        FirstPlayer.setGameMode(GameMode.ADVENTURE);
        SecondPlayer.setGameMode(GameMode.ADVENTURE);

        for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
            ThePlayer.sendTitle(" ", "§6§l%s §avs §6§l%s".formatted(FirstPlayer.getDisplayName(), SecondPlayer.getDisplayName()), 0, 50, 20);
        }

        isPlaying = true;
    }

    @Override
    public void StopWithoutMessage() {
        super.StopWithoutMessage();

        for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
            ThePlayer.setGameMode(GameMode.SPECTATOR);
            ThePlayer.getInventory().clear();
            Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> {
                ThePlayer.playSound(ThePlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 1, 1);
                ThePlayer.playSound(ThePlayer.getLocation(), "random.levelup", SoundCategory.AMBIENT, 1, 1);
            }, 2);
        }

        FirstPlayer.teleport(new Location(FirstPlayer.getWorld(), 0, 10, 0));
        SecondPlayer.teleport(new Location(FirstPlayer.getWorld(), 0, 10, 0));
    }

    @Override
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer() == FirstPlayer || event.getPlayer() == SecondPlayer) {
            StopWithoutMessage();

            for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
                ThePlayer.sendTitle(" ", "§6§l%s§a left :(".formatted(event.getPlayer().getDisplayName()), 0, 100, 20);
            }
        }
    }

    @Override
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() == FirstPlayer) {
            StopWithoutMessage();

            for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
                ThePlayer.sendTitle(" ", "§6§l%s §ahas won".formatted(SecondPlayer.getDisplayName()), 0, 100, 20);
            }
        }
        if (event.getEntity() == SecondPlayer) {
            StopWithoutMessage();

            for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
                ThePlayer.sendTitle(" ", "§6§l%s §ahas won!".formatted(FirstPlayer.getDisplayName()), 0, 100, 20);
            }
        }
    }
}
