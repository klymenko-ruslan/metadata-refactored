package com.turbointernational.metadata.domain.part.salesnote;

/**
 *
 * @author jrodriguez
 */
public enum SalesNoteState {
    draft, submitted, approved, published, rejected;
    
    public static void checkState(SalesNoteState currentState, SalesNoteState... allowedStates) {
        for (SalesNoteState allowedState : allowedStates) {
            if (allowedState.equals(currentState)) {
                return;
            }
        }
        
        throw new IllegalStateException("Sales note in state " + currentState + ", must be one of: " + allowedStates);
    }
}
