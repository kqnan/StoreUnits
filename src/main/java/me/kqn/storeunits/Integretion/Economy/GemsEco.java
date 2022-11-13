package me.kqn.storeunits.Integretion.Economy;

import me.kqn.storeunits.Config.Config;
import me.xanium.gemseconomy.api.GemsEconomyAPI;
import me.xanium.gemseconomy.currency.Currency;
import org.bukkit.OfflinePlayer;

public class GemsEco implements Economy{
    private GemsEconomyAPI api;
    private Currency currency;
    public GemsEco (){
        api=new GemsEconomyAPI();
        currency=api.getCurrency(Config.getGems_singular());
    }
    @Override
    public void take(OfflinePlayer player, double amount) {
        api.withdraw(player.getUniqueId(),amount,currency);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {

        double balance=api.getBalance(player.getUniqueId(),currency);

        return amount<=balance;
    }

    @Override
    public double get(OfflinePlayer player) {
        return api.getBalance(player.getUniqueId(),currency);
    }

    @Override
    public void give(OfflinePlayer player, double amount) {
        api.deposit(player.getUniqueId(),amount,currency);
    }
}
