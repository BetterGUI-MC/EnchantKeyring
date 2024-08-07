package me.hsgamer.bettergui.enchantkeyring;

import me.hsgamer.hscore.bukkit.item.modifier.ItemMetaComparator;
import me.hsgamer.hscore.bukkit.item.modifier.ItemMetaModifier;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class CustomEnchantmentModifier implements ItemMetaModifier, ItemMetaComparator {
    private List<String> enchantmentList = Collections.emptyList();

    private Map<Enchantment, Integer> getParsed(UUID uuid, StringReplacer stringReplacer) {
        Map<Enchantment, Integer> enchantments = new LinkedHashMap<>();
        for (String string : enchantmentList) {
            String replaced = stringReplacer.replaceOrOriginal(string, uuid);
            String[] split;
            if (replaced.indexOf(',') != -1) {
                split = replaced.split(",", 2);
            } else {
                split = replaced.split(" ", 2);
            }
            Optional<Enchantment> enchantment = Optional.of(split[0].trim()).map(NamespacedKey::fromString).map(Enchantment::getByKey);
            int level = 1;
            if (split.length > 1) {
                String rawLevel = split[1].trim();
                Optional<BigDecimal> optional = Validate.getNumber(rawLevel);
                if (optional.isPresent()) {
                    level = optional.get().intValue();
                } else {
                    continue;
                }
            }
            if (enchantment.isPresent()) {
                enchantments.put(enchantment.get(), level);
            }
        }
        return enchantments;
    }

    @Override
    public @NotNull ItemMeta modifyMeta(@NotNull ItemMeta meta, UUID uuid, @NotNull StringReplacer stringReplacer) {
        Map<Enchantment, Integer> map = getParsed(uuid, stringReplacer);
        if (map instanceof EnchantmentStorageMeta) {
            map.forEach((enchant, level) -> ((EnchantmentStorageMeta) meta).addStoredEnchant(enchant, level, true));
        } else {
            map.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));
        }
        return meta;
    }

    @Override
    public boolean loadFromItemMeta(ItemMeta meta) {
        if (!meta.hasEnchants()) {
            return false;
        }
        this.enchantmentList = meta.getEnchants().entrySet()
                .stream()
                .map(entry -> entry.getKey().getKey() + ", " + entry.getValue())
                .collect(Collectors.toList());
        return true;
    }

    @Override
    public boolean compare(ItemMeta meta, UUID uuid, @NotNull StringReplacer stringReplacer) {
        Map<Enchantment, Integer> list1 = getParsed(uuid, stringReplacer);
        Map<Enchantment, Integer> list2 = meta.getEnchants();
        if (list1.size() != list2.size()) {
            return false;
        }
        for (Map.Entry<Enchantment, Integer> entry : list1.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int lvl = entry.getValue();
            if (!list2.containsKey(enchantment) || list2.get(enchantment) != lvl) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object toObject() {
        return enchantmentList;
    }

    @Override
    public void loadFromObject(Object object) {
        this.enchantmentList = CollectionUtils.createStringListFromObject(object, true);
    }
}
