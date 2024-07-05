package me.jakubok.nationsmod.gui.miscellaneous.property;

import me.jakubok.nationsmod.administration.law.Act;
import me.jakubok.nationsmod.collection.Pair;

public interface MutableProperty<T> {
    Pair<String, T> getTheRule();
    Act<?> getAct();
    EditScreenFactory<T> getEditScreenFactory();
}
