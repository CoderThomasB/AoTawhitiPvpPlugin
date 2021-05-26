package CoderThomasB.AoTawhitiPvpPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GameCommandExecutor implements CommandExecutor {
    public final AoTawhitiPvpPlugin Owner;

    public GameCommandExecutor(AoTawhitiPvpPlugin plugin){
        Owner = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
       try {
           switch (command.getName()) {
               //noinspection SpellCheckingInspection
               case "startgame" -> {
                   Owner.NowGame.Start();
                   return true;
               }
               //noinspection SpellCheckingInspection
               case "stopgame" -> {
                   Owner.NowGame.Stop();
                   return true;
               }
               default -> {
                   sender.sendMessage("Unknown command name" + command.getName());
                   return false;
               }
           }
       }catch (Exception E){
           sender.sendMessage("Error when executing command: " + E);
           return false;
       }
    }

}
