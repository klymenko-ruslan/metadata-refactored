package com.turbointernational.metadata.entity;

import java.util.Arrays;

/**
 *
 * @author jrodriguez
 */
public enum SalesNoteState {
    draft, submitted, approved, published, rejected;

    @Override
    public String toString() {
        return name();
    }
    
    public static void checkState(SalesNoteState currentState, SalesNoteState... allowedStates) {
        for (SalesNoteState allowedState : allowedStates) {
            if (allowedState.equals(currentState)) {
                return;
            }
        }
        
        throw new IllegalStateException("Sales note in state " + currentState + ", must be one of: " + Arrays.toString(allowedStates));
    }
}
