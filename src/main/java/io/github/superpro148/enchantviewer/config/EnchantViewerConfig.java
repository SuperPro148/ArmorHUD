package io.github.superpro148.enchantviewer.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.BooleanController;
import dev.isxander.yacl.gui.controllers.cycling.EnumController;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class EnchantViewerConfig {

    public static boolean SHOW_ARMORHUD;
    public static Side ARMORHUD_SIDE;
    public static int ARMORHUD_HEIGHT;

    public static void save() {
        updateConfigFile();
    }

    public static Screen createGui(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("category.enchantviewer.enchantviewer"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("category.enchantviewer.armorhud"))
                        .option(Option.createBuilder(Boolean.class)
                                .name(Text.translatable("option.enchantviewer.show_armorhud"))
                                .tooltip(Text.translatable("tooltip.enchantviewer.show_armorhud"))
                                .binding(
                                        true,
                                        () -> SHOW_ARMORHUD,
                                        newValue -> SHOW_ARMORHUD = newValue
                                )
                                .controller(BooleanController::new)
                                .build())
                        .option(Option.createBuilder(Side.class)
                                .name(Text.translatable("option.enchantviewer.armorhud_side"))
                                .tooltip(Text.translatable("tooltip.enchantviewer.armorhud_side"))
                                .binding(
                                        Side.RIGHT,
                                        () -> ARMORHUD_SIDE,
                                        newValue -> ARMORHUD_SIDE = newValue
                                )
                                .controller(option -> new EnumController<Side>(option, enumConst -> Text.translatable("enum.enchantviewer.armorhud_side_" + enumConst.toString().toLowerCase())))
                                .build())
                        .option(Option.createBuilder(Integer.class)
                                .name(Text.translatable("option.enchantviewer.armorhud_height"))
                                .tooltip(Text.translatable("tooltip.enchantviewer.armorhud_height"))
                                .binding(
                                        50,
                                        () -> ARMORHUD_HEIGHT,
                                        newValue -> ARMORHUD_HEIGHT = newValue
                                )
                                .controller(option -> new IntegerSliderController(option,0, 100, 1))
                                .build())
                        .build())
                .save(EnchantViewerConfig::save)
                .build()
                .generateScreen(parent);
    }

    public static void updateConfigFile() {
        try {
            FileWriter configWriter = new FileWriter(FabricLoader.getInstance().getConfigDir().toString() + "/enchantviewer.json");
            JsonObject configJson = new JsonObject();
            configJson.addProperty("show_armorhud", SHOW_ARMORHUD);
            configJson.addProperty("armorhud_side", ARMORHUD_SIDE.toString());
            configJson.addProperty("armorhud_height", ARMORHUD_HEIGHT);
            configWriter.write(configJson.toString());
            configWriter.close();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public static void readConfigFile() {
        try {
            File configFile = new File(FabricLoader.getInstance().getConfigDir().toString() + "/enchantviewer.json");
            Scanner configReader = new Scanner(configFile);
            String configString = "";
            while (configReader.hasNextLine()) {
                configString = configString + configReader.nextLine();
            }
            JsonObject configJson = (JsonObject) JsonParser.parseString(configString);
            SHOW_ARMORHUD = configJson.get("show_armorhud").getAsBoolean();
            ARMORHUD_SIDE = Side.valueOf(configJson.get("armorhud_side").getAsString());
            ARMORHUD_HEIGHT = configJson.get("armorhud_height").getAsInt();
        } catch (FileNotFoundException fnfe) {
            SHOW_ARMORHUD = true;
            ARMORHUD_SIDE = Side.RIGHT;
            ARMORHUD_HEIGHT = 50;
            updateConfigFile();
        }
    }
}
