package com.example.java8;

public class Job {
    public enum Type { MOTOR_REPAIR, LOSS_ADJUSTER, SOLICITOR }

    private Type type;

    public Job(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

