package entity.Eventlistener;

/**
 * EventListener - Base interface for all event listeners
 *
 * @author Wanru Cheng
 */
public interface EventListener {

    /**
     * Check if the event is currently triggered
     *
     * @return true if triggered, false otherwise
     */
    boolean isTriggered();
}