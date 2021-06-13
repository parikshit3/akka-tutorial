package com.tutorial.akka;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class WorkerBehavior extends AbstractBehavior<WorkerBehavior.Command>{
	
	public static class Command implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8623096587113457298L;
		private String message;
		private ActorRef<com.tutorial.akka.ManagerBehavior.Command> sender;
		public Command(String message, ActorRef<com.tutorial.akka.ManagerBehavior.Command> actorRef) {
			super();
			this.message = message;
			this.sender = actorRef;
		}
		public String getMessage() {
			return message;
		}
		public ActorRef<com.tutorial.akka.ManagerBehavior.Command> getSender() {
			return sender;
		}
		
	}

	public WorkerBehavior(ActorContext<Command> context) {
		super(context);
	}
	
	public static Behavior<Command> create(){
		return Behaviors.setup(WorkerBehavior::new);
	}

	//This actor creates a big integer and returns back the result to its sender using tell.
	@Override
	public Receive<Command> createReceive() {
		return newReceiveBuilder()
				.onAnyMessage( (command)->{
					if("start".equalsIgnoreCase(command.getMessage())) {
						BigInteger bigInteger = new BigInteger(2000, new Random());
						command.sender.tell(new ManagerBehavior.ResultCommand(bigInteger));
					}
					return this;
				})
				.build();
	}

}
