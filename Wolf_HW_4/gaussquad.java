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

public class gaussquad {

  public static void main(String []args) throws IOException {

      Scanner in = new Scanner(System.in);
      System.out.print("what is the mass of the spring (in kilograms)?: ");
      double mass = in.nextDouble();

      Scanner in2 = new Scanner(System.in);
      System.out.print("What is the amplitude (in meters)?: ");
      double amp = in2.nextDouble();

      Scanner in3 = new Scanner(System.in);
      System.out.print("What is the number of divisions you would prefer for the Gaussian quadrature method?: ");
      double ndiv = in3.nextDouble();


  // System.out.println("mass is "+mass);
  // System.out.println("amplitude is "+amp);
  // System.out.println("ndiv is "+ndiv);

  //open file to write to
  //PrintWriter writer = new PrintWriter("gaussquadData.txt", "UTF-8");
  // FileWriter f0 = new FileWriter("gqData.txt");

  String newLine = System.getProperty("line.separator");

   FileWriter fwriter = new FileWriter("gqData.txt");
   PrintWriter outputFile = new PrintWriter(fwriter);
   double a=0.01;
   double diff=(amp-a)/100;
  //Begin while loop
while (a<=amp){
    double periodCoeff=Math.sqrt(8*mass);
    double step=a/ndiv;
    // System.out.println("stepsize is "+step);
    double lower=0;
    double upper=0+step;
    double inttotal=0;
    double vAmp=Math.pow(a,4);
    while (upper<=a){
      //Set up terms for gaussian quadrature 
      double firstTerm=(upper-lower)/2;
      double secondTerm=(upper+lower)/2;
      // double firstVar=(firstTerm*(-1/Math.sqrt(3))+secondTerm);
      // double secondVar=(firstTerm*(1/Math.sqrt(3))+secondTerm);
      // //Change these two according to the function
      // // double firstAdd=firstTerm*Math.pow(firstVar,4);
      // // double secondAdd=firstTerm*Math.pow(secondVar,4);
      // double firstAdd=firstTerm*(1/Math.sqrt((vAmp-Math.pow(firstVar,4))));
      // double secondAdd=firstTerm*(1/Math.sqrt((vAmp-Math.pow(secondVar,4))));
      // //hardcode it
      double stepT=periodCoeff*(firstTerm*1/Math.sqrt(vAmp-Math.pow((firstTerm*(-1/Math.sqrt(3))+(secondTerm)),4))+firstTerm*1/Math.sqrt(vAmp-Math.pow((firstTerm*(1/Math.sqrt(3))+(secondTerm)),4)));
      // double quadsum=firstAdd+secondAdd;
      inttotal=inttotal+stepT;
      // double stepT=periodCoeff*quadsum;
      
            
      lower=lower+step;
      upper=upper+step;
      // System.out.println("calculating...");
      // System.out.println("inttotal= "+inttotal);
    }
    outputFile.print(a+" "+inttotal+"\n"); 
    System.out.println("the result of the integral is: "+inttotal);
    a=a+diff;
  }
  
    outputFile.close();
}
}