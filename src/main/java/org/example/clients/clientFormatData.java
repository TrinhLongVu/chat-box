package org.example.clients;

class clientFormatData {
    private String id;
    private String name;
    private String content;
    private int index;

    clientFormatData(String id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    void setIndex(int index) {
        this.index = index;
    }

    int getIndex() {
        return this.index;
    }

    String getId() {
        return this.id;
    }

    String getName() {
        return this.name;
    }

    String getContent() {
        return this.content;
    }
}
