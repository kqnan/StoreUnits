package me.kqn.storeunits.Config;

import me.kqn.storeunits.StoreUnits;
import me.kqn.storeunits.Utils.ExpParser;
import me.kqn.storeunits.Utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class UnitsConfig {
    private static List<String> permission;
    private static HashMap<Pair,String> permissions;
    private static List<String> msg_dialog;

    public static String getPermission(int unitID) {
        for (Pair pair : permissions.keySet()) {
            if(pair.p1<=unitID&&unitID<=pair.p2){
                return permissions.get(pair);
            }
        }
        return null;
    }

    public static double getMoney(int unitsID) {

         return ExpParser.parseMathExpression(money.replace("%unitID%",String.valueOf(unitsID)));

    }
    private static List<String> msg_noperm;
    private static List<String> msg_nomoney;

    public static List<String> getMsg_noperm(int unitID,String permission) {
        ArrayList<String> msg=new ArrayList<>();
        for (String s : msg_noperm) {
            msg.add(s.replace("%unitID%",String.valueOf(unitID)).replace("%permission%",permission));
        }
        return msg;
    }
    public static List<String> getMsg_MaxPage(){
        return Collections.singletonList("已达到解锁上限");
    }

    public static List<String> getMsg_dialog() {
        return msg_dialog;
    }

    public static List<String> getMsg_unlock(int unitID) {
        ArrayList<String> msg=new ArrayList<>();
        for (String s : msg_unlock) {
            msg.add(s.replace("%unitID%",String.valueOf(unitID)));
        }
        return msg;
    }

    private static List<String> msg_unlock;
    public static List<String> getMsg_nomoney(int unitID,double money) {
        ArrayList<String> msg=new ArrayList<>();
        for (String s : msg_nomoney) {
            msg.add(s.replace("%unitID%",String.valueOf(unitID)).replace("%money%",String.valueOf(money)));
        }
        return msg;
    }

    private static String money;
    private static YamlConfiguration file;
    public static void read(){
        StoreUnits.plugin.saveResource("UnitConfig.yml",false);
        file= YamlConfiguration.loadConfiguration(new File("plugins\\StoreUnits\\UnitConfig.yml"));
        permission=file.getStringList("permission");
        parsePermission();
        money=file.getString("money");
        msg_noperm=file.getStringList("msg_noperm");
        msg_nomoney=file.getStringList("msg_nomoney");
        msg_unlock=file.getStringList("msg_unlock");
        msg_dialog=file.getStringList("dialog");
    }
    private static void parsePermission(){
        permissions=new HashMap<>();
        for (String s : permission) {
            String perm=s.split(":")[1];
            String range=s.split(":")[0];
            int lo=Integer.parseInt(range.split("-")[0]);
            int hi=Integer.parseInt(range.split("-")[1]);
            Pair pair=new Pair(lo,hi);
            permissions.put(pair,perm);

        }
    }
    private static class  Pair{
        public int p1;
        public int p2;
        public Pair(int pa1,int pa2){
            p1=pa1;p2=pa2;
        }

        @Override
        public int hashCode() {
            return p1*7+p2*31;
        }
    }
}
