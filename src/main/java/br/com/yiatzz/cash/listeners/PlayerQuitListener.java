package br.com.yiatzz.cash.listeners;

import br.com.yiatzz.cash.CashPlugin;
import com.google.inject.Inject;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class PlayerQuitListener {

    private final CashPlugin plugin;

    @Inject
    PlayerQuitListener(CashPlugin plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect playerQuitEvent, @First Player player) {
        plugin.getDatabase().removeCache(player.getName());
    }
}
