"Binary-Breaking" 1.15 Changes and plans
========================================
Some these probably won't work, some may. who knows, exactly why theyre plans
- Change the data system to make more sense. data keys should start with capital letters. Ex: "HomePos",
    and remove data fixers - This whole thing WILL remove all current wyrmroost entities in world.
    tbh, not my problem. this is a caution all users take when updating.
    
- Add a "data helper" system to make reading, writing, and OPTIONAL dataManager registering (`DataController`?)

- Completely re-work the staff complete with an GUI and properly rendering outlining the bounded dragon
    currently not "exactly" possible with <1.15 limiting rendering
    
- Make many, MANY more things universal

- contemplate a client package - follow vanilla bullshit, sEpErAtE cLiEnT fRoM sErVEr

- massive clean up (util package looks like a dead whale's guts exploded)

- make data generators easier to use and actually not make peoples eyes desintegrate from cancer

- "Special" dragons will hook off of "Variant" and instead use -1 as the value. No sense in having a boolean for another texture change...

- Rework the animation system; make it similar to goal tasks (debating if this is even gonna work or not because logical sides...):
    * `animate(Entity, Model, Tick (int))` Method - client side, actual animation of the model. Pass in the entity, entity model, and the current animation tick
    * `tick(Entity, Tick (int)) Method` - common, controls what to do during the animation. Actions should be done through tick checks. eg. `if (tick == 2) playSound(...)`
    * some priority system similar to goals? add flags to if it overrides idles, movements, etc.
    * OR, make a new goal selector ("animationSelector") and create a base AnimationGoal with the above functions.
        Have the selector run on both client and server for best control
    * for simpler animations, just use interpolation. e.g sitting and sleeping

- Dedicated "`DragonInventoryController`"
    * extends `ItemStackHandler`? all the functionality is there, could just add our extra bits
    * make a method dedicated to it in AbstractDragonEntity (`onInvSlotChanged(int slot, ItemStack changedStack)`?) to handle changes e.g: `if (slot == 1) setSaddled(changedStack.isEmpty()))`
    * some sort of dummy instance for those without inventories
    * don't sync every item to the client. just sync whatever's needed. If we need to render a saddle,
     make a datamanager entry for it and change it in `#onInvSlotChanged`. Way cleaner, and organized, and handled in one
     place.
- piss everyone off
- delete this file