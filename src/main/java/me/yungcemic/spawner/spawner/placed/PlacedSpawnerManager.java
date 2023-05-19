package me.yungcemic.spawner.spawner.placed;

import java.util.*;

public final class PlacedSpawnerManager {

    private final Map<UUID, PlacedSpawner> spawnerMap;
    private final PlacedSpawnerSerializer serializer;

    public PlacedSpawnerManager(PlacedSpawnerSerializer serializer) {
        this.spawnerMap = new HashMap<>();
        this.serializer = serializer;
    }

    public void addSpawner(PlacedSpawner spawner) {
        spawnerMap.put(spawner.getUniqueId(), spawner);
    }

    public void removeSpawner(PlacedSpawner spawner) {
        spawnerMap.remove(spawner.getUniqueId(), spawner);
    }

    public void removeSpawner(UUID uniqueId) {
        spawnerMap.remove(uniqueId);
    }

    public Optional<PlacedSpawner> getSpawner(UUID uniqueId, boolean config) {
        Optional<PlacedSpawner> placedSpawner = Optional.ofNullable(spawnerMap.get(uniqueId));
        if (placedSpawner.isEmpty() && config) {
            placedSpawner = serializer.deserialize(uniqueId);
            placedSpawner.ifPresent(spawner -> spawnerMap.put(uniqueId, spawner));
        }
        return placedSpawner;
    }

    public Set<PlacedSpawner> getSpawners() {
        return new HashSet<>(spawnerMap.values());
    }
}