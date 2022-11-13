package me.kqn.storeunits.Integretion.Economy;

import org.bukkit.OfflinePlayer;


public interface Economy {
    public void take(OfflinePlayer player, double amount);
    public boolean has(OfflinePlayer player,double amount);
    public double get(OfflinePlayer player);
    public void give(OfflinePlayer player,double amount);
}
