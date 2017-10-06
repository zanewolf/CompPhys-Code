import java.applet.*;
import java.awt.*;
import java.math.*;
import java.lang.*;
import java.util.Random;
import java.awt.event.*;

/*      <applet code="oscillator" width=1200 height=550>
         </applet>
*/
public class oscillator extends Applet implements Runnable, ActionListener {

  int x = 150, y = 50, r=70;         
  int dx = 3, dy = 2, dyo, dxo;      
  int n=1;
  int m=1;
  int xscale=100, yscale=100;
  Dimension size;                  // The size of the apple
  Image buffer;                    // The off-screen image for double-buffering
  Image ball,fire,spring;
  Graphics2D big;
  Graphics bufferGraphics;         // A Graphics object for the buffer
  Thread animator;                 // Thread that performs the animation
  Color background;
  boolean please_stop;             // A flag asking animation thread to stop
  Random m_r=new Random();
  double pi = 4.0*Math.atan(1.0); 
  double vi, vix, viy, xo,yo,t,dt ; 
  double A, w;
  TextField newA,newk,kinenergy,potenergy;
    double newk2=0.0;
  double newA2=0.0;
  double k;
  double xcal;
  double vel;


  /** Set up an off-screen Image for double-buffering */
  public void init() {
    newA = new TextField("Initial displacement",30);  
    newk = new TextField("Spring Constant",30);
    kinenergy=new TextField(16);
    potenergy=new TextField(16);

    Button resetbutton= new Button("RESET");
    size = this.size();
    buffer = this.createImage(size.width, size.height);
    ball = getImage(getCodeBase(), "ball.gif" );
    fire = getImage(getCodeBase(), "fire.jpg" );
    spring = getImage(getCodeBase(), "spr.jpg");
    bufferGraphics = buffer.getGraphics();
    background = new Color(255,255,255);
    setBackground(background);
    t=0;
    dt=.01;
    xo=0;
    vi=70;
    vix=vi*Math.cos(45.*pi/180.);
    viy=vi*Math.sin(45.*pi/180.);
    A=250;
    w=2.75;
    k=Math.pow(w,2);
    add(resetbutton);
    add(newA);//added textfield to new window
    add(newk);
    add(kinenergy);
    add(potenergy);
    resetbutton.addActionListener(this); 
  }

  /** Draw the circle at its current position, using double-buffering */
  public void paint(Graphics g) {
  Graphics2D g2 = (Graphics2D) g;
    // Draw into the off-screen buffer.
    bufferGraphics.setColor(background);
    bufferGraphics.fillRect(0, 0, 3*size.width, 3*size.height); // clear the buffer
      bufferGraphics.drawImage(spring,0,500,x+8,35,this);
      bufferGraphics.drawImage(ball,x,y,this);
      bufferGraphics.setColor(new Color(0,0,0));
      bufferGraphics.drawLine(400,300,600,300);
      bufferGraphics.drawLine(500,200,500,400);
      bufferGraphics.drawString("X",600,300);
      bufferGraphics.drawString("V",500,400);
      g2.drawImage(buffer, 0, 0, this);

      g2.fillOval((int)((.05)*xcal+500),(int)((.05)*vel+300),5,5);
      

       
    
  }
    public void actionPerformed(ActionEvent event) { 
        String newA1 = newA.getText();
        String newk1 = newk.getText();
        double newA2=Double.parseDouble(newA1);
        double newk2=Double.parseDouble(newk1);
        w=Math.sqrt(newk2);
        k=newk2;
        t=0;
        A=newA2;
        t=0;
        t=t+dt;
        x=(int)(300+A*Math.cos(w*t));
        y= 500; 
        repaint();
      }

  public void update(Graphics g) { paint(g);}

  /** The body of the animation thread */
  public void run() {
    while(!please_stop) {

      // Move the ball 
      t=t+dt;
      x=(int)(300+A*Math.cos(w*t));
      y= 500;
      double midx=(A/2+300);
      xcal=x-300;
      double vmax=w*(A);
      vel=-w*(A*Math.sin(w*t));
      double vel2=vmax*Math.sqrt(1-(Math.pow(xcal,2)/Math.pow(A,2)));
      
      
      double emax=0.5*Math.pow(vmax,2);
      // double emax2=0.5*k*Math.pow(300+A,2);
      double potE1=0.5*k*Math.pow(xcal,2);
      double kinE1=0.5*Math.pow(vel2,2);
      double potE=emax-kinE1;      
      double kinE2=emax-potE1;
      
      double emax3=kinE1+potE1;
      // System.out.println("Emax: "+emax);
      // System.out.println("Emax3: "+emax3);
      // double potE=0.5*newk2*Math.pow(x,2);
      // System.out.println("PotE: "+potE1);
      // System.out.println("KinE: "+kinE2);
      kinenergy.setText("KinEnergy:"+Double.toString(kinE1));
      potenergy.setText("PotEnergy:"+Double.toString(potE1));

            repaint();


      // Now pause some milliseconds before drawing the shrimp again.
      try {
      Thread.sleep(60);
      } catch (InterruptedException e) { ; }
    }
    animator = null;
  }

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
