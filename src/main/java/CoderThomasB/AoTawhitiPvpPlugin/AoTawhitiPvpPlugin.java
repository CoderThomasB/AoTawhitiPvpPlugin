package CoderThomasB.AoTawhitiPvpPlugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.HashMap;
import java.util.Random;

public class AoTawhitiPvpPlugin extends JavaPlugin implements Runnable {
	public HashMap<Player, Instant> LastPlayed = new HashMap<>();
	public Game NowGame;
	public GameCommandExecutor MyExecutor;
	
	@Override
	public void onEnable() {
		MyExecutor = new GameCommandExecutor(this);
		this.getCommand("startgame").setExecutor(MyExecutor);
		this.getCommand("stopgame").setExecutor(MyExecutor);
		
		// Stops null pointer
		NowGame = new _1V1Game(this);
		
		ScheduleNextRun();
	}
	
	public void StartNewGame() {
		if (NowGame.isPlaying) return;
		try {
			if (new Random().nextFloat() > 1 / 4) {
				try {
					NowGame = new FreeForAll(this);
					NowGame.Start();
					return;
				} catch (Exception ignored) {
				}
			}
			
			NowGame = new _1V1Game(this);
			NowGame.Start();
			return;
			
		} catch (Exception e) {
			for (Player ThePlayer : getServer().getOnlinePlayers()) {
				ThePlayer.sendMessage(e.getMessage());
			}
			return;
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
