Plans and TODO's
================
Some of these probably won't work, some may. who knows, exactly why theyre plans

- Universatility:
    - *Maybe* Find a way to remove the need for entity renderers?
      (Make a "DefaultRenderer" where textures can be obtained thru the model class?) Of course, there'll be way(s) to
      override this for alternative needs.
    - Entity Modulus system
        - Modules that entities can have to store data, read, write, and logic.
- Fix All flyers ai and movements
- Fix Animations to interpolate TO positions, not FROM.
- Optimize the fuck out of attacks. 
- Add Model Physics. (tails etc)
- Better Sleep System

1.16
====
Overall its a shitshow for forge and as we keep updating its getting increasingly more annoying to work with
forge AND mojang. Nothing is getting mapped, Pull Requests aren't getting reviewed, and things are just flat out broken.
Half the hooks don't even work anymore and the modloader as a whole is looking terrible.
Forge is becoming once again a slow ecosystem and a quarter of the api has gotten inefficent and
sometimes unreliable for even the best of modders and programmers.
Sandbox API is looking really good right about now, and yeah, i'm not joking. for once.

- EntityAttributes are fucked af: register entity attributes alongside EntityType registration
    - Perhaps a map that is iterated at entity type registration? not sure.
    - Additional Notes:
        - why?
            - I have mixed Feelings: Attributes was never used as intended, and they needed to be re-done.
            welp wish fulfilled, and lol it fucking sucks. This is what happens when you want something from mojang
            of all people...
        - Static Registeration?
            - Controversial, nobody has experimented enough with it to know enough about that.
            - Is it safe to register during parallel mod loading?
            - Would it be as easy as using a callback on RegistryObject static initialization?

- AbstractBlock wtf??
    - Not a clue why. Tho, block properties are being worked on afaik, so the tracks aren't stopping.