import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class AverageCalculatorTest {

    // 1. Basic test with distinct numbers
    @org.junit.Test
    @Test
    public void testBasicAverage() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        double result = AverageCalculator.calculateAverageExcludingMinMax(numbers);
        assertEquals(3.0, result, 0.0001);
    }

    // 2. Test with duplicates in the middle
    @org.junit.Test
    @Test
    public void testWithDuplicates() {
        List<Integer> numbers = List.of(1, 2, 2, 3, 5);
        double result = AverageCalculator.calculateAverageExcludingMinMax(numbers);
        assertEquals(2.3333, result, 0.0001);
    }

}