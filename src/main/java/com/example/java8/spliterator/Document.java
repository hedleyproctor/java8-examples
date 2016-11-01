package com.example.java8.spliterator;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Document {

    private org.jsoup.nodes.Document jsoupDoc;

    public Document(org.jsoup.nodes.Document jsoupDoc) {
        this.jsoupDoc = jsoupDoc;
    }

    public String getTitle() {
        return jsoupDoc.title();
    }

    public List<Image> getImages() {
        Elements elements = jsoupDoc.select("img");
        List<Image> images = new ArrayList<>();
        for (Element element : elements) {
            images.add(new Image(element));
        }
        return images;
    }
}
