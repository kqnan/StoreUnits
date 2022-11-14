package me.kqn.storeunits.Data;

import me.kqn.storeunits.Config.InterfaceConfig;
import me.kqn.storeunits.Config.MessageConfig;
import me.kqn.storeunits.Config.UpgradeConfig;
import me.kqn.storeunits.StoreUnits;
import me.kqn.storeunits.Utils.ExpParser;
import me.kqn.storeunits.Utils.ItemBuilder;
import me.kqn.storeunits.Utils.Msg;
import me.kqn.storeunits.Utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.UUID;

public class StorePage extends JsonStorePage{


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
        InterfaceConfig.Icon icon=InterfaceConfig.getMainui_unit_icon();
        this.unitIcon=new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();
      //  Msg.debug(icon);
        name=InterfaceConfig.getDefault_name();

    }
    public StorePage(UUID player,int unlock_init){
        this.pID=player;
        this.amount_unlock=unlock_init;
        unlocked=new boolean[amount_unlock];
        Arrays.fill(unlocked, false);
        for(int i=0;i<amount_unlock;i++)unlocked[i]=true;
        contents=new ItemStack[amount_unlock];
        InterfaceConfig.Icon icon=InterfaceConfig.getMainui_unit_icon();
        this.unitIcon=new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

        name=InterfaceConfig.getDefault_name();
    }

    public boolean unlock(int slot,int unitID){
        OfflinePlayer player=Bukkit.getOfflinePlayer(pID);
        if(!(0<=slot&&slot<=44)){
            throw new RuntimeException("解锁的槽位必须在0~44之间");

        }
        if(slot!=amount_unlock){
            //如果槽位不是当前解锁的槽位的最后一个的后一位
            Msg.msg(pID, MessageConfig.cannot_unlock);
            return false;
        }
        //检查经济是否足够
        double moneyNeed= ExpParser.parseMathExpression(UpgradeConfig.getMoney_upgrade(unitID,level));//获取需要多少金币解锁%slot%号槽位
        if(!StoreUnits.plugin.economy.has(Bukkit.getOfflinePlayer(pID),moneyNeed)){//如果没有足够的金币
            Msg.msg(pID, UpgradeConfig.getMsg_nomoney());//发送金币不足消息
            return false;
        }

        //解锁
        expandPage();//创建一个新空间，把旧空间复制到新空间，再丢弃旧空间
        StoreUnits.plugin.economy.take(player,moneyNeed);

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
