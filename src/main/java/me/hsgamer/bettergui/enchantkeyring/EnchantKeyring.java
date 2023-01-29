package me.hsgamer.bettergui.enchantkeyring;

import me.hsgamer.bettergui.builder.ItemModifierBuilder;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;

public final class EnchantKeyring extends PluginAddon {
    @Override
    public void onEnable() {
        ItemModifierBuilder.INSTANCE.register(CustomEnchantmentModifier::new, "custom-enchantment", "custom-enchant", "enchantment-key", "enchant-key");
    }
}
