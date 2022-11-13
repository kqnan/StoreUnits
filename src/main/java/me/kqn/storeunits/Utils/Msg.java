package me.kqn.storeunits.Utils;

import me.kqn.storeunits.Config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Msg {
    public static void Log(String msg){
        Bukkit.getLogger().info(msg);
    }
    public static void msg(UUID pID,String msg){
        OfflinePlayer player= Bukkit.getOfflinePlayer(pID);
        if(player.isOnline()){
            ((Player)player).sendMessage(ChatColor.translateAlternateColorCodes('&',msg));
        }
    }
    public static void msg(UUID pID, List<String> msg){
        OfflinePlayer player= Bukkit.getOfflinePlayer(pID);
        if(player.isOnline()){
            for (String s : msg) {
                ((Player)player).sendMessage(ChatColor.translateAlternateColorCodes('&',s));
            }
        }
    }
    public static void debug(Object obj){
        if(Config.getDebug()){
            System.out.println(obj);
        }
    }
    public static void msg(CommandSender player, String msg){


            (player).sendMessage(ChatColor.translateAlternateColorCodes('&',msg));

    }
}
