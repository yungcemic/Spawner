package me.yungcemic.spawner.listener;

import me.yungcemic.spawner.SpawnerPlugin;
import me.yungcemic.spawner.spawner.Spawner;
import me.yungcemic.spawner.spawner.SpawnerManager;
import me.yungcemic.spawner.spawner.placed.PlacedSpawner;
import me.yungcemic.spawner.spawner.placed.PlacedSpawnerManager;
import me.yungcemic.spawner.spawner.placed.PlacedSpawnerSerializer;
import me.yungcemic.spawner.util.SpawnerUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static me.yungcemic.spawner.util.SpawnerUtil.*;

public final class SpawnerListener implements Listener {

    private final SpawnerPlugin plugin;
    private final Random random;
    private final SpawnerManager spawnerManager;
    private final PlacedSpawnerManager placedSpawnerManager;
    private final PlacedSpawnerSerializer placedSpawnerSerializer;

    public SpawnerListener(SpawnerPlugin plugin, Random random, SpawnerManager spawnerManager, PlacedSpawnerManager placedSpawnerManager, PlacedSpawnerSerializer placedSpawnerSerializer) {
        this.plugin = plugin;
        this.random = random;
        this.spawnerManager = spawnerManager;
        this.placedSpawnerManager = placedSpawnerManager;
        this.placedSpawnerSerializer = placedSpawnerSerializer;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.getItemInHand().getType().equals(Material.SPAWNER)) {
            Block block = e.getBlock();
            CreatureSpawner spawner = getSpawner(block);
            EntityType type = getType(e.getItemInHand());
            spawner.setSpawnedType(type);
            PersistentDataContainer container = spawner.getPersistentDataContainer();
            UUID uniqueId = UUID.randomUUID();
            container.set(key(plugin), PersistentDataType.STRING, uniqueId.toString());
            spawner.update();
            PlacedSpawner placedSpawner = new PlacedSpawner(uniqueId, type);
            placedSpawnerManager.addSpawner(placedSpawner);
            placedSpawnerSerializer.serialize(placedSpawner, true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (block.getType().equals(Material.SPAWNER)) {
            Location location = e.getBlock().getLocation();
            CreatureSpawner creatureSpawner = getSpawner(block);
            PersistentDataContainer container = creatureSpawner.getPersistentDataContainer();
            NamespacedKey key = key(plugin);
            if (container.has(key, PersistentDataType.STRING)) {
                e.setCancelled(true);
                block.setType(Material.AIR);
                UUID spawnerId = UUID.fromString(container.get(key, PersistentDataType.STRING));
                placedSpawnerManager.getSpawner(spawnerId, true).ifPresent(spawner -> {
                    spawner.getMaterialStorage().forEach((material, count) -> location.getWorld().dropItemNaturally(location, new ItemStack(material, count)));
                    ExperienceOrb exp = (ExperienceOrb) location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
                    exp.setExperience(spawner.getXpStorage());
                    placedSpawnerManager.removeSpawner(spawner);
                    placedSpawnerSerializer.delete(spawner.getUniqueId());
                });
                spawnerManager.getSpawner(creatureSpawner.getSpawnedType()).ifPresent(spawner -> {
                    ItemStack drop = spawner.getItemStack(1);
                    block.getLocation().getWorld().dropItem(block.getLocation(), drop);
                });
            }
        }
    }

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent e) {
        PersistentDataContainer container = e.getSpawner().getPersistentDataContainer();
        NamespacedKey key = SpawnerUtil.key(plugin);
        if (container.has(key, PersistentDataType.STRING)) {
            Bukkit.broadcastMessage("#1");
            CreatureSpawner creatureSpawner = e.getSpawner();
            creatureSpawner.setDelay(-1);
            creatureSpawner.update();
            e.setCancelled(true);
            UUID uniqueId = UUID.fromString(container.get(key, PersistentDataType.STRING));
            placedSpawnerManager.getSpawner(uniqueId, true).ifPresent(spawner -> {
                Bukkit.broadcastMessage("£");
                Optional<Spawner> type = spawnerManager.getSpawner(spawner.getType());
                Bukkit.broadcastMessage(type.get().toString());
                type.ifPresent(spawnerType -> spawnerType.materials().forEach(spawnerMaterial -> {
                    Bukkit.broadcastMessage("#2");
                    int result = spawnerMaterial.drop(this.random);
                    int storage = spawner.getMaterialStorage().getOrDefault(spawnerMaterial.type(), 0);
                    spawner.getMaterialStorage().put(spawnerMaterial.type(), storage + result);
                    spawner.setXpStorage(spawner.getXpStorage() + spawnerType.xpToDrop());
                }));
            });
        }
    }
}