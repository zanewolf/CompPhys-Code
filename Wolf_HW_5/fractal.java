import java.applet.*;
import java.awt.*;
import java.math.*;
import java.lang.*;
import java.util.Random;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class  fractal {
	public static void main (String []args) {
	//fractal extends Applet implements Runnable {
	double x,y;
	int z;
	int n;
	Random rand;
	x=0;
	y=0;
	n=0;

		while (n<=50000000){
			// Generate random number
			rand=new Random();
			z=rand.nextInt(3)+1;

			if (z==1){
				x=(x+2.0)/2.0;
				y=y/2.0;
			} else if (z==2){
				x=(x+1.0)/2.0;
				y=(y+Math.sqrt(3.0))/2.0;
			} else if (z==3){
				x=x/2.0;
				y=y/2.0;
			}
			System.out.println (x+" "+y);
			n=n+1;

		}

	}
}