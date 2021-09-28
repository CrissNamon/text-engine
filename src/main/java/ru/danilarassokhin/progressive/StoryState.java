package ru.danilarassokhin.progressive;

/**
 * Represents story state for state manager
 */
public enum StoryState {
    /**
     * Default state on before Story instance creation
     * <br>
     * Returns null
     */
    UNDEFINED,
    /**
     * Called on first Story instance creation
     * <br>
     * Returns Story object
     */
    INIT,
    /**
     * Called on {@link Story#setNext(StoryNode)}
     * <br>
     * Returns old node
     */
    NODE_TRANSITION_START,
    /**
     * Called on {@link Story#next()}
     * <br>
     * Returns new node
     */
    NODE_TRANSITION_END,
    /**
     * Called on {@link StoryCharacter#setLocation(StoryLocation, StoryExtraAction, StoryExtraAction)}
     * <br>
     * Returns old location
     */
    LOCATION_MOVE_START,
    /**
     * Called on {@link StoryCharacter#setLocation(StoryLocation, StoryExtraAction, StoryExtraAction)} if old location is not null
     * and new location has been set successfully
     */
    LOCATION_MOVE_COMPLETE,
    END
}
