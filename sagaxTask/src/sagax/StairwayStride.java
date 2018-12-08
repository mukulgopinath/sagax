package sagax;

import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class StairwayStride {
	private static final int min_stair = 1, max_stair = 20, min_stair_flight = 1, max_stair_flight = 30, min_stride = 1, max_stride = 4, stride_per_landing = 2;
	private static int[] stair_arr = new int[max_stair_flight];
	private static int no_of_flights, no_of_step_per_stride, totalStrides = 0; 
	public static void main(String[] args) {
		JSONArray responseJson = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("msg", "error");
			boolean readFlag = false, calculateFlag = false;
			readFlag = readAndValidateInputs();
			if(readFlag) {
				calculateFlag = calculateTotalStrides(false);
			}
			if(readFlag && calculateFlag) {
				System.out.println("Total number of Strides = "+totalStrides); // For testing as a standalone Java 
				jsonObj.put("totalStrides",totalStrides); //totalStride Key in JSONObject contains the calculated value
				jsonObj.put("msg", "success"); // overwrite when no exception
			}else {
				System.out.println("Error ");
				jsonObj.put("msg", "error"); // overwrite when no exception
			}
		}catch(Exception e) {
			System.out.println("Exception >>>>>"+e.getMessage());
		}
		responseJson.put(jsonObj);
	}
	public static boolean readAndValidateInputs() {
		boolean finalFlag = true;
		try {
			Scanner scan = new Scanner(System.in);
			boolean errFlag = false;
			do {
				System.out.println("Please enter the number of flights of stairs ["+min_stair_flight+","+max_stair_flight+"]: ");
				no_of_flights = scan.nextInt();
				if(no_of_flights < min_stair_flight || no_of_flights > max_stair_flight) {
					System.out.println("Error! Please enter a number between ["+min_stair_flight+","+max_stair_flight+"]: ");
					errFlag = true;
				}
			}while(errFlag);
			System.out.println("Please enter the stairs in each flight ["+min_stair+","+max_stair+"]: ");
			for(int i=0; i < no_of_flights; i++) {
				stair_arr[i] = scan.nextInt();
				if(stair_arr[i] < min_stair || stair_arr[i] > max_stair) {
					System.out.println("Error! Please enter a number between ["+min_stair+","+max_stair+"]: ");
					i--;
				}
			}
			errFlag = false;
			do {
				System.out.println("Please enter number of steps per stride ["+min_stride+","+max_stride+"]: ");
				no_of_step_per_stride = scan.nextInt();
				if(no_of_step_per_stride < min_stride || no_of_step_per_stride > max_stride) {
					System.out.println("Error! Please enter a number between ["+min_stride+","+max_stride+"]: ");
					errFlag = true;
				}
			}while(errFlag);
			scan.close();
		}catch(Exception e) {
			finalFlag = false;
			System.out.println(e.getMessage());
		}
		return finalFlag;
	}
	public static boolean calculateTotalStrides(boolean isTest) {
		boolean finalFlag = true;
		try {
			if(isTest) {
				for(int i = 0; i < stair_arr.length; i++) {
					if(stair_arr[i] < min_stair || stair_arr[i] > max_stair) {
						System.out.println("Invalid data in stairs in a flight");
						finalFlag = false; //For HTML validation. Not necessary if handled at Front end
					}
				}
				if(no_of_flights < min_stair_flight || no_of_flights > max_stair_flight) {
					System.out.println("Invalid data: in stairs in a flight");
					finalFlag = false; //For HTML validation. Not necessary if handled at Front end
				}
				if(stair_arr.length != no_of_flights) {
					System.out.println("Invalid data: Flight Length and Number of Flights don't match");
					finalFlag = false; //For HTML validation. Not necessary if handled at Front end
				}
				if(no_of_step_per_stride < min_stride || no_of_step_per_stride > max_stride) {
					System.out.println("Invalid data: in Number of Steps in a Stride");
					finalFlag = false; //For HTML validation. Not necessary if handled at Front end	
				}
			}
			totalStrides = (no_of_flights - 1) * stride_per_landing; // each pair of flight has a landing so 3 flights will have 3-1 = 2 landings
			for(int i = 0; i < no_of_flights; i++) {
				totalStrides += (stair_arr[i] / no_of_step_per_stride); //number of strides in whole (eg 10/3) 3stride
				if(stair_arr[i] % no_of_step_per_stride != 0) {
					totalStrides++; // any remaining steps lesser than stride size (10%3=1) 3+1=4strides
				}
			}
		}catch(Exception e) {
			finalFlag = false;
			System.out.println(e.getMessage());
		}
		return finalFlag;
	}
	public static boolean testFunction() {
		boolean flag = false;
		int expectedResult;
		no_of_step_per_stride = 3;
		no_of_flights = 4;
		stair_arr[0] = 13;
		stair_arr[1] = 10;
		stair_arr[2] = 12;
		stair_arr[3] = 10;
		expectedResult = 23;
		
		System.out.println("Test case 1:");
		no_of_flights = 5; // only 4 flights in array but flights is 5 should throw error message
		flag = calculateTotalStrides(true); //isTest is true as it is a test case
		System.out.println("Expected: false \n Obtained: "+flag);	

		no_of_step_per_stride = max_stride + 2; // 2 stair more than the limit should throw error message
		System.out.println("Test case 2:");
		flag = calculateTotalStrides(true); //isTest is true as it is a test case
		System.out.println("Expected: false \n Obtained: "+flag);
		
		no_of_flights = 4; //correct value
		System.out.println("Test case 3:");
		flag = calculateTotalStrides(true); //isTest is true as it is a test case
		System.out.println("Expected: "+expectedResult+" Obtained: "+totalStrides);
		System.out.println("Test passed>> "+flag);
		
		
		no_of_flights = 5; //correct value
		System.out.println("Test case 4:");
		flag = calculateTotalStrides(true); //isTest is true as it is a test case
		System.out.println("Expected: "+expectedResult+" Obtained: "+totalStrides);
		System.out.println("Test passed>> "+flag);
		
		no_of_flights = 5; //correct value
		no_of_step_per_stride = 2;
		stair_arr[0] = 16;
		stair_arr[1] = 9;
		stair_arr[2] = 11;
		stair_arr[3] = 15;
		stair_arr[4] = 5;
		expectedResult = 38;
		System.out.println("Test case 5:");
		flag = calculateTotalStrides(true); //isTest is true as it is a test case
		System.out.println("Expected: "+expectedResult+" Obtained: "+totalStrides);
		System.out.println("Test passed>> "+flag);
		
		return flag;
	}
}
