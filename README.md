# HexKinetics

HexKinetics:


Utility Patterns:

	X1. Sniper's Distillation (vector, vector) - Architects Distillation but it gives the position of pixel it hits. Works for everything except air, even for entities.
	Pattern "weqaqded"
	Release 0.5.0


	X2. Argument Distillation (vector, number) - Deletes vector and number, then pushes the number in vector indexed by the given number to the top of the stack.
	Pattern "eeeeeq"
	Release 0.5.0


	X3. Optician's Distillation (vector, vector) - Takes the vector, second vector and third vector off the stack and puts a vector reflecton of first vector using second vector (a normal) that defines the plane of reflection.
	Pattern "qqqqqdqqqqq"
	Release 0.5.0


	X4. Hadamard's Distillation (vector, vector) - Elements corresponding to the same columns of given vectors are multiplied together to form a new vector.
	Pattern "awaqawa"
	Release 0.6.0


	X5. Imprecision Purification (number) - Rounds given number.
	Pattern "aadeeaa"
	Release 0.6.0


	X6. Inertia Purification (entity) - Checks if entity is in state of inertia or has gravity.
	Pattern "daad"
	Release 0.6.0



Great Spells:


	X1. Greater Impulse (entity, number, vector) - Regular impulse, but it additionaly makes an entity not affected by gravity or friction for certain amount of time. Which when combined can make player go in a certain direction with given speed of a vector for given amount of time. Although it can be difficult to move on your own in this state of inertia.
	Costs units of Amethyst Dust equal to the lenght of a vector to the power of 2, plus time * two.
	Pattern "wqeqaaeeeweeeaaqeqqaaq".
	Release 0.5.0



Spells:

	X1. Lesser Teleportation (entity, number/vector) - Takes entity and number or vector off the stack, then teleports the entity to its vector but with all fractions changed to given number, or fractions of every component changed to number in responding component in given vector. For example if given 5 and entity is on [35.11, 9.56, -10] it teleports it to [35.05, 9.05, -10.05]. It doesn't take numbers from 100 and up.
	Costs 1/5 of amethyst dust.
	Pattern "edqdewqaeaq"
	Release 0.5.0


	X2. Rotate (entity, vector) - Accepts an entity and a vector, rotates the entity in direction given vector is pointing.
	It costs 1/8 amethyst dust.
	Pattern "qqqadeeed"
	Release 0.5.0


	X3. Add Gravity (entity, number) -  It adds given number of blocks to fall distance perceived by the entity, resulting in increased fall damage upon landing.
	Costs that many blocks as there is blocks the spell will add or subtract from the fallen distance, plus 1/5 of amethyst dust for every block in fall distance perceived by target entity .
	Pattern "aadedade"
	Release 0.6.0


	X4. Rotate II (entity, vector) -  Accepts an entity and a rotation, rotates the entities movement direction.
	Costs 3/4 of amethyst dust.
	Pattern "eeedaqqqa"
	Release 0.6.0


	X6. Momentum Swap (entity, entity) - Swaps motion of two entities.
	Costs 1 and 3/4 of amethyst dust.
	Pattern "adaadaqedaddad"

