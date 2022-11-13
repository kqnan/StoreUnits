package me.kqn.storeunits.Integretion.Permission;

import java.util.UUID;

public interface Permission {
    public boolean hasPerm(UUID playerID, String perm);
}
