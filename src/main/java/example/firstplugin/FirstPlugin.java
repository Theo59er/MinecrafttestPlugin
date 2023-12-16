package example.firstplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class FirstPlugin extends JavaPlugin implements CommandExecutor {

    private final Random random = new Random();

    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled!");
        getCommand("rtp").setExecutor(new RTPCommand());
        getCommand("stp").setExecutor(new STPCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");
    }
    private void teleportPlayerRandomly(Player player) {
        int x = random.nextInt(200001) - 100000; // Bereich: -100000 bis 100000
        int z = random.nextInt(200001) - 100000; // Bereich: -100000 bis 100000

        int y = player.getWorld().getHighestBlockYAt(x, z);

        Location randomLocation = new Location(player.getWorld(), x + 0.5, y, z + 0.5);

        player.teleport(randomLocation);
    }

    private void teleportPlayerRandomlywish(Player player, int x, int z) {
        int y = player.getWorld().getHighestBlockYAt(x, z);

        Location randomLocation = new Location(player.getWorld(), x + 0.5, y + 1, z + 0.5);

        player.teleport(randomLocation);
    }


    private class RTPCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (cmd.getName().equalsIgnoreCase("rtp")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    teleportPlayerRandomly(player);
                } else {
                    sender.sendMessage("Dieser Befehl kann nur von einem Spieler verwendet werden.");
                }
                return true;
            }
            return false;
        }
    }

    private class STPCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (cmd.getName().equalsIgnoreCase("stp")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;

                    // Überprüfe, ob genügend Argumente übergeben wurden
                    if (args.length >= 2) {
                        try {
                            int x = Integer.parseInt(args[0]);
                            int z = Integer.parseInt(args[1]);

                            teleportPlayerRandomlywish(player, x, z);
                        } catch (NumberFormatException e) {
                            sender.sendMessage("Ungültige Koordinaten. Bitte gib gültige Zahlen ein.");
                        }
                    } else {
                        sender.sendMessage("Verwendung: /stp <x> <z>");
                    }
                } else {
                    sender.sendMessage("Dieser Befehl kann nur von einem Spieler verwendet werden.");
                }
                return true;
            }
            return false;
        }
    }
}
