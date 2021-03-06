package me.hsgamer.bettergui.enchantkeyring;

import me.hsgamer.bettergui.api.addon.BetterGUIAddon;
import me.hsgamer.bettergui.builder.ItemModifierBuilder;

public final class EnchantKeyring extends BetterGUIAddon {
    @Override
    public boolean onLoad() {
        ItemModifierBuilder.INSTANCE.register(CustomEnchantmentModifier::new, "custom-enchantment", "custom-enchant");
        return true;
    }
}
