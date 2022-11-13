package me.kqn.storeunits.Utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {
    private Material material;
    private  int amount=1;
    private int cmd=-1;
    private String name=null;
    private NBTCompound NBT=null;
    private List<String > lore=null;
    public ItemBuilder(Material material){
        this.material=material;
    }
    public ItemBuilder setMat(Material material){
        this.material=material;
        return  this;
    }
    public ItemBuilder setAmount(int amount){
        this.amount=amount;
        return this;
    }
    public ItemBuilder setLore(List<String> lore){
        this.lore=lore;
        return this;
    }
    public ItemBuilder setCustomModelData(int cmd){
        this.cmd=cmd;
        return this;
    }
    public ItemBuilder setName(String str){
        this.name=str;
        return this;
    }
    public ItemBuilder setNBT(NBTCompound NBT){
        this.NBT=NBT;
        return this;
    }
    public ItemStack build(){
        ItemStack itemStack=new ItemStack(material);
        itemStack.setAmount(amount);
        ItemMeta meta=itemStack.getItemMeta();

        meta.setCustomModelData(cmd);
        if(cmd!=-1){
            meta.setCustomModelData(cmd);
        }
        if(name!=null){
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',name));
        }
        if(lore!=null){
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i,ChatColor.translateAlternateColorCodes('&',lore.get(i)));
            }
            meta.setLore(lore);
        }
        itemStack.setItemMeta(meta);
        if(NBT!=null){
            NBTItem nbtItem=new NBTItem(itemStack);
            nbtItem.mergeCompound(NBT);
            itemStack=nbtItem.getItem();
        }
        return itemStack;

    }
}
