import java.util.Arrays;
import java.util.Random;

/**
 * Created by Stefan on 16-May-17.
 */

public class Utils {
	
	/* Fill array */
	public static void fillArray(int[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		printArray(array, "Array Filled: " + array.length);
	}
	
	/* Verify sorted array */
	public static void checkArray(int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (i != array[i]) {
				printArray(array, "FAIL: Array not sorted...");
				return;
			}
 		}
		printArray(array, "SUCCESS: Array sorted");	
	}

    /* Print array */
    public static void printArray(int[] array, String message) {
        System.out.print(message + " ");
        printArray(array);
    }
    public static void printArray(int[] array) {
    	if (array.length > 500) {
    		System.out.println("\n(Array too big to print)\n");
    		return;
    	} else {
    		System.out.println();
    		Arrays.stream(array).forEach((i) -> {
    			System.out.print(i + " ");
    		});
    		System.out.println();
    	}
    }

    /* Shuffle array */
    public static int[] shuffleArray(int[] array) {

        if (array.length < 2)
            return array;

        Random rnd = new Random();

        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }

        printArray(array, "Array Shuffled");

        return array;
    }
}