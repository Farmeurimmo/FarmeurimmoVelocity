package fr.farmeurimmo.farmeurimmovelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.util.GameProfile;
import fr.farmeurimmo.farmeurimmovelocity.cmds.HubCmd;
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

        logger.info("Registering commands...");
        proxyServer.getCommandManager().register("hub", new HubCmd(), "lobby");

        logger.info("Plugin loaded.");
    }

    public RegisteredServer getAHub() {
        for (RegisteredServer server : proxyServer.getAllServers()) {
            if (server.getServerInfo().getName().startsWith("hub")) {
                return server;
            }
        }
        return null;
    }

    public int getPlayerCount() {
        return proxyServer.getPlayerCount();
    }

    public ArrayList<Player> getOnlinePlayers() {
        return new ArrayList<>(proxyServer.getAllPlayers());
    }

    public void setDisplayName(Player p, String displayName) {
        GameProfile gp = p.getGameProfile();
        //set player's display name
        gp = gp.withName(displayName);
        //update player's game profile
        p.setGameProfileProperties(gp.getProperties());
    }
}
