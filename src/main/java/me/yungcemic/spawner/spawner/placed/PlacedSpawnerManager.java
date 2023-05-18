package me.yungcemic.spawner.spawner.placed;

import java.util.*;

public final class PlacedSpawnerManager {

    private final Map<UUID, PlacedSpawner> spawnerMap;

    public PlacedSpawnerManager() {
        this.spawnerMap = new HashMap<>();
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

    public Optional<PlacedSpawner> getSpawner(UUID uniqueId) {
        return Optional.ofNullable(spawnerMap.get(uniqueId));
    }

    public Set<PlacedSpawner> getSpawners() {
        return new HashSet<>(spawnerMap.values());
    }
}