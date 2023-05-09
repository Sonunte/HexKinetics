# HexKinetics



Utility Patterns:

	1. Sniper's Distillation (vector, vector) - Architects Distillation but it gives the position of pixel it hits. Works for everything except air, even for entities.
	Pattern "weqaqded"


	2. Argument Distillation (vector, number) - Deletes vector and number, then pushes the number in vector indexed by the given number to the top of the stack.
	Pattern "eeeeeq"


	3. Reflection Distillation (vector, vector) - Takes the vector, second vector and third vector off the stack and puts a vector reflecton of first vector using second vector (a normal) that defines the plane of reflection.
	Pattern "qqqqqdqqqqq"
	
	


Spells:

	1. Lesser Teleportation (entity, number) - Takes entity and vector off the stack, then teleports the entity to its vector but with all fractions changed to given number. For example if given 5 and entity is on [35.11, 9.56, 10] it teleports it to [35.05, 9.05, 10.05]. It doesn't take numbers from 100 and up.
	Costs 1/5 of amethyst dust.
	Pattern "edqdewqaeaq"


	2. Rotate (entity, vector) - Accepts an entity and a vector, rotates the entity in direction of a vector.
	It costs 1/8 amethyst dust.
	Pattern "qqqadeeed"



Great Spells:


	1. Greater Impulse (entity, number, vector) - Regular impulse, but it additionaly makes an entity not affected by gravity or friction for certain amount of time. Which when combined can make player go in a certain direction with given speed of a vector for given amount of time. Although it can be difficult to move on your own in this state of inertia.
	Costs units of Amethyst Dust equal to the lenght of a vector to the power of 2, plus time to the power of 2.
	Pattern "wqeqaaeeeweeeaaqeqqaaq".
