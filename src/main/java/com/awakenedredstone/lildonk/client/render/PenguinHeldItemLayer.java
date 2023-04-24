package com.awakenedredstone.lildonk.client.render;


import com.awakenedredstone.lildonk.client.render.model.PenguinModel;
import com.awakenedredstone.lildonk.entity.Penguin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class PenguinHeldItemLayer extends FeatureRenderer<Penguin, PenguinModel> {
    private final HeldItemRenderer itemInHandRenderer;

    public PenguinHeldItemLayer(FeatureRendererContext<Penguin, PenguinModel> renderLayerParent, HeldItemRenderer itemInHandRenderer) {
        super(renderLayerParent);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider buffer, int packedLight, Penguin livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStack.push();
        this.getContextModel().head.rotate(matrixStack);
        matrixStack.translate(0.1f, -0.05f, -0.2f);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(135f));
        ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.MAINHAND);
        this.itemInHandRenderer.renderItem(livingEntity, itemStack, ModelTransformationMode.GROUND, false, matrixStack, buffer, packedLight);
        matrixStack.pop();
    }
}
