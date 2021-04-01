package br.com.yiatzz.cash.commands;

import br.com.yiatzz.cash.CashPlugin;
import br.com.yiatzz.cash.config.Settings;
import org.slf4j.Logger;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;

public abstract class AbstractCommand implements CommandExecutor {

    protected final CashPlugin plugin;
    protected final Logger logger;
    protected final Settings settings;

    public AbstractCommand(CashPlugin plugin, Logger logger, Settings settings) {
        this.plugin = plugin;
        this.logger = logger;
        this.settings = settings;
    }

    public abstract CommandSpec buildSpec(Settings settings);
}
