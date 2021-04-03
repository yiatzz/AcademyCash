package br.com.yiatzz.cash.config;

import br.com.yiatzz.cash.config.objects.GeneralObject;
import com.focamacho.sealconfig.SealConfig;

import java.io.File;

public class GeneralConfig {

    private static SealConfig sealConfig;
    public static GeneralObject config;

    public static void loadConfig() {
        sealConfig = new SealConfig();
        config = sealConfig.getConfig(new File("./config/AcademyCash/config.json"), GeneralObject.class);
        saveConfig();
    }

    public static void reloadConfig() {
        sealConfig.reload();
    }

    public static void saveConfig() {
        sealConfig.save();
        sealConfig.save(config);
    }
}
