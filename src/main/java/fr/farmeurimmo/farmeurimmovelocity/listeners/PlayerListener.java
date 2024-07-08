package fr.farmeurimmo.farmeurimmovelocity.listeners;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerPing;
import fr.farmeurimmo.farmeurimmovelocity.Velocity;
import fr.farmeurimmo.users.UsersManager;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerListener {

    @Subscribe
    public void onProxyPing(ProxyPingEvent e) {
        ArrayList<ServerPing.SamplePlayer> samplePlayers = new ArrayList<>();
        samplePlayers.add(new ServerPing.SamplePlayer("§dA project/problem ?", UUID.fromString("00000000-0000-0000-0000-000000000000")));
        samplePlayers.add(new ServerPing.SamplePlayer("§c→ §6contact@farmeurimmo.fr", UUID.fromString("00000000-0000-0000-0000-000000000000")));
        samplePlayers.add(new ServerPing.SamplePlayer("§e-- Player(s) online --", UUID.fromString("00000000-0000-0000-0000-000000000000")));
        ArrayList<Player> onlinePlayers = Velocity.INSTANCE.getOnlinePlayers();
        int i = 0;
        for (Player player : onlinePlayers) {
            if (i > 4) return;
            samplePlayers.add(new ServerPing.SamplePlayer("§a" + player.getUsername(), player.getUniqueId()));
            i++;
        }

        Component description = Component.text("§c§lNetwork of Farmeurimmo §8§l| §a§l1.21+\n§6§lTest server for plugins.");

        ServerPing.Players players = new ServerPing.Players(Velocity.INSTANCE.getPlayerCount(), 0, samplePlayers);

        int versionProtocol = e.getPing().getVersion().getProtocol();
        if (versionProtocol < 767) versionProtocol = 767;
        ServerPing.Version version = new ServerPing.Version(versionProtocol, "§c§lFarmeurimmo §8| §a1.21+");

        e.setPing(new ServerPing(version, players, description, e.getPing().getFavicon().orElse(null)));

        e.getConnection().getVirtualHost().ifPresent(virtualHost -> {
            if (!virtualHost.getHostName().equalsIgnoreCase("mc.farmeurimmo.fr")) {
                e.setPing(new ServerPing(new ServerPing.Version(999, "§c§l1.21+"),
                        null, Component.text("§c§lPlease join from mc.farmeurimmo.fr"), null));
            }
        });
    }

    @Subscribe
    public void onPreLogin(PreLoginEvent e) {
        e.getConnection().getVirtualHost().ifPresent(virtualHost -> {
            if (!virtualHost.getHostName().equalsIgnoreCase("mc.farmeurimmo.fr")) {
                e.setResult(PreLoginEvent.PreLoginComponentResult.denied(Component.text("§c§lPlease join from mc.farmeurimmo.fr")));
            }
        });
    }

    @Subscribe
    public void onLogin(LoginEvent e) {
        Player p = e.getPlayer();

        if (!p.hasPermission("farmeurimmo.maintenance")) {
            e.setResult(ResultedEvent.ComponentResult.denied(Component.text(
                    "§c§lYou are not allowed to join this server.\n§cAccess is temporarily restricted.")));
            return;
        }

        UsersManager.getUserOrCreate(p.getUniqueId(), p.getUsername()).thenAccept(usr -> {
            //TODO: nick
        }).exceptionally(ex -> {
            ex.printStackTrace();
            p.disconnect(Component.text("§c§lAn error occurred while loading your data.\n§cPlease try again later."));
            return null;
        });
    }

}
