/*
<applet code ="doublePendula1" width="1600" height="1000">
</applet>

*/

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.lang.*;
import java.io.*;
import java.applet.*;

public class doublePendula1 extends Applet implements ActionListener, Runnable{
    private Panel buttonPanel,buttonPanel2;
    private Label prompt1, prompt2, prompt3, prompt4, prompt5;
    private TextField input1, input2, input3, input4, input5;
    private Button butt, butt2, butt3;
    double pi= 4*Math.atan(1.);
    double l=100;
    double l1=100;
    double l2=100;
    double g=-10;
   
    double x1_o, x2_o,y1_o,y2_o;
    double w1_o, w2_o,z1_o, z2_o;
    double b=0;//.1;
    double v=0;
    double v1=0;
    double v2=0;
    double h1=0;
    double h2=0;
    double theta1=pi/2;//-pi/2;
    double theta2=pi/2;
    double m1=1, m2=1;
    double x1=l1*Math.sin(theta1), y1=Math.sqrt(l1*l1-x1*x1);
    double x2=l2*Math.sin(theta2), y2=Math.sqrt(l2*l2-x2*x2);
    double vx_1=v*Math.cos(theta1);
    double vy_1=v*Math.sin(-theta1);
    int[][] r_n= new int[1000000][2];
    int[][] r_o= new int[1000000][2];
    double phi1=-pi/2-.1;
    double phi2=-pi/2-.05;

    double w1=l1*Math.sin(phi1), z1=Math.sqrt(l*l-w1*w1);
    double w2=l2*Math.sin(phi2), z2=Math.sqrt(l*l-w2*w2);
    
    int[][] s_n= new int[1000000][2];
    int[][] s_o= new int[1000000][2];


    double dt=.01;
    boolean secondPendulum=false;
    Thread th=null;
    double phi;
    boolean threadSuspended;
    Image Buffer;
    Graphics gBuffer;
    Color background;
    int t=0;
    double lya;
    double Lt;
    double Lt0;
    double thetatemp2;



    public void init(){
	Buffer=createImage(4000, 1500);
	gBuffer=Buffer.getGraphics();
	background=new Color(18, 205, 175);
	setBackground(background);
	setLayout(new BorderLayout());
	buttonPanel = new Panel();
	buttonPanel2=new Panel();
	butt=new Button("Start");
	butt.addActionListener(this);
	buttonPanel.add(butt);
	butt2=new Button("Add second pendulum");
	butt2.addActionListener(this);
	buttonPanel.add(butt2);
	butt3=new Button("Reset");
	butt3.addActionListener(this);
	buttonPanel.add(butt3);
	
	prompt1=new Label("theta2");
	input1=new TextField(10);
	input1.addActionListener(this);
	input1.setText(Double.toString(theta2));
	prompt2=new Label("theta1");
	input2=new TextField(10);
	input2.addActionListener(this);
	input2.setText(Double.toString(theta1));
	prompt3=new Label("Drag");
	input3=new TextField(10);
	input3.addActionListener(this);
	input3.setText(Double.toString(b));
	prompt4=new Label("phi1");
	input4=new TextField(10);
	input4.addActionListener(this);
	input4.setText(Double.toString(phi1));
	prompt5=new Label("phi2");
	input5=new TextField(10);
	input5.addActionListener(this);
	input5.setText(Double.toString(phi2));


	buttonPanel2.add(prompt2);
	buttonPanel2.add(input2);
	buttonPanel2.add(prompt1);
	buttonPanel2.add(input1);
	buttonPanel2.add(prompt4);
	buttonPanel2.add(input4);
	buttonPanel2.add(prompt5);
	buttonPanel2.add(input5);
	buttonPanel2.add(prompt3);
	buttonPanel2.add(input3);

	add(buttonPanel, BorderLayout.NORTH);
	add(buttonPanel2,BorderLayout.SOUTH);
    }

    public void destroy(){
	System.out.println("destroy()");
    }

    public void start(){
	System.out.println("start(): begin");
	if (th== null){
	    System.out.println("start(): creating thread");
	    th = new Thread( this );
	    System.out.println("start(): starting thread");
	    threadSuspended=false;
	    th.start();
	}else {
	    if (threadSuspended){
		threadSuspended=false;
		System.out.println("start(): notifying thread");
		synchronized(this){
		    notify();
		}
	    }
	}
	System.out.println("start(): end");
    }

    public void setActionCommand(String command){
    }

    //excuted when browser leaves the page containting applet
    public void stop(){
	System.out.println("stop(): begin");
	threadSuspended=true;
    }
    public void run(){
	
	try{
	    while(true){
		if(t==0){
		    x2_o=x2+x1;
		    y2_o=-y2-y1;
		}
		double thetatemp1=theta1;
		thetatemp2=theta2;
		double vtemp1=v1;
		theta1=theta1+dt*(l2*v1-l1*v2*Math.cos(theta1-theta2))/(l1*l1*l2*(m1+m2*Math.sin(theta1-theta2)*Math.sin(theta1-theta2)));
		v1=v1+dt*(-v1*b-(m1+m2)*g*l1*Math.sin(thetatemp1)-v1*v2*Math.sin(thetatemp1-theta2)/(l1*l2*(m1+m2*Math.pow(Math.sin(thetatemp1-thetatemp2),2)))+(l2*l2*m2*vtemp1*vtemp1+l1*l1*(m1+m2)*v2*v2-l1*l2*m2*v1*v2*Math.cos(thetatemp1-theta2))/(2*l1*l1*l2*l2*Math.pow(m1+m2*Math.pow(Math.sin(thetatemp1-thetatemp2),2),2))*Math.sin(2*(thetatemp1-thetatemp2)));//upper pendulum velocity
		
		theta2=theta2+dt*(l1*(m1+m2)*v2-l2*m2*vtemp1*Math.cos(thetatemp1-theta2))/(l1*l2*l2*m2*(m1+m2*Math.sin(thetatemp1-theta2)*Math.sin(thetatemp1-theta2)));
		v2=v2+dt*(-v2*b-g*m2*l2*Math.sin(thetatemp2)+vtemp1*v2*Math.sin(thetatemp1-thetatemp2)/(l1*l2*(m1+m2*Math.pow(Math.sin(thetatemp1-thetatemp2),2)))-(l2*l2*m2*vtemp1*vtemp1+l1*l1*(m1+m2)*v2*v2-l1*l2*m2*vtemp1*v2*Math.cos(thetatemp1-thetatemp2))/(2*l1*l1*l2*l2*Math.pow(m1+m2*Math.pow(Math.sin(thetatemp1-thetatemp2),2),2))*Math.sin(2*(thetatemp1-thetatemp2)));//lower pendulum velocity

	
		x1=l1*Math.sin(theta1);
		y1=l1*Math.cos(theta1);
		x2=l2*Math.sin(theta2);
		y2=l2*Math.cos(theta2);
		r_n[(int)t][0]=(int)(x2+x1);
		r_n[(int)t][1]=(int)(y2+y1);
		

		if(secondPendulum){


		    if(t==0){
			w2_o=w2+w1;
			z2_o=-z2-z1;
			Lt0=Math.sqrt((phi2-theta2)*(phi2-theta2)+(phi1-theta1)*(phi1-theta1)+(h2-v2)*(h2-v2)+(h1-v1)*(h1-v1));
		    }

		    double phitemp1=phi1;
		    double phitemp2=phi2;
		    double htemp1=h1;

		    phi1=phi1+dt*(l2*h1-l1*h2*Math.cos(phi1-phi2))/(l1*l1*l2*(m1+m2*Math.sin(phi1-phi2)*Math.sin(phi1-phi2)));
		    h1=h1+dt*(-h1*b-(m1+m2)*g*l1*Math.sin(phitemp1)-h1*h2*Math.sin(phitemp1-phi2)/(l1*l2*(m1+m2*Math.pow(Math.sin(phitemp1-phitemp2),2)))+(l2*l2*m2*htemp1*htemp1+l1*l1*(m1+m2)*h2*h2-l1*l2*m2*h1*h2*Math.cos(phitemp1-phi2))/(2*l1*l1*l2*l2*Math.pow(m1+m2*Math.pow(Math.sin(phitemp1-phitemp2),2),2))*Math.sin(2*(phitemp1-phitemp2)));//upper pendulum velocity
		
		    phi2=phi2+dt*(l1*(m1+m2)*h2-l2*m2*htemp1*Math.cos(phitemp1-phitemp2))/(l1*l2*l2*m2*(m1+m2*Math.sin(phitemp1-phitemp2)*Math.sin(phitemp1-phitemp2)));
		    h2=h2+dt*(-h2*b-g*m2*l2*Math.sin(phitemp2)+htemp1*h2*Math.sin(phitemp1-phitemp2)/(l1*l2*(m1+m2*Math.pow(Math.sin(phitemp1-phitemp2),2)))-(l2*l2*m2*htemp1*htemp1+l1*l1*(m1+m2)*h2*h2-l1*l2*m2*htemp1*h2*Math.cos(phitemp1-phitemp2))/(2*l1*l1*l2*l2*Math.pow(m1+m2*Math.pow(Math.sin(phitemp1-phitemp2),2),2))*Math.sin(2*(phitemp1-phitemp2)));//lower pendulum velocity
		    w1=l*Math.sin(phi1);
		    z1=l*Math.cos(phi1);
		    w2=l2*Math.sin(phi2);
		    z2=l2*Math.cos(phi2);
		    s_n[(int)t][0]=(int)(w2+w1);
		    s_n[(int)t][1]=(int)(z2+z1);

		    Lt=Math.sqrt((phi2-theta2)*(phi2-theta2)+(phi1-theta1)*(phi1-theta1)+(h2-v2)*(h2-v2)+(h1-v1)*(h1-v1));
		    //Lt=Math.sqrt((w2+w1-x2-x1)*(w2+w1-x2-x1)+(z2+z1-y1-y2)*(z2+z1-y1-y2));
			// Lt= Math.abs((w2+w1)*(w2+w1)+(z1+z2)*(z2+z1)-(x1+x2)*(x1+x2)-(y1+y2)*(y1+y2));
		    lya=1/((double)t*dt)*Math.log(Lt/Lt0);
		    System.out.println("lya= "+lya);
		}

	       
		if (threadSuspended){
		    synchronized(this){
			while(threadSuspended){
			    System.out.println("run(): waiting");
			    wait();
			}
		    }
		}
		repaint();
		t++;
		try{
		    Thread.sleep(1);
		    } catch(InterruptedException ie){}
	    }
	}
	catch (InterruptedException ie){}
	System.out.println("run(): end");
    }

    public void actionPerformed( ActionEvent ae){
	if(ae.getSource()==butt2){
	    secondPendulum=true;
	   
	}
	//v=Double.valueOf(input1.getText()).doubleValue();
	//	input1.setText(input1.getText());
	
	if(ae.getSource()==butt){
	    g=-9.81;
	    t=0;
	}
	if(ae.getSource()==butt3){//reset
	    v1=0;
	    v2=0;
	    theta1=-pi/2;
	    theta2=-pi/2;
	    g=0;
	    t=0;
	    h1=0;
	    h2=0;
	    phi1=-pi/2;
	    phi2=-pi/2;
	    input2.setText(Double.toString(theta1));
	    input1.setText(Double.toString(theta2));
	    input4.setText(Double.toString(phi1));
	    input5.setText(Double.toString(phi2));
	}
	if(ae.getSource()==input2){
	    theta1=1*Double.valueOf(input2.getText()).doubleValue()+pi;
	    t=0;
	}
	if(ae.getSource()==input1){
	    theta2=1*Double.valueOf(input1.getText()).doubleValue()+pi;
	    t=0;
	}
	if(ae.getSource()==input3){
	    b=Double.valueOf(input3.getText()).doubleValue();
	    t=0;
	}
	if(ae.getSource()==input4){
	    t=0;
	    phi1=Double.valueOf(input4.getText()).doubleValue()+pi;
	}
	if(ae.getSource()==input5){
	    t=0;
	    phi2=Double.valueOf(input5.getText()).doubleValue()+pi;
	}
    }




    public void paint(Graphics g){
	Graphics2D g2=(Graphics2D) g;
	gBuffer.setColor(background);
	gBuffer.fillRect(0,0,5000,5000);
	gBuffer.setColor(Color.WHITE);
	//g2.setLineWidth(10);
	gBuffer.drawLine((int)(x1_o+300),(int)(-y1_o+200),(int)(x1+300),(int)(-y1+200));
	gBuffer.setColor(Color.RED);
	//gBuffer.setStroke(new BasicStroke(100));
	gBuffer.drawLine((int)(x1+300),(int)(-y1+200),(int)(x2+x1+300),(int)(-y2-y1+200));
	gBuffer.setColor(Color.BLUE);
    gBuffer.drawLine(600,300,1200,300);
    gBuffer.drawLine(900,0,900,600);
    gBuffer.drawString("Theta",1200,300);
    gBuffer.drawString("ThetaDot",900,600);
    // g2.drawImage(buffer, 0, 0, this);

   double theta2D=Math.toDegrees(theta2);
   double theta1D=Math.toDegrees(theta1);


   while (theta2D>360|| theta2D<-360) {
   		// theta2D=theta2D%360;
   		if (theta2D>360){
   		theta2D=theta2D-360;
  		} else if (theta2D<-360){
   		theta2D=theta2D+360;
  		} else {
   		theta2D=theta2D;
   		}
   	}
   	   while (theta1D>360|| theta1D<-360) {
   		if (theta1D>360){
   		theta1D=theta1D-360;
  		} else if (theta1D<-360){
   		theta1D=theta1D+360;
  		} else {
   		theta1D=theta1D;
   		}
   	}
   System.out.println(theta2D+"          "+v2);
    gBuffer.fillOval((int)((.1)*(theta2D)+900),(int)((.02)*(v2)+300),10,10);

    gBuffer.setColor(new Color(0,255,0)); 


    // gBuffer.fillOval((int)((.1)*(theta1D)+900),(int)((.01)*(v1)+300),10,10);


  


	//for(int i=1+t; i<(int)t;i++){
	int i=1;
	while(i<t){
	    //System.out.println(r_n[i][0]+", "+r_n[i][0]+", "+x2+", "+x1+", "+i);
	    if(t-i<5000){
		gBuffer.drawLine((int)(300)+(int)r_n[i-1][0],(int)(200)-r_n[i-1][1],(int)(300)+r_n[i][0],(int)(200)-r_n[i][1]);
	    }
		i++;
	    
	}
	r_o[t][0]=r_n[t][0];
	r_o[t][1]=r_n[t][1];

	if(secondPendulum){
	    Font font=new Font("Times New Roman", Font.BOLD, 30);
	    gBuffer.setFont(font);
	    gBuffer.drawString("lya= "+(double)(int)(lya*1000)/1000,800,500);

	    gBuffer.setColor(Color.WHITE);
	    //g2.setStroke(new BasicStroke(10));
	    gBuffer.drawLine((int)(w1_o+300),(int)(-z1_o+200),(int)(w1+300),(int)(-z1+200));
	    gBuffer.setColor(Color.ORANGE);
	    //gBuffer.setStroke(new BasicStroke(100));
	    gBuffer.drawLine((int)(w1+300),(int)(-z1+200),(int)(w2+w1+300),(int)(-z2-z1+200));
	    gBuffer.setColor(Color.MAGENTA);
	    int j=1;
	    while(j<t){
	    //for(int i=1+t/100; i<(int)t;i++){
		//System.out.println(r_n[i][0]+", "+r_n[i][0]+", "+x2+", "+x1+", "+i);
		if(t-j<5000){
		    gBuffer.drawLine((int)(300)+(int)s_n[j-1][0],(int)(200)-s_n[j-1][1],(int)(300)+s_n[j][0],(int)(200)-s_n[j][1]);
		}
		j++;
	    }
	    s_o[t][0]=s_n[t][0];
	    s_o[t][1]=s_n[t][1];
	    
	}
		g.drawImage(Buffer,0,0,this);
	
    }
    public void update(Graphics g){paint(g);}
}
