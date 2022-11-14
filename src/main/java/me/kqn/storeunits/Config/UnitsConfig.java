package me.kqn.storeunits.Config;

import me.kqn.storeunits.StoreUnits;
import me.kqn.storeunits.Utils.ExpParser;
import me.kqn.storeunits.Utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UnitsConfig {
    private static String permission;

    public static String getPermission() {
        return permission;
    }

    public static double getMoney(int unitsID) {

         return ExpParser.parseMathExpression(money.replace("%unitID%",String.valueOf(unitsID)));

    }
    private static List<String> msg_noperm;
    private static List<String> msg_nomoney;

    public static List<String> getMsg_noperm(int unitID) {
        ArrayList<String> msg=new ArrayList<>();
        for (String s : msg_noperm) {
            msg.add(s.replace("%unitID%",String.valueOf(unitID)));
        }
        return msg;
    }
    public static List<String> getMsg_MaxPage(){
        return Collections.singletonList("已达到解锁上限");
    }
    public static List<String> getMsg_unlock(int unitID) {
        ArrayList<String> msg=new ArrayList<>();
        for (String s : msg_unlock) {
            msg.add(s.replace("%unitID%",String.valueOf(unitID)));
        }
        return msg;
    }

    private static List<String> msg_unlock;
    public static List<String> getMsg_nomoney(int unitID) {
        ArrayList<String> msg=new ArrayList<>();
        for (String s : msg_nomoney) {
            msg.add(s.replace("%unitID%",String.valueOf(unitID)));
        }
        return msg;
    }

    private static String money;
    private static YamlConfiguration file;
    public static void read(){
        StoreUnits.plugin.saveResource("UnitConfig.yml",false);
        file= YamlConfiguration.loadConfiguration(new File("plugins\\StoreUnits\\UnitConfig.yml"));
        permission=file.getString("permission");
        money=file.getString("money");
        msg_noperm=file.getStringList("msg_noperm");
        msg_nomoney=file.getStringList("msg_nomoney");
        msg_unlock=file.getStringList("msg_unlock");
    }

}
