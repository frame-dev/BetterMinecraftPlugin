package ch.framedev.betterminecraft.managers;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CraftHistoryHolder implements InventoryHolder {

    private final UUID targetId;
    private final String targetName;
    private final int page;
    private final CraftHistoryFilter filter;

    public CraftHistoryHolder(
            UUID targetId,
            String targetName,
            int page,
            CraftHistoryFilter filter
    ) {
        this.targetId = targetId;
        this.targetName = targetName;
        this.page = page;
        this.filter = filter;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public int getPage() {
        return page;
    }

    public CraftHistoryFilter getFilter() {
        return filter;
    }

    @Override
    public @NotNull Inventory getInventory() {
        throw new UnsupportedOperationException();
    }
}