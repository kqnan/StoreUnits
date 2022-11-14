package me.kqn.storeunits.Config;


import me.kqn.storeunits.StoreUnits;
import me.kqn.storeunits.Utils.Utils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InterfaceConfig {
    private static String mainui_title;
    private static Icon mainui_unit_icon;

    private static Icon unlock_icon_noperms;
    private static HashMap<String,Icon> mainui_unlock_icon;
    private static Icon mainui_nextpage;
    private static Icon mainui_prepage;
    private static String title;
    private static String setting_title;
    private static String name_format;
    private static String default_name;
    private static Icon setting_upgrade;

    public static Icon getSetting_upgrade(double money) {
        ArrayList<String> msg=new ArrayList<>();
        for (String s : setting_upgrade.lore) {
            msg.add(s.replace("%money%",String.valueOf(money)));
        }
        Icon icon=setting_upgrade.clone();
        icon.lore=msg;
        icon.name= icon.name.replace("%money%",String.valueOf(money));
        return icon;
    }

    public static String getSetting_title() {
        return setting_title;
    }

    public static Icon getUnlock_slot() {
        return unlock_slot;
    }

    private static Icon unlock_slot;


    private static YamlConfiguration file;
    public static void read(){
        StoreUnits.plugin.saveResource("InterfaceConfig.yml",false);
        file= YamlConfiguration.loadConfiguration(new File("plugins\\StoreUnits\\InterfaceConfig.yml"));
        mainui_title=file.getString("Default-MainUI-Title");
       // Msg.debug(file.getConfigurationSection("Default-MainUI-Unit-Icon"));
        mainui_unit_icon=readIcon(file.getConfigurationSection("Default-MainUI-Unit-Icon"));
        mainui_unlock_icon=new HashMap<>();
        ConfigurationSection unlockicons=file.getConfigurationSection("Default-MainUI-Unlock-Icon");
        for (String key : unlockicons.getKeys(false)) {
            if(unlockicons.get(key+".permission")==null)unlock_icon_noperms=readIcon(unlockicons.getConfigurationSection(key));
            else {
               mainui_unlock_icon.put(unlockicons.getString(key+".permission"),readIcon(unlockicons.getConfigurationSection(key)));
            }
        }
        setting_title=file.getString("Default-Setting-Title");
        mainui_nextpage=readIcon(file.getConfigurationSection("Default-MainUI-NextPage"));
        mainui_prepage=readIcon(file.getConfigurationSection("Default-MainUI-PrePage"));
        title=file.getString("Default-Title");
        name_format=file.getString("Default-Name-Format");
        default_name=file.getString("Default-Name");
        unlock_slot=readIcon(file.getConfigurationSection("Default-Unlock-Slot"));
        setting_upgrade=readIcon(file.getConfigurationSection("Default-Setting-Upgrade"))    ;
    }

    private static Icon readIcon(ConfigurationSection section){
        Icon icon=new Icon();
        icon.name=section.getString("name");
        icon.lore=section.getStringList("lore");
        icon.custommodeldata=Integer.parseInt(section.getString("custommodeldata"));
        icon.material=Material.valueOf(section.getString("material").toUpperCase());
        return icon;
    }
    public static String getMainui_title() {
        return mainui_title;
    }

    public static Icon getMainui_unit_icon() {

        return mainui_unit_icon;
    }

    public static Icon getMainui_unlock_icon(OfflinePlayer offlinePlayer,double money_need,String perm_need) {
        Icon icon=unlock_icon_noperms.clone();
        for (String perm : mainui_unlock_icon.keySet()) {
            if(StoreUnits.plugin.permission.hasPerm(offlinePlayer.getUniqueId(),perm)){
                icon=mainui_unlock_icon.get(perm).clone();
                break;
            }
        }
        icon.name=icon.name.replace("%money%",String.valueOf(money_need)).replace("%permission%",perm_need);
        ArrayList<String> lore=new ArrayList<>();
        for (String s : icon.lore) {
            lore.add(s.replace("%money%",String.valueOf(money_need)).replace("%permission%",perm_need));
        }
        icon.lore=lore;
        return icon;
    }
    public static String getFullName(String name,int unitID,int level){
     return Utils.pareseColor(name_format.replace("%unitID%",String.valueOf(unitID)).replace("%unitLevel%",String.valueOf(level)).replace("%unitName%",name));


    }

    public static Icon getMainui_nextpage() {
        return mainui_nextpage;
    }

    public static Icon getMainui_prepage() {
        return mainui_prepage;
    }

    public static String getTitle() {
        return title;
    }

    public static String getName_format(int unitID,int unitLevel,String unitName) {
        return name_format.replace("%unitID%",String.valueOf(unitID)).replace("%unitLevel%",String.valueOf(unitLevel)).replace("%unitName%",unitName);
    }

    public static String getDefault_name() {
        return default_name;
    }
    public  static class Icon implements Cloneable{
        public String name;
        public List<String> lore;
        public    int custommodeldata;
        public   Material material;
        public Icon clone(){
            Icon icon=new Icon();
            icon.name=this.name;
            icon.lore=new ArrayList<>();
            icon.lore.addAll(this.lore);
            icon.custommodeldata=this.custommodeldata;
            icon.material=this.material;
            return icon;
        }
        @Override
        public String toString(){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(name).append("  ");
            for (String string : lore) {
                stringBuilder.append(string).append("  ");
            }
            stringBuilder.append("  cmd:").append(custommodeldata).append(material.toString());
            return stringBuilder.toString();
        }
    }
}
