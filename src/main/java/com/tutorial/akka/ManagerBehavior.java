package com.tutorial.akka;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ManagerBehavior extends AbstractBehavior<ManagerBehavior.Command>{
	
	public interface Command extends Serializable{}
	
	public static class InstructionCommand implements Command{
		private static final long serialVersionUID = -1214606359284602615L;
		private String message;
		public InstructionCommand(String message) {
			super();
			this.message = message;
		}
		public String getMessage() {
			return message;
		}
		
	}
	
	public static class ResultCommand implements Command{
		private static final long serialVersionUID = 7616749223739138359L;
		private BigInteger primeInt;
		public ResultCommand(BigInteger primeInt) {
			super();
			this.primeInt = primeInt;
		}
		public BigInteger getPrimeInt() {
			return primeInt;
		}
	}

	public ManagerBehavior(ActorContext<Command> context) {
		super(context);
	}
	
	private SortedSet<BigInteger> primes = new TreeSet<>();
	
	public static Behavior<Command> create(){
		return Behaviors.setup(context-> new ManagerBehavior(context));
	}

	// If InstructionCommand is received it spawns 20 Worker actors and tells them to create a big prime number
	//If ResultCommand is received it adds the the prime number to primes.
	@Override
	public Receive<Command> createReceive() {
		return newReceiveBuilder()
				.onMessage(InstructionCommand.class, (command)->{
					if("start".equalsIgnoreCase(command.getMessage())) {
						List<ActorRef<WorkerBehavior.Command>> actors = new ArrayList<>();
						for(int i=0;i<20;i++) {
							actors.add(getContext().spawn(WorkerBehavior.create(), "actor"+i));
						}
						actors.forEach(actor->actor.tell(new WorkerBehavior.Command("start",getContext().getSelf())));
					}
					return this;
				})
				.onMessage(ResultCommand.class, (command)->{
					primes.add(command.getPrimeInt());
					System.out.println("I have received "+primes.size()+" prime numbers");
					if(primes.size()==20) {
						primes.forEach(System.out::println);
					}
					return this;
				})
				.build();
	}

}
