package liu.mars;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import clojure.lang.IFn;

import java.util.Map;

public class Actor extends AbstractBehavior<Object> {
    private IFn receiver;

    private Actor(ActorContext<Object> context, IFn receiver) {
        super(context);
        this.receiver = receiver;
    }

    public static Behavior<Object> create(IFn receiver) {
        return Behaviors.setup(context -> new Actor(context, receiver));
    }

    private Behavior<Object> onMessage(Map message) {
        return (Behavior<Object>)receiver.invoke(getContext(), message);
    }

    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder().onMessage(Map.class, this::onMessage).build();
    }

}
