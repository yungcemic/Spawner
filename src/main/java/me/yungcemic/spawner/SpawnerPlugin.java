package me.yungcemic.spawner;

import me.yungcemic.spawner.command.SpawnerCommand;
import me.yungcemic.spawner.listener.SpawnerListener;
import me.yungcemic.spawner.spawner.SpawnerManager;

import me.yungcemic.spawner.spawner.placed.PlacedSpawnerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;;

public final class SpawnerPlugin extends JavaPlugin {

    private File dataFile;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        this.dataFile = new File(getDataFolder(), "data.yml");
        createDataFile();
        SpawnerManager spawnerManager = new SpawnerManager();
        PlacedSpawnerManager placedSpawnerManager = new PlacedSpawnerManager();
        getConfig().getConfigurationSection("spawner-list")
                .getKeys(false)
                .forEach(s -> spawnerManager.addSpawner(getConfig().getConfigurationSection("spawner-list." + s)));
        getCommand("spawner").setExecutor(new SpawnerCommand(spawnerManager));
        getServer().getPluginManager().registerEvents(new SpawnerListener(this, spawnerManager, placedSpawnerManager), this);
    }

    @Override
    public void onDisable() {

    }

    public void createDataFile() {
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

    public File getDataFile() {
        return dataFile;
    }
}