package asahi2986github.com.afk;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AFKPlugin extends JavaPlugin implements CommandExecutor {

    private Map<UUID, Long> afkPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("AFKPlugin has been enabled.");
        getCommand("afk").setExecutor(this);
        getCommand("unafk").setExecutor(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("AFKPlugin has been disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("afk") && sender instanceof Player) {
            Player player = (Player) sender;

            if (!afkPlayers.containsKey(player.getUniqueId())) {
                afkPlayers.put(player.getUniqueId(), System.currentTimeMillis() + 60000); // 1 minute from now
                player.setInvulnerable(true);
                player.setWalkSpeed(0);
                player.sendMessage("You are now AFK.");
                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("unafk") && sender instanceof Player) {
            Player player = (Player) sender;

            if (afkPlayers.containsKey(player.getUniqueId())) {
                long unafkTime = afkPlayers.get(player.getUniqueId());
                if (System.currentTimeMillis() >= unafkTime) {
                    afkPlayers.remove(player.getUniqueId());
                    player.setInvulnerable(false);
                    player.setWalkSpeed(0.2f);
                    player.sendMessage("You are no longer AFK.");
                } else {
                    long remainingTime = (unafkTime - System.currentTimeMillis()) / 1000;
                    player.sendMessage("You can't un-AFK yet. Please wait " + remainingTime + " seconds.");
                }
                return true;
            }
        }
        return false;
    }
}
