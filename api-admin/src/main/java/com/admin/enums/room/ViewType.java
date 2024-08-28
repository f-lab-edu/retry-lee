package com.admin.enums.room;

import lombok.Getter;

@Getter
public enum ViewType {
    CITY("City View"),
    OCEAN("Ocean View"),
    MOUNTAIN("Mountain View"),
    GARDEN("Garden View");

    private final String description;

    ViewType(String description) {
        this.description = description;
    }
}
