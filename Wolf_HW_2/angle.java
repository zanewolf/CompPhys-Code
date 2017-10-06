import java.util.Scanner;
public class angle {

public static void main(String []args) {

		Scanner in = new Scanner(System.in);
		System.out.print("Please specify the incoming height of the projectile (assuming the radius is at height 0): ");
		Double height = in.nextDouble();

		Scanner in2 = new Scanner(System.in);
		System.out.print("Do you want to change the radius from a default of 1.0? (yes/no): ");
		String option = in2.nextLine();

		String yes="yes";

		if (option.equals(yes)){
			Scanner in3 = new Scanner(System.in);
			System.out.print("What is the new radius? ");
			Double newrad = in3.nextDouble();
			if (height>newrad){
				System.out.println("Error: height is greater than radius and will not impact the circle.");
			} else {
				System.out.println("the radius is "+newrad);
				double angleArcL=Math.toDegrees(Math.asin(height/newrad));
				double angleRef=180-2*angleArcL;
				System.out.println("The angle of reflection is "+angleRef);
			}	

		} else {
			double radius=1.0;
			if (height>radius){
				System.out.println("Error: height is greater than radius and will not impact the circle.");
			} else {
				System.out.println("the radius is 1");
				double angleArcL_1=Math.toDegrees(Math.asin(height/radius));
				double angleRef=180-2*angleArcL_1;
				System.out.println("The angle of reflection is "+angleRef);
			}
		}
	}
}


