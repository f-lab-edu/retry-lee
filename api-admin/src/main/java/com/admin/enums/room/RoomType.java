package com.admin.enums.room;

import lombok.Getter;

@Getter
public enum RoomType {
    SINGLE("Single Room"),
    DOUBLE("Double Room"),
    DELUXE("Deluxe Room"),
    SUITE("Suite Room");

    private final String description;

    RoomType(String description) {
        this.description = description;
    }

}
