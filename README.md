VanillaBreak
============

Reintroduces a handful of useful vanilla bugs that Forge oversteps its bounds by patching, with a focus on game-mechanic serverside bugs.

It goes without saying that this is unabashedly a coremod, however, all patches are disableable in the configuration file. You will have to restart the game though.

Forge should be a modloader, not a bugfix mod of its own right. Some of Forge's patchery makes the loader quite useless for creating redstone devices and inventions that are intended to be wholly compatible with vanilla.

## Featureset

* Reintroduce "chestloading", where chests load chunks as they try to determine their doublechest-ness.
* Reintroduce "village loading", where villages load themselves when verifying the existence of doors, notably when the game starts up for the first time.
* Change Forge's default dimension unload queue time to vanilla's 300 ticks, up from 0. This allows for keeping the overworld/etc loaded by firing things through portals faster than every fifteen seconds. (This is configurable in Forge itself, but VanillaBreak changes the default value of the config option.)
* *(untested)* Remove the mod ID check and always use the vanilla "does this block block opening of a chest" logic for all blocks, not just vanilla blocks. Fully reintroduces MC-378. You're welcome, LexManos, I know you like this bug.

#### Planned featureset/todo list

* Readd MC-99321 "Hoppers cannot pull items from double chests if second chest is blocked", fixed in 1.13, but it shouldn't be in Forge. I tried but wasn't successful.
* Investigate https://github.com/MinecraftForge/MinecraftForge/pull/5100
* Possibly break some of https://github.com/MinecraftForge/MinecraftForge/pull/5160 - I think this is the infamous "entities dupe/delete themselves across borders" bug, and while the patch is useful, it could provide a false sense of security that things will be working in vanilla. Although these patches are very new
* Rebreak more of https://github.com/MinecraftForge/MinecraftForge/pull/4689 - I got the villages and the chests but there's still a bunch more to whack with the asmhammer
* Rebreak item frame dupe? Lol, it's vanilla!
* Break skeleton/chicken jockeys, they spawn in Forge but not in 1.12...
* Browse the patchset more!! Gh seems to like to hide some results when I just search for "forge: fix"

## License

Too lazy to paste in proper license info right now but it's Mozilla Public License 2.0, like 90% of my mods

## Rant

If you'll let me soapbox for a minute, I think part of the reason is this harmful "no coremods, ever" mentality. People are afraid of coremodding since Lex is unconditionally such a dick about it, so they PR their changes instead. Which is great in the cases of more Forge hooks for modders to use and enjoy. But then people throw bugfixes in too, since "oh, i don't wanna coremod just for a bug fix, because coremods are Bad"... and now the modloader-without-any-actual-mods-installed *plays significantly differently than the vanilla game.* What? Not to mention increasing the patch weight for not much of a reason, since now updating Forge requires checking on the status of a zillion Mojira tickets. And most people simply don't care because they don't see how things like village chunkloading are valid and useful game mechanics. Wah wah wah, just install some chunkloading mod and shut up, no we don't care about vanilla compat with 0 mods loaded. Ugh. What's even the point of including the mod loading features if you're packing a bunch of mods in the loader itself :wink:

***ANYHOO***

I like Forge apart from that little bit. Lol.