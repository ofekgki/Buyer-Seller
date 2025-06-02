package Managers;
import java.util.Scanner;

import Models.Address;
public class UserInput {
	
		public static Scanner sc = new Scanner(System.in);
				
		public static String stringInput(String msg) {
			System.out.println(msg);
			String choise= sc.nextLine();
			return choise;
			
		}
		
		
		
		
		
	
}
