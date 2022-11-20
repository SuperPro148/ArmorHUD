package io.github.superpro148.armorhud;

import io.github.superpro148.armorhud.config.ArmorHUDConfig;
import net.fabricmc.api.ClientModInitializer;

public class ArmorHUD implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ArmorHUDConfig.CONFIG_LIST.readConfig();
    }
}
