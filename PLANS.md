"Binary-Breaking" 1.15 Changes and plans
========================================
Some these probably won't work, some may. who knows, exactly why theyre plans

- massive clean up (util package looks like a dead whale's guts exploded)

- make data generators easier to use and actually not make peoples eyes desintegrate from cancer

- Rework the animation system; make it similar to goal tasks (debating if this is even gonna work or not because logical sides...):
    * `animate(Entity, Model, Tick (int))` Method - client side, actual animation of the model. Pass in the entity, entity model, and the current animation tick
    * `tick(Entity, Tick (int)) Method` - common, controls what to do during the animation. Actions should be done through tick checks. eg. `if (tick == 2) playSound(...)`
    * some priority system similar to goals? add flags to if it overrides idles, movements, etc.
    * OR, make a new goal selector ("animationSelector") and create a base AnimationGoal with the above functions.
        Have the selector run on both client and server for best control
    * for simpler animations, just use interpolation. e.g sitting and sleeping
- delete this file