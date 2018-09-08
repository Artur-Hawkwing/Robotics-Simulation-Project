This repo is meant to house code I used to build a simulation of the FTC game Velocity Vortex.

-----------------------------------------------------Background
The simulations was written and modeled by Jeffrey Sardina for the team Buffalo Wings (team number 5015), and the latest version (housed here) also includes a Robopuffs (9049)-modeled robot.
This project was not meant to be scaled to more than two players, and so the code structure reflect a lack of necessity (or planning for) scaling. For more large-scale adaptations of a FTC simulation, I would recommend altering the code structure so that it be more easily scaled.

-----------------------------------------------------Creation Tools
I modeled all 3-D figures, and created pertinent animations, using Blender. 2-D art, with the exception of the Velocity Vortex logo (found online from FTC) were created using Blender rendering of a 3-D model to a 2D image. To code the game, I used JMonkey Engine 3.0.

-----------------------------------------------------To those who would seek to create a similar simulation
I highly suggest using Blender for 3D models and animations, since it is very powerful and very easy. You can find Blender downloads here (https://www.blender.org/download/). My favorite Blender tutorials is the Noob to Pro tutorial published on Wikibooks (https://en.wikibooks.org/wiki/Blender_3D:_Noob_to_Pro). It covers everything you could ever need, and you can start / skip to anywhere without difficulty once you get the basics of what it teaches.

For a game engine, there are a few options I like. 

JME3 is what I used to create this simulation. Unlike unity, is emphasized full-on coding over scripting for game objects. JME3 can directly import Blender files. As always, use UV mapping or plain colors (rather than generated textures) in Blender models for the best, most consistent results in model display in-game. It runs on Java and has served me well in many projects. Note that the more recent versions (belong 3.0) revamped their lighting, so the lighting configuration I have here will not work in modern JME3 distros. However, I love the new lighting system—just make sure you have both a directional and an ambient light. Also, to be compatible, set the specular color of the object to white (the default) in Blender. That will render best in JME3, by my experience. JME3 has a Scene composer (drag-and-drop for 3D elements), but I have found Unity's to be easier to use.

Unity is amazing. I did not use it, though it is very easy to pick up and the ability to create and control scenes by dragging models and scripts in is phenomenally implemented. It runs on C# (or JavaScript). Unity can directly import Blender files. As always, use UV mapping or plain colors (rather than generated textures) in Blender models for the best, most consistent results in model display in-game. 

BGE (the Blender Game Engine) is very nice, using visual programming (as well as Python scripts) to create a game. It is built-in to Blender and can run on Mac, Linux, and Windows (note you must compile it on the OS you want it to run on), and is very easy to use. It is surprisingly powerful, though less popular than many other game engines. Of course, neither Unity nor JME3 can come close to rivaling BGE in how easy it is to place objects in 3D space with the scene composer. Blender is, after all, a full-fledged 3D modeling tool. As always, use UV mapping or plain colors (rather than generated textures) in Blender models for the best, most consistent results in model display in-game.

-----------------------------------------------------Some useful links
Downloads:
Blender: 
https://www.blender.org/download/

Unity: 
https://store.unity.com/download?ref=personal

JME3: 
https://github.com/jMonkeyEngine/sdk/releases/tag/v3.2.1-stable-sdk3

Docs / Tutorials: (I have only listed those I have used and like)
Blender:
https://docs.blender.org/manual/ko/dev/game_engine/index.html
https://www.youtube.com/watch?v=23m9nz575Ag
https://en.wikibooks.org/wiki/Blender_3D:_Noob_to_Pro

Unity:
https://docs.unity3d.com/ScriptReference/

JME3:
https://wiki.jmonkeyengine.org/jme3.html
https://javadoc.jmonkeyengine.org/overview-summary.html

