package com.example.java8.spliterator;

import org.jsoup.nodes.Element;

public class Image {
    private org.jsoup.nodes.Element element;

    public Image(Element element) {
        this.element = element;
    }

    public String getURL() {
        return element.attr("abs:src");
    }
}
