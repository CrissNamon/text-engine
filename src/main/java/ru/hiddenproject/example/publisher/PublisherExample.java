package ru.hiddenproject.example.publisher;

import ru.hiddenproject.example.game.component.GameItem;
import ru.hiddenproject.progressive.PublisherType;
import ru.hiddenproject.progressive.basic.BasicComponentManager;
import ru.hiddenproject.progressive.basic.manager.BasicGamePublisher;
import ru.hiddenproject.progressive.basic.manager.PublisherSubscription;
import ru.hiddenproject.progressive.manager.GamePublisher;

public class PublisherExample {

  String message;

  public PublisherExample() {
    //Get publisher instance
    GamePublisher<PublisherSubscription, String> basicGamePublisher =
        BasicGamePublisher.getInstance();

    //Set publisher type
    //PARALLEL - use parallel streams to call listeners
    //SEQUENCE - iterate through listeners in order they subscribed
    basicGamePublisher.setPublisherType(PublisherType.PARALLEL);

    //Subscribe to topic "method" and use print method while listener call
    PublisherSubscription methodSubscription =
        basicGamePublisher.subscribeOn("method", this::print);
    //Subscribe to topic "print" and print received object to console
    basicGamePublisher.subscribeOn("print", m -> System.out.println(m));
    //Subscribe to topic "set" and set current message to received message
    basicGamePublisher.subscribeOn("set", m -> message = m.toString());

    //Send message "hello" to "method" topic, e.g. call all listeners on "method" topic and pass "hello" to them
    basicGamePublisher.sendTo("method", "hello");
    //Send message "message" to "print" topic, e.g. call all listeners on "print" and pass "message" to them
    basicGamePublisher.sendTo("print", "message");
    //Send GameItem object to "set" topic, e.g. call all listeners on "set" and pass GameItem to them
    basicGamePublisher.sendTo("set", new GameItem());

    //Remove listener from topic
    basicGamePublisher.unsubscribe(methodSubscription);
  }

  public void print(Object message) {
    BasicComponentManager
        .getGameLogger().info(message);
  }

}
