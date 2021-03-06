package CoderThomasB.AoTawhitiPvpPlugin;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.LinkedList;
import java.util.Objects;

@Deprecated
public class FreeForAll extends AdvancedGame {
    public final LinkedList<Player> PlayersInGame = new LinkedList<>();

    public FreeForAll(AoTawhitiPvpPlugin Plugin) {
        super(Plugin);
    }

    @Override
    public void StartNow() {
        super.StartNow();

        if (Owner.getServer().getOnlinePlayers().size() < 3) {
            throw new RuntimeException("There are not enough players to start a new game");
        }

        for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
            Objects.requireNonNull(ThePlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
            ThePlayer.setFoodLevel(20);
            ThePlayer.setSaturation(5);
            ThePlayer.setGameMode(GameMode.ADVENTURE);
            PlayersInGame.add(ThePlayer);
        }

        for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
            ThePlayer.sendTitle(" ", "§aFree for all!", 0, 50, 20);
        }

        isPlaying = true;
    }

    public void StopPlayer(Player ThePlayer) {
        ThePlayer.setGameMode(GameMode.SPECTATOR);
        PlayersInGame.remove(ThePlayer);
    }

    @Override
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        StopPlayer(event.getPlayer());
        for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
            ThePlayer.sendTitle(" ", "§6§l%s§a left :(".formatted(event.getPlayer().getDisplayName()), 0, 100, 20);
        }
    }

    @Override
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        StopPlayer(event.getEntity());
        if(PlayersInGame.size() == 1){
            for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
                ThePlayer.sendTitle(" ", "§6§l%s §ahas won".formatted(PlayersInGame.getFirst()), 0, 100, 20);
            }
        }
    }
}
