package me.kqn.storeunits.Data;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class JsonStorePage {
    protected UUID pID;
    public int amount_unlock=7;
    public boolean[] unlocked=null;
    public ItemStack[] contents=null;

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
