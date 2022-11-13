package me.kqn.storeunits.Config;

import me.kqn.storeunits.StoreUnits;
import me.kqn.storeunits.Utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MessageConfig {
    public static String cannot_unlock;
    public static String unlock;
    private static YamlConfiguration file;
    private static List<String> dialog;

    public static List<String> getDialog() {
        ArrayList<String> msg=new ArrayList<>();
        for (String s : dialog) {
            msg.add(Utils.pareseColor(s));
        }
        return msg;
    }

    public static  void read(){
        try {
            StoreUnits.plugin.saveResource("Message.yml",false);
            file= YamlConfiguration.loadConfiguration(new File("plugins\\StoreUnits\\Message.yml"));
            cannot_unlock=file.getString("cannot_unlock");
            unlock=file.getString("unlock");
            dialog=file.getStringList("dialog");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
