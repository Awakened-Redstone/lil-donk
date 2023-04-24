package com.awakenedredstone.lildonk;

import com.awakenedredstone.lildonk.command.CommandHandler;
import com.awakenedredstone.lildonk.config.ConfigManager;
import com.awakenedredstone.lildonk.entity.Penguin;
import com.awakenedredstone.lildonk.registry.Registrar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LilDonk implements ModInitializer {
    public static final String MOD_ID = "lil-donk";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Map<ServerWorld, Penguin> THE_PENGUIN = new HashMap<>();

    @Override
    public void onInitialize() {
        Registrar.init();
        CommandHandler.init();
        ConfigManager.loadConfig();

        FabricDefaultAttributeRegistry.register(Registrar.PENGUIN, Penguin.createAttributes());

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof Penguin penguin) {
                if (THE_PENGUIN.containsKey(world)) {
                    Penguin oldPenguin = THE_PENGUIN.get(world);
                    oldPenguin.remove(Entity.RemovalReason.DISCARDED);
                }

                THE_PENGUIN.put(world, penguin);
                List<ServerPlayerEntity> players = world.getPlayers();
                if (players.size() == 0) return;

                boolean isCaptainPlayer = false;
                ServerPlayerEntity captain = null;
                for (ServerPlayerEntity player : players) {
                    if (player.getUuid().equals(UUID.fromString("5f820c39-5883-4392-b174-3125ac05e38c"))) {
                        isCaptainPlayer = true;
                        captain = player;
                        break;
                    }
                }

                if (!isCaptainPlayer) {
                    entity.remove(Entity.RemovalReason.DISCARDED);
                    LilDonk.LOGGER.debug("Penguins are not enabled unless CaptainSparklez is playing!");
                    return;
                }

                penguin.setCustomName(Text.of("Lil' Donk"));
                penguin.setOwner(captain);
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (handler.player.getUuid().equals(UUID.fromString("5f820c39-5883-4392-b174-3125ac05e38c")) || FabricLoader.getInstance().isDevelopmentEnvironment()) {
                if (handler.player.world instanceof ServerWorld serverWorld) {
                    Penguin penguin = new Penguin(Registrar.PENGUIN, serverWorld);
                    penguin.setPos(handler.player.getX(), handler.player.getY(), handler.player.getZ());
                    serverWorld.spawnEntity(penguin);
                    penguin.setCustomName(Text.of("Lil' Donk"));
                    penguin.setOwner(handler.player);
                    THE_PENGUIN.put(serverWorld, penguin);
                }
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if (handler.player.getUuid().equals(UUID.fromString("5f820c39-5883-4392-b174-3125ac05e38c")) || FabricLoader.getInstance().isDevelopmentEnvironment()) {
                if (handler.player.world instanceof ServerWorld serverWorld) {
                    THE_PENGUIN.remove(serverWorld);
                }
            }
        });
    }
}
