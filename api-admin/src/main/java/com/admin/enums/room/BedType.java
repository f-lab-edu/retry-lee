package com.admin.enums.room;

import lombok.Getter;

@Getter
public enum BedType {
    SINGLE("Single Bed"),
    TWIN("Twin Beds"),
    DOUBLE("Double Bed"),
    QUEEN("Queen Size Bed"),
    KING("King Size Bed");

    private final String description;

    BedType(String description) {
        this.description = description;
    }
}
