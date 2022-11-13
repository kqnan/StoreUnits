package me.kqn.storeunits.Data;

import me.kqn.storeunits.Config.MessageConfig;
import me.kqn.storeunits.Config.PageConfig;
import me.kqn.storeunits.StoreUnits;
import me.kqn.storeunits.Utils.ExpParser;
import me.kqn.storeunits.Utils.Msg;
import me.kqn.storeunits.Utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.UUID;

public class StorePage extends JsonStorePage{
    // TODO: 2022/11/10 多线程问题，容易引发错误 

    public StorePage(){
        super();
    }
    public StorePage(UUID player){
        super();
        this.pID=player;
        unlocked=new boolean[amount_unlock];
        Arrays.fill(unlocked, false);
        for(int i=0;i<amount_unlock;i++)unlocked[i]=true;
        contents=new ItemStack[amount_unlock];
    }
    public StorePage(UUID player,int unlock_init){
        this.pID=player;
        this.amount_unlock=unlock_init;
        unlocked=new boolean[amount_unlock];
        Arrays.fill(unlocked, false);
        for(int i=0;i<amount_unlock;i++)unlocked[i]=true;
        contents=new ItemStack[amount_unlock];
    }

    public boolean unlock(int slot){
        OfflinePlayer player=Bukkit.getOfflinePlayer(pID);
        if(!(0<=slot&&slot<=47)){
            throw new RuntimeException("解锁的槽位必须在0~47之间");

        }
        if(slot!=amount_unlock){
            //如果槽位不是当前解锁的槽位的最后一个的后一位
            Msg.msg(pID, MessageConfig.cannot_unlock);
            return false;
        }
        //检查经济是否足够
        double moneyNeed= ExpParser.parseMathExpression(PageConfig.getMoeny_unlock(slot));//获取需要多少金币解锁%slot%号槽位
        if(!StoreUnits.plugin.economy.has(Bukkit.getOfflinePlayer(pID),moneyNeed)){//如果没有足够的金币
            Msg.msg(pID,PageConfig.getMsg_nomoney(slot));//发送金币不足消息
            return false;
        }
        //检查权限是否有
        String permNeed=PageConfig.getPerm_unlock(slot);
        if(!StoreUnits.plugin.permission.hasPerm(pID,permNeed)){//如果没有
            Msg.msg(pID,PageConfig.getMsg_noperm(slot));
            if(player.isOnline()){
                Player p=(Player) player;
                SoundUtils.playSound(p,PageConfig.getNoperm_sound());//给玩家播放无权限时的声音。可以改为发包
            }
            return false;
        }
        //解锁
        expandPage();//创建一个新空间，把旧空间复制到新空间，再丢弃旧空间
        StoreUnits.plugin.economy.take(player,moneyNeed);
        if(player.isOnline()){
            SoundUtils.playSound((Player) player,PageConfig.getUnlock_sound());
        }
        return  true;
    }
    //创建一个新空间，把旧空间复制到新空间，再丢弃旧空间
    private void expandPage(){
        amount_unlock++;
        ItemStack[] tmp=new ItemStack[amount_unlock];
        for (int i = 0; i < contents.length; i++) {
            tmp[i]=contents[i];
        }
        contents=tmp;
        boolean[] tmp2=new boolean[amount_unlock];
        for (int i = 0; i < unlocked.length; i++) {
            tmp2[i]=unlocked[i];
        }
        unlocked=tmp2;
    }

}
