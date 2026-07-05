# Recipe Shuffler

**Shuffle every crafting recipe in your world — vanilla and modded.**

Recipe Shuffler takes every crafting recipe in your modpack and randomly reassigns their outputs, turning progression into a fun guessing game. Works with any mod that adds recipes, not just vanilla Minecraft.

***

## ✨ Features

*   🔀 **Shuffles ALL recipes** — vanilla and every modded recipe in your pack
*   🧩 **Full modpack compatibility** — recipes are generated per-mod namespace, so shuffled outputs correctly override the originals (even in heavily modded packs)
*   🌱 **Seed-based shuffling** — the same seed always produces the same shuffle, and the mod remembers your world's seed automatically
*   ♻️ **Auto rebuild & reload** — the shuffle regenerates automatically if your config or mod list changes, no manual steps needed
*   🚫 **Blacklists** — exclude specific items, recipes, or entire mods from being shuffled
*   🎲 **Chaos Mode** — an even more unpredictable shuffle option for the brave
*   🛠️ **In-game commands** — reshuffle, look up, and debug recipes without leaving your world

***

## 📥 Installation

1.  Requires **Forge** for **Minecraft 1.20.1**
2.  Drop the `recipe_shuffler-1.0.4.jar` into your `mods` folder
3.  Launch the game — the shuffle generates automatically on first world start

***

## 💬 Commands

| Command              |Description                                                     |
| -------------------- |--------------------------------------------------------------- |
| <code>/rshhelp</code> |Shows the list of available commands                            |
| <code>/rshreshuffle</code> |Reshuffles all recipes using a new random seed                  |
| <code>/rshreshuffle &lt;seed&gt;</code> |Reshuffles using a specific seed (repeatable results)           |
| <code>/rshfind &lt;item&gt;</code> |Finds what recipe now produces a given item                     |
| <code>/rshrecipe &lt;recipe&gt;</code> |Looks up what a specific recipe now crafts                      |
| <code>/rshdebug</code> |Prints debug info about the current shuffle state               |
| <code>/rshcheck</code> |Verifies the generated datapack is active and healthy           |
| <code>/rshdump</code> |Dumps the full shuffled recipe list for inspection              |
| <code>/dkrsreload</code> |Legacy alias for reshuffling (kept for backwards compatibility) |

***

## ⚙️ Configuration

Config file: `config/recipe_shuffler-common.toml`

| Option               |What it does                                       |
| -------------------- |-------------------------------------------------- |
| <code>randomize_recipes</code> |Master toggle — turns recipe shuffling on/off      |
| <code>chaos_mode</code> |Enables a more extreme, less predictable shuffle   |
| <code>item_blacklist</code> |Items that should never appear in shuffled outputs |
| <code>recipe_blacklist</code> |Specific recipes to leave untouched                |
| <code>mod_blacklist</code> |Entire mods to exclude from shuffling              |
| <code>show_welcome_message</code> |Shows a chat message when the shuffle loads        |

***

## ❓ FAQ

**Does this work with modded recipes?** Yes — this was the main fix in the latest update. Shuffled recipes are now written into each mod's own namespace, so they properly override the originals instead of only affecting vanilla recipes.

**Will I get the same shuffle every time?** Yes, if you use the same seed. The mod remembers your world's seed automatically, or you can set one manually with `/rshreshuffle <seed>`.

**Can I stop certain items from being shuffled?** Yes — use `item_blacklist`, `recipe_blacklist`, or `mod_blacklist` in the config to protect specific items, recipes, or whole mods.

***

## 🧾 Version

**Current version:** `1.0.4` **Mod ID:** `recipe_shuffler` **Loader:** Forge **Minecraft:** 1.20.1
