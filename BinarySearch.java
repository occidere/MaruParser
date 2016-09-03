import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		int d[] = new int[1000];
		Arrays.setAll(d, i->(int)(Math.random()*1000)+1);
		Arrays.sort(d);
		
		long st = System.currentTimeMillis();
		System.out.println(new BinarySearch().bs(d, 37));
		System.out.println((System.currentTimeMillis()-st)+"ms");
	}
}

class BinarySearch {
	public int bs(int arr[], int key) {
		int front, mid, rear;
		front = 0; rear = arr.length - 1;
		while (true) {
			mid = (front + rear) / 2;
			if (arr[mid] == key) return arr[mid];
			if (arr[front] == key) return arr[front];
			if (arr[rear] == key) return arr[rear];

			if (arr[mid] < key) front = mid + 1;
			else rear = mid - 1;
			if (rear <= front) return -1;
		}
	}
}
