package me.jakubok.nationsmod.administration;

import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.NbtCompound;

public class DirectivesEntry implements ComponentV3 {
    public String key;
    public Object value;

    public DirectivesEntry(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.key = tag.getString("key");

        if (tag.getString("valuesType").equals("unknown")) {}
        else if (tag.getString("valuesType").equals("boolean")) {
            this.value = tag.getBoolean("value");
        }
        else if (tag.getString("valuesType").equals("byte")) {
            this.value = tag.getByte("value");
        }
        else if (tag.getString("valuesType").equals("double")) {
            this.value = tag.getDouble("value");
        }
        else if (tag.getString("valuesType").equals("float")) {
            this.value = tag.getFloat("value");
        }
        else if (tag.getString("valuesType").equals("int")) {
            this.value = tag.getInt("value");
        }
        else if (tag.getString("valuesType").equals("long")) {
            this.value = tag.getLong("value");
        }
        else if (tag.getString("valuesType").equals("short")) {
            this.value = tag.getShort("value");
        }
        else if (tag.getString("valuesType").equals("string")) {
            this.value = tag.getString("value");
        }
        else if (tag.getString("valuesType").equals("uuid")) {
            this.value = tag.getUuid("value");
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        writeToNbtAndReturn(tag);
    }

    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        
        tag.putString("key", key);

        if (value instanceof Boolean) {
            tag.putBoolean("value", (Boolean)value);
            tag.putString("valuesType", "boolean");
        }
        else if (value instanceof Byte) {
            tag.putByte("value", (Byte)value);
            tag.putString("valuesType", "byte");
        }
        else if (value instanceof Double) {
            tag.putDouble("value", (Double)value);
            tag.putString("valuesType", "double");
        }
        else if (value instanceof Float) {
            tag.putFloat("value", (Float)value);
            tag.putString("valuesType", "float");
        }
        else if (value instanceof Integer) {
            tag.putInt("value", (Integer)value);
            tag.putString("valuesType", "int");
        }
        else if (value instanceof Long) {
            tag.putLong("value", (Long)value);
            tag.putString("valuesType", "long");
        }
        else if (value instanceof Short) {
            tag.putShort("value", (Short)value);
            tag.putString("valuesType", "short");
        }
        else if (value instanceof String) {
            tag.putString("value", (String)value);
            tag.putString("valuesType", "string");
        }
        else if (value instanceof UUID) {
            tag.putUuid("value", (UUID)value);
            tag.putString("valuesType", "uuid");
        }
        else {
            System.out.println("[WARN] Any type has been found for the value of the key " + this.key);
            tag.putString("valuesType", "unknown");
        }
        
        return tag;
    }
}
