package com.example.java8.spliterator;

public class CharacterStreamSequenceCounter {
    private final int counter;
    private final boolean lastByteWasDigit;

    public CharacterStreamSequenceCounter(int counter, boolean lastByteWasDigit) {
        this.counter = counter;
        this.lastByteWasDigit = lastByteWasDigit;
    }

    public CharacterStreamSequenceCounter accumulate(Character character) {
        if (Character.isDigit(character)) {
            if (lastByteWasDigit) {
                // still traversing digits, no need to increment counter or change our flag
                return this;
            } else {
                // just switched from letter to digit.
                // need to update our flag, but don't want to use mutable state, so return a new counter
                return new CharacterStreamSequenceCounter(counter,true);
            }
        } else {
            if (lastByteWasDigit) {
                // switched from digit to letter
                return new CharacterStreamSequenceCounter(counter+1,false);
            } else {
                // still traversing letters, no need change anything
                return this;
            }
        }
    }

    public CharacterStreamSequenceCounter combine(CharacterStreamSequenceCounter recordCounter) {
        return new CharacterStreamSequenceCounter(counter + recordCounter.counter,lastByteWasDigit);
    }

    public int getCounter() {
        return counter;
    }
}
