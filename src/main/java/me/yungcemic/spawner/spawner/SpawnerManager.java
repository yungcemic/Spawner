package me.yungcemic.spawner.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.lang.module.Configuration;
import java.util.*;

public final class SpawnerManager {

    private final Map<EntityType, Spawner> spawnerMap;

    public SpawnerManager() {
        this.spawnerMap = new EnumMap<>(EntityType.class);
    }

    public void addSpawner(ConfigurationSection section) {
        final EntityType type = EntityType.valueOf(section.getName().toUpperCase(Locale.ENGLISH));
        final String displayName = section.getString("displayName");
        final List<String> lore = section.getStringList("lore");
        final List<SpawnerMaterial> materialList = new ArrayList<>();
        final int xpDrop = section.getInt("xpDrop");
        section.getConfigurationSection("material-list").getKeys(false).forEach(s -> {
            Material material = Material.matchMaterial(section.getString("material-list." + s).toUpperCase(Locale.ENGLISH));
            if (material == null) return;
            ConfigurationSection itemSection = section.getConfigurationSection("material-list." + s);
            double chance = itemSection.getDouble("chance");
            int minDrop = itemSection.getInt("minDrop");
            int maxDrop = itemSection.getInt("maxDrop");
            double price = itemSection.getDouble("price");
            materialList.add(new SpawnerMaterial(material, chance, minDrop, maxDrop, price));
        });
        Spawner spawner = new Spawner(type, displayName, lore, materialList, xpDrop);
        spawnerMap.put(type, spawner);
    }

    public void addSpawner(Spawner spawner) {
        spawnerMap.put(spawner.type(), spawner);
    }

    public void removeSpawner(Spawner spawner) {
        spawnerMap.remove(spawner.type(), spawner);
    }

    public void removeSpawner(EntityType type) {
        spawnerMap.remove(type);
    }

    public Optional<Spawner> getSpawner(EntityType type) {
        return Optional.ofNullable(spawnerMap.get(type));
    }

    public Set<Spawner> getSpawners() {
        return new HashSet<>(spawnerMap.values());
    }
}
