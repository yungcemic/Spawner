package me.yungcemic.spawner.spawner;

import me.yungcemic.spawner.util.ChatUtil;

import me.yungcemic.spawner.util.SpawnerUtil;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public record Spawner(EntityType type, String displayName, List<String> lore, List<SpawnerMaterial> materials,
                      int xpToDrop) {

    public ItemStack getItemStack(int amount) {
        ItemStack itemStack = new ItemStack(Material.SPAWNER, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatUtil.colorize(displayName));
        itemMeta.setLore(ChatUtil.colorize(lore));
        itemStack.setItemMeta(itemMeta);
        SpawnerUtil.setType(itemStack, this.type);
        return itemStack;
    }
}