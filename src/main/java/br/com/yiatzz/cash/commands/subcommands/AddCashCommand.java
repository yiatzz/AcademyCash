package br.com.yiatzz.cash.commands.subcommands;

import br.com.yiatzz.cash.CashPlugin;
import br.com.yiatzz.cash.commands.AbstractCommand;
import br.com.yiatzz.cash.config.Settings;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;

import static org.spongepowered.api.command.args.GenericArguments.integer;
import static org.spongepowered.api.command.args.GenericArguments.string;

public class AddCashCommand extends AbstractCommand {

    @Inject
    AddCashCommand(CashPlugin plugin, Logger logger, Settings settings) {
        super(plugin, logger, settings);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String target = (String) args.getOne("player").orElse(null);
        if (target == null) {
            throw new CommandException(settings.getText().getInvalidUser());
        }

        Double cash = (Double) args.getOne("cash").orElse(null);
        if (cash == null || cash.isNaN()) {
            throw new CommandException(settings.getText().getInvalidDouble());
        }

        plugin.getDatabase().updateCash(target, cash).thenAcceptSync($ -> {
            TextTemplate defineCash = settings.getText().getDefineCash();
            src.sendMessage(defineCash.toText().replace("<cash>", Text.of(cash)));
        });

        return CommandResult.success();
    }

    @Override
    public CommandSpec buildSpec(Settings settings) {
        return CommandSpec.builder()
                .permission("cash.admin")
                .arguments(
                        string(Text.of("player")),
                        integer(Text.of("cash"))
                )
                .executor(this)
                .build();
    }
}
