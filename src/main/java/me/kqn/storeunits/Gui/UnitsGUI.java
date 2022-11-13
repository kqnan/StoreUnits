package me.kqn.storeunits.Gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kqn.storeunits.Config.InterfaceConfig;
import me.kqn.storeunits.Config.UnitsConfig;
import me.kqn.storeunits.Data.PlayerData;
import me.kqn.storeunits.StoreUnits;
import me.kqn.storeunits.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class UnitsGUI {
    // TODO: 2022/11/13 主页面的多页的测试 
    Player player;
    PlayerData pData;
    int page_current=0;

    public UnitsGUI(Player player){
        this.player=player;
        this.pData=PlayerData.getPlayerData(player);
    }

    public void show(int pageID){
        if(!pData.isPrepared)return;

        ChestGui gui=new ChestGui(6, Utils.pareseColor(InterfaceConfig.getMainui_title()));
        //阻止下方滑条栏放置物品
        gui.setOnGlobalClick(x->{
            Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{
                Inventory inv=gui.getInventory();

                if(getInv(inv,1,5)!=null&&getInv(inv,1,5).getType()!= Material.AIR){
                    player.getInventory().addItem(inv.getItem(46));
                    inv.setItem(46,new ItemStack(Material.AIR));
                }
                if(getInv(inv,2,5)!=null&&getInv(inv,2,5).getType()!=Material.AIR){
                    player.getInventory().addItem(inv.getItem(47));
                    inv.setItem(47,new ItemStack(Material.AIR));
                }
                if(getInv(inv,3,5)!=null&&getInv(inv,3,5).getType()!=Material.AIR){
                    player.getInventory().addItem(inv.getItem(48));
                    inv.setItem(48,new ItemStack(Material.AIR));
                }
                if(getInv(inv,4,5)!=null&&getInv(inv,4,5).getType()!=Material.AIR){
                    player.getInventory().addItem(inv.getItem(49));
                    inv.setItem(49,new ItemStack(Material.AIR));
                }
                if(getInv(inv,5,5)!=null&&getInv(inv,5,5).getType()!=Material.AIR){
                    player.getInventory().addItem(inv.getItem(50));
                    inv.setItem(50,new ItemStack(Material.AIR));
                }
                if(getInv(inv,6,5)!=null&&getInv(inv,6,5).getType()!=Material.AIR){
                    player.getInventory().addItem(inv.getItem(51));
                    inv.setItem(51,new ItemStack(Material.AIR));
                }
                if(getInv(inv,7,5)!=null&&getInv(inv,7,5).getType()!=Material.AIR){
                    player.getInventory().addItem(inv.getItem(52));
                    inv.setItem(52,new ItemStack(Material.AIR));
                }
            },1);
        });
        gui.setOnGlobalDrag(x->x.setCancelled(true));
        //读取pData到gui界面，没有对pData进行任何写操作
        //创建窗口主体,
        StaticPane page=new StaticPane(9,5);
        int unlock_start=0;
        for(int j=45*pageID;j<Math.min(pData.storePages.length,45*pageID+45);j++){
            int inv_index=j-45*pageID;
            unlock_start=Math.max(inv_index,unlock_start);
            ItemStack uIcon=pData.storePages[j].getUnitIcon(j);
            int finalJ = j;
            page.addItem(new GuiItem(uIcon, x->{x.setCancelled(true);
                SettingGUI settingGUI=new SettingGUI(player);
                settingGUI.show(finalJ,pData.storePages[finalJ],pageID);}),inv_index%9,inv_index/9);
        }

        //创建未解锁槽位的图标
        GenerateUnlockIcon(unlock_start,page, pageID,gui);
        gui.addPane(page);
        //创建下边滑块
        StaticPane spane=new StaticPane(0,5,9,1);
        //上一页按钮
        spane.addItem(new GuiItem(preIcon(pageID), x->{if(page_current-1>=0){
            //player.closeInventory();
            page_current--;
            Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{show(pageID-1);},1);
        }x.setCancelled(true);}),0,0);
        //下一页按钮
        spane.addItem(new GuiItem(nextIcon(pageID), x->{if(page_current+1<pData.storePages.length){
            //player.closeInventory();
            page_current++;
            Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{show(pageID+1);},1);
        }x.setCancelled(true);}),8,0);

        //返回总界面按钮

       // spane.addItem(new GuiItem(slideIcon(pageID),x->{x.setCancelled(true);}),backbutton,0);
        gui.addPane(spane);
       // gui.setOnClose(x->CallonClose(gui,x,pageID));
        gui.show(player);
    }
    private void GenerateUnlockIcon(int unlock_start,StaticPane page,int pageID,ChestGui gui){
        int x_lock= unlock_start%9;
        int y_lock=unlock_start/9;
        for(;y_lock<5;y_lock++){
            for(;x_lock<9;x_lock++){
                int finalY_lock = y_lock;
                int finalX_lock = x_lock;
                page.addItem(new GuiItem(UnlockIcon(pageID), x->{x.setCancelled(true);
                    //如果不是左键或者右键点的，那么返回，防止卡bug
                    if(!(x.getClick()== ClickType.LEFT||x.getClick()==ClickType.RIGHT))return;
                    int slot=finalY_lock*9+finalX_lock;
                    int unitID=slot+45*pageID;
                    if(unitID==pData.storePages.length){//如果点击的槽位时第一个未解锁的，那么解锁，需要注意inv的序号到数组的序号的映射
                        boolean r1= StoreUnits.plugin.permission.hasPerm(((OfflinePlayer)player).getUniqueId(), UnitsConfig.getPermission());
                        boolean r2=StoreUnits.plugin.economy.has(((OfflinePlayer) player), UnitsConfig.getMoney(unitID));
                        if(!r1){
                            Msg.msg(((OfflinePlayer)player).getUniqueId(), UnitsConfig.getMsg_noperm(unitID));
                            return;
                        }
                        if(!r2){
                            Msg.msg(((OfflinePlayer)player).getUniqueId(), UnitsConfig.getMsg_nomoney(unitID));
                            return;
                        }
                        PlayerData.addPage(((OfflinePlayer)player).getUniqueId());
                        gui.getInventory().setItem(slot,null);
                        player.closeInventory();
                        Msg.msg(((OfflinePlayer)player).getUniqueId(),UnitsConfig.getMsg_unlock(unitID));
                        Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{show(pageID);},1);
                    }
                }),x_lock,y_lock);

            }
            x_lock=0;
        }
    }
    private ItemStack slideIcon(int pageID){
        // InterfaceConfig.Icon icon= InterfaceConfig.getSlideIcon(pData.storePages.length-1,pageID);
        return new ItemBuilder(Material.CHEST).setName(Utils.pareseColor("&f返回存储单元界面")).build();

    }
    private ItemStack preIcon(int pageID){
        InterfaceConfig.Icon icon= InterfaceConfig.getMainui_prepage();
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    private ItemStack nextIcon(int pageID){
        InterfaceConfig.Icon icon= InterfaceConfig.getMainui_nextpage();
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    private ItemStack UnlockIcon(int pageID){
        InterfaceConfig.Icon icon= InterfaceConfig.getUnlock_slot();
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    //在gui关闭时，保存数据到pData


    private ItemStack getInv(Inventory inv, int x, int y){
        ItemStack [] itemStacks=inv.getContents();
        return itemStacks[y*9+x];
    }
}
