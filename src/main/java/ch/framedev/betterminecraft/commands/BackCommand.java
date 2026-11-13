package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class BackCommand implements CommandExecutor, Listener {

    private final BetterMinecraft plugin;
    private final Map<UUID, Location> backLocations;

    public BackCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
        this.backLocations = new HashMap<>();
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!command.getName().equalsIgnoreCase("back")) {
            return false;
        }
        if(!sender.hasPermission("betterminecraft.back")) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
            return true;
        }
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.only-player"));
            return true;
        }
        if(args.length != 0) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("back.usage"));
            return true;
        }
        if(!backLocations.containsKey(player.getUniqueId())) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("back.no-location"));
            return true;
        }
        player.teleport(backLocations.get(player.getUniqueId()));
        backLocations.remove(player.getUniqueId());
        player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("back.teleported"));
        return true;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(!player.hasPermission("betterminecraft.back")) {
            return;
        }
        backLocations.put(player.getUniqueId(), player.getLocation());
        player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("back.death-location-saved"));
    }
}
