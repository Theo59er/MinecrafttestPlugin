package example.firstplugin;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class FirstPlugin extends JavaPlugin implements CommandExecutor, @NotNull Listener {

    private final Random random = new Random();
    private final Map<Player, Long> doubleJumpCooldown = new HashMap<>();

    private final Map<Player, Boolean> doubleJumpAvailable = new HashMap<>();
    private static final long COOLDOWN_TIME = 500; // Zeit in Millisekunden


    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled!");
        getCommand("rtp").setExecutor(new RTPCommand());
        getCommand("stp").setExecutor(new STPCommand());
        getServer().getPluginManager().registerEvents(this, this);
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


    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
//adding text so github thinks im doing tuff
        if (event.isSneaking()) {
            if (!isOnCooldown(player)) {
                // Überprüfe, ob der Spieler im letzten Sprung war und noch einen Doppelsprung hat
                if (doubleJumpAvailable.containsKey(player) && doubleJumpAvailable.get(player)) {
                    // Spieler hat Doppelsprung verbraucht
                    doubleJumpAvailable.put(player, false);

                    // Führe den Doppelsprung durch (erhöhe die vertikale Geschwindigkeit)
                    player.setVelocity(player.getVelocity().add(player.getLocation().getDirection().multiply(0.5).setY(1)));

                    // Setze den Cooldown für den Doppelsprung
                    setCooldown(player);
                } else {
                    // Aktiviere Doppelsprung für den nächsten Sprung
                    doubleJumpAvailable.put(player, true);
                }
            } else {
                // Spieler ist noch im Cooldown, hier kannst du eine Nachricht ausgeben oder andere Maßnahmen ergreifen
                player.sendMessage("Du musst warten, bevor du wieder springen kannst.");
            }
        }
    }

    private boolean isOnCooldown(Player player) {
        if (doubleJumpCooldown.containsKey(player)) {
            long lastTime = doubleJumpCooldown.get(player);
            long currentTime = System.currentTimeMillis();
            return currentTime - lastTime < 2000; // Cooldown von 2000 Millisekunden (2 Sekunden)
        }
        return false;
    }

    private void setCooldown(Player player) {
        doubleJumpCooldown.put(player, System.currentTimeMillis());
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
