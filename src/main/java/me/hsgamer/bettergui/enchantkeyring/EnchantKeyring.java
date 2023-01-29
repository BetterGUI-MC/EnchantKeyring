package me.hsgamer.bettergui.enchantkeyring;

import me.hsgamer.bettergui.builder.ItemModifierBuilder;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;
import me.hsgamer.hscore.common.Validate;

public final class EnchantKeyring extends PluginAddon {
    @Override
    public boolean onLoad() {
        Class<?> namespaceKeyClass;
        try {
            namespaceKeyClass = Class.forName("org.bukkit.NamespacedKey");
        } catch (ClassNotFoundException e) {
            return false;
        }

        return Validate.isMethodLoaded("org.bukkit.enchantments.Enchantment", "getByKey", namespaceKeyClass);
    }

    @Override
    public void onEnable() {
        ItemModifierBuilder.INSTANCE.register(CustomEnchantmentModifier::new, "custom-enchantment", "custom-enchant", "enchantment-key", "enchant-key");
    }
}
