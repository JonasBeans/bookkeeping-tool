package be.jonasboon.book_keeping_tool.restore.common;

import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;

public interface RestoreEntityMapper<ENTITY> {
    ENTITY map(CSVObject restoreModel);
}
