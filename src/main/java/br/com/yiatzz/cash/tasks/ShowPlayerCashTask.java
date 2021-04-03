package br.com.yiatzz.cash.tasks;

import br.com.yiatzz.cash.CashPlugin;
import br.com.yiatzz.cash.config.GeneralConfig;
import br.com.yiatzz.cash.misc.utils.NumberUtils;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ShowPlayerCashTask implements Runnable {

    private final CashPlugin plugin;
    private final String playerName;
    private final boolean self;

    private final CommandSource commandSource;

    public ShowPlayerCashTask(CashPlugin plugin, String playerName, CommandSource commandSource, boolean self) {
        this.plugin = plugin;
        this.playerName = playerName;
        this.commandSource = commandSource;
        this.self = self;
    }

    @Override
    public void run() {
        Double cash = plugin.getDatabase().requestCash(playerName);
        if (cash == null) {

            if (self) {
                commandSource.sendMessage(Text.of(TextColors.RED, "Você não possui cash."));
            } else {
                commandSource.sendMessage(Text.of(TextColors.RED, "Este jogador não existe."));
            }

            return;
        }

        Text text;

        if (self) {
            text = Text.of(GeneralConfig.langConfig.seeCash.replaceAll("\\{cash\\}", NumberUtils.format(cash)));
        } else {
            text = Text.of(GeneralConfig.langConfig.seeOtherCash
                    .replaceAll("\\{name\\}", playerName)
                    .replaceAll("\\{cash\\}", NumberUtils.format(cash)));
        }

        commandSource.sendMessage(text);
    }
}
