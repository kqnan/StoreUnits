package me.kqn.storeunits.Integretion.Economy;

import me.kqn.storeunits.Utils.Msg;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class Vault implements Economy {
    net.milkbowl.vault.economy.Economy economy;
    public Vault(){
        RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if(rsp==null){
            Msg.Log("Vault挂钩失败");
            return;
        }
        economy=rsp.getProvider();

    }
    @Override
    public void take(OfflinePlayer player, double amount) {
        economy.withdrawPlayer(player,amount);
    }

    @Override
    public boolean has(OfflinePlayer player,double  amount) {
        return economy.has(player,amount);
    }

    @Override
    public double  get(OfflinePlayer player) {
        return economy.getBalance(player);
    }

    @Override
    public void give(OfflinePlayer player,double amount) {
        economy.depositPlayer(player,amount);
    }
}
