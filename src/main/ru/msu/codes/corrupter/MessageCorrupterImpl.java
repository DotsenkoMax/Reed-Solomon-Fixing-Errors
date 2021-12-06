package ru.msu.codes.corrupter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MessageCorrupterImpl implements MessageCorrupter {
    private final List<Integer> listOfIndices;
    private final Random gen = new Random();
    private final int originalWordLen;

    public MessageCorrupterImpl(int n) {
        // 0, ..., n-1 - for choosing m random
        this.listOfIndices = IntStream.range(0, n).boxed().collect(Collectors.toList());
        this.originalWordLen = n;
    }

    @Override
    public int[] corruptMessage(int[] encodedMessage, int nBytesToCorrupt) {
        var clonedIndices = new ArrayList<>(listOfIndices);
        Collections.shuffle(clonedIndices);
        var corruptedMessage = Arrays.copyOf(encodedMessage, encodedMessage.length);
        var positions = new ArrayList<Integer>();
        int index = 0;
        for (var idx : clonedIndices) {
            if (index == nBytesToCorrupt) {
                break;
            }
            positions.add(idx);
            corruptedMessage[idx] = gen.nextInt(256);
            index += 1;
        }
        positions.sort(Integer::compareTo);
        System.out.printf("Error positions: %s\n", positions);
        return corruptedMessage;
    }
}
