package me.jakubok.nationsmod.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.feature.StuckArrowsFeatureRenderer;
import net.minecraft.client.render.entity.feature.StuckStingersFeatureRenderer;
import net.minecraft.client.render.entity.feature.TridentRiptideFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class HumanEntityRenderer extends LivingEntityRenderer<HumanEntity, PlayerEntityModel<HumanEntity>> {
    private static boolean slim = false;
    public HumanEntityRenderer(Context ctx) {
        super(ctx, new PlayerEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER), slim), 0.5f);
        this.addFeature(new ArmorFeatureRenderer<HumanEntity, PlayerEntityModel<HumanEntity>, BipedEntityModel<HumanEntity>>(this, new BipedEntityModel<HumanEntity>(ctx.getPart(EntityModelLayers.PLAYER_INNER_ARMOR)), new BipedEntityModel<HumanEntity>(ctx.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR))));
        this.addFeature(new HeldItemFeatureRenderer<HumanEntity, PlayerEntityModel<HumanEntity>>(this));
        this.addFeature(new StuckArrowsFeatureRenderer<HumanEntity, PlayerEntityModel<HumanEntity>>(ctx, this));
        this.addFeature(new HeadFeatureRenderer<HumanEntity, PlayerEntityModel<HumanEntity>>(this, ctx.getModelLoader()));
        this.addFeature(new ElytraFeatureRenderer<HumanEntity, PlayerEntityModel<HumanEntity>>(this, ctx.getModelLoader()));
        this.addFeature(new TridentRiptideFeatureRenderer<HumanEntity>(this, ctx.getModelLoader()));
        this.addFeature(new StuckStingersFeatureRenderer<HumanEntity, PlayerEntityModel<HumanEntity>>(this));
    }

    @Override
    public Identifier getTexture(HumanEntity var1) {
        return DefaultSkinHelper.getTexture();
    }

    @Override
    public void render(HumanEntity abstractHumanEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.setModelPose(abstractHumanEntity);
        super.render(abstractHumanEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Vec3d getPositionOffset(HumanEntity abstractHumanEntity, float f) {
        if (abstractHumanEntity.isInSneakingPose()) {
            return new Vec3d(0.0, -0.125, 0.0);
        }
        return super.getPositionOffset(abstractHumanEntity, f);
    }

    @Override
    protected void scale(HumanEntity abstractHumanEntity, MatrixStack matrixStack, float f) {
        //float g = 0.9375f;
        matrixStack.scale(0.9375f, 0.9375f, 0.9375f);
    }

    @Override
    protected void setupTransforms(HumanEntity human, MatrixStack matrixStack, float f, float g, float h) {
        float i = human.getLeaningPitch(h);
        if (human.isFallFlying()) {
            super.setupTransforms(human, matrixStack, f, g, h);
            float j = (float)human.getRoll() + h;
            float k = MathHelper.clamp(j * j / 100.0f, 0.0f, 1.0f);
            if (!human.isUsingRiptide()) {
                matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(k * (-90.0f - human.getPitch())));
            }
            Vec3d vec3d = human.getRotationVec(h);
            Vec3d vec3d2 = human.getVelocity();
            double d = vec3d2.horizontalLengthSquared();
            double e = vec3d.horizontalLengthSquared();
            if (d > 0.0 && e > 0.0) {
                double l = (vec3d2.x * vec3d.x + vec3d2.z * vec3d.z) / Math.sqrt(d * e);
                double m = vec3d2.x * vec3d.z - vec3d2.z * vec3d.x;
                matrixStack.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion((float)(Math.signum(m) * Math.acos(l))));
            }
        } else if (i > 0.0f) {
            super.setupTransforms(human, matrixStack, f, g, h);
            float j = human.isTouchingWater() ? -90.0f - human.getPitch() : -90.0f;
            float k = MathHelper.lerp(i, 0.0f, j);
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(k));
            if (human.isInSwimmingPose()) {
                matrixStack.translate(0.0, -1.0, 0.3f);
            }
        } else {
            super.setupTransforms(human, matrixStack, f, g, h);
        }
    }

    public void renderRightArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, HumanEntity abstractHumanEntity) {
        this.renderArm(matrices, vertexConsumers, light, abstractHumanEntity, ((PlayerEntityModel<HumanEntity>)this.model).rightArm, ((PlayerEntityModel<HumanEntity>)this.model).rightSleeve);
    }

    public void renderLeftArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, HumanEntity abstractHumanEntity) {
        this.renderArm(matrices, vertexConsumers, light, abstractHumanEntity, ((PlayerEntityModel<HumanEntity>)this.model).leftArm, ((PlayerEntityModel<HumanEntity>)this.model).leftSleeve);
    }

    private void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, HumanEntity humanEntity, ModelPart arm, ModelPart sleeve) {
        PlayerEntityModel<HumanEntity> playerEntityModel = (PlayerEntityModel<HumanEntity>)this.getModel();
        this.setModelPose(humanEntity);
        playerEntityModel.handSwingProgress = 0.0f;
        playerEntityModel.sneaking = false;
        playerEntityModel.leaningPitch = 0.0f;
        playerEntityModel.setAngles(humanEntity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        arm.pitch = 0.0f;
        arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(this.getTexture(humanEntity))), light, OverlayTexture.DEFAULT_UV);
        sleeve.pitch = 0.0f;
        sleeve.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(humanEntity))), light, OverlayTexture.DEFAULT_UV);
    }

    private void setModelPose(HumanEntity human) {
        PlayerEntityModel<HumanEntity> playerEntityModel = (PlayerEntityModel<HumanEntity>)this.getModel();
        if (human.isSpectator()) {
            playerEntityModel.setVisible(false);
            playerEntityModel.head.visible = true;
            playerEntityModel.hat.visible = true;
        } else {
            playerEntityModel.setVisible(true);
            playerEntityModel.hat.visible = human.isPartVisible(PlayerModelPart.HAT);
            playerEntityModel.jacket.visible = human.isPartVisible(PlayerModelPart.JACKET);
            playerEntityModel.leftPants.visible = human.isPartVisible(PlayerModelPart.LEFT_PANTS_LEG);
            playerEntityModel.rightPants.visible = human.isPartVisible(PlayerModelPart.RIGHT_PANTS_LEG);
            playerEntityModel.leftSleeve.visible = human.isPartVisible(PlayerModelPart.LEFT_SLEEVE);
            playerEntityModel.rightSleeve.visible = human.isPartVisible(PlayerModelPart.RIGHT_SLEEVE);
            playerEntityModel.sneaking = human.isInSneakingPose();
            BipedEntityModel.ArmPose armPose = HumanEntityRenderer.getArmPose(human, Hand.MAIN_HAND);
            BipedEntityModel.ArmPose armPose2 = HumanEntityRenderer.getArmPose(human, Hand.OFF_HAND);
            if (armPose.isTwoHanded()) {
                // BipedEntityModel.ArmPose armPose3 = armPose2 = human.getOffHandStack().isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM;
            }
            if (human.getMainArm() == Arm.RIGHT) {
                playerEntityModel.rightArmPose = armPose;
                playerEntityModel.leftArmPose = armPose2;
            } else {
                playerEntityModel.rightArmPose = armPose2;
                playerEntityModel.leftArmPose = armPose;
            }
        }
    }

    private static BipedEntityModel.ArmPose getArmPose(HumanEntity human, Hand hand) {
        ItemStack itemStack = human.getStackInHand(hand);
        if (itemStack.isEmpty()) {
            return BipedEntityModel.ArmPose.EMPTY;
        }
        if (human.getActiveHand() == hand && human.getItemUseTimeLeft() > 0) {
            UseAction useAction = itemStack.getUseAction();
            if (useAction == UseAction.BLOCK) {
                return BipedEntityModel.ArmPose.BLOCK;
            }
            if (useAction == UseAction.BOW) {
                return BipedEntityModel.ArmPose.BOW_AND_ARROW;
            }
            if (useAction == UseAction.SPEAR) {
                return BipedEntityModel.ArmPose.THROW_SPEAR;
            }
            if (useAction == UseAction.CROSSBOW && hand == human.getActiveHand()) {
                return BipedEntityModel.ArmPose.CROSSBOW_CHARGE;
            }
            if (useAction == UseAction.SPYGLASS) {
                return BipedEntityModel.ArmPose.SPYGLASS;
            }
        } else if (!human.handSwinging && itemStack.isOf(Items.CROSSBOW) && CrossbowItem.isCharged(itemStack)) {
            return BipedEntityModel.ArmPose.CROSSBOW_HOLD;
        }
        return BipedEntityModel.ArmPose.ITEM;
    }
}
