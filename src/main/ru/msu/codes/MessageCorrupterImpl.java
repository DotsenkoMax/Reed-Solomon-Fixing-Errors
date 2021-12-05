package ru.msu.codes;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MessageCorrupterImpl implements MessageCorrupter {
    private final List<Integer> listOfIndices;
    private final Random gen = new Random();

    public MessageCorrupterImpl(int n) {
        // 0, ..., n-1 - for choosing m random
        this.listOfIndices = IntStream.range(0, n).boxed().collect(Collectors.toList());
    }

    @Override
    public char[] corruptMessage(char[] encodedMessage, int nBytesToCorrupt) {
        var clonedIndices = new ArrayList<>(listOfIndices);
        Collections.shuffle(clonedIndices);
        var corruptedMessage = Arrays.copyOf(encodedMessage, encodedMessage.length);

        int index = 0;
        for (var idx : clonedIndices) {
            if (index == nBytesToCorrupt) {
                break;
            }
            corruptedMessage[idx] = (char) gen.nextInt(256);
            index += 1;
        }
        return corruptedMessage;
    }
}
