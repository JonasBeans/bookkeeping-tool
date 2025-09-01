package be.jonasboon.book_keeping_tool.domain.synchronization.executors.restore;

public class EntityHandlerDefault implements EntityHandler {

    @Override
    public <I> I handle(I entity) {
        return entity;
    }
}
