package com.example.newsaggregator;

public class SourcesObj {

    String name,id,category;

    public SourcesObj(String name, String id, String category) {
        this.name = name;
        this.id = id;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
