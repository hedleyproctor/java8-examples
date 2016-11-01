package com.example.java8.spliterator;

import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.testng.Assert.assertEquals;

public class CharacterStreamSpliteratorTest {

    /** Creates sample test data, as alternating sequences of letters and digits.
     *
     * @param numberOfRecords
     * @return
     * @throws IOException
     */
    public String createSampleData(int numberOfRecords) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (int i=0; i<numberOfRecords; i++) {
            //  1-5 letters
            final long howManyLetters = Math.round(Math.random()*4) + 1;
            for (int x=0; x<howManyLetters; x++) {
                // letters A to Z are values 65 to 90
                char randomChar = (char) (Math.round(Math.random()*25) + 65);
                sb.append(randomChar);
            }
            // now 1-5 numbers
            final long howManyDigits = Math.round(Math.random()*4) + 1;
            for (int x=0; x<howManyDigits; x++) {
                int randomNumber = (int) (Math.random() * 10);
                sb.append(randomNumber);
            }
        }
        return sb.toString();
    }

    @Test
    public void recordDataSpliterator() throws IOException, URISyntaxException {
        String sampleData = createSampleData(1000);
        Stream<Character> characterStream = IntStream.range(0,sampleData.length()).mapToObj(sampleData::charAt);

        CharacterStreamSequenceCounter counter = characterStream.reduce(new CharacterStreamSequenceCounter(0,true),
                                                    CharacterStreamSequenceCounter::accumulate,
                                                    CharacterStreamSequenceCounter::combine);
        assertEquals(counter.getCounter(),1000);

        // now try in parallel

        // naive attempt would fail
        // Stream<Character> characterStream2 = IntStream.range(0,sampleData.length()).mapToObj(sampleData::charAt).parallel();
        CharacterStreamSpliterator characterStreamSpliterator = new CharacterStreamSpliterator(sampleData);
        Stream<Character> characterStream2 = StreamSupport.stream(characterStreamSpliterator,true);

        CharacterStreamSequenceCounter counter2 = characterStream2.reduce(new CharacterStreamSequenceCounter(0,true),
                                                    CharacterStreamSequenceCounter::accumulate,
                                                    CharacterStreamSequenceCounter::combine);
        assertEquals(counter2.getCounter(),1000);
    }


}
