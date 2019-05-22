package com.phaseos.antilootsteal;

import com.phaseos.listener.GeneralListener;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Antilootsteal extends JavaPlugin {

    @Override
    public void onEnable() {
        File file = new File("plugins/customcells/config.yml");

        if (file.exists())
            reloadConfig();
        else
            saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new GeneralListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
