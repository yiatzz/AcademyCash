package br.com.yiatzz.cash;

import br.com.yiatzz.cash.commands.CashCommand;
import br.com.yiatzz.cash.commands.subcommands.AddCashCommand;
import br.com.yiatzz.cash.commands.subcommands.DefineCashCommand;
import br.com.yiatzz.cash.config.Settings;
import br.com.yiatzz.cash.listeners.PlayerQuitListener;
import br.com.yiatzz.cash.storage.CashDatabase;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.scheduler.Task;

import java.io.IOException;
import java.sql.SQLException;

@Plugin(id = "cashplugin", name = "AcademyCash", version = "0.1", description = "Plugin de Cash")
public class CashPlugin {

    private final Logger logger;
    private final Injector injector;
    private final Settings config;
    private final CommandManager commandManager;

    @Inject
    private EventManager eventManager;

    @Inject
    private PluginManager pluginManager;

    private CashDatabase database;

    @Inject
    CashPlugin(Logger logger, Injector injector, Settings settings, CommandManager commandManager) {
        this.logger = logger;
        this.config = settings;

        this.injector = injector;
        this.commandManager = commandManager;
    }

    @Listener
    public void onPreInitialization(GamePreInitializationEvent event) {
        init();
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        commandManager.register(this, CommandSpec.builder()
                .child(injector.getInstance(CashCommand.class).buildSpec(config), "ver", "see")
                .child(injector.getInstance(DefineCashCommand.class).buildSpec(config), "definir", "define")
                .child(injector.getInstance(AddCashCommand.class).buildSpec(config), "adicionar", "add")
                .build(), "cashplugin", "cash");

        eventManager.registerListeners(this, injector.getInstance(PlayerQuitListener.class));
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Plugin de Cash ligado!");

    }

    @Listener
    public void onDisable(GameStoppingServerEvent stoppingEvent) {
        if (database != null) {
            database.close();
        }
    }

    private void init() {
        config.load();
        try {
            database = new CashDatabase(logger, config);
            database.createTable(this, config);
        } catch (SQLException | UncheckedExecutionException | IOException ex) {
            logger.error("Cannot connect to cash storage", ex);
            Task.builder().execute(() -> Sponge.getServer().shutdown()).submit(this);
        }
    }

    public CashDatabase getDatabase() {
        return database;
    }

    public Logger getLogger() {
        return logger;
    }

    public Settings getConfigManager() {
        return config;
    }
}
