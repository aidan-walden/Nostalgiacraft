package me.aidanwalden.nostalgiacraft.client.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.aidanwalden.nostalgiacraft.NostalgiacraftCore;
import me.aidanwalden.nostalgiacraft.client.NostalgiacraftClient;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

import java.io.IOException;


public class NostalgiacraftModMenuApiImpl implements ModMenuApi {

    @Override
    public String getModId() {
        return "Nostalgiacraft";
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> getClothConfigScreen(parent);
    }

    private Screen getClothConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableText("nostalgiacraft.config.title"));
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("nostalgiacraft.config.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("nostalgiacraft.config.watermark"), NostalgiacraftCore.showWatermark)
                .setDefaultValue(true)
                .setTooltip(new TranslatableText("nostalgiacraft.config.watermark.tooltip"))
                .setSaveConsumer(newValue -> NostalgiacraftCore.showWatermark = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("nostalgiacraft.config.hotbartooltip"), NostalgiacraftCore.doHotbarTooltip)
                .setDefaultValue(false)
                .setTooltip(new TranslatableText("nostalgiacraft.config.hotbartooltip.tooltip"))
                .setSaveConsumer(newValue -> NostalgiacraftCore.doHotbarTooltip = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("nostalgiacraft.config.oldtooltip"), NostalgiacraftCore.doOldTooltip)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> NostalgiacraftCore.doOldTooltip = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("nostalgiacraft.config.notchsplash"), NostalgiacraftCore.doNotchSplash)
                .setDefaultValue(true)
                .setTooltip(new TranslatableText("nostalgiacraft.config.notchsplash.tooltip"))
                .setSaveConsumer(newValue -> NostalgiacraftCore.doNotchSplash = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("nostalgiacraft.config.dirtscreen"), NostalgiacraftCore.dirtBackground)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> NostalgiacraftCore.dirtBackground = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("nostalgiacraft.config.oldcactus"), NostalgiacraftCore.doOldCactusRender)
                .setTooltip(new TranslatableText("nostalgiacraft.config.oldcactus.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> NostalgiacraftCore.doOldCactusRender = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("nostalgiacraft.config.menuversion"), NostalgiacraftCore.doOldMenuVersion)
                .setTooltip(new TranslatableText("nostalgiacraft.config.menuversion.tooltip"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> NostalgiacraftCore.doOldMenuVersion = newValue)
                .build());

        /*general.addEntry(entryBuilder.startStrField(new TranslatableText("nostalgiacraft.config.watermarktext"), NostalgiacraftCore.watermarkText)
                .setDefaultValue("Minecraft %s")
                .setSaveConsumer(newValue -> NostalgiacraftCore.watermarkText = newValue)
                .build());*/

        builder.setSavingRunnable(() -> {
            try {
                NostalgiacraftCore.saveConfig(NostalgiacraftClient.configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                NostalgiacraftCore.loadConfig(NostalgiacraftClient.configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return builder.build();
    }
}
