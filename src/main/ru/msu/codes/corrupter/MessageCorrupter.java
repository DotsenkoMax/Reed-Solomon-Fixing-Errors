package ru.msu.codes.corrupter;

/**
 * Генератор помех
 */
public interface MessageCorrupter {
    /**
     * @param encodedMessage  закодированное сообщение
     * @param nBytesToCorrupt количество ошибок
     * @return сообщение c помехами
     */
    int[] corruptMessage(int[] encodedMessage, int nBytesToCorrupt);
}
