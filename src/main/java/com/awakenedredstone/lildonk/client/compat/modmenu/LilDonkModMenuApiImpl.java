package com.awakenedredstone.lildonk.client.compat.modmenu;

import com.awakenedredstone.lildonk.config.ConfigManager;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class LilDonkModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ConfigManager.getConfigScreen();
    }
}
