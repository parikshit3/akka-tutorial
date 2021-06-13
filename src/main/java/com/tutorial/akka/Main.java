package com.tutorial.akka;

import com.tutorial.akka.ManagerBehavior.Command;

import akka.actor.typed.ActorSystem;

public class Main {
	
	/*public static void main(String[] args) {
		ActorSystem<String> actorSystem = ActorSystem.create(FirstSimpleBehavior.create(), "firstActorSystem");
		actorSystem.tell("say hello");
		actorSystem.tell("who are you?");
		actorSystem.tell("create a child");
		actorSystem.tell("This is any message");
	}*/
	
	public static void main(String[] args) {
		ActorSystem<ManagerBehavior.Command> actorSystem = ActorSystem.create(ManagerBehavior.create(), "firstActorSystem");
		actorSystem.tell(new ManagerBehavior.InstructionCommand("start"));
		
	}

}
