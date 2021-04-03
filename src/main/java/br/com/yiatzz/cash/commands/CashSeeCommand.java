package br.com.yiatzz.cash.commands;

import br.com.yiatzz.cash.CashPlugin;
import br.com.yiatzz.cash.config.GeneralConfig;
import br.com.yiatzz.cash.tasks.ShowPlayerCashTask;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class CashSeeCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("Somente players."));
            return CommandResult.success();
        }

        Optional<String> name = args.getOne("name");

        if (name.isPresent()) {
            Optional<Player> player = Sponge.getServer().getPlayer(name.get());
            if (!player.isPresent()) {
                src.sendMessage(Text.of(GeneralConfig.langConfig.invalidUser));
                return CommandResult.success();
            }

            Task.builder()
                    .async()
                    .execute(new ShowPlayerCashTask(CashPlugin.getInstance(), name.get(), src, false))
                    .submit(CashPlugin.getInstance());
        } else {
            Task.builder()
                    .async()
                    .execute(new ShowPlayerCashTask(CashPlugin.getInstance(), src.getName(), src, true))
                    .submit(CashPlugin.getInstance());
        }

        return CommandResult.success();
    }
}
