package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.Game;
import ru.danilarassokhin.progressive.basic.manager.BasicGameStateManager;
import ru.danilarassokhin.progressive.component.GameObject;
import ru.danilarassokhin.progressive.manager.GameState;
import ru.danilarassokhin.progressive.util.ComponentCreator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class BasicGame implements Game {

    private static BasicGame INSTANCE;
    private final Map<Long, GameObject> gameObjects;
    private final Set<GameObjectWorker> gameObjectWorkers;
    private BasicGameStateManager stateManager;
    private Class<? extends GameObject> gameObjClass;
    private int tick;
    private final ScheduledExecutorService scheduler;
    private boolean isStatic;

    private BasicGame() {
        gameObjects = new HashMap<>();
        stateManager = BasicGameStateManager.getInstance();
        gameObjectWorkers = new HashSet<>();
        scheduler = Executors.newScheduledThreadPool(2);
        stateManager.setState(GameState.INIT, this);
    }

    public static BasicGame getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new BasicGame();
        }
        return INSTANCE;
    }

    public void start() {
        gameObjectWorkers.forEach(w -> ComponentCreator.invoke(w.getStartMethod(), gameObjects.get(w.getGameObjId())));
        if(!isStatic) {
            gameObjectWorkers.forEach(w -> {
                ScheduledFuture resultFuture = scheduler
                        .scheduleAtFixedRate(() -> ComponentCreator.invoke(w.getUpdateMethod(), gameObjects.get(w.getGameObjId())), 0, tick, TimeUnit.MILLISECONDS);
            });
        }
    }

    public void stop() throws InterruptedException {
        scheduler.shutdown();
        if(scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
            scheduler.shutdownNow();
        }
    }

    @Override
    public GameObject addGameObject() {
        if(!isGameObjectClassSet()) {
            throw new RuntimeException("GameObject class has not been set in game! Use setGameObjectClass method in your game");
        }
        try {
            Long lastId = gameObjects.keySet().stream().max(Long::compareTo).orElse(0L);
            GameObject gameObject = ComponentCreator.create(gameObjClass, ++lastId, this);
            gameObjects.putIfAbsent(lastId, gameObject);
            Method update = gameObject.getClass().getDeclaredMethod("update");
            Method start = gameObject.getClass().getDeclaredMethod("start");
            update.setAccessible(true);
            start.setAccessible(true);
            gameObjectWorkers.add(new GameObjectWorker(update, start, lastId));
            return gameObject;
        }catch (NoSuchMethodException e) {
            throw new RuntimeException("void update() method not found in " + gameObjClass.getName());
        }
    }

    @Override
    public boolean removeGameObject(GameObject o) {
        if(gameObjects.remove(o.getId()) != null) {
            gameObjectWorkers.removeIf(w -> w.getGameObjId().equals(o.getId()));
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean setGameObjectClass(Class<? extends GameObject> c) {
        if(gameObjClass != null) {
            return false;
        }
        gameObjClass = c;
        return true;
    }

    public void setTickRate(int milliseconds) {
        if(milliseconds < 100) {
            milliseconds = 100;
        }
        this.tick = milliseconds;
    }

    @Override
    public boolean isGameObjectClassSet() {
        return gameObjClass != null;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

}
