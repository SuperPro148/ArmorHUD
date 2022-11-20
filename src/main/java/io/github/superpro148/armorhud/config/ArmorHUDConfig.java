package io.github.superpro148.armorhud.config;

import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.BooleanController;
import dev.isxander.yacl.gui.controllers.cycling.EnumController;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import io.github.superpro148.configlib148.ConfigList;
import io.github.superpro148.configlib148.ConfigOption;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ArmorHUDConfig {

    public static ConfigList CONFIG_LIST = new ConfigList("armorhud");

    public static ConfigOption<Boolean> SHOW_ARMORHUD = CONFIG_LIST.register(new ConfigOption<>("show_armorhud", Boolean.class, true));
    public static ConfigOption<Side> ARMORHUD_SIDE = CONFIG_LIST.register(new ConfigOption<>("armorhud_side", Side.class, Side.LEFT));
    public static ConfigOption<Integer> ARMORHUD_HEIGHT = CONFIG_LIST.register(new ConfigOption<>("armorhud_height", Integer.class, 50));

    public static void save() {
        CONFIG_LIST.saveConfig();
    }

    public static Screen createGui(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("ArmorHUD"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("ArmorHUD"))
                        .option(Option.createBuilder(Boolean.class)
                                .name(Text.translatable("option.armorhud.show_armorhud"))
                                .tooltip(Text.translatable("tooltip.armorhud.show_armorhud"))
                                .binding(
                                        true,
                                        () -> SHOW_ARMORHUD.getValue(),
                                        newValue -> SHOW_ARMORHUD.setValue(newValue)
                                )
                                .controller(BooleanController::new)
                                .build())
                        .option(Option.createBuilder(Side.class)
                                .name(Text.translatable("option.armorhud.armorhud_side"))
                                .tooltip(Text.translatable("tooltip.armorhud.armorhud_side"))
                                .binding(
                                        Side.LEFT,
                                        () -> ARMORHUD_SIDE.getValue(),
                                        newValue -> ARMORHUD_SIDE.setValue(newValue)
                                )
                                .controller(option -> new EnumController<Side>(option, enumConst -> Text.translatable("enum.enchantviewer.armorhud_side_" + enumConst.toString().toLowerCase())))
                                .build())
                        .option(Option.createBuilder(Integer.class)
                                .name(Text.translatable("option.armorhud.armorhud_height"))
                                .tooltip(Text.translatable("tooltip.armorhud.armorhud_height"))
                                .binding(
                                        50,
                                        () -> ARMORHUD_HEIGHT.getValue(),
                                        newValue -> ARMORHUD_HEIGHT.setValue(newValue)
                                )
                                .controller(option -> new IntegerSliderController(option,0, 100, 1))
                                .build())
                        .build())
                .save(ArmorHUDConfig::save)
                .build()
                .generateScreen(parent);
    }

}
