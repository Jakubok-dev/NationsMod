package me.Jakubok.nations.administration;

import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Province extends TeritorryClaimer {

    public String name;
    public Country belonging;
    public List<Town> towns = new ArrayList<>();
    public Province(String name, World world, Country belonging) {
        super(world);
        this.name = name;
        this.belonging = belonging;
    }

    @Override
    protected boolean expand() {
        return false;
    }

    @Override
    protected void abandon() {
        for (int i = 0; i < towns.size(); i++) {
            towns.get(i).province = null;
        }
        towns.clear();
    }
}
