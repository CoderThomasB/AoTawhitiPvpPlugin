package CoderThomasB.AoTawhitiPvpPlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public abstract class AdvancedGame extends Game implements Listener {
    public AdvancedGame(AoTawhitiPvpPlugin Plugin) {
        super(Plugin);
        Owner.getServer().getPluginManager().registerEvents(this, Owner);
    }

    @Override
    public void Stop() {
        StopWithoutMessage();

        for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
            ThePlayer.sendTitle(" ", "§aThe game was stopped by an admin.", 0, 100, 20);
        }
    }

    @EventHandler
    public abstract void onPlayerQuit(PlayerQuitEvent event);

    @EventHandler
    public abstract void onPlayerDeath(PlayerDeathEvent event);
}
