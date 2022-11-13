package me.kqn.storeunits.Command;


import me.kqn.storeunits.Data.PlayerData;
import me.kqn.storeunits.Gui.Gui;
import me.kqn.storeunits.StoreUnits;
import me.kqn.storeunits.Utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MainCommand implements CommandExecutor , TabCompleter {
    String prefix="[storeunits]";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==1){
            if(sender instanceof Player && sender.isOp()){
                if(args[0].equals("save")){
                    //开发者命令
                    PlayerData playerData=PlayerData.getPlayerData((Player)sender);
                    UUID uuid=((OfflinePlayer)sender).getUniqueId();
                    StoreUnits.plugin.dataSource.write(playerData,uuid);

                }
                if(args[0].equals("load")){
                    //开发者命令
                    PlayerData playerData= StoreUnits.plugin.dataSource.readToPlayerData(((OfflinePlayer)sender).getUniqueId());
                    PlayerData.setData(((OfflinePlayer)sender).getUniqueId(),playerData);
                }
            }
        }
        if(args.length>1&&args[0].equalsIgnoreCase("open")){
            if((sender instanceof Player && sender.isOp())||(sender instanceof ConsoleCommandSender)){
                Player player=Bukkit.getPlayerExact(args[1]);
                if(player==null){
                    Msg.msg(sender,prefix+"该玩家不存在");
                }
                else {

                    Gui gui=new Gui(player);
                    gui.showPage(0);
                }
            }
            return true;
        }
        if(args.length==1&&args[0].equalsIgnoreCase("reload")){
            if((sender instanceof Player && sender.isOp())||(sender instanceof ConsoleCommandSender)){
                StoreUnits.plugin.loadConfig();
                StoreUnits.plugin.initIntegretion();
                Msg.msg(sender,prefix+"重新载入了配置文件和挂钩");
            }
            return true;
        }
        if(sender.isOp()||sender instanceof ConsoleCommandSender){
            sendHelp(sender);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if((sender instanceof Player && sender.isOp())){
            if(args.length<2){
                return Arrays.asList("open","reload");
            }
            else{
                ArrayList<String> playername=new ArrayList<>();
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    playername.add(onlinePlayer.getName());
                }
                return playername;
            }
        }
        return null;
    }
    public void sendHelp(CommandSender sender){
        Msg.msg(sender,"§7§m--------------------------------------");
        Msg.msg(sender,"§6<> §7- §d必填");
        Msg.msg(sender,"&e/ss open <玩家名> §b- §a为玩家打开存储空间");
        Msg.msg(sender,"&e/ss reload &b- &a重载配置文件");
       // Msg.msg(sender,"&e/ss clear <玩家名> &b- &a清除玩家的存储空间");
        Msg.msg(sender,"§7§m--------------------------------------");
    }
}
