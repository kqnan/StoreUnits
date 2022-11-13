package me.kqn.storeunits;

import me.kqn.storeunits.Config.DropConfig;
import me.kqn.storeunits.Data.PlayerData;
import me.kqn.storeunits.Data.StorePage;
import me.kqn.storeunits.Integretion.Drop.DropIntegretion;
import me.kqn.storeunits.Utils.NBTUtils;
import me.kqn.storeunits.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DropListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player=event.getEntity();
        for (DropIntegretion dropIntegretion : StoreUnits.plugin.dropIntegretion) {
            if(!dropIntegretion.canDrop(player.getLocation(),null,player)){
                return;//看看其他插件是否允许掉落，有一个不允许都不掉落
            }
        }
        //允许掉落 异步
        UUID uuid=((OfflinePlayer) player).getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(StoreUnits.plugin,()->{
            PlayerData playerData=PlayerData.getPlayerData(uuid);
            for (StorePage storePage : playerData.storePages) {//遍历每一页
                for (ItemStack content : storePage.contents) {//遍历每一页的每一个物品
                    if(content==null||content.getType().isAir())continue;//如果物品是空则跳过
                    if(haslore(content,DropConfig.getLore())){//检测是否包含关键lore
                        if(roll(DropConfig.getDrop_prob())){//随机是否掉落
                            int amt=content.getAmount()*DropConfig.getDrop_percent()/100;//掉落百分比
                             //System.out.println(amt);
                            if(amt==0)continue;//如果计算出来需要掉落的物品数量为0那么退出

                            ItemStack drop=content.clone();
                            drop.setAmount(amt);
                            if(content.getAmount()-amt<=0)content.setType(Material.AIR);//如果掉落后物品数量为0
                            else{content.setAmount(content.getAmount()-amt);}
                            Bukkit.getScheduler().runTask(StoreUnits.plugin,()->{
                                player.getLocation().getWorld().dropItem(player.getLocation(),NBTUtils.removeGuiNBT(drop));
                            });//掉落物品到玩家位置
                        }
                    }
                }
            }
        });
    }
    Random random=new Random();
    private boolean roll(int prob){

        return random.nextInt(100)<=prob;
    }
    private boolean haslore(ItemStack itemStack, List<String> lore){

        ItemMeta meta=itemStack.getItemMeta();
        if(meta==null||meta.getLore()==null)return false;//如果没有lore就直接返回false

        for (String s : lore) {//对于每一条关键lore
            s=Utils.pareseColor(s);//解析颜色代码
            for (String s1 : meta.getLore()) {

                if(s1.contains(s))return true;
            }
            /*
            if(meta.getLore().contains(s)){
                return true;
            }*/
        }
        return false;
    }
}
