package me.hsgamer.bettergui.enchantkeyring;

import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.lib.core.bukkit.item.ItemMetaModifier;
import me.hsgamer.bettergui.lib.core.bukkit.utils.MessageUtils;
import me.hsgamer.bettergui.lib.core.common.CollectionUtils;
import me.hsgamer.bettergui.lib.core.common.Validate;
import me.hsgamer.bettergui.lib.core.common.interfaces.StringReplacer;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class CustomEnchantmentModifier extends ItemMetaModifier {
    private List<String> enchantmentList = Collections.emptyList();

    @Override
    public String getName() {
        return "custom-enchantment";
    }

    private Map<Enchantment, Integer> getParsed(UUID uuid, Collection<StringReplacer> stringReplacers) {
        Map<Enchantment, Integer> enchantments = new LinkedHashMap<>();
        for (String string : enchantmentList) {
            String[] split = StringReplacer.replace(string, uuid, stringReplacers).split(",", 2);
            Optional<Enchantment> enchantment = Optional.of(split[0].trim()).map(NamespacedKey::fromString).map(Enchantment::getByKey);
            int level = 1;
            if (split.length > 1) {
                String rawLevel = split[1].trim();
                Optional<BigDecimal> optional = Validate.getNumber(rawLevel);
                if (optional.isPresent()) {
                    level = optional.get().intValue();
                } else {
                    MessageUtils.sendMessage(uuid, MessageConfig.INVALID_NUMBER.getValue().replace("{input}", rawLevel));
                    continue;
                }
            }
            if (enchantment.isPresent()) {
                enchantments.put(enchantment.get(), level);
            } else {
                MessageUtils.sendMessage(uuid, MessageConfig.INVALID_ENCHANTMENT.getValue().replace("{input}", string));
            }
        }
        return enchantments;
    }

    @Override
    public ItemMeta modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
        Map<Enchantment, Integer> map = getParsed(uuid, stringReplacerMap.values());
        if (map instanceof EnchantmentStorageMeta) {
            map.forEach((enchant, level) -> ((EnchantmentStorageMeta) meta).addStoredEnchant(enchant, level, true));
        } else {
            map.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));
        }
        return meta;
    }

    @Override
    public void loadFromItemMeta(ItemMeta meta) {
        this.enchantmentList = meta.getEnchants().entrySet()
                .stream()
                .map(entry -> entry.getKey().getKey() + ", " + entry.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public boolean canLoadFromItemMeta(ItemMeta meta) {
        return meta.hasEnchants();
    }

    @Override
    public boolean compareWithItemMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
        Map<Enchantment, Integer> list1 = getParsed(uuid, stringReplacerMap.values());
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
