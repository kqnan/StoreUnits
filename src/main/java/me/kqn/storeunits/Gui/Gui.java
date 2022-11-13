package me.kqn.storeunits.Gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kqn.storeunits.Config.MessageConfig;
import me.kqn.storeunits.Config.PageConfig;
import me.kqn.storeunits.Config.PageIcon;
import me.kqn.storeunits.Data.PlayerData;
import me.kqn.storeunits.Data.StorePage;
import me.kqn.storeunits.StoreUnits;
import me.kqn.storeunits.Utils.ItemBuilder;
import me.kqn.storeunits.Utils.Msg;
import me.kqn.storeunits.Utils.NBTUtils;
import me.kqn.storeunits.Utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Gui {
    // TODO: 2022/11/12 shift左键有bug
    Player player;
    PlayerData pData;
    int page_current=0;

    public Gui(Player player){
        this.player=player;
        this.pData=PlayerData.getPlayerData(player);
    }

    public void showPage(int pageID){
        if(!pData.isPrepared)return;
        if(pData.storePages.length<=pageID||pageID<0)return;
        ChestGui gui=new ChestGui(6,PageIcon.getTitle(pData.storePages.length-1,pageID));

        float percent=(float)(pageID +1)/(float)pData.storePages.length;
        int slidepos=(int)(4.0*percent);
        if(slidepos==0)slidepos=1;
        slidepos=1;
        int finalSlidepos = slidepos;
        //阻止滑条栏放置物品
        gui.setOnGlobalClick(x->{

            Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{
                Inventory inv=gui.getInventory();

                if(getInv(inv,8,1)!=null&&getInv(inv,8,1).getType()!=Material.AIR&& finalSlidepos !=1){
                    player.getInventory().addItem(inv.getItem(17));
                    inv.setItem(17,new ItemStack(Material.AIR));
                }
                if(getInv(inv,8,2)!=null&&getInv(inv,8,2).getType()!=Material.AIR&& finalSlidepos !=2){
                    player.getInventory().addItem(inv.getItem(26));
                    inv.setItem(26,new ItemStack(Material.AIR));
                }
                if(getInv(inv,8,3)!=null&&getInv(inv,8,3).getType()!=Material.AIR&& finalSlidepos !=3){
                    player.getInventory().addItem(inv.getItem(35));
                    inv.setItem(35,new ItemStack(Material.AIR));
                }
                if(getInv(inv,8,4)!=null&&getInv(inv,8,4).getType()!=Material.AIR&& finalSlidepos !=4){
                    player.getInventory().addItem(inv.getItem(44));
                    inv.setItem(44,new ItemStack(Material.AIR));
                }
            },1);
        });
        gui.setOnGlobalDrag(x->x.setCancelled(true));
        //读取pData到gui界面，没有对pData进行任何写操作
        //创建窗口主体
        StorePage storePage=pData.storePages[pageID];
            StaticPane page=new StaticPane(9,5);
            for(int j=0;j<storePage.contents.length;j++){
                if(storePage.contents[j]!=null&&storePage.contents[j].getType()!= Material.AIR){
                    GuiItem storeItem=new GuiItem(storePage.contents[j]);
                    storeItem.setAction(x->{x.getWhoClicked().getInventory().addItem(NBTUtils.removeGuiNBT(storeItem.getItem()));x.setCancelled(true);
                        x.getInventory().setItem(x.getSlot(),null);

                    });
                    page.addItem(storeItem,j%9,j/9);
                }
            }
            //创建未解锁槽位的图标
            GenerateUnlockIcon(storePage,page, pageID,gui);
            gui.addPane(page);
            //创建下边滑块
            StaticPane spane=new StaticPane(0,5,9,1);
            //上一页按钮
            spane.addItem(new GuiItem(preIcon(pageID), x->{if(page_current-1>=0){
                //player.closeInventory();
                page_current--;
                Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{showPage(pageID-1);},1);
            }x.setCancelled(true);}),0,0);
            //下一页按钮
            spane.addItem(new GuiItem(nextIcon(pageID), x->{if(page_current+1<pData.storePages.length){
                //player.closeInventory();
                page_current++;
                Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{showPage(pageID+1);},1);
            }x.setCancelled(true);}),0,5);

            //滑块
           //  slidepos=1;
            spane.addItem(new GuiItem(slideIcon(pageID),x->{x.setCancelled(true);}),0,slidepos);
       // spane.addItem(new GuiItem(slideIcon(pageID),x->{x.setCancelled(true);}),0,1);
            gui.addPane(spane);
            gui.setOnClose(x->CallonClose(gui,x,pageID));
            gui.show(player);
    }
    private void GenerateUnlockIcon(StorePage storePage,StaticPane page,int pageID,ChestGui gui){
        int x_lock= storePage.amount_unlock%8;
        int y_lock=storePage.amount_unlock/8;
        for(;y_lock<6;y_lock++){
            for(;x_lock<8;x_lock++){
                int finalY_lock = y_lock;
                int finalX_lock = x_lock;
                page.addItem(new GuiItem(UnlockIcon(pageID), x->{x.setCancelled(true);
                    if(!(x.getClick()==ClickType.LEFT||x.getClick()==ClickType.RIGHT))return;
                    //如果不是左键或者右键点的，那么返回，防止卡bug
                    int slot=finalY_lock*8+finalX_lock;
                   if( storePage.unlock(slot)){//尝试解锁
                       if(slot==47)//是否是本页最后一个槽位
                       {
                            PlayerData.addPage(((OfflinePlayer)player).getUniqueId());//解锁新页
                       }
                       SoundUtils.playSound(player,PageConfig.getUnlock_sound());
                       gui.getInventory().setItem(finalY_lock*9+finalX_lock,null);
                       player.closeInventory();
                       Msg.msg(((OfflinePlayer)player).getUniqueId(), MessageConfig.unlock);
                       Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{showPage(pageID);},1);
                   }
                }),x_lock,y_lock);

            }
            x_lock=0;
        }
    }
    private ItemStack slideIcon(int pageID){
        PageIcon.Icon icon= PageIcon.getSlideIcon(pData.storePages.length-1,pageID);
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    private ItemStack preIcon(int pageID){
        PageIcon.Icon icon= PageIcon.getPreIcon(pData.storePages.length-1,pageID);
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    private ItemStack nextIcon(int pageID){
        PageIcon.Icon icon= PageIcon.getNextIcon(pData.storePages.length-1,pageID);
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    private ItemStack UnlockIcon(int pageID){

            PageIcon.Icon icon= PageIcon.getUnlockIcon(pData.storePages.length-1,pageID);
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    //在gui关闭时，保存数据到pData
    private void CallonClose(ChestGui gui,InventoryCloseEvent event,int pageid){
        Inventory pageInv= gui.getInventory();
        StorePage storePage=pData.storePages[pageid];
        storePage.contents=new ItemStack[storePage.amount_unlock];
        for(int i=0;i<storePage.contents.length;i++){
            int x=i%8,y=i/8;
            if(getInv(pageInv,x,y)!=null&&getInv(pageInv,x,y).getType()!=Material.AIR){
                storePage.contents[i]=getInv(pageInv,x,y);

            }
        }
    }

    private ItemStack getInv(Inventory inv, int x, int y){
        ItemStack [] itemStacks=inv.getContents();
        return itemStacks[y*9+x];
    }
}
