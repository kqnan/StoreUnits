package me.kqn.storeunits.Gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kqn.storeunits.Config.InterfaceConfig;
import me.kqn.storeunits.Data.PlayerData;
import me.kqn.storeunits.Data.StorePage;
import me.kqn.storeunits.StoreUnits;
import me.kqn.storeunits.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

    public void showPage(int unitID){
        if(!pData.isPrepared)return;
        if(pData.storePages.length<=unitID||unitID<0)return;
        ChestGui gui=new ChestGui(6,InterfaceConfig.getFullName(pData.storePages[unitID].name,unitID,pData.storePages[unitID].level));
        int backbutton=4;

        //阻止下方滑条栏放置物品
        gui.setOnGlobalClick(x->{
            Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{
                Inventory inv=gui.getInventory();

                if(getInv(inv,1,5)!=null&&getInv(inv,1,5).getType()!=Material.AIR){
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
        //创建窗口主体
        StorePage storePage=pData.storePages[unitID];
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
            GenerateUnlockIcon(storePage,page, unitID,gui);
            gui.addPane(page);
            //创建下边滑块
            StaticPane spane=new StaticPane(0,5,9,1);
            //上一页按钮
            spane.addItem(new GuiItem(preIcon(unitID), x->{if(unitID-1>=0){
                player.closeInventory();
                Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{showPage(unitID-1);},1);
            }x.setCancelled(true);}),0,0);
            //下一页按钮
            spane.addItem(new GuiItem(nextIcon(unitID), x->{if(unitID+1<pData.storePages.length){
                player.closeInventory();
                Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{showPage(unitID+1);},1);
            }x.setCancelled(true);}),8,0);

            //返回总界面按钮
            spane.addItem(new GuiItem(slideIcon(unitID),x->{x.setCancelled(true);
                player.closeInventory();
                Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{
                    UnitsGUI gui1=new UnitsGUI(player);
                    gui1.show(0);
                },1);
            }),backbutton,0);
            gui.addPane(spane);
            gui.setOnClose(x->CallonClose(gui,x,unitID));
            gui.show(player);
    }
    private void GenerateUnlockIcon(StorePage storePage,StaticPane page,int pageID,ChestGui gui){
        int x_lock= storePage.amount_unlock%9;
        int y_lock=storePage.amount_unlock/9;
        for(;y_lock<5;y_lock++){
            for(;x_lock<9;x_lock++){
                int finalY_lock = y_lock;
                int finalX_lock = x_lock;
                page.addItem(new GuiItem(UnlockIcon(pageID), x->{x.setCancelled(true);
                    //如果不是左键或者右键点的，那么返回，防止卡bug
                    if(!(x.getClick()==ClickType.LEFT||x.getClick()==ClickType.RIGHT))return;
                    int slot=finalY_lock*9+finalX_lock;

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
        InterfaceConfig.Icon icon= InterfaceConfig.getPrepage_unit();
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    private ItemStack nextIcon(int pageID){
        InterfaceConfig.Icon icon= InterfaceConfig.getNextpage_unit();
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    private ItemStack UnlockIcon(int pageID){
            InterfaceConfig.Icon icon= InterfaceConfig.getUnlock_slot();
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    //在gui关闭时，保存数据到pData
    private void CallonClose(ChestGui gui,InventoryCloseEvent event,int pageid){
        Inventory pageInv= gui.getInventory();
        StorePage storePage=pData.storePages[pageid];
        storePage.contents=new ItemStack[storePage.amount_unlock];
        for(int i=0;i<storePage.contents.length;i++){
            int x=i%9,y=i/9;
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
