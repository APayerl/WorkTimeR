package se.payerl;

import java.util.concurrent.atomic.AtomicInteger;

@FunctionalInterface
public interface Logic {
    public void action(AtomicInteger i);
}
