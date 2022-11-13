package me.kqn.storeunits.Utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class NBTUtils {
    public static ItemStack removeGuiNBT(ItemStack itemStack){
        NBTItem nbtItem=new NBTItem(itemStack);
        if( nbtItem.hasKey("PublicBukkitValues")){
            NBTCompound compound=nbtItem.getCompound("PublicBukkitValues");
            if(compound.hasKey("storeunits:if-uuid")){
                compound.removeKey("storeunits:if-uuid");
            }
            if(compound.getKeys().size()==0){
                nbtItem.removeKey("PublicBukkitValues");
            }
        }
        return nbtItem.getItem();
    }

}
