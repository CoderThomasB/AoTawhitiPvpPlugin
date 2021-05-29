package CoderThomasB.AoTawhitiPvpPlugin;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

        FirstPlayer.setFoodLevel(20);
        FirstPlayer.setSaturation(5);
        SecondPlayer.setFoodLevel(20);
        SecondPlayer.setSaturation(5);

        FirstPlayer.teleport(new Location(FirstPlayer.getWorld(), 0, 4, 0, -45, 0));
        SecondPlayer.teleport(new Location(FirstPlayer.getWorld(), 20, 4, 20, 135, 0));

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
