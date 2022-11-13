package me.kqn.storeunits.Config;


import me.kqn.storeunits.StoreUnits;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PageIcon {
    private static ArrayList<String> TitlePages=new ArrayList<>();
    private static ArrayList<ArrayList<Icon>> unlockedIcon=new ArrayList<>();
    private static ArrayList<ArrayList<Icon>> prePageIcon=new ArrayList<>();
    private static ArrayList<ArrayList<Icon>> nextPageIcon=new ArrayList<>();
    private static ArrayList<ArrayList<Icon>> slidePageIcon=new ArrayList<>();
    private static ArrayList<ArrayList<String>> titles=new ArrayList<>();
    private static YamlConfiguration file;
    public static void read(){
        StoreUnits.plugin.saveResource("PageIcon.yml",false);
        file= YamlConfiguration.loadConfiguration(new File("plugins\\storeunits\\PageIcon.yml"));
        unlockedIcon.clear();
        prePageIcon.clear();
        nextPageIcon.clear();
        slidePageIcon.clear();
        for (String key : file.getKeys(false)) {
            ConfigurationSection page=file.getConfigurationSection(key);
            ArrayList<Icon> unlocked=new ArrayList<>();
            ArrayList<Icon> prePage=new ArrayList<>();
            ArrayList<Icon> nextPage=new ArrayList<>();
            ArrayList<Icon> slidPage=new ArrayList<>();
            ArrayList<String> title=new ArrayList<>();
            for (String pageKey : page.getKeys(false)) {
                ConfigurationSection section=page.getConfigurationSection(pageKey);
                unlocked.add(readIcon(section.getConfigurationSection("unlock")));
                prePage.add(readIcon(section.getConfigurationSection("prePageIcon")));
                nextPage.add(readIcon(section.getConfigurationSection("nextPageIcon")));
                slidPage.add(readIcon(section.getConfigurationSection("sliderIcon")));
                title.add(ChatColor.translateAlternateColorCodes('&',section.getString("title")));
            }
            titles.add(title);
            unlockedIcon.add(unlocked);
            prePageIcon.add(prePage);
            nextPageIcon.add(nextPage);
            slidePageIcon.add(slidPage);
        }

    }

    private static Icon readIcon(ConfigurationSection section){
        Icon icon=new Icon();
        icon.name=section.getString("name");
        icon.lore=section.getStringList("lore");
        icon.custommodeldata=Integer.parseInt(section.getString("custommodeldata"));
        icon.material=Material.valueOf(section.getString("material").toUpperCase());
        return icon;
    }
    public static String getTitle(int UnlockPages,int pageID){
        if(UnlockPages>=titles.size()){
            UnlockPages=titles.size()-1;
        }
        if(pageID>=titles.get(UnlockPages).size()){
            pageID=titles.get(UnlockPages).size()-1;
        }
        return titles.get(UnlockPages).get(pageID);
    }
    public static Icon getUnlockIcon(int UnlockPages,int pageID){
        if(UnlockPages>=unlockedIcon.size()){
            UnlockPages=unlockedIcon.size()-1;
        }
        if(pageID>=unlockedIcon.get(UnlockPages).size()){
            pageID=unlockedIcon.get(UnlockPages).size()-1;
        }
        return unlockedIcon.get(UnlockPages).get(pageID);
    }
    public static Icon getPreIcon(int UnlockPages,int pageID){
        if(UnlockPages>=prePageIcon.size()){
            UnlockPages=prePageIcon.size()-1;
        }
        if(pageID>=prePageIcon.get(UnlockPages).size()){
            pageID=prePageIcon.get(UnlockPages).size()-1;
        }
        return prePageIcon.get(UnlockPages).get(pageID);
    }
    public static Icon getNextIcon(int UnlockPages,int pageID){
        if(UnlockPages>=nextPageIcon.size()){
            UnlockPages=nextPageIcon.size()-1;
        }
        if(pageID>=nextPageIcon.get(UnlockPages).size()){
            pageID=nextPageIcon.get(UnlockPages).size()-1;
        }
        return nextPageIcon.get(UnlockPages).get(pageID);
    }
    public static Icon getSlideIcon(int UnlockPages,int pageID){
        if(UnlockPages>=slidePageIcon.size()){
            UnlockPages=slidePageIcon.size()-1;
        }
        if(pageID>=slidePageIcon.get(UnlockPages).size()){
            pageID=slidePageIcon.get(UnlockPages).size()-1;
        }
        return slidePageIcon.get(UnlockPages).get(pageID);
    }
    public  static class Icon{
        public String name;
        public List<String> lore;
        public    int custommodeldata;
        public   Material material;
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
