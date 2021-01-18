package me.aidanwalden.nostalgiacraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class NostalgiacraftCore {
    public static boolean showWatermark = false;
    public static boolean doHotbarTooltip = true;
    public static boolean doOldTooltip = true;
    public static boolean doNotchSplash = false;
    public static boolean dirtBackground = true;
    public static boolean doOldCactusRender = true;
    public static boolean doOldMenuVersion = true;

    public static void saveConfig(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file, false);
        Properties prop = new Properties();

        prop.setProperty("showWatermark", String.valueOf(showWatermark));
        prop.setProperty("doHotbarTooltip", String.valueOf(doHotbarTooltip));
        prop.setProperty("doOldTooltip", String.valueOf(doOldTooltip));
        prop.setProperty("doNotchSplash", String.valueOf(doNotchSplash));
        prop.setProperty("dirtBackground", String.valueOf(dirtBackground));
        prop.setProperty("doOldCactusRender", String.valueOf(doOldCactusRender));
        prop.setProperty("doOldMenuVersion", String.valueOf(doOldMenuVersion));

        prop.store(fos, null);

        fos.close();
    }

    public static void loadConfig(File file) throws IOException {
        if (!file.exists() || !file.canRead()) {
            saveConfig(file);
        }
        FileInputStream fis = new FileInputStream(file);
        Properties prop = new Properties();

        prop.load(fis);
        showWatermark = Boolean.parseBoolean(prop.getProperty("showWatermark"));
        doHotbarTooltip = Boolean.parseBoolean(prop.getProperty("doHotbarTooltip"));
        doOldTooltip = Boolean.parseBoolean(prop.getProperty("doOldTooltip"));
        doNotchSplash = Boolean.parseBoolean(prop.getProperty("doNotchSplash"));
        dirtBackground = Boolean.parseBoolean(prop.getProperty("dirtBackground"));
        doOldCactusRender = Boolean.parseBoolean(prop.getProperty("doOldCactusRender"));
        doOldMenuVersion = Boolean.parseBoolean(prop.getProperty("doOldMenuVersion"));

        fis.close();
    }
}
