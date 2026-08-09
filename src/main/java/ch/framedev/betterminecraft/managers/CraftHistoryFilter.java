package ch.framedev.betterminecraft.managers;

public enum CraftHistoryFilter {

    LAST_CRAFTED_NEWEST,
    LAST_CRAFTED_OLDEST,
    AMOUNT_HIGHEST,
    AMOUNT_LOWEST;

    public CraftHistoryFilter next() {
        CraftHistoryFilter[] values = values();

        return values[
                (ordinal() + 1) % values.length
                ];
    }
}