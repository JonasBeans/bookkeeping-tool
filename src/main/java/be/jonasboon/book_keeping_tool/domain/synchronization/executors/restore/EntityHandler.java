package be.jonasboon.book_keeping_tool.domain.synchronization.executors.restore;

public interface EntityHandler {
    <I> I handle(I entity);
}
