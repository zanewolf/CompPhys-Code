import java.applet.*;
import java.awt.*;
import java.math.*;
import java.lang.*;
import java.util.Scanner;
import java.awt.event.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class tempA {

  public static void main(String []args) throws IOException {

        Scanner in = new Scanner(System.in);
        System.out.print("what is the minimum value of Temperature (in Kelvin)? (Choose 0 if integrating over only value of T.): ");
        double mintemp = in.nextDouble();

        Scanner in3 = new Scanner(System.in);
        System.out.print("what is the maximum value of Temperature (in Kelvin)?: ");
        double maxtemp = in3.nextDouble();

        Scanner in2 = new Scanner(System.in);
        System.out.print("What is the number of divisions you would prefer for the Gaussian quadrature method?: ");
        double ndiv = in2.nextDouble();

    String newLine = System.getProperty("line.separator");
    FileWriter fwriter = new FileWriter("TempA.txt");
    PrintWriter outputFile = new PrintWriter(fwriter);

    double diffT=(maxtemp-mintemp)/1000;
    double upperT=mintemp+diffT;
    // double upperT=maxTemp;

    while (upperT<=maxtemp) {
        double debyeT=428;
        double tempFrac=debyeT/upperT;
        double invtempFrac=upperT/debyeT;
        double step=(tempFrac)/ndiv;

        double lower=0;
        double upper=0+step;
        //Begin while loop
        double coeff1=9*0.001*(6.022e28)*(1.38065e-23)*Math.pow(invtempFrac,3);
        double inttotal=0;

          while (upper<=tempFrac){
            //Set up terms for gaussian quadrature 
            double diff=(upper-lower)/2;
            double sum=(upper+lower)/2;
            double root1=-1/Math.sqrt(3);
            double root2=1/Math.sqrt(3);

            double var1=diff*root1+sum;
            double var2=diff*root2+sum;

            double term1=diff*((Math.pow(var1,4)*Math.exp(var1))/(Math.pow(Math.exp(var1)-1,2)));
            double term2=diff*((Math.pow(var2,4)*Math.exp(var2))/(Math.pow(Math.exp(var2)-1,2)));

            double total=coeff1*(term1+term2);
            inttotal=inttotal+total;
            System.out.println("total "+total);
            System.out.println("inttotal "+inttotal);

            lower=lower+step;
            upper=upper+step;

          }
      System.out.println("the result of the integral is: "+inttotal);      
      outputFile.print(upperT+" "+inttotal+"\n");
      upperT=upperT+diffT;


    }
      outputFile.close();
  }
}