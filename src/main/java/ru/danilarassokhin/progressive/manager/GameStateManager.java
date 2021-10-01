package ru.danilarassokhin.progressive.manager;

import ru.danilarassokhin.progressive.lambda.GameActionObject;

import java.util.List;

/**
 * Represents story state manager
 * @param <S> State type
 */
public interface GameStateManager<S extends GameState> {

    /**
     * Returns current state
     * @return Current story state
     */
    S getCurrentState();

    /**
     * Sets state in manager
     * @param state New state
     * @param <O> Action param type
     * @param actionParam Param to pass in action of this state
     */
    <O> void setState(S state, O actionParam);

    /**
     * Returns all listeners attached to {@code state}
     * @param state State to search
     * @return List with actions for {@code state}
     */
    List<GameActionObject> getListeners(S state);


    /**
     * Adds listener to state
     * @param state State to add action
     * @param <V> Action param type
     * @param action Action to add
     */
    <V> void addListener(S state, GameActionObject<V> action);


}
