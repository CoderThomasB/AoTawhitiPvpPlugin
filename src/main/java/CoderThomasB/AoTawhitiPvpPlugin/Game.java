package CoderThomasB.AoTawhitiPvpPlugin;

import org.bukkit.entity.Player;

import java.time.Instant;

public abstract class Game {
    public boolean isPlaying = false;
    public final AoTawhitiPvpPlugin Owner;

    public Game(AoTawhitiPvpPlugin Plugin){
        Owner = Plugin;
    }

    public void Start(){
        if(isPlaying){
            throw new RuntimeException("Can not start a game while game is running");
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
