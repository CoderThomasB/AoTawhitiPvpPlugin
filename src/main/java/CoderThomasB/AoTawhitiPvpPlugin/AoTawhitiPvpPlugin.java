package CoderThomasB.AoTawhitiPvpPlugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class AoTawhitiPvpPlugin extends JavaPlugin implements Runnable {
    public HashMap<Player, Instant> LastPlayed = new HashMap<>();
    public Game NowGame;
    public GameCommandExecutor MyExecutor;

    @Override
    public void onEnable() {
        MyExecutor = new GameCommandExecutor(this);
        Objects.requireNonNull(this.getCommand("startgame")).setExecutor(MyExecutor);
        Objects.requireNonNull(this.getCommand("stopgame")).setExecutor(MyExecutor);

        NowGame = new _1V1Game(this);
        ScheduleNextRun();


    }

    public void StartNewGame(){
        if(NowGame.isPlaying) return;
        if(NowGame.isCountdown) return;
        try {
            NowGame.StartCountdown();
        } catch (Exception e){
            getServer().broadcastMessage(e.getMessage());
        }
    }

    @Override
    public void run() {
        StartNewGame();

        ScheduleNextRun();
    }

    public void ScheduleNextRun() {
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, this, 20);
    }

}
