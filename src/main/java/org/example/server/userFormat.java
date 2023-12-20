package org.example.server;

class userFormat {
    String id;
    String username;

    userFormat(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return this.id;
    }

    String getUsername() {
        return this.username;
    }

}
