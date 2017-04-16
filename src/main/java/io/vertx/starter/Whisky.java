package io.vertx.starter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@ToString
public class Whisky {
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private final int id;

    @Setter
    private String name;

    @Setter
    private String origin;

    public Whisky() {
        this.id = COUNTER.getAndIncrement();
    }

    public Whisky(String name, String origin) {
        this.name = name;
        this.origin = origin;
        this.id = COUNTER.getAndIncrement();
    }
}
