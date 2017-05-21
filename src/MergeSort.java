import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Stefan on 16-May-17.
 */

class MergeSort {
	
	private int[] array;
	private int threshold;
	private Sorter sorter;
	private ForkJoinPool pool;
	
	public MergeSort(int[] array) {
		this.array = array;
	}

	/*
	 * Starts serial sorting.
	 */
	public int[] serial() {
		if (!validArray(array)) return array;
		System.out.print("Starting serial sort... aaand ");
		
		int high = array.length - 1;
		int mid = high / 2;

		this.sorter = new Sorter();
		
		return array = sorter.merge(sorter.sort(array, 0, mid), sorter.sort(array, mid + 1, high));
	}
	
	/*
	 * Starts serial sorting.
	 */
	public int[] parallel() {
		if (!validArray(array)) return array;
		System.out.print("Starting parallel sort... aaand ");
		
		int low = 0;
		int high = array.length - 1;
		int mid = high / 2;

		int[] array1 = Arrays.copyOfRange(array, 0, mid + 1 /* exclusive */);
		int[] array2 = Arrays.copyOfRange(array, mid + 1 /* inclusive */, high + 1 /* exclusive */);
		
		Sorter sorter1 = new Sorter(array1);
		Sorter sorter2 = new Sorter(array2);
		
		Thread t1 = new Thread(() -> sorter1.merge());
		Thread t2 = new Thread(() -> sorter2.merge());
		
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return array = sorter1.merge(sorter1.getArray(), sorter2.getArray());
	}
	
	/*
	 * Starts enhanced forkjoin parallel sorting.
	 */
	public int[] forkjoin() {
		if (!validArray(array)) return array;
		System.out.print("Starting parallel forkjoin sort... aaand ");
		
		int length = array.length - 1;

		threshold = array.length / Runtime.getRuntime().availableProcessors();
		if (threshold < 5) threshold = 5;
		
		this.sorter = new Sorter(array, 0, length, threshold);
		
		pool = new ForkJoinPool();
		pool.invoke(sorter);
		
		return sorter.compute();
	}
	
	/*
	 * Checks if array is filled.
	 */
	private static boolean validArray(int[] array) {
		if (array == null) {
			System.out.println("Array is null");
			return false;
		} else if (array.length < 2) {
			return false;
		}
		return true;
	}
}


class Sorter extends RecursiveTask<int[]> {
	
	private static final long serialVersionUID = 1L;
	public static int iterations = 0;
	public static int belowThresholds = 0;
	
	private int[] array;
	private int low, mid, high;
	private int threshold;
	
	public Sorter() {}
	public Sorter(int[] array) {
		this(array, 0, array.length - 1, 0);
	}
	public Sorter(int[] array, int low, int high, int threshold) {
		this.low = low;
		this.high = high;
		this.mid = low + (high-low) / 2;
		this.array = array;
		this.threshold = threshold;
	}
	
	public int[] getArray() {
		return Arrays.copyOfRange(array, low, high + 1 /* exclusive */);
	}
	
	@Override
	protected int[] compute() {
		
		iterations++;
		
		if (high - low <= threshold) {
			int[] sorted = Arrays.copyOfRange(array, low, high + 1);
			Arrays.sort(sorted);
			
			belowThresholds++;
			
			return sorted;
		}
		
		Sorter s1 = new Sorter(array, low, mid, threshold);
		Sorter s2 = new Sorter(array, mid + 1, high, threshold); 		
		
		s2.fork();
		
		return merge(s1.compute(), s2.join());
	}
		
	/*
	 * Sorts a given part of the array by calling merge.
	 */
	protected int[] sort(int[] array) {
		return sort(array, 0, array.length - 1);
	}
	protected int[] sort(int[] array, int low, int high) {
		if (low < high) {
			int half = low + (high - low) / 2;
			return merge(sort(array, low, half), sort(array, half + 1, high));
		} else {
			return new int[] {array[low]};
		}
	}
	
	/*
	 * Merges two arrays in a sorted fashion.
	 */
	protected void merge() {
		this.array = merge(sort(this.array, low, mid), sort(this.array, mid + 1, high));
	}
	protected int[] merge(int[] array1, int[] array2) {
		int[] result = new int[array1.length + array2.length];
		
//		System.out.println("Lengths: " + array1.length + " + " + array2.length);
		
		int pointer1 = 0;
		int pointer2 = 0;
		
		for (int i = 0; i < result.length; i++) {
			if (pointer1 > array1.length - 1) {
				result[i] = array2[pointer2];
//				System.out.println("X\tArr2: " + array2[pointer2]);
				pointer2++;
			} else if (pointer2 > array2.length - 1) {
				result[i] = array1[pointer1];
//				System.out.println("X\tArr1: " + array1[pointer1]);
				pointer1++;
			} else if (array1[pointer1] < array2[pointer2]) {
				result[i] = array1[pointer1];
//				System.out.println("\tArr1: " + array1[pointer1]);
				pointer1++;
			} else {
				result[i] = array2[pointer2];
//				System.out.println("\tArr2: " + array2[pointer2]);
				pointer2++;
			}
		}
		
		return result;
	}
}