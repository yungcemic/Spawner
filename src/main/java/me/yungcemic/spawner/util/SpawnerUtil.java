package me.yungcemic.spawner.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpawnerUtil {

    private SpawnerUtil() {

    }

    public static NamespacedKey key(JavaPlugin plugin) {
        return new NamespacedKey(plugin, "spawner-uuid");
    }

    public static CreatureSpawner getSpawner(Block block) {
        if (!block.getType().equals(Material.SPAWNER)) throw new IllegalArgumentException();
        return (CreatureSpawner) block.getState();
    }

    public static CreatureSpawner getSpawner(ItemStack item) {
        if (!item.getType().equals(Material.SPAWNER)) throw new IllegalArgumentException();
        BlockStateMeta blockStateMeta = (BlockStateMeta) item.getItemMeta();
        return (CreatureSpawner) blockStateMeta.getBlockState();
    }

    public static EntityType getType(Block block) {
        if (!block.getType().equals(Material.SPAWNER)) throw new IllegalArgumentException();
        CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
        return creatureSpawner.getSpawnedType();
    }

    public static EntityType getType(ItemStack item) {
        if (!item.getType().equals(Material.SPAWNER)) throw new IllegalArgumentException();
        BlockStateMeta blockStateMeta = (BlockStateMeta) item.getItemMeta();
        CreatureSpawner creatureSpawner = (CreatureSpawner) blockStateMeta.getBlockState();
        return creatureSpawner.getSpawnedType();
    }

    public static EntityType setType(Block block, EntityType type) {
        if (!block.getType().equals(Material.SPAWNER)) throw new IllegalArgumentException();
        CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
        creatureSpawner.setSpawnedType(type);
        creatureSpawner.update();
        return type;
    }

    public static EntityType setType(ItemStack item, EntityType type) {
        if (!item.getType().equals(Material.SPAWNER)) throw new IllegalArgumentException();
        BlockStateMeta blockStateMeta = (BlockStateMeta) item.getItemMeta();
        CreatureSpawner creatureSpawner = (CreatureSpawner) blockStateMeta.getBlockState();
        creatureSpawner.setSpawnedType(type);
        creatureSpawner.update();
        return type;
    }

    public static PersistentDataContainer getContainer(Block block) {
        if (!block.getType().equals(Material.SPAWNER)) throw new IllegalArgumentException();
        CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
        return creatureSpawner.getPersistentDataContainer();
    }

    public static PersistentDataContainer getContainer(ItemStack item) {
        if (!item.getType().equals(Material.SPAWNER)) throw new IllegalArgumentException();
        BlockStateMeta blockStateMeta = (BlockStateMeta) item.getItemMeta();
        CreatureSpawner creatureSpawner = (CreatureSpawner) blockStateMeta.getBlockState();
        return creatureSpawner.getPersistentDataContainer();
    }
}