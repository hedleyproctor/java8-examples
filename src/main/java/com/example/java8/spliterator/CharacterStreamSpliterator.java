package com.example.java8.spliterator;

import java.util.Spliterator;
import java.util.function.Consumer;

/** The spliterator is the equivalent of the iterator, but for streams.
 * It allows you to expose a collection or any other data source as a stream.
 *
 */
public class CharacterStreamSpliterator implements Spliterator<Character> {

    private final String string;
    private int currentChar = 0;

    public CharacterStreamSpliterator(String string) {
        this.string = string;
        System.out.println("Spliterator creating with string of length: " + string.length());
    }

    /** The tryAdvance method is the one that exposes your underlying data as a stream.
     * It simply takes the operation(s) that you want to take place on the stream and
     * invokes them on the data. The opearation(s) are exposed to this method as an
     * implementation of the Consumer functional interface.
     *
     * After invoking the operation(s) on the current stream element, it returns a boolean
     * saying if there are any more elements left.
     *
     * @param action
     * @return
     */
    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(string.charAt(currentChar));
        currentChar++;
        return currentChar < string.length();
    }

    @Override
    public Spliterator<Character> trySplit() {
        int currentLength = string.length() - currentChar;
        if (currentLength < 100) {
            // not worth splitting
            return null;
        }

        int splitPosition = currentChar + (currentLength/2);
        if (Character.isAlphabetic(string.charAt(splitPosition))) {
            while (Character.isAlphabetic(string.charAt(splitPosition))) {
                splitPosition++;
            }
        }
        if (Character.isDigit(string.charAt(splitPosition))) {
            while (Character.isDigit(string.charAt(splitPosition))) {
                splitPosition++;
            }
        }

        // create a new spliterator for the first half of the string
        CharacterStreamSpliterator spliterator = new CharacterStreamSpliterator(string.substring(currentChar,splitPosition));
        // now update our own position to be the second half of the string
        currentChar = splitPosition;
        return spliterator;
    }

    @Override
    public long estimateSize() {
        return string.length() - currentChar;
    }

    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
}
