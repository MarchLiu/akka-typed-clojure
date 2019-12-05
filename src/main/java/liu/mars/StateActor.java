package liu.mars;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import clojure.lang.IFn;
import clojure.lang.Obj;

import java.util.Map;

public class StateActor extends AbstractBehavior<Object> {
    private Map state;
    private IFn receiver;

    private StateActor(ActorContext<Object> context, Map state, IFn receiver) {
        super(context);
        this.state = state;
        this.receiver = receiver;
    }

    public static Behavior<Object> create(Map state, IFn receiver) {
        return Behaviors.setup(context -> new StateActor(context, state, receiver));
    }

    private Behavior<Object> onMessage(Object message) {
        return (Behavior<Object>)receiver.invoke(getContext(), state, message);
    }

    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder().onAnyMessage(this::onMessage).build();
    }
}
