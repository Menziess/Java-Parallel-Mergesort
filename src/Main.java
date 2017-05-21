import java.util.Arrays;

/**
 * Created by Stefan on 16-May-17.
 */

public class Main {

    public static void main(String[] args) {
    	
    	// Initializing profiler
    	EventProfiler logger = new EventProfiler(true);
    	
        // Create sorted array
        int[] original = new int[
//			500_000
//			1_000_000
//			2_000_000
//			4_000_000
//			8_000_000
//			16_000_000
//			24_000_000
			48_000_000
//			96_000_000
        ];
        Utils.fillArray(original);
        
        // Shuffle array
        int[] array = original.clone();
        Utils.shuffleArray(array);
        
        // Sorting arrays
        javaSort(array, logger);
        serialMergeSort(array, logger); 
        parallelMergeSort(array, logger);
        forkjoinParallelMergeSort(array, logger);
    }
     
    // Sort array serially
    private static void javaSort(int[] array, EventProfiler logger) { 
    	System.out.print("Starting java sort... aaand ");
    	logger.start();
    	Arrays.sort(array);
    	logger.time();
    	Utils.checkArray(array);
    }
    
    // Sort array serially
    private static void serialMergeSort(int[] array, EventProfiler logger) { 
    	MergeSort sorter = new MergeSort(array);
    	logger.start();
    	array = sorter.serial();
    	logger.time();
    	Utils.checkArray(array);
    }
    
    // Sort array serially
    private static void parallelMergeSort(int[] array, EventProfiler logger) {   
    	MergeSort sorter = new MergeSort(array);
    	logger.start();
    	array = sorter.parallel();
    	logger.time();
    	Utils.checkArray(array);
    }
        
    // Sort array in parallel
    private static void forkjoinParallelMergeSort(int[] array, EventProfiler logger) {
    	MergeSort sorter = new MergeSort(array);
    	logger.start();
        array = sorter.forkjoin();
        logger.time();   
        Utils.checkArray(array);
    }
}
