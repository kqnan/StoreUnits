package me.kqn.storeunits.Data;

import me.kqn.storeunits.Config.InterfaceConfig;
import me.kqn.storeunits.Config.UpgradeConfig;
import me.kqn.storeunits.StoreUnits;
import me.kqn.storeunits.Utils.ExpParser;
import me.kqn.storeunits.Utils.ItemBuilder;
import me.kqn.storeunits.Utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
        this.amount_unlock=UpgradeConfig.getDefault_unlock();
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
    public StorePage(UUID player,int init){
        this.pID=player;
        this.amount_unlock=UpgradeConfig.getDefault_unlock();
        unlocked=new boolean[amount_unlock];
        Arrays.fill(unlocked, false);
        for(int i=0;i<amount_unlock;i++)unlocked[i]=true;
        contents=new ItemStack[amount_unlock];
        InterfaceConfig.Icon icon=InterfaceConfig.getMainui_unit_icon();
        this.unitIcon=new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

        name=InterfaceConfig.getDefault_name();
    }
/*
* amt解锁的槽位数量， unitID存储单元的序号
* */
    public boolean unlock(int amt,int unitID){
        OfflinePlayer player=Bukkit.getOfflinePlayer(pID);
        if(amount_unlock>=45)return false;
        //检查经济是否足够
        double moneyNeed= ExpParser.parseMathExpression(UpgradeConfig.getMoney_upgrade(unitID,level));//获取需要多少金币解锁
        if(!StoreUnits.plugin.economy.has(Bukkit.getOfflinePlayer(pID),moneyNeed)){//如果没有足够的金币
            Msg.msg(pID, UpgradeConfig.getMsg_nomoney());//发送金币不足消息
            return false;
        }
        //解锁
        expandPage(amt);//创建一个新空间，把旧空间复制到新空间，再丢弃旧空间
        StoreUnits.plugin.economy.take(player,moneyNeed);
        this.level++;
        Msg.msg(pID,UpgradeConfig.getMsg_upgrade());//发送升级消息
        return  true;
    }
    //创建一个新空间，把旧空间复制到新空间，再丢弃旧空间
    private void expandPage(int amt){
        amount_unlock+=amt;
        amount_unlock=Math.min(amount_unlock,45);
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
