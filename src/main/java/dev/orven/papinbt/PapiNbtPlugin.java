package dev.orven.papinbt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PapiNbtPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().severe("PlaceholderAPI not found. Disabling.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (!Bukkit.getPluginManager().isPluginEnabled("NBTAPI")) {
            getLogger().severe("NBTAPI (Item-NBT-API) is required. Install https://github.com/tr7zw/Item-NBT-API");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        NbtExpansion expansion = new NbtExpansion(this);
        if (!expansion.register()) {
            getLogger().severe("Failed to register PlaceholderAPI expansion.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
