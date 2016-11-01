package com.example.java8.spliterator;

import java.util.Spliterator;
import java.util.function.Consumer;

public class WebPageSpliterator implements Spliterator<Document> {
    private WebPageProvider webPageProvider;

    public WebPageSpliterator(WebPageProvider webPageProvider) {
        this.webPageProvider = webPageProvider;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Document> action) {
        action.accept(webPageProvider.getPage());
        return true;
    }

    @Override
    public Spliterator<Document> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return 0;
    }

    @Override
    public int characteristics() {
        return 0;
    }
}
