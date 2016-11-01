package com.example.java8.spliterator;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WebPageSpliteratorTest {

    @Test
    public void webPageSpliterator() throws IOException {

        // can define (an infinite) stream as follows
        Stream<Document> documentStream = StreamSupport.stream(new WebPageSpliterator(new WebPageProvider()), false);

        // can now call any stream methods on this. e.g. convert to stream of images and limit to 10
        Stream<Image> imageStream = StreamSupport.stream(new WebPageSpliterator(new WebPageProvider()), false)
                                        .map(Document::getImages)
                                        .flatMap(List::stream)
                                        .limit(10);

        imageStream.forEach(image -> System.out.println("Image URL: " + image.getURL()));

        // filter only those documents with more than five images
        Stream<Document> docsWithFiveOrMoreImages = StreamSupport.stream(new WebPageSpliterator(new WebPageProvider()), false)
                                                        .filter(doc -> doc.getImages().size() >= 5)
                                                        .limit(10);

        docsWithFiveOrMoreImages.forEach(doc -> System.out.println(doc.getTitle()));

    }
}
