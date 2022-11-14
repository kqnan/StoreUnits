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

    public static int getDefault_unlock() {
        return default_unlock;
    }

    public static int getUpgrade_unlock() {
        return upgrade_unlock;
    }

    private static String msg_upgrade;
    private static YamlConfiguration file;
    private static int default_unlock;
    private static  int upgrade_unlock;

    public static String getMsg_upgrade() {
        return msg_upgrade;
    }

    public static void read(){
        try {
            StoreUnits.plugin.saveResource("UpgradeConfig.yml",false);
            file=YamlConfiguration.loadConfiguration(new File("plugins\\StoreUnits\\UpgradeConfig.yml"));
            money_upgrade=file.getString("money_upgrade");
            msg_nomoney=file.getStringList("msg_nomoney");
            default_unlock=Integer.parseInt(file.getString("default-unlock"));
            upgrade_unlock=Integer.parseInt(file.getString("upgrade-unlock"));
            msg_upgrade=file.getString("upgrade");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
