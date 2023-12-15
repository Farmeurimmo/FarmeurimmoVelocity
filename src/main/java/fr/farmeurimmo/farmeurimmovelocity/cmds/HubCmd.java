package fr.farmeurimmo.farmeurimmovelocity.cmds;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import fr.farmeurimmo.farmeurimmovelocity.Velocity;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HubCmd implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player p)) {
            invocation.source().sendMessage(Component.text("§cYou must be a player to execute this command."));
            return;
        }
        RegisteredServer hub = Velocity.INSTANCE.getAHub();
        if (hub == null) {
            p.sendMessage(Component.text("§cNo hub found."));
            return;
        }
        if (p.getCurrentServer().isPresent() && p.getCurrentServer().get().getServerInfo().getName().startsWith("hub")) {
            p.sendMessage(Component.text("§cYou are already connected to a hub."));
            return;
        }
        p.sendMessage(Component.text("§aConnecting to hub..."));
        p.createConnectionRequest(hub).connect().thenRun(() -> p.sendMessage(Component.text("§aYou have been connected to the hub.")));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return SimpleCommand.super.suggest(invocation);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return SimpleCommand.super.suggestAsync(invocation);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return SimpleCommand.super.hasPermission(invocation);
    }
}
