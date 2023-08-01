package fr.farmeurimmo.farmeurimmovelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fr.farmeurimmo.farmeurimmovelocity.listeners.PlayerListener;
import org.slf4j.Logger;

import java.util.ArrayList;

@Plugin(
        id = "farmeurimmovelocity",
        name = "FarmeurimmoVelocity",
        version = "1.0.0-SNAPSHOT"
)
public class Velocity {

    public static Velocity INSTANCE;
    private final Logger logger;
    private final ProxyServer proxyServer;

    @Inject
    public Velocity(Logger logger, ProxyServer proxyServer) {
        this.logger = logger;
        this.proxyServer = proxyServer;

        INSTANCE = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent e) {
        logger.info("Registering listeners...");
        proxyServer.getEventManager().register(this, new PlayerListener());
    }

    public int getPlayerCount() {
        return proxyServer.getPlayerCount();
    }

    public ArrayList<Player> getOnlinePlayers() {
        return new ArrayList<>(proxyServer.getAllPlayers());
    }
}
