package me.kqn.storeunits.Config;

import me.kqn.storeunits.StoreUnits;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageConfig {
    public static String cannot_unlock;
    public static String unlock;
    private static YamlConfiguration file;
    public static  void read(){
        try {
            StoreUnits.plugin.saveResource("Message.yml",false);
            file= YamlConfiguration.loadConfiguration(new File("plugins\\storeunits\\Message.yml"));
            cannot_unlock=file.getString("cannot_unlock");
            unlock=file.getString("unlock");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
