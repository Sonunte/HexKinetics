# HexKinetics

Here are my notes on this project. Here are my plans for the future. If you are looking for this addon documentation go to https://sonunte.github.io/HexKinetics/ instead. Remember all unimplemented patterns are only "maybies" and are likely to change in the future.


How to read this:

1. These notes are divided into:
- Utility Patterns (Where are all patterns that aren't spells)
- Great Spells (Where all great spells are stored)
- Spells (Here are all spells that aren't great spells)
2. Every description has:
- Name
- What arguments it takes
- Description
- Pattern (In angles)
- Release (When it was released / will be released)
- Sources (Here I have all sources of my ideas, numerated by their impact and importance.)
- Sometimes it can have a note written under it.


HexKinetics:



Utility Patterns:

	X1. Sniper's Distillation (vector, vector) - Architect's Distillation but it  returns the precise hit position. Very versatile, it can be used on various entities and materials with the exception of air.
	Pattern "weqaqded"
	Release 0.5.0
	Sources:
	1. https://discord.com/channels/936370934292549712/940747085622419456/1097020543439491173


	X2. Vehicle Purification (entity)  - Pops the entity off the stack and pushes a reference to the vehicle it is currently occupying. Returns Null if the entity is not riding any vehicle.
	Pattern "qqdeewee"
	Release 0.7.0
	Sources
	1. https://discord.com/channels/936370934292549712/940747085622419456/1107860505445552179


	X3. Optician's Distillation (vector, vector) - This operation retrieves two vectors from the stack and calculates a vector reflection of the first vector based on the second vector, which acts as a normal and defines the reflection plane.
	Pattern "qqqqqdqqqqq"
	Release 0.5.0
	Sources:
	1.Me


	X4. Hadamard's Distillation (vector, vector) - Elements corresponding to the same columns of given vectors are multiplied together to form a new vector.
	Pattern "awaqawa"
	Release 0.6.0
	Sources:
	1. https://forum.petra-k.at/viewtopic.php?t=19
	2. https://discord.com/channels/936370934292549712/960215808682905610/1105907177580920853


	X5. Imprecision Purification (number) - Number or elements in given vector are rounded to the nearest whole.
	Pattern "aadeeaa"
	Release 0.6.0
	Sources:
	1. Me


	X6. Inertia Purification (entity) - Determines whether the entity is currently in a state of inertia or if it is subject to the force of gravity. If the entity is found to be in a state of inertia, purification returns true.
	Pattern "daad"
	Release 0.6.0
	Sources:
	1. Me


	X8. Alidade's Purification II (entity) - Alidade's Purification but it returns the direction player is looking in form of full, round vectors, as (0,1,0).
	Release 0.5.0
	Sources:
	1. Me


	9. Bezier's Distillation ([vector], num) - Calculates a point on a Bezier curve given a list of control points and a number between 0 and 1.
	Release 0.8.0
	Sources:
	1. https://discord.com/channels/936370934292549712/986383249456644166/1108732957054730300
	Only maybe, it is already a macro


	X10. Jockey Purification (entity)  - Pops the entity (vehicle) off the stack and pushes a reference to the entity that is currently occupying it. If vehicle has more than one passenger it returns the first one.
	Pattern "qqdeeaedeaee"
	Release 0.7.0
	Sources
	1. https://discord.com/channels/936370934292549712/940747085622419456/1107860505445552179


	X11. Shooter Purification (entity)  - Pops the entity off the stack and pushes coordinates of entity that shot given entity. Returns Null if the entity isn't a projectile.
	Pattern "aadedade"
	Release 0.7.0
	Sources
	1. https://discord.com/channels/936370934292549712/940747085622419456/1107860505445552179


	12. Zone Distillation II - Take a position and maximum distance on the stack, and combine them into a list of all block positions that are within that range.
	Pattern "qqqqqeddedq"
	Release 0.7.5
	Sources:
	1. Me



Great Spells:


	X1. Twokai's Ideal Condition (entity, number) - Makes an entity not affected by gravity or friction for certain amount of time. Which when combined with Impulse can make player go in a certain direction with given speed of a vector for given amount of time. Although it can be difficult to move on your own in this state of inertia.  
	Costs units of Amethyst Dust equal to amount of time given times two. Unless time is 1 or less then it will cost only time.
	Pattern "wwqqqwadaadawqqqww"
	Release 0.5.0
	Sources:
	1. https://discord.com/channels/936370934292549712/1073666769551642624/1073669150372798504


	2. Chronos' Gambit (entity) - Stops given entity from ticking and doing any action for one tick. When cast repeatedly within a single instance, it extends the duration of the entity's immobilization. Any motion and knockback inflicted upon that entity will be preserved, and upon the conclusion of the spell, it will be applied simultaneously.
	Costs 1/10 of amethyst dust.
	Pattern "wwawawwwawawwdwdwwdw"
	Release 0.7.5
	Sources:
	1. https://discord.com/channels/936370934292549712/940747085622419456/973205603876995082
	2. https://discord.com/channels/936370934292549712/1011455473528098857/1108674816296357911


	3. Energy Conversion (entity, vector) - Converts kinetic energy of an entity into potential energy. Weirdly potential energy somehow can react with media and charge it. When put into Amethyst Block it can crush it into many amethyst shards, more then it was made from.
	Costs 1 charged amethyst.
	Pattern "wawqwawawewadaqeqadawaeaqaawaeaeaeaddawqqeqqwqaaqeaeae"
	Release 0.7.5
	Sources:
	1. Me


	X4. Greater Translocation (vector, vector) - Works like Greater Teleport and costs the same, but allows me to move blocks instead of entities. It works on all possible blocks except blocks that can store something, maybe that is too much to handle for Greater Translocation. When translocated to a harder block, the original block is completely removed. If moved to a softer block, the destination block is destroyed, similary to using Break Block on it. In the case of blocks with equal hardness, the translocated block remains in place. Nature seems to struggle to find a solution in such instances, rendering the translocation ineffective, yet it still consumes amount of media needed for normal translocation.
	Costs 10 charged amethyst.
	Pattern "eeqeeqeeeqeeqdeeqeqqwqqqeeqeqqwqq"
	Release 0.7.0
	Sources:
	1. Me


	X5. Propulsion (entity, number, vector) - This spell utilizes a vector to impulse a target entity, applying a continuous force over a duration determined by a 	given number. The force is exerted in intervals of 5 ticks (1/4 of a second). The duration is limited to a range of 0 to 100 units of time.
	Costs length of the vector squared times time or if length of this vector is less then one it will cost one * time.
	Pattern "wqeqaaeeeweeeaaqeqqaaq"
	Release 0.7.0
	Sources:
	1. Me
	2. https://discord.com/channels/936370934292549712/1073666769551642624/1073667621976817665



Spells:

	X1. Lesser Teleport (entity, number/vector) - Takes entity and number or vector off the stack, then teleports the entity to its vector but with all fractions changed to given number, or fractions of every component changed to number in responding component in given vector. For example if given 5 and entity is on [35.11, 9.56, -10] it teleports it to [35.05, 9.05, -10.05]. It doesn't take numbers from 100 and up.
	Costs 1/5 of amethyst dust.
	Pattern "edqdewqaeaq"
	Release 0.5.0
	Sources:
	1. Me


	X2. Rotate (entity, vector) - Takes an entity and a vector as inputs and performs a rotation of the entity, to the direction indicated by the vector.
	It costs 1/8 amethyst dust.
	Pattern "qqqadeeed"
	Release 0.5.0
	1. https://discord.com/channels/936370934292549712/950847275549229086/1101920220714647602


	X3. Add Gravity (entity, number) -  It adds given number of blocks to fall distance perceived by the entity, resulting in increased fall damage upon landing.
	Costs that many blocks as there is blocks the spell will add or subtract from the fallen distance, plus 1/5 of amethyst dust for every block in fall distance perceived by target entity .
	Pattern "eeeeeddwdaddad"
	Release 0.6.0
	Sources:
	1. Me


	X4. Rotate II (entity, vector) -  Accepts an entity and a vector, rotates the entity motion direction in direction of a vector. If rotated to [0, 0, 0] it will completly stop its velocity.
	Costs 3/4 of amethyst dust.
	Pattern "eeedaqqqa"
	Release 0.6.0
	Sources:
	1. https://discord.com/channels/936370934292549712/1073666769551642624/1073668349722103839
	2. https://discord.com/channels/936370934292549712/1073666769551642624/1073981253340123196


	X5. Rotate: Block (vector, vector) - Accepts a vector and direction vector, rotates block at a given vector.
	Costs 1/8 of amethyst dust.
	Pattern "qqqqqaqqqwadeeed"
	Release 0.7.0
	Sources:
	1. https://discord.com/channels/936370934292549712/1011455473528098857/1105519312141287524


	X6. Momentum Swap (entity, entity) - Swaps the motion of two entities.
	Costs 1 amethyst dust.
	Pattern "adaadaqedaddad"
	Sources:
	1. https://discord.com/channels/936370934292549712/1073666769551642624/1073981253340123196
	2. Me


	7. Impulse II (entity, vector) - Pulls the entity to a given vector and not any further. Costs lenght of this vector squared.
	Pattern "awwqqqwwaqww"
	Release 0.7.5
	1. Me
	2. https://discord.com/channels/936370934292549712/1073666769551642624/1073668073980182648


