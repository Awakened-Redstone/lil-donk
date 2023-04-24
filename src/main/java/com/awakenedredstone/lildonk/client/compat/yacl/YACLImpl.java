package com.awakenedredstone.lildonk.client.compat.yacl;

import com.awakenedredstone.lildonk.config.Config;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class YACLImpl {
    public static Screen getScreen() {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Random Assistant Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("General"))
                        .tooltip(Text.literal("General settings"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Main"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Enable Lil' Donk"))
                                        .tooltip(Text.literal("Toggles Lil' Donk"))
                                        .binding(true, () -> Config.getInstance().donkEnabled, (value) -> {
                                            Config.getInstance().donkEnabled = value;
                                        })
                                        .controller(TickBoxController::new)
                                        .build()
                                ).build()
                        ).build()
                ).build()
                .generateScreen(null);
    }
}
