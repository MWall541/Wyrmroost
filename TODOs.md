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
- Using Static Data handlers rather than making it instance based.