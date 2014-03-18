package fb.demo;

import java.util.Scanner;

public class Demo {
	public static void main(String[] args) {
		while(true){
			Scanner cin = new Scanner(System.in);
			System.out.println("Please input your name:(Input 'quit' to quit)");
			String name = cin.nextLine();
			if(name.equalsIgnoreCase("quit"))break;
			System.out.println("Please input your age:");
			int age = cin.nextInt();
			System.out.println("Your name:"+name+" and aged "+age);
			cin.close();
		}
	}
}
