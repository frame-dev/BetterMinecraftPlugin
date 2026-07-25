# BetterMinecraft

A server-side **Vanilla+** plugin for Spigot. It adds quality-of-life gameplay improvements using only the
Spigot API — no client mods, no custom launcher, no resource pack, and no NMS/CraftBukkit access.

- **Group / Artifact:** `ch.framedev` / `BetterMinecraft`
- **Main class:** `ch.framedev.betterminecraft.BetterMinecraft`
- **Spigot API:** `26.1.2-R0.1-SNAPSHOT` (`api-version: '26.1'`)
- **Java:** 21

## Features

### Stone-variant tool recipes

Twenty shaped recipes let you craft the vanilla stone tool set from decorative stone variants instead of
cobblestone. Each variant covers the full tool set — sword, pickaxe, axe, shovel and hoe — and produces the
regular vanilla `STONE_*` item:

| Variant | Ingredient | Tools |
|---|---|---|
| Granite | `GRANITE` | Sword, Pickaxe, Axe, Shovel, Hoe |
| Andesite | `ANDESITE` | Sword, Pickaxe, Axe, Shovel, Hoe |
| Diorite | `DIORITE` | Sword, Pickaxe, Axe, Shovel, Hoe |
| Tuff | `TUFF` | Sword, Pickaxe, Axe, Shovel, Hoe |

Recipes are registered on startup under plugin-owned `NamespacedKey`s of the form
`betterminecraft:better_vanilla_recipe_<variant>_<tool>`, and the number of successful/failed registrations is
written to the server log.

**Recipe discovery** — when a player right-clicks a crafting table, the plugin checks their inventory against
every registered recipe and unlocks the ones they currently have the materials for, so the recipes show up in
the vanilla recipe book.

### Double door and fence gate synchronisation

Opening one half of a double door opens its partner. A neighbouring door counts as a partner when it is the
same block type, faces the same direction, and has the opposite hinge side. Both the upper and lower halves are
updated. Fence gates synchronise with directly adjacent gates on all four horizontal faces.

### Right-click crop harvesting

Right-clicking a fully grown crop harvests it and drops the produce naturally. Supported crops:

| Crop | Drop | Replant seed |
|---|---|---|
| Wheat | `WHEAT` ×1 | `WHEAT_SEEDS` |
| Carrots | `CARROT` ×1–2 | `CARROT` |
| Potatoes | `POTATO` ×1–2 | `POTATO` |
| Beetroots | `BEETROOT` ×1 | `BEETROOT_SEEDS` |
| Nether Wart | `NETHER_WART` ×1–2 | `NETHER_WART` |
| Cocoa | `COCOA_BEANS` ×1 | `COCOA_BEANS` |

When `listeners.replantCrops` is enabled, one seed item is taken from the player's inventory and the crop is
reset to age 0. If the player has no seed, the crop block is cleared instead. With the option disabled, the
crop block is always cleared after harvesting.

## Configuration

`plugins/BetterMinecraft/config.yml`:

```yaml
listeners:
  replantCrops: true
```

| Key | Default | Description |
|---|---|---|
| `listeners.replantCrops` | `true` | Consume a seed and replant the crop after right-click harvesting. |

Defaults are copied on first startup; the file is not reloaded at runtime, so restart the server after editing.

## Building

Requires JDK 21 and Maven. The Spigot API is a `provided` dependency and is pulled from the SpigotMC snapshot
repository.

```bash
mvn clean package
```

The shaded jar is written to `target/BetterMinecraft-1.0-SNAPSHOT.jar`. Resource filtering substitutes the
project version into `plugin.yml`.

## Installation

1. Build the jar (or download a release).
2. Drop it into your server's `plugins/` directory.
3. Start the server — `config.yml` is generated automatically.

## Project layout

```
src/main/java/ch/framedev/betterminecraft/
  BetterMinecraft.java    # JavaPlugin entry point, wires up manager and listeners
  RecipesManager.java     # Registers stone-variant recipes, inventory-based craftability check
  PlayerListeners.java    # PlayerInteractEvent: recipe discovery, doors/gates, crop harvesting
  DoorPhysics.java        # Double door / fence gate synchronisation logic
src/main/resources/
  config.yml
  plugin.yml
todo.txt                  # Long-form roadmap for the full Vanilla+ feature set
```

## Roadmap

`todo.txt` tracks the planned scope in detail — graves, homes and waystones, skills, quests, custom mobs and
mini-bosses, world events, seasons, backpacks, claims and more, along with a migration checklist for
Minecraft 26.2 (Java 25, Spigot API 26.2). Everything currently implemented lives in the Features section above.

## Author

FrameDev
