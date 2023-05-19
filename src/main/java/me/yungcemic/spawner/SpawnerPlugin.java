package me.yungcemic.spawner;

import me.yungcemic.spawner.command.SpawnerCommand;
import me.yungcemic.spawner.listener.SpawnerListener;
import me.yungcemic.spawner.spawner.SpawnerManager;
import me.yungcemic.spawner.spawner.placed.PlacedSpawnerManager;
import me.yungcemic.spawner.spawner.placed.PlacedSpawnerSerializer;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public final class SpawnerPlugin extends JavaPlugin {

    private File dataFile;
    private PlacedSpawnerSerializer placedSpawnerSerializer;
    private SpawnerManager spawnerManager;
    private PlacedSpawnerManager placedSpawnerManager;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        this.dataFile = new File(getDataFolder(), "data.yml");
        createDataFile();
        this.placedSpawnerSerializer = new PlacedSpawnerSerializer(dataFile);
        this.spawnerManager = new SpawnerManager();
        this.placedSpawnerManager = new PlacedSpawnerManager(placedSpawnerSerializer);
        getConfig().getConfigurationSection("spawner-list")
                .getKeys(false)
                .forEach(s -> spawnerManager.addSpawner(getConfig().getConfigurationSection("spawner-list." + s)));
        getCommand("spawner").setExecutor(new SpawnerCommand(spawnerManager));
        getServer().getPluginManager().registerEvents(new SpawnerListener(this, new Random(), spawnerManager, placedSpawnerManager, placedSpawnerSerializer), this);
    }

    @Override
    public void onDisable() {
        placedSpawnerManager.getSpawners().forEach(spawner -> placedSpawnerSerializer.serialize(spawner, false));
        try {
            placedSpawnerSerializer.getConfiguration().save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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