Plans and TODO's
================
Some of these probably won't work, some may. who knows, exactly why theyre plans

- Universatility:
    - Yeah, about this. Wait for 1.17. Apparently everything entity rendering is getting completely fucked so. ~~*Maybe* Find a way to remove the need for entity renderers?
      (Make a "DefaultRenderer" where textures can be obtained thru the model class?) Of course, there'll be way(s) to
      override this for alternative needs.~~
      - ~~Entity Models should have access to the entity, partialTicks, ageInTicks, etc. through fields~~
          - ~~It's annoying having to pass around these things, especially when runtime-animating where restarts are constantly required~~
- Fix Animations to interpolate TO positions, not FROM.
- Add Model Physics. (tails etc)
- Adding a concept for Goals, essentially disabling certain ones when tamed:
    - Either remove and re-apply goals when tamed
    - OR remove only the ones we want when tamed (would need to be stored)
    - This is beneficial as it will disallow certain actions from a dragon from happening when tamed, and even removes more goals so it's more efficient.