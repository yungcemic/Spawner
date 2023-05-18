package me.yungcemic.spawner.spawner;

import me.yungcemic.spawner.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public record Spawner(EntityType type, String displayName, List<String> lore, List<SpawnerMaterial> materials,
                      int xpToDrop) {

    public ItemStack getItemStack(int amount) {
        ItemStack itemStack = new ItemStack(Material.SPAWNER, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatUtil.colorize(displayName));
        itemMeta.setLore(ChatUtil.colorize(lore));
        BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
        BlockState blockState = blockStateMeta.getBlockState();
        CreatureSpawner creatureSpawner = (CreatureSpawner) blockState;
        creatureSpawner.setSpawnedType(this.type);
        blockStateMeta.setBlockState(blockState);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}