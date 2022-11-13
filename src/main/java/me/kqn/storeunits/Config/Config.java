package me.kqn.storeunits.Config;

import me.kqn.storeunits.StoreUnits;
import me.kqn.storeunits.Utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {

    private static String datasource;
    private static int maxPages;
    private static YamlConfiguration file;

    public static int getMaxPages() {
        return maxPages;
    }

    public static String getMysql_host() {
        return mysql_host;
    }

    public static String getMysql_port() {
        return mysql_port;
    }

    public static String getMysql_database() {
        return mysql_database;
    }

    public static String getMysql_username() {
        return mysql_username;
    }

    public static String getMysql_password() {
        return mysql_password;
    }

    public static double getAutosave_interval() {
        return autosave_interval;
    }

    private static double autosave_interval;
    private static String mysql_host;
    private static String mysql_port;
    private static String mysql_database;
    private static String mysql_username;
    private static String mysql_password;
    private static String gems_singular;
    private static int load_delay;

    public static String getGems_singular() {
        return gems_singular;
    }

    public static String getGems_plural() {
        return gems_plural;
    }

    public static String getGems_id() {
        return gems_id;
    }
    private static Boolean debug;

    public static Boolean getDebug() {
        return debug;
    }

    private static String gems_plural;
    private static String gems_id;

    public static String getDatasource() {
        if(datasource==null){
            return "file";
        }
        return datasource;
    }

    public static int getLoad_delay() {
        return load_delay;
    }

    public static void read() {
        try {
            StoreUnits.plugin.saveResource("Config.yml", false);
            file = YamlConfiguration.loadConfiguration(new File("plugins\\StoreSpace\\Config.yml"));
            datasource = file.getString("datasource");
            maxPages=Integer.parseInt(file.getString("maxPages"));
            mysql_host=file.getString("mysql.host");
            mysql_port=file.getString("mysql.port");
            mysql_database=file.getString("mysql.database");
            mysql_username=file.getString("mysql.username");
            mysql_password=file.getString("mysql.password");
            gems_singular=file.getString("GemsEconomy.Singular");
            gems_plural=file.getString("GemsEconomy.Plural");
            gems_id=file.getString("GemsEconomy.ID");
            autosave_interval=Double.parseDouble(file.getString("autosave.interval_minutes"));
            load_delay=Integer.parseInt(file.getString("load_delay"));
            debug=Boolean.parseBoolean(file.getString("debug"));
        } catch (Exception e) {
            Msg.Log("[StoreSpace]读取Config.yml时出错，启用内置默认值");
            e.printStackTrace();
            maxPages=3;
            load_delay=20;
            autosave_interval=1;
        }
    }
}