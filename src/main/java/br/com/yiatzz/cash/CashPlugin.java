package br.com.yiatzz.cash;

import br.com.yiatzz.cash.commands.CashAddCommand;
import br.com.yiatzz.cash.commands.CashSeeCommand;
import br.com.yiatzz.cash.config.GeneralConfig;
import br.com.yiatzz.cash.config.LangConfig;
import br.com.yiatzz.cash.listeners.PlayerQuitListener;
import br.com.yiatzz.cash.storage.CashDatabase;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.sql.SQLException;

@Plugin(id = "cashplugin", name = "AcademyCash", version = "0.1", description = "Plugin de Cash")
public class CashPlugin {

    private static CashPlugin instance;

    @Inject
    private Logger logger;

    private CashDatabase database;

    @Listener
    public void onInit(GameInitializationEvent event) {
        instance = this;

        GeneralConfig.loadConfig();
        LangConfig.loadConfig();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        try {
            database = new CashDatabase(logger);
            database.createTable(this);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        Sponge.getEventManager().registerListeners(this, new PlayerQuitListener(this));

        CommandSpec addCommand = CommandSpec.builder()
                .permission("cash.admin")
                .arguments(
                        GenericArguments.string(Text.of("nome")),
                        GenericArguments.integer(Text.of("quantia"))
                )
                .executor(new CashAddCommand())
                .build();

        CommandSpec seeCommand = CommandSpec.builder()
                .arguments(
                        GenericArguments.optional(GenericArguments.string(Text.of("name")))
                )
                .executor(new CashSeeCommand())
                .build();

        CommandSpec main = CommandSpec.builder()
                .child(addCommand, "adicionar", "add")
                .child(seeCommand, "ver", "see")
                .build();

        Sponge.getCommandManager().register(
                this,
                main,
                "cash"
        );

        logger.info("Plugin de Cash ligado!");
    }

    @Listener
    public void onDisable(GameStoppingServerEvent stoppingEvent) {
        if (database != null) {
            database.close();
        }
    }

    public CashDatabase getDatabase() {
        return database;
    }

    public Logger getLogger() {
        return logger;
    }

    public static CashPlugin getInstance() {
        return instance;
    }
}
