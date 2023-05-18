package me.yungcemic.spawner.command;

import me.yungcemic.spawner.spawner.Spawner;
import me.yungcemic.spawner.spawner.SpawnerManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Locale;

public final class SpawnerCommand implements CommandExecutor {

    private final SpawnerManager spawnerManager;

    public SpawnerCommand(SpawnerManager spawnerManager) {
        this.spawnerManager = spawnerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                return true;
                //TODO RELOAD
            }
            return true;
        }
        if (args.length >= 2) {
            if (sender instanceof Player p) {
                if (args[0].equalsIgnoreCase("give")) {
                    Player target = p;
                    int quantity = 1;
                    EntityType type = EntityType.valueOf(args[1].toUpperCase(Locale.ENGLISH));
                    if (args.length > 2) quantity = Integer.parseInt(args[2]);
                    if (args.length > 3) {
                        target = Bukkit.getPlayer(args[3]);
                        if (target == null) {
                            p.sendMessage("Target null");
                            return true;
                        }
                    }
                    Spawner spawner = spawnerManager.getSpawner(type).orElse(null);
                    if (spawner == null) {
                        p.sendMessage("Type is null");
                        return true;
                    }
                    target.getInventory().addItem(spawner.getItemStack(quantity));
                    target.sendMessage("You got the spawner");
                    if (target.equals(p)) {
                        p.sendMessage("You give the spawner");
                    }
                    return true;
                }
                p.sendMessage("Help message");
                return true;
            }
        }
        return false;
    }
}