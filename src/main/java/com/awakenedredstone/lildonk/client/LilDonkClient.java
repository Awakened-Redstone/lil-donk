package com.awakenedredstone.lildonk.client;

import com.awakenedredstone.lildonk.client.render.PenguinRenderer;
import com.awakenedredstone.lildonk.client.render.model.PenguinModel;
import com.awakenedredstone.lildonk.registry.Registrar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class LilDonkClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(PenguinModel.LAYER_LOCATION, PenguinModel::createBodyLayer);
        EntityRendererRegistry.register(Registrar.PENGUIN, PenguinRenderer::new);
    }
}
