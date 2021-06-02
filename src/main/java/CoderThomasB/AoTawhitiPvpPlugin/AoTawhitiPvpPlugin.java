package CoderThomasB.AoTawhitiPvpPlugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class AoTawhitiPvpPlugin extends JavaPlugin implements Runnable {
	public HashMap<Player, Instant> LastPlayed = new HashMap<>();
	public Game NowGame;
	public GameCommandExecutor MyExecutor;
	public URI GithubVersionUrl;
	
	@Override
	public void onEnable() {
		MyExecutor = new GameCommandExecutor(this);
		Objects.requireNonNull(this.getCommand("startgame")).setExecutor(MyExecutor);
		Objects.requireNonNull(this.getCommand("stopgame")).setExecutor(MyExecutor);
		
		GithubVersionUrl = URI.create("https://raw.githubusercontent.com/CoderThomasB/AoTawhitiPvpPlugin/master/src/main/resources/Version.txt");
		
		try {
			CheckVersion();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Stops null pointer
		NowGame = new _1V1Game(this);

		ScheduleNextRun();
	}
	
	public void CheckVersion() throws ExecutionException, InterruptedException {
		getLogger().info("checking version of The AoTawhitiPvpPlugin");
		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(GithubVersionUrl)
				.build();
		var responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
		
		var response = responseFuture.get();
		
		if(response.body().equals(Objects.requireNonNull(getResource("Version.txt")).toString())){
			getLogger().info("The AoTawhitiPvpPlugin is up to date on version: " + Objects.requireNonNull(getResource("Version.txt")));
		} else {
			getLogger().warning("The AoTawhitiPvpPlugin is out of date!");
			getLogger().warning("Current Version: " + Objects.requireNonNull(getResource("Version.txt")));
			getLogger().warning("New Version" + response.body());
		}
	}
	
	public void StartNewGame() {
        if(NowGame.isPlaying) return;
        if(NowGame.isCountdown) return;
        try {
            NowGame.StartCountdown();
        } catch (Exception e){
            getServer().broadcastMessage(e.getMessage());
        }
	}
	
	@Deprecated
	@Override
	public void run(){
		StartNewGame();
		
		ScheduleNextRun();
	}
	
	public void ScheduleNextRun() {
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, this, 20);
	}
	
}
