import java.util.List;

public class AverageCalculator {

    public static double calculateAverageExcludingMinMax(List<Integer> numbers) {
        // Check for null or insufficient elements
        if (numbers == null || numbers.size() <= 2) {
            throw new IllegalArgumentException("List must contain more than two numbers.");
        }

        // Find sum, min, and max
        int sum = 0;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int num : numbers) {
            sum += num;
            if (num < min) min = num;
            if (num > max) max = num;
        }

        // Subtract one instance of min and max from sum
        sum -= min;
        sum -= max;

        // Calculate average excluding one min and one max
        return (double) sum / (numbers.size() - 2);
    }

    public static void main(String[] args) {
        List<Integer> nums = List.of(4, 5, 1, 3, 5, 10, 1);
        double average = calculateAverageExcludingMinMax(nums);
        System.out.println("Average excluding one min and one max: " + average);
    }
}
