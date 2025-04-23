package Exercises.exercise7;

import java.util.*;
import java.util.function.Predicate;

public class GenericMethodsDemo {

    // (a) Count elements with specific property
    public static <T> int countIf(Collection<T> collection, Predicate<T> predicate) {
        int count = 0;
        for (T item : collection) {
            if (predicate.test(item)) {
                count++;
            }
        }
        return count;
    }

    // (b) Swap two elements in an array
    public static <T> void swap(T[] array, int i, int j) {
        if (i >= 0 && j >= 0 && i < array.length && j < array.length && i != j) {
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    // (c) Find maximal element in a subrange [begin, end)
    public static <T extends Comparable<T>> T maxInRange(List<T> list, int begin, int end) {
        if (begin < 0 || end > list.size() || begin >= end) {
            throw new IllegalArgumentException("Invalid range");
        }
        T max = list.get(begin);
        for (int i = begin + 1; i < end; i++) {
            if (list.get(i).compareTo(max) > 0) {
                max = list.get(i);
            }
        }
        return max;
    }

    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6);
        int countOdd = countIf(nums, n -> n % 2 != 0);
        System.out.println("Count of odd numbers: " + countOdd);

        String[] words = {"apple", "banana", "cherry"};
        swap(words, 0, 2);
        System.out.println("Swapped array: " + Arrays.toString(words));

        List<Double> scores = Arrays.asList(2.5, 3.9, 4.1, 3.6);
        Double maxScore = maxInRange(scores, 1, 4);
        System.out.println("Max score in range [1, 4): " + maxScore);
    }
}
