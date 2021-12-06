package ru.msu.codes.corrupter;

public interface MessageCorrupter {
    int[] corruptMessage(int[] encodedMessage, int nBytesToCorrupt);
}
