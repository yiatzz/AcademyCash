package br.com.yiatzz.cash.commands;

import br.com.yiatzz.cash.CashPlugin;
import br.com.yiatzz.cash.config.GeneralConfig;
import br.com.yiatzz.cash.tasks.DepositCashToPlayerTask;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CashAddCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne(Text.of("nome")).get();
        double amountRaw = args.<Integer>getOne("quantia").get();

        if (amountRaw <= 0.0) {
            System.out.println(2);
            src.sendMessage(Text.of(TextColors.RED, GeneralConfig.langConfig.invalidAmount));
            return CommandResult.success();
        }

        Task.builder()
                .async()
                .execute(() -> new DepositCashToPlayerTask(name, amountRaw, src, CashPlugin.getInstance()).run())
                .submit(CashPlugin.getInstance());

        return CommandResult.success();
    }
}
