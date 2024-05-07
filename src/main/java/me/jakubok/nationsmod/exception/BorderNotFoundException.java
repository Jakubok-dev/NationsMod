package me.jakubok.nationsmod.exception;

public class BorderNotFoundException extends Exception{
    public BorderNotFoundException() {
        super("In a given chunk wasn't found a single relevant border edge");
    }
}
