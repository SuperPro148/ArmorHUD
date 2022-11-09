package io.github.superpro148.enchantviewer;

import io.github.superpro148.enchantviewer.config.EnchantViewerConfig;
import net.fabricmc.api.ClientModInitializer;

public class EnchantViewer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EnchantViewerConfig.readConfigFile();
    }
}
