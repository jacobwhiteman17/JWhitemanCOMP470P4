package com.company;/*
MODIFY THIS CLASS TO MATE TWO CARS AND MUTATE CARS

Your code will go in methods BREED() and MUTATE().  Find the TODO lines.
  you will call these methods from your code in GeneticCars

A "Car" is a collection of balls and links
*/

public class Car
{
	//how many balls in the car
	int nodes;
	//position of balls
	int[] balls_x;
	int[] balls_y;
	//for every ball i,j  true if there's a link between them
	boolean[][] linkmatrix;

	//these are set by the setScore function after a simulated race
	double score_position;		//how far did the car get
	double score_iterations;	//how long did it take the car to reach the end

	//the simulated world the car is running in.  null until the car is raced.
	World world;

	//construct a car with nodes balls and random links
	//every ball is placed between (5,5) and (50,50)

	Car thisCar = this;

	public Car(int nodes)
	{
		this.world=null;
		this.nodes=nodes;

		balls_x=new int[nodes];
		balls_y=new int[nodes];
		linkmatrix=new boolean[nodes][nodes];

		//randomly place balls between (5,5 and 50,50)
		for(int i=0; i<nodes; i++)
		{
			balls_x[i]=randint(5,50);
			balls_y[i]=randint(5,50);
		}

		//assign a link between two balls with probability 1/3
		for(int i=0; i<nodes; i++)
		{
			for(int j=0; j<nodes; j++)
			{
				if(randint(1,3)==1)
					linkmatrix[i][j]=true;
			}
		}
	}

	//return the average x position of the nodes
	//this is called only after the car has been raced
	public double getPosition()
	{
		int sum=0;
		for(int i=0; i<nodes; i++)
			sum+=world.getBall(i).position.x;
		return sum/nodes;
	}

	//set the car's score
	//this is called once the race simulation is done
		//don't call it before then or you'll get a nullpointerexception
	public void setScore(int iterations)
	{
		score_position=getPosition();
		if(score_position>world.WIDTH)
			score_position=world.WIDTH;
		score_iterations=iterations;
	}

	//build the car into the world: create its balls and links
	//call this when you're ready to start racing
	public void constructCar(World world)
	{
		this.world=world;
		for(int i=0; i<nodes; i++)
		{
			world.makeBall(balls_x[i],balls_y[i]);
		}
		for(int i=0; i<nodes; i++)
			for(int j=0; j<nodes; j++)
				if(linkmatrix[i][j])
					world.makeLink(i,j);
	}

	//returns a random integer between [a,b]
	private int randint(int a, int b)
	{
		return (int)(Math.random()*(b-a+1)+a);
	}

	//TODO
	//YOU WRITE THIS FUNCTION
	//It should return a "child" car that is the crossover between this car and parameter car c
	public Car breed(Car c)
	{
		Car child=new Car(nodes);
		//YOUR WORK HERE
		//Choose a random crossover point.
		int crossPoint = randint(5,nodes);
		//Also choose a car to go first
		for(int i=0;i<crossPoint;i++)
		{
			// copy the balls from the first car's balls_x and balls_y to the child
			child.balls_x[i] = thisCar.balls_x[i];
			child.balls_y[i] = thisCar.balls_y[i];
		}
		// after the crossover, copy the balls_x and balls_y from the second car to the child
		for(int i=crossPoint;i<nodes;i++)
		{
			child.balls_x[i] = c.balls_x[i];
			child.balls_y[i] = c.balls_y[i];
		}
		//pick a new crossover point, then do the same with the linkmatrix
		int crossPoint2 = randint(5,nodes);
		if(crossPoint2 == crossPoint)
		{
			crossPoint2++;
			if(crossPoint2 > nodes)
			{
				crossPoint2 = crossPoint2-2;
			}
		}
		for(int i=0;i<crossPoint2;i++)
		{
			child.linkmatrix[i] = thisCar.linkmatrix[i];
		}
		for(int i=crossPoint2;i<nodes;i++)
		{
			child.linkmatrix[i] = c.linkmatrix[i];
		}

		return child;
	}

	//TODO
	//YOU WRITE THIS FUNCTION
	//It should return a car "newcar" that is identical to the current car, except with mutations
	public Car mutate(double probability)//check this out if it things seem off
	{
		Car newcar=new Car(nodes);

		//YOUR WORK HERE
		//You should copy over the car's balls_x and balls_y to newcar
		newcar.balls_x = thisCar.balls_x;
		newcar.balls_y = thisCar.balls_y;
		//with probability "probability",
		if(Math.random()<probability)
		{
			//change the balls_x and balls_y to a random number from 5 to 50
			for(int i: newcar.balls_x)
			{
				i = randint(5,50);
			}
			for(int i: newcar.balls_y)
			{
				i = randint(5,50);
			}
			//Then copy over the links
			for(int i = 0; i<linkmatrix.length;i++)
			{
				for (int j = 0; j < linkmatrix[i].length;j++)
				{
					newcar.linkmatrix[i][j] = thisCar.linkmatrix[i][j];
				}
			}

			//with probability "probability", set the link to true/false (50/50 chance)
			if(Math.random()<probability)
			{
				for(int i=0; i< newcar.nodes; i++)
					for(int j=0; j< newcar.nodes; j++)
						newcar.linkmatrix[i][j] = !newcar.linkmatrix[i][j];
			}
		}
		return newcar;
	}
}
