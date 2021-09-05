package me.hsgamer.bettergui.exampleaddon;

import me.hsgamer.bettergui.api.addon.BetterGUIAddon;

public final class ExampleAddon extends BetterGUIAddon {

    /**
     * Called when loading the addon
     *
     * @return whether the addon is loaded properly
     */
    @Override
    public boolean onLoad() {
        return true;
    }

    /**
     * Called when enabling the addon
     */
    @Override
    public void onEnable() {
        // Enable logic
    }

    /**
     * Called after all addons were loaded
     */
    @Override
    public void onPostEnable() {
        // Post Enable logic
    }

    /**
     * Called when disabling the addon
     */
    @Override
    public void onDisable() {
        // Disable logic
    }


    /**
     * Called when reloading
     */
    @Override
    public void onReload() {
        // Reload logic
    }
}
