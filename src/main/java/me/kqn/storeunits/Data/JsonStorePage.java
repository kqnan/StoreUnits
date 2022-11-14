package me.kqn.storeunits.Data;

import me.kqn.storeunits.Config.InterfaceConfig;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public abstract class JsonStorePage {
    protected UUID pID;
    public int amount_unlock=7;
    public boolean[] unlocked=null;
    public ItemStack[] contents=null;
    protected ItemStack unitIcon=null;

    public void setUnitIcon(ItemStack unitIcon) {
        this.unitIcon = unitIcon.clone();
    }

    public ItemStack getUnitIcon(int unitID) {
        ItemMeta meta=unitIcon.getItemMeta();
        ItemStack r=unitIcon.clone();
        if(meta!=null) {
            meta.setDisplayName(InterfaceConfig.getFullName(name, unitID, level));

            r.setItemMeta(meta);
        }

        return r;
    }

    public int unitID=0;
    public String name=null;
    public int level=0;//等级，主要用于显示，并不参与实际的空间大小计算
    public JsonStorePage(){}
    public UUID getpID() {
        return pID;
    }

    public void setpID(UUID pID) {
        this.pID = pID;
    }

    public int getAmount_unlock() {
        return amount_unlock;
    }

    public void setAmount_unlock(int amount_unlock) {
        this.amount_unlock = amount_unlock;
    }

    public boolean[] getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean[] unlocked) {
        this.unlocked = unlocked;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public void setContents(ItemStack[] contents) {
        this.contents = contents;
    }
}
