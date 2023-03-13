package me.jakubok.nationsmod.administration;

import java.util.Set;
import java.util.UUID;

import me.jakubok.nationsmod.collections.PlayerAccount;

public interface Joinable {
    public Set<PlayerAccount> getPlayerMembers();
    public Set<UUID> getAIMembers();
}
