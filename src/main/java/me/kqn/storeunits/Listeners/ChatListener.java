package me.kqn.storeunits.Listeners;

import me.kqn.storeunits.Data.StorePage;
import me.kqn.storeunits.Gui.SettingGUI;
import me.kqn.storeunits.StoreUnits;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatListener implements Listener {
    private static ConcurrentHashMap<UUID,Info> dialogPlayer=new ConcurrentHashMap<>();
    private static ConcurrentHashMap<UUID, BukkitTask> tasks=new ConcurrentHashMap<>();
    public static void startDialog(UUID p,int unitID, StorePage page,int pageID){
        dialogPlayer.put(p,new Info(unitID,page,pageID));//直接覆盖
        if(tasks.containsKey(p)){
            tasks.get(p).cancel();//取消后再覆盖
        }
        BukkitTask t=Bukkit.getScheduler().runTaskLaterAsynchronously(StoreUnits.plugin,()->{
            dialogPlayer.remove(p);
        },1200);//一分钟后自动取消对话
        tasks.put(p,t);
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        UUID uuid=((OfflinePlayer)event.getPlayer()).getUniqueId();
        if(dialogPlayer.containsKey(uuid)){//发现玩家正在对话
            String msg=event.getMessage();
            Info info=dialogPlayer.get(uuid);
            dialogPlayer.remove(uuid);//取消对话
            if(tasks.get(uuid)!=null)tasks.get(uuid).cancel();//取消任务
            tasks.remove(uuid);
            if(msg.equals("cancel")||msg.equals("取消")){//直接返回设定界面
                SettingGUI settingGUI=new SettingGUI(event.getPlayer());
                settingGUI.show(info.unitID,info.page,info.pageID);
            }
            else{
                info.page.name=msg;
                SettingGUI settingGUI=new SettingGUI(event.getPlayer());
                settingGUI.show(info.unitID,info.page,info.pageID);
            }
        }
    }
    private static class Info{
        int unitID;
        StorePage page;

        public Info(int unitID, StorePage page, int pageID) {
            this.unitID = unitID;
            this.page = page;
            this.pageID = pageID;
        }

        int pageID;
    }
}
