package me.kqn.storeunits.Utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class NBTUtils {
    public static ItemStack removeGuiNBT(ItemStack itemStack){
        NBTItem nbtItem=new NBTItem(itemStack);
        if( nbtItem.hasKey("PublicBukkitValues")){
            NBTCompound compound=nbtItem.getCompound("PublicBukkitValues");
            if(compound.hasKey("storespace:if-uuid")){
                compound.removeKey("storespace:if-uuid");
            }
            if(compound.getKeys().size()==0){
                nbtItem.removeKey("PublicBukkitValues");
            }
        }
        return nbtItem.getItem();
    }

}
