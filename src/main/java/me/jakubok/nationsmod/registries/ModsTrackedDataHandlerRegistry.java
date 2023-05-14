package me.jakubok.nationsmod.registries;

import me.jakubok.nationsmod.collection.Name;
import me.jakubok.nationsmod.collection.trackedDataHandler.ListOfUUID;
import me.jakubok.nationsmod.entity.human.HumanInventory;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;

public class ModsTrackedDataHandlerRegistry {
    public static final ListOfUUID LIST_OF_UUID_HANDLER;
    static {
        LIST_OF_UUID_HANDLER = new ListOfUUID();
		TrackedDataHandlerRegistry.register(Name.NAME_HANDLER); 
        TrackedDataHandlerRegistry.register(HumanInventory.HUMAN_INVENTORY_DATA_HANDLER);
        TrackedDataHandlerRegistry.register(LIST_OF_UUID_HANDLER);
    }
    
    public static void init() {}
}
