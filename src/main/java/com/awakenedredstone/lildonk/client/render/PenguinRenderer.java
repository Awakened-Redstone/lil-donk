package com.awakenedredstone.lildonk.client.render;

import com.awakenedredstone.lildonk.LilDonk;
import com.awakenedredstone.lildonk.client.render.model.PenguinModel;
import com.awakenedredstone.lildonk.entity.Penguin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PenguinRenderer extends MobEntityRenderer<Penguin, PenguinModel> {

    public PenguinRenderer(EntityRendererFactory.Context context) {
        super(context, new PenguinModel(context.getPart(PenguinModel.LAYER_LOCATION)), 0.4F);
        this.addFeature(new PenguinHeldItemLayer(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(Penguin entity) {
        if (entity.isBaby()) {
            return new Identifier(LilDonk.MOD_ID, "textures/entity/baby_penguin.png");
        }
        return new Identifier(LilDonk.MOD_ID, "textures/entity/penguin.png");
    }
}
