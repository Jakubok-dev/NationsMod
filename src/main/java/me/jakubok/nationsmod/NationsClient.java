package me.jakubok.nationsmod;

import me.jakubok.nationsmod.collections.BorderGroup;
import me.jakubok.nationsmod.collections.ClientBorderDrawer;
import me.jakubok.nationsmod.entity.HumanEntityRenderer;
import me.jakubok.nationsmod.map.MapStorage;
import me.jakubok.nationsmod.networking.ClientNetworking;
import me.jakubok.nationsmod.registries.EntityRegistry;
import me.jakubok.nationsmod.registries.KeyBindingRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;
public class NationsClient implements ClientModInitializer {

    public static ClientBorderDrawer drawer = new ClientBorderDrawer();
    public static MapStorage map = new MapStorage();
    public static int selectedSlot = -1;
    public static BorderGroup borderSlot = new BorderGroup();

    public static final EntityModelLayer MODEL_HUMAN_LAYER = new EntityModelLayer(new Identifier(NationsMod.MOD_ID, "human"), "main");

    @Override
    public void onInitializeClient() {
        ClientNetworking.register();
        KeyBindingRegistry.init();
        EntityRendererRegistry.register(EntityRegistry.HUMAN, HumanEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_HUMAN_LAYER, () -> {
            return TexturedModelData.of(PlayerEntityModel.getTexturedModelData(Dilation.NONE, false), 64, 64);
        });
    }
}
