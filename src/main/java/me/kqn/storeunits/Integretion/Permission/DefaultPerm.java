package me.kqn.storeunits.Integretion.Permission;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DefaultPerm implements Permission{

    @Override
    public boolean hasPerm(UUID playerID, String perm) {
        if(perm==null)return true;
        OfflinePlayer offlinePlayer=Bukkit.getOfflinePlayer(playerID);
        if(offlinePlayer.isOnline()){
            return ((Player)offlinePlayer).hasPermission(perm);
        }
        return false;
    }
}
