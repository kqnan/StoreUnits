package me.kqn.storeunits.Gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kqn.storeunits.Config.UnitsConfig;
import me.kqn.storeunits.Config.UpgradeConfig;
import me.kqn.storeunits.Listeners.ChatListener;
import me.kqn.storeunits.Config.InterfaceConfig;
import me.kqn.storeunits.Data.PlayerData;
import me.kqn.storeunits.Data.StorePage;
import me.kqn.storeunits.StoreUnits;
import me.kqn.storeunits.Utils.ExpParser;
import me.kqn.storeunits.Utils.ItemBuilder;
import me.kqn.storeunits.Utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class SettingGUI {
    Player player;
    PlayerData pData;

    public SettingGUI(Player player){
        this.player=player;
        this.pData=PlayerData.getPlayerData(player);
    }
    public void show(int unitID, StorePage page,int pageID){
        ChestGui gui=new ChestGui(3, InterfaceConfig.getSetting_title());
        StaticPane pane=new StaticPane(0,0,9,3);
        gui.setOnTopClick(x->{
            Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{
                Inventory inv=x.getInventory();
                for(int i=0;i<27;i++){
                    if(i==8||i==11||i==13||i==14||i==15){
                    }
                    else if(inv.getContents()[i]!=null&&inv.getContents()[i].getType()!=Material.AIR){
                        player.getInventory().addItem(inv.getItem(i));
                        inv.setItem(i,new ItemStack(Material.AIR));
                    }
                }
            },1);
        });
        //返回按钮 右上角
        pane.addItem(new GuiItem(new ItemBuilder(Material.BARRIER).setName("&f返回").build(),x->{
            x.setCancelled(true);
            UnitsGUI unitsGUI=new UnitsGUI(player);
            player.closeInventory();
            Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->unitsGUI.show(pageID),1);
        }),8,0);
        //显示图标
        GuiItem guiItem=new GuiItem(page.getUnitIcon(unitID));
        guiItem.setAction(x->{
            x.setCancelled(true);
            player.closeInventory();
            Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{
                Gui gui1=new Gui(player);
                gui1.showPage(unitID);
            },1);
        });
        pane.addItem(guiItem,2,1);
        //命名牌
        pane.addItem(new GuiItem(new ItemBuilder(Material.NAME_TAG).setName(InterfaceConfig.getFullName(page.name,unitID,page.level)).setLore(Collections.singletonList("&8点击更改")).build(), x->{
            x.setCancelled(true);
            player.closeInventory();
            Msg.msg(((OfflinePlayer)player).getUniqueId(), UnitsConfig.getMsg_dialog());
            ChatListener.startDialog(((OfflinePlayer)player).getUniqueId(),unitID,page,pageID);
        }),4,1);
        //指南针，设置显示图标
        pane.addItem(new GuiItem(new ItemBuilder(Material.COMPASS).setName("&f设置存储单元图标").build(),x->{
            x.setCancelled(true);
            if(!(x.getClick()== ClickType.LEFT||x.getClick()==ClickType.RIGHT))return;//不是左键或右键就返回
            ItemStack itemStack=x.getCursor();
            if(itemStack==null||itemStack.getType()==Material.AIR)return;//若是空气就返回
            page.setUnitIcon(itemStack);
            player.closeInventory();
            Bukkit.getScheduler().runTaskLater(StoreUnits.plugin,()->{show(unitID,page,pageID);},1);
        }),5,1);
        //下界之星
        //升级逻辑在这里写的
        double money= ExpParser.parseMathExpression(UpgradeConfig.getMoney_upgrade(unitID,page.level));
        InterfaceConfig.Icon icon=InterfaceConfig.getSetting_upgrade(money);
        pane.addItem(new GuiItem(new ItemBuilder(icon.material).setName(icon.name).setLore(icon.lore).setCustomModelData(icon.custommodeldata).build(),x->{
            x.setCancelled(true);
            if(x.getClick()==ClickType.LEFT||x.getClick()==ClickType.RIGHT){

                page.unlock(UpgradeConfig.getUpgrade_unlock(),unitID);
                player.closeInventory();
                SettingGUI settingGUI=new SettingGUI(player);
                settingGUI.show(unitID, page, pageID);

            }
        }),6,1);
        gui.addPane(pane);
        gui.show(player);
    }
}
