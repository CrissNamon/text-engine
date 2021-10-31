package ru.danilarassokhin.progressive.basic.manager;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.lambda.GameActionObject;
import ru.danilarassokhin.progressive.manager.GamePublisher;

/**
 * Basic implementation of {@link ru.danilarassokhin.progressive.manager.GamePublisher}.
 * <p>Allow you to subscribe on and make global events</p>
 * <p>Thread safe. Uses {@link java.util.concurrent.ConcurrentLinkedQueue} and
 * {@link java.util.concurrent.ConcurrentSkipListMap} as event storage</p>
 */
public final class BasicGamePublisher implements GamePublisher {

  private static BasicGamePublisher INSTANCE;

  private final Map<String, Queue<GameActionObject<Object>>> feed;

  private BasicGamePublisher() {
    feed = new ConcurrentSkipListMap<>();
  }

  public static BasicGamePublisher getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new BasicGamePublisher();
    }
    return INSTANCE;
  }

  @Override
  public void sendTo(String topic, Object message) {
    Queue<GameActionObject<Object>> subscribers = feed.getOrDefault(topic, new ConcurrentLinkedQueue<>());
    switch (BasicGame.getInstance().getFrameTimeType()) {
      case SEQUENCE:
        sendSequence(message, subscribers);
        break;
      case PARALLEL:
        sendParallel(message, subscribers);
        break;
    }
  }

  @Override
  public <V> void subscribeOn(String topic, GameActionObject<V> action) {
    feed.putIfAbsent(topic, new ConcurrentLinkedQueue<>());
    feed.get(topic).add((GameActionObject) action);
  }

  private void sendSequence(Object message, Collection<GameActionObject<Object>> subscribers) {
    Iterator<GameActionObject<Object>> iterator = subscribers.iterator();
    while (iterator.hasNext()) {
      iterator.next().make(message);
    }
  }

  private void sendParallel(Object message, Collection<GameActionObject<Object>> subscribers) {
    subscribers.parallelStream().unordered()
        .forEach(s -> s.make(message));
  }
}
