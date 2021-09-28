package ru.danilarassokhin.progressive.basic;

import ru.danilarassokhin.progressive.lambdas.StoryActionParam;
import ru.danilarassokhin.progressive.data.StoryState;
import ru.danilarassokhin.progressive.managers.StoryStateManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleStoryStateManager implements StoryStateManager<StoryState>, Serializable {

    private static SimpleStoryStateManager INSTANCE;
    private StoryState state;
    private transient Map<StoryState, List<StoryActionParam>> actions;

    private SimpleStoryStateManager() {
        actions = new HashMap<>();
        setState(StoryState.UNDEFINED, null);
    }

    public static SimpleStoryStateManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SimpleStoryStateManager();
        }
        return INSTANCE;
    }

    @Override
    public StoryState getCurrentState() {
        return state;
    }

    @Override
    public <O> void setState(StoryState state, O o) {
        if(actions.containsKey(state)) {
            actions.get(state).forEach(a -> a.make(o));
        }else{
            actions.put(state, new ArrayList<>());
        }
        this.state = state;
    }

    @Override
    public List<StoryActionParam> getActions(StoryState state) {
        return actions.getOrDefault(state, new ArrayList<>());
    }

    @Override
    public <V> void addAction(StoryState state, StoryActionParam<V> action) {
        actions.putIfAbsent(state, new ArrayList<>());
        actions.get(state).add(action);
    }
}
