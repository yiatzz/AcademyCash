package br.com.yiatzz.cash.tasks;

import br.com.yiatzz.cash.CashPlugin;
import br.com.yiatzz.cash.config.LangConfig;
import br.com.yiatzz.cash.misc.utils.NumberUtils;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

public class DepositCashToPlayerTask implements Runnable {

    private final String userName;
    private final double amount;
    private final CommandSource src;
    private final CashPlugin plugin;

    public DepositCashToPlayerTask(String userName, double amount, CommandSource src, CashPlugin plugin) {
        this.userName = userName;
        this.amount = amount;
        this.src = src;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        double cash = plugin.getDatabase().requestCash(userName) + amount;

        System.out.println(cash);

        plugin.getDatabase().updateCash(userName, cash);

        String addCash = LangConfig.config.addCash
                .replace("{cash}", NumberUtils.format(cash))
                .replace("{name}", userName);

        System.out.println(addCash);

        src.sendMessage(Text.of(addCash));
    }
}
