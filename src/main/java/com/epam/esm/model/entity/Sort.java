package com.epam.esm.model.entity;

public class Sort {
    private String property;
    private Direction direction;
    public static final Direction DEFAULT_DIRECTION = Direction.ASC;

    public enum Direction {
        ASC, DESC
    }

    public Sort(String property) {
        this(property, DEFAULT_DIRECTION);
    }

    public Sort(String property, Direction direction) {
        this.property = property;
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public Direction getDirection() {
        return direction;
    }
}
