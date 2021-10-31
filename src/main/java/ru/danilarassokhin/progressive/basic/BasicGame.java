package ru.danilarassokhin.progressive.basic;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.GameFrameTimeType;
import ru.danilarassokhin.progressive.basic.manager.BasicGamePublisher;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.basic.util.BasicGameLogger;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.manager.GameState;
import ru.danilarassokhin.progressive.util.GameSecurityManager;

/**
 * Basic implementation of {@link ru.danilarassokhin.progressive.Game}
 */
public final class BasicGame implements Game {

  private static BasicGame INSTANCE;
  private final AtomicLong idGenerator;

  private GameFrameTimeType gameFrameTimeType;
  private boolean isStatic;
  private int frameTime;

  private final Map<Long, GameObject> gameObjects;
  private final BasicGameStateManager stateManager;
  private Class<? extends GameObject> gameObjClass;
  private final ScheduledExecutorService scheduler;

  private boolean isStarted;
  private long deltaTime;

  private BasicGame() {
    BasicGameLogger.getInstance().info("Progressive IoC initialization...\n");
    gameFrameTimeType = GameFrameTimeType.PARALLEL;
    gameObjects = new ConcurrentSkipListMap<>();
    idGenerator = new AtomicLong(0);
    stateManager = BasicGameStateManager.getInstance();
    scheduler = Executors.newScheduledThreadPool(4);
    isStarted = false;
    stateManager.setState(GameState.INIT, this);
  }

  protected synchronized static boolean createInstance() {
    if(INSTANCE == null) {
      INSTANCE = new BasicGame();
      return true;
    }
    return false;
  }

  public static BasicGame getInstance() {
    if (INSTANCE == null) {
      throw new RuntimeException("DI Container has not been initialized! Call GameStarter.init(String[] args) first!");
    }
    return INSTANCE;
  }

  @Override
  public synchronized void start() {
    GameSecurityManager.denyAccessIf("Game has been already started!", () -> isStarted);
    stateManager.setState(GameState.STARTED, true);
    BasicGamePublisher.getInstance().sendTo("start", true);
    isStarted = true;
    if (!isStatic) {
      scheduler.scheduleAtFixedRate(this::update, frameTime, frameTime, TimeUnit.MILLISECONDS);
    }
    deltaTime = System.currentTimeMillis();
    stateManager.setState(GameState.PLAYING, true);
  }

  private void update() {
    long now = System.currentTimeMillis();
    long delta = now - deltaTime;
    deltaTime = now;
    update(delta);
  }

  @Override
  public synchronized void update(long delta) {
    GameSecurityManager.denyAccessIf("Game param isStatic is set to false. Can't update manually!",
        () -> !isStatic && !GameSecurityManager.getCallerClass().equals(BasicGame.class));
    BasicGamePublisher.getInstance().sendTo("update", delta);
  }

  public synchronized void stop() {
    GameSecurityManager.allowAccessIf("Game hasn't been started!", () -> isStarted);
    stateManager.setState(GameState.STOPPED, true);
    isStarted = false;
    scheduler.shutdownNow();
  }

  @Override
  public GameObject addGameObject() {
    if (!isGameObjectClassSet()) {
      throw new RuntimeException("GameObject class has not been set in game! Use setGameObjectClass method in your game");
    }
    GameObject gameObject = BasicDIContainer.create(gameObjClass, idGenerator.incrementAndGet());
    gameObjects.putIfAbsent(idGenerator.get(), gameObject);
    return gameObject;
  }

  @Override
  public synchronized boolean removeGameObject(GameObject o) {
    if (!gameObjects.containsKey(o.getId())) {
      return false;
    }
    GameObject gameObject = gameObjects.get(o.getId());
    gameObject.dispose();
    gameObjects.remove(o.getId());
    return true;
  }

  @Override
  public synchronized boolean setGameObjectClass(Class<? extends GameObject> c) {
    if (gameObjClass != null) {
      return false;
    }
    gameObjClass = c;
    return true;
  }

  public synchronized void setFrameTime(int milliseconds) {
    if (milliseconds < 1) {
      throw new RuntimeException("Frame rate can't be less than 1 millisecond!");
    }
    this.frameTime = milliseconds;
  }

  @Override
  public boolean isGameObjectClassSet() {
    return gameObjClass != null;
  }

  public synchronized void setStatic(boolean isStatic) {
    if(!isStarted) {
      this.isStatic = isStatic;
    }
  }

  public GameFrameTimeType getFrameTimeType() {
    return gameFrameTimeType;
  }

  public synchronized void setFrameTimeType(GameFrameTimeType gameFrameTimeType) {
    if(isStarted) {
      this.gameFrameTimeType = gameFrameTimeType;
    }
  }
}
