import java.util.Arrays;
import java.util.Comparator;

public class TwoDimensionArraySorting {
	public static void twoDimensionArraySorting(int a[][]){
		Arrays.sort(a, new Comparator<int []>(){
			public int compare(int arr1[], int arr2[]) {
				int a1 = arr1[0]; //첫번째 기준으로 정렬
				int a2 = arr2[0];
				return Integer.compare(a1, a2); //오름차순. a2, a1은 내림치순
			}
		});
		//using Lamda
		//Arrays.sort(a,(Comparator<int []>)(int arr1[], int arr2[])->Integer.compare(arr1[0], arr2[0]));
	}
}
