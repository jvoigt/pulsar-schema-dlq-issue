package com.jvoigt.issue;

import lombok.Data;

@Data
class Payload {
    String message;

    public Payload() {

    }

    public Payload(String message) {
        this.message = message;
    }
}
