import java.io.*;
import java.applet.*;
import java.awt.*;
import java.math.*;
import java.lang.*;
import java.util.Random;
import java.awt.event.*;

/*      <applet code="leapfrog" width=1200 height=550>
         </applet>
*/
public class leapfrog extends Applet implements Runnable {

  int x = 50, y = 550, r=70;         
  int dx = 2, dy = 3, dyo, dxo,i;  
  int n=1;
  double m=.1; //mass in kg, so 500 grams
  int xscale=100, yscale=100;
  Dimension size;                  // The size of the apple
  Image buffer;                    // The off-screen image for double-buffering
  Image ball,spring;
  Graphics2D big;
  Graphics bufferGraphics;         // A Graphics object for the buffer
  Thread animator;                 // Thread that performs the animation
  Color background;
  boolean please_stop;             // A flag asking animation thread to stop
  // Random m_r=new Random();
  double pi = 4.0*Math.atan(1.0); 
  double v0, vix, viy, xo,t,dt ; 
  double A, w;
  double k,g;
  double timemax;
  double aNow;
  double yeq;
  double Vph,Vmh;
  int yNext;

  /** Set up an off-screen Image for double-buffering */
  public void init() {

    size = this.size();
    buffer = this.createImage(size.width, size.height);
    ball = getImage(getCodeBase(), "ball.gif" );
    spring = getImage(getCodeBase(), "spring.jpg");
    bufferGraphics = buffer.getGraphics();
    background = new Color(255,255,255);
    setBackground(background);
    //Set initial conditions
    t=0;
    g=9.8;
    k=8.0;    
    v0=0;
    double spH=300; 
    yeq=(m*g/k)+spH;


    dt=0.01; 
    Vmh=0;
  }
   
  /** Draw the circle at its current position, using double-buffering */
  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    // Draw into the off-screen buffer.
    bufferGraphics.setColor(background);
    bufferGraphics.fillRect(0, 0, 3*size.width, 3*size.height); // clear the buffer

    bufferGraphics.drawImage(spring,50,y,35,550-y,this);
    bufferGraphics.drawImage(ball,50,y-10,this);

     g2.drawImage(buffer, 0, 0, this); //this x,y pair dictates the starting point of the string
  }

  public void update(Graphics g) { paint(g);}

  public void run() {
    while(!please_stop) {
      aNow=g-(k/m)*(y-yeq);
      Vph=Vmh+aNow*dt;
      yNext=(int)(y+Vph*dt);
      y=yNext;
      Vmh=Vph;

      // Move the ball

      x= 0;

      repaint();

      try {
      Thread.sleep(60);
      } catch (InterruptedException e) { ; }
    
    animator = null;
    }
  }
//}
/** Start the animation thread */
  public void start() {
    if (animator == null) {
      please_stop = false;
      animator = new Thread(this);
      animator.start();
    }
  }

/** Stop the animation thread */
  public void stop() { please_stop = true; }

/** Allow the user to start and stop the animation by clicking */
  public boolean mouseDown(Event e, int x, int y) {
    if (animator != null) please_stop = true;  // if running request a stop
    else start();                              // otherwise start it.
    return true;
  }
}


