package CoderThomasB.AoTawhitiPvpPlugin;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Instant;

public class _1V1Game extends AdvancedGame implements Listener {
    public Player FirstPlayer;
    public Player SecondPlayer;

    public _1V1Game(AoTawhitiPvpPlugin Plugin) {
        super(Plugin);
        Owner.getServer().getPluginManager().registerEvents(this, Owner);
    }

    public void Start() {
        super.Start();

        if (Owner.getServer().getOnlinePlayers().size() < 2) {
            throw new RuntimeException("There are not enough players to start a new game");
        }

        FirstPlayer = GetNextPlayer();
        Owner.LastPlayed.put(FirstPlayer, Instant.now());

        SecondPlayer = GetNextPlayer();
        Owner.LastPlayed.put(SecondPlayer, Instant.now());

        FirstPlayer.setHealth(FirstPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        SecondPlayer.setHealth(SecondPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

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

    public void StopWithoutMessage() {
        super.StopWithoutMessage();

        for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
            ThePlayer.setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(event.getPlayer() == FirstPlayer || event.getPlayer() == SecondPlayer) {
            StopWithoutMessage();

            for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
                ThePlayer.sendTitle(" ", "§6§l%s §aleft :(".formatted(event.getPlayer().getDisplayName()), 0, 100, 20);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(event.getEntity() == FirstPlayer) {
            StopWithoutMessage();

            for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
                ThePlayer.sendTitle(" ", "§6§l%s §ahas won".formatted(SecondPlayer.getDisplayName()), 0, 100, 20);
            }
        }
        if(event.getEntity() == SecondPlayer) {
            StopWithoutMessage();

            for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
                ThePlayer.sendTitle(" ", "§6§l%s §ahas won!".formatted(FirstPlayer.getDisplayName()), 0, 100, 20);
            }
        }
    }
}
