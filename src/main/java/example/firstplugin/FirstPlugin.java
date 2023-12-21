package example.firstplugin;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class FirstPlugin extends JavaPlugin implements CommandExecutor, @NotNull Listener {

    private final Random random = new Random();
    private final Map<Player, Long> doubleJumpCooldown = new HashMap<>();

    private final Map<Player, Boolean> doubleJumpAvailable = new HashMap<>();
    private static final long COOLDOWN_TIME = 500; // Zeit in Millisekunden

    private boolean runTurtleCommandActivated = false;

    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled!");
        getCommand("rtp").setExecutor(new RTPCommand());
        getCommand("stp").setExecutor(new STPCommand());
        getCommand("runturtle").setExecutor(new RunTurtleCommand());
        getCommand("pspsps").setExecutor(new PspspsCommand());
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        if (message.equalsIgnoreCase("pspsps")) {
            final Player player = event.getPlayer();

            // Verschiebe die asynchrone Operation in den nächsten Tick
            Bukkit.getScheduler().runTaskLater(this, () -> catcall(player), 1L);

            event.setCancelled(true); // Verhindere, dass die Nachricht im Chat angezeigt wird
        } else if (message.equalsIgnoreCase("pups")) {
            final Player player  = event.getPlayer();
            // Verschiebe die asynchrone Operation in den nächsten Tick
            Bukkit.getScheduler().runTaskLater(this, () -> scareMobs(player), 1L);
            event.setCancelled(true); // Verhindere, dass die Nachricht im Chat angezeigt wird
        }
    }


    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");
    }

    private void scareMobs(Player player) {
        player.sendMessage("Du hast gepupst jetzt sind alle verwirrt und wissen nicht was sie machen sollen...");
        // Iteriere durch alle feindlichen Mobs im Umkreis von 10 Blöcken um den Spieler
        for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
            if (entity instanceof Mob) {
                // Lass den feindlichen Mob von dem Spieler wegrennen
                ((Mob) entity).setTarget(null);

                // Planen, dass der feindliche Mob nach 10 Sekunden wieder normal verhält
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (entity.isValid() && entity instanceof Mob) {
                            ((Mob) entity).setTarget(player);
                        }
                    }
                }.runTaskLater(this, 500L); // 200 Ticks entsprechen 10 Sekunden
            }
        }
    }

        private void catcall(Player player) {
        player.sendMessage("Ein Spuch denn nur wahre liebhaber kennen");
        // Iteriere durch alle Entitäten im Umkreis von 10 Blöcken um den Spieler
        for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
            // Überprüfe, ob die Entität eine Katze ist
            if (entity instanceof Cat) {
                Cat cat = (Cat) entity;
                // Lass die Katze zum Spieler laufen
                cat.setTarget(player);

                // Planen, dass die Katze nach 10 Sekunden aufhört, dem Spieler zu folgen
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (cat.isValid()) {
                            cat.setTarget(null);
                        }
                    }
                }.runTaskLater(this, 200L); // 200 Ticks entsprechen 10 Sekunden
            }
        }
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

    private class PspspsCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (cmd.getName().equalsIgnoreCase("pspsps")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    catcall(player);

                }
            }else{
                sender.sendMessage("Dieser Befehl kann nur von einem Spieler verwendet werden.");
            }
            return true;
        }
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


    private class RunTurtleCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (cmd.getName().equalsIgnoreCase("runturtle")) {
                runTurtleCommandActivated = !runTurtleCommandActivated;
                Player player = (Player) sender;
                player.sendMessage("Turtle speed effect " + (runTurtleCommandActivated ? "activated" : "deactivated"));
                return true;
            }
            return false;
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Überprüfe, ob der Befehl aktiviert ist
        if (runTurtleCommandActivated) {
            // Überprüfe, ob sich der Spieler in der Nähe einer Schildkröte befindet
            for (Entity entity : event.getPlayer().getNearbyEntities(5, 5, 5)) {
                if (entity instanceof Turtle) {
                    // Verleihe der Schildkröte den Geschwindigkeitseffekt
                    Turtle turtle = (Turtle) entity;
                    turtle.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1000));
                }
            }
        }
    }

}
