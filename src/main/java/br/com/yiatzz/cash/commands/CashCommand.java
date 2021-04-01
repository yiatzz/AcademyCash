package br.com.yiatzz.cash.commands;

import br.com.yiatzz.cash.CashPlugin;
import br.com.yiatzz.cash.config.Settings;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;

import java.util.Optional;

import static org.spongepowered.api.command.args.GenericArguments.optional;
import static org.spongepowered.api.command.args.GenericArguments.player;
import static org.spongepowered.api.text.Text.of;

public class CashCommand extends AbstractCommand {

    @Inject
    CashCommand(CashPlugin plugin, Logger logger, Settings settings) {
        super(plugin, logger, settings);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            throw new CommandException(settings.getText().getPlayersOnly());
        }

        Optional<Player> target = args.getOne("nome");

        if (target.isPresent()) {
            Player player = target.get();

            plugin.getDatabase().requestCash(player.getName()).thenAcceptSync(cash -> {
                TextTemplate cashInfo = settings.getText().getCashInfo();
                player.sendMessage(cashInfo.toText().replace("<cash>", Text.of(cash)));
            });

            return CommandResult.success();
        }

        Player player = (Player) src;

        plugin.getDatabase().requestCash(player.getName()).thenAcceptSync(cash -> {
            TextTemplate selfCashInfo = settings.getText().getSelfCashInfo();
            player.sendMessage(selfCashInfo.toText().replace("<cash>", Text.of(cash)));
        });

        return CommandResult.success();
    }

    @Override
    public CommandSpec buildSpec(Settings settings) {
        return CommandSpec.builder()
                .executor(this)
                .arguments(
                        optional(
                                player(of("nome"))
                        )
                )
                .build();
    }
}
