package br.com.yiatzz.cash.config;

import br.com.yiatzz.cash.config.objects.LangObject;
import com.focamacho.sealconfig.SealConfig;

import java.io.File;

public class LangConfig {

    private static SealConfig sealConfig;
    public static LangObject config;

    public static void loadConfig() {
        sealConfig = new SealConfig();
        config = sealConfig.getConfig(new File("./config/AcademyCash/lang.json"), LangObject.class);
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
