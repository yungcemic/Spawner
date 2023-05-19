package me.yungcemic.spawner.spawner.placed;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public final class PlacedSpawnerSerializer {

    private static final String PATH = "spawners";

    private final File dataFile;
    private final YamlConfiguration configuration;

    public PlacedSpawnerSerializer(File dataFile) {
        this.dataFile = dataFile;
        this.configuration = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void serialize(PlacedSpawner placedSpawner, boolean save) {
        String spawnerPath = PATH + "." + placedSpawner.getUniqueId().toString();
        configuration.set(spawnerPath + ".type", placedSpawner.getType().name());
        placedSpawner.getMaterialStorage().forEach((material, integer) -> configuration.set(spawnerPath + ".storage." + material.name(), integer));
        configuration.set(spawnerPath + ".storage.xp", placedSpawner.getXpStorage());
        configuration.set(spawnerPath + ".stack-size", placedSpawner.getStackSize());
        if (save) {
            try {
                configuration.save(dataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public PlacedSpawner deserialize(UUID uniqueId) {
        String spawnerPath = PATH + "." + uniqueId.toString();
        EntityType type = EntityType.valueOf(configuration.getString(spawnerPath + ".type"));
        Map<Material, Integer> storage = new EnumMap<>(Material.class);
        configuration.getConfigurationSection(spawnerPath + ".storage").getKeys(false).forEach(s -> {
            if (!s.equals("xp")) {
                Material material = Material.matchMaterial(s);
                Integer amount = configuration.getInt(spawnerPath + ".storage." + s);
                storage.put(material, amount);
            }
        });
        int xpStorage = configuration.getInt(spawnerPath + ".storage.xp");
        int stackSize = configuration.getInt(spawnerPath + ".stack-size");
        return new PlacedSpawner(uniqueId, type, storage, xpStorage, stackSize);
    }

    public void delete(UUID uniqueId) {
        String spawnerPath = PATH + "." + uniqueId.toString();
        configuration.set(spawnerPath, null);
        try {
            configuration.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}