package ru.msu.codes;

public interface MessageCorrupter {
    char[] corruptMessage(char[] encodedMessage, int nBytesToCorrupt);
}
