package CoderThomasB.AoTawhitiPvpPlugin;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.time.Instant;

public abstract class Game {
    public boolean isPlaying = false;
    public boolean isCountdown = false;
    public AoTawhitiPvpPlugin Owner;

    public Game(AoTawhitiPvpPlugin Plugin){
        Owner = Plugin;
    }

    public void StartCountdown(){
        if(isPlaying || isCountdown){
            throw new RuntimeException("Can not start countdown while another countdown or game is running");
        }

        isCountdown = true;

        Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> Owner.getServer().broadcastMessage("New Game starting in 30s"));
        Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> Owner.getServer().broadcastMessage("New Game starting in 20s"), 200);
        Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> Owner.getServer().broadcastMessage("New Game starting in 10s"), 400);
        Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> Owner.getServer().broadcastMessage("New Game starting in 9s"), 420);
        Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> Owner.getServer().broadcastMessage("New Game starting in 8s"), 440);
        Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> Owner.getServer().broadcastMessage("New Game starting in 7s"), 460);
        Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> Owner.getServer().broadcastMessage("New Game starting in 6s"), 480);
        Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> Owner.getServer().broadcastMessage("New Game starting in 5s"), 500);
        Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> Owner.getServer().broadcastMessage("New Game starting in 4s"), 520);
        Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> {
            Owner.getServer().broadcastMessage("New Game starting in 3s");
            for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
                    ThePlayer.playSound(ThePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.AMBIENT, 1, 1);
//                    ThePlayer.playSound(ThePlayer.getLocation(), "random.levelup", SoundCategory.AMBIENT, 1, 1);
            }
        }, 540);
        Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> {
            Owner.getServer().broadcastMessage("New Game starting in 2s");
            for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
                ThePlayer.playSound(ThePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.AMBIENT, 1, 1);
//                    ThePlayer.playSound(ThePlayer.getLocation(), "random.levelup", SoundCategory.AMBIENT, 1, 1);
            }
        }, 560);
        Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> {
            Owner.getServer().broadcastMessage("New Game starting in 1s");
            for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
                ThePlayer.playSound(ThePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.AMBIENT, 1, 1);
//                    ThePlayer.playSound(ThePlayer.getLocation(), "random.levelup", SoundCategory.AMBIENT, 1, 1);
            }
        }, 580);
        Owner.getServer().getScheduler().scheduleSyncDelayedTask(Owner, () -> {
            Owner.getServer().broadcastMessage("GO!");
            for (Player ThePlayer : Owner.getServer().getOnlinePlayers()) {
                ThePlayer.playSound(ThePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.AMBIENT, 1, 2);
//                    ThePlayer.playSound(ThePlayer.getLocation(), "random.levelup", SoundCategory.AMBIENT, 1, 1);
            }
            isCountdown = false;
            try {
                StartNow();
            } catch (Exception e){
                Owner.getServer().broadcastMessage(e.getMessage());
            }
        }, 600);
    }

    public void StartNow(){
        if(isPlaying || isCountdown){
            throw new RuntimeException("Can not start a game while another countdown or game is running");
        }
    }

    public Player GetNextPlayer(){
        Player NextPlayer = null;
        Instant TheDate = Instant.MAX;

        for (Player ThePlayer:Owner.getServer().getOnlinePlayers()) {
            if(!Owner.LastPlayed.containsKey(ThePlayer)){
                return ThePlayer;
            }

            if(Owner.LastPlayed.get(ThePlayer).isBefore(TheDate)){
                NextPlayer = ThePlayer;
                TheDate = Owner.LastPlayed.get(ThePlayer);
            }
        }

        return NextPlayer;
    }

    public abstract void Stop();

    public void StopWithoutMessage(){
        if(!isPlaying){
            throw new RuntimeException("can not stop game that is not in progress");
        }
        isPlaying = false;
    }
}
