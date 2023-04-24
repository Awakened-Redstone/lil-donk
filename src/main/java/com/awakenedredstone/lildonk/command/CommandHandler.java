package com.awakenedredstone.lildonk.command;

import com.awakenedredstone.lildonk.LilDonk;
import com.awakenedredstone.lildonk.config.Config;
import com.awakenedredstone.lildonk.config.ConfigManager;
import com.awakenedredstone.lildonk.entity.Penguin;
import com.awakenedredstone.lildonk.registry.Registrar;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class CommandHandler {
    public static void init() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            registerDonkToggleCommand(dispatcher);
            registerDonkSpawnCommand(dispatcher);
        }));
    }

    private static void registerDonkToggleCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("toggledonk").executes(CommandHandler::donkToggle);
        dispatcher.register(builder);
    }

    private static void registerDonkSpawnCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("spawndonk").executes(CommandHandler::donkSpawn);
        dispatcher.register(builder);
    }

    private static int donkSpawn(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        if (Config.getInstance().donkEnabled) {
            source.sendFeedback(Text.of("Lil' Donk enabled"), false);
            ServerWorld world = source.getWorld();
            Penguin penguin = new Penguin(Registrar.PENGUIN, world);
            PlayerEntity player = source.getPlayer();
            if (player != null) {
                penguin.updatePosition(player.getX(), player.getY(), player.getZ());
                penguin.setOwner(player);
            } else {
                penguin.updatePosition(world.getSpawnPos().getX(), world.getSpawnPos().getY(), world.getSpawnPos().getZ());
            }
            world.spawnEntity(penguin);
            LilDonk.THE_PENGUIN.put(world, penguin);
        }
        return Config.getInstance().donkEnabled ? 1 : 0;
    }

    private static int donkToggle(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        Config.getInstance().donkEnabled = !Config.getInstance().donkEnabled;
        if (Config.getInstance().donkEnabled) {
            source.sendFeedback(Text.of("Lil' Donk enabled"), false);
            ServerWorld world = source.getWorld();
            Penguin penguin = new Penguin(Registrar.PENGUIN, world);
            PlayerEntity player = source.getPlayer();
            if (player != null) {
                penguin.updatePosition(player.getX(), player.getY(), player.getZ());
                penguin.setOwner(player);
            } else {
                penguin.updatePosition(world.getSpawnPos().getX(), world.getSpawnPos().getY(), world.getSpawnPos().getZ());
            }
            world.spawnEntity(penguin);
            LilDonk.THE_PENGUIN.put(world, penguin);
        } else {
            source.sendFeedback(Text.of("Lil' Donk disabled"), false);
            LilDonk.THE_PENGUIN.forEach((world, penguin) -> {
                penguin.remove(Entity.RemovalReason.DISCARDED);
            });
        }
        ConfigManager.saveConfig();
        return 1;
    }
}
