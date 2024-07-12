package me.jakubok.nationsmod.administration.law;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisation;
import me.jakubok.nationsmod.administration.abstractEntities.LegalOrganisationLawDescription;
import me.jakubok.nationsmod.collection.EnumWithText;
import me.jakubok.nationsmod.registries.LegalOrganisationRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Act<D extends LegalOrganisationLawDescription> extends LawHolder<D> {
    private UUID id;
    protected String name;
    protected Map<String, NbtCompound> orders;
    protected UUID affectedBodyID;
    public ActStatus status = ActStatus.UNSUBMITTED;
    public Act(String name, D description, LegalOrganisation<D> affectedBody) {
        super(description);
        this.id = UUID.randomUUID();
        this.name = name;
        this.affectedBodyID = affectedBody.getId();
        this.orders = new HashMap<>();
    }
    public Act(D description, NbtCompound nbt) {
        super(description, nbt);
    }

    public UUID getID() {
        return id;
    }

    public UUID getAffectedBodyID() {
        return affectedBodyID;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object resetARule(String ruleName) {
        return this.law.remove(ruleName);
    }

    public Map<String, NbtCompound> getOrders() {
        return Map.copyOf(this.orders);
    }

    public boolean order(String orderLabel, NbtCompound nbt) {
        if (this.description.getOrders().get(orderLabel) == null)
            return false;
        return this.orders.put(orderLabel, nbt) != null;
    }

    public boolean removeAnOrder(String orderLabel) {
        return this.orders.remove(orderLabel) != null;
    }

    @SuppressWarnings("unchecked")
    public LegalOrganisation<D> getAffectedBody(MinecraftServer server) {
        return (LegalOrganisation<D>)LegalOrganisationRegistry.getRegistry(server).get(this.getAffectedBodyID());
    }

    public void implement(MinecraftServer server) {
        LegalOrganisation<D> organisation = this.getAffectedBody(server);
        for (Map.Entry<String, Object> entry : this.law.entrySet())
            organisation.law.putARule(entry.getKey(), entry.getValue());
        for (Map.Entry<String, NbtCompound> entry : this.orders.entrySet()) {
            Order order = this.description.getOrders().get(entry.getKey());
            order.onTrigger().trigger(entry.getValue(), organisation, server);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.id = tag.getUuid("act_id");
        this.name = tag.getString("act_name");
        this.status = ActStatus.values()[tag.getInt("act_status")];
        this.affectedBodyID = tag.getUuid("act_affectedBodyID");
        this.orders = new HashMap<>();
        for (int i = 0; i < tag.getInt("ordersSize"); i++)
            this.orders.put(tag.getString("orderKey" + i), tag.getCompound("orderValue" + i));
        super.readFromNbt(tag);
    }

    @Override
    public NbtCompound writeToNbtAndReturn(NbtCompound tag) {
        tag.putUuid("act_id", this.id);
        tag.putString("act_name", this.name);
        tag.putInt("act_status", this.status.value);
        tag.putUuid("act_affectedBodyID", this.affectedBodyID);
        int size = 0;
        for (String key : this.orders.keySet()) {
            tag.putString("orderKey" + size, key);
            tag.put("orderValue" + size, this.orders.get(key));
            size++;
        }
        tag.putInt("ordersSize", size);
        return super.writeToNbtAndReturn(tag);
    }

    public enum ActStatus implements EnumWithText {
        UNSUBMITTED(0, Text.literal("Unsubmitted").formatted(Formatting.GRAY)),
        DELIBERATED_BY_THE_LEGISLATIVE(1, Text.literal("Deliberated by the legislative")),
        REJECTED_BY_THE_LEGISLATIVE(2, Text.literal("Rejected by the legislative").formatted(Formatting.RED)),
        DELIBERATED_BY_THE_EXECUTIVE(3, Text.literal("Deliberated by the executive")),
        REJECTED_BY_THE_EXECUTIVE(4, Text.literal("Rejected by the executive").formatted(Formatting.RED)),
        APPROVED(5, Text.literal("Approved").formatted(Formatting.GREEN));

        ActStatus(int value, Text displayText) {
            this.value = value;
            this.displayText = displayText;
        }

        public final int value;

        public final Text displayText;

        @Override
        public Text getDisplayText() {
            return this.displayText;
        }
    }
}
