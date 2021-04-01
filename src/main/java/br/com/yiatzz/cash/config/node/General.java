package br.com.yiatzz.cash.config.node;

import ninja.leaping.configurate.objectmapping.Setting;

@SuppressWarnings("FieldMayBeFinal")
public class General {

    @Setting(comment = "FlexibleDatabase configuration")
    private SQLConfig sqlConfiguration = new SQLConfig();

    public SQLConfig getSQL() {
        return sqlConfiguration;
    }
}
