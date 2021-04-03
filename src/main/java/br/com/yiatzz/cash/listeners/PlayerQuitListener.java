package br.com.yiatzz.cash.listeners;

import br.com.yiatzz.cash.CashPlugin;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class PlayerQuitListener {

    private final CashPlugin plugin;

    public PlayerQuitListener(CashPlugin plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect playerQuitEvent, @First Player player) {
        plugin.getDatabase().removeCache(player.getName());
    }
}
