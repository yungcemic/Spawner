package me.yungcemic.spawner.spawner.placed;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public final class PlacedSpawner {

    private final UUID uniqueId;
    private final EntityType type;
    private final Map<Material, Integer> materialStorage;
    private int xpStorage;
    private int stackSize;

    public PlacedSpawner(UUID uniqueId, EntityType type, Map<Material, Integer> materialStorage, int xpStorage, int stackSize) {
        this.uniqueId = uniqueId;
        this.type = type;
        this.materialStorage = materialStorage;
        this.xpStorage = xpStorage;
        this.stackSize = stackSize;
    }

    public PlacedSpawner(UUID uniqueId, EntityType type) {
        this(uniqueId, type, new EnumMap<>(Material.class), 0, 1);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public EntityType getType() {
        return type;
    }

    public Map<Material, Integer> getMaterialStorage() {
        return materialStorage;
    }

    public void clearStorage() {
        materialStorage.clear();
        xpStorage = 0;
    }

    public int getXpStorage() {
        return xpStorage;
    }

    public void setXpStorage(int xpStorage) {
        this.xpStorage = xpStorage;
    }

    public int getStackSize() {
        return stackSize;
    }

    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }
}