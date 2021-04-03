package br.com.yiatzz.cash.config;

import br.com.yiatzz.cash.config.objects.GeneralObject;
import br.com.yiatzz.cash.config.objects.LangObject;
import com.focamacho.sealconfig.SealConfig;

import java.io.File;

public class GeneralConfig {

    public static LangObject langConfig;
    private static SealConfig sealConfig;
    public static GeneralObject config;

    public static void loadConfig() {
        sealConfig = new SealConfig();
        config = sealConfig.getConfig(new File("./config/AcademyCash/config.json"), GeneralObject.class);
        langConfig = sealConfig.getConfig(new File("./config/AcademyCash/lang.json"), LangObject.class);
    }

    public static void reloadConfig() {
        sealConfig.reload();
    }

}
