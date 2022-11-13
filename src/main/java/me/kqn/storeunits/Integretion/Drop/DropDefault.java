package me.kqn.storeunits.Integretion.Drop;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class DropDefault implements DropIntegretion{
    @Override
    public boolean canDrop(Location location, Entity killer, Player player) {
        return true;
    }
}
