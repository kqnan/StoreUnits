package me.kqn.storeunits.Config;

import me.kqn.storeunits.StoreUnits;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpgradeConfig {
    public static String getMoney_upgrade(int unitID,int level) {
        return money_upgrade.replace("%unitID%",String.valueOf(unitID)).replace("%unitLevel%",String.valueOf(level));
    }

    public static List<String> getMsg_nomoney() {
        return msg_nomoney;
    }

    private static String money_upgrade;
    private static List<String> msg_nomoney;
    private static YamlConfiguration file;
    public static void read(){
        try {
            StoreUnits.plugin.saveResource("UpgradeConfig.yml",false);
            file=YamlConfiguration.loadConfiguration(new File("plugins\\StoreUnits\\UpgradeConfig.yml"));
            money_upgrade=file.getString("money_upgrade");
            msg_nomoney=file.getStringList("msg_nomoney");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
