package me.jakubok.nationsmod.entity;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;

public class HumanEntityRenderer extends LivingEntityRenderer<HumanEntity, PlayerEntityModel<HumanEntity>> {

    public HumanEntityRenderer(Context ctx) {
        super(ctx, new PlayerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER), false), 0.5f);
    }

    @Override
    public Identifier getTexture(HumanEntity var1) {
        return DefaultSkinHelper.getTexture();
    }
}
