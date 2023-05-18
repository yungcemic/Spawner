package me.yungcemic.spawner.spawner;

import org.bukkit.Material;

import java.util.Random;

public record SpawnerMaterial(Material type, double chance, int minDrop, int maxDrop, double price) {

    public int drop(Random random) {
        if (chance == 100 || Math.random() * 100 < chance) {
            return random.nextInt(maxDrop - minDrop) + maxDrop;
        }
        return 0;
    }
}