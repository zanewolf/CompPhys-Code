import java.applet.*;
import java.awt.*;
import java.math.*;
import java.lang.*;
import java.util.Random;
import java.awt.event.*;

/*      <applet code="predprey2" width=1200 height=550>
         </applet>
*/
public class predprey2 extends Applet implements Runnable, ActionListener {

  int x = 0, y = 0;         
  double m1=0.1; //mass in kg, so grams
  double m2=0.1;
  double b2=0.2;
  double b1=0.1;
  double e1=1.0;
  double e2=2.0;
  double K=500;
  double k=0.2;
  double a=0.2;
  double b=0.1;
  double Q1now, Q2now;
  Dimension size;                  // The size of the apple
  Image buffer;                    // The off-screen image for double-buffering
  //Image pred, prey,jungle;
  Graphics2D big;
  Graphics gbuffer;         // A Graphics object for the buffer
  Thread animator;                 // Thread that performs the animation
  Color background;
  boolean please_stop,click;             // A flag asking animation thread to stop
  //Random m_r=new Random();
  //double pi = 4.0*Math.atan(1.0); 
  double Pprev, Pnow, Q1prev, Q2prev;
  double t,dt;
  TextField bparam, bparam2;

  /** Set up an off-screen Image for double-buffering */
  public void init() {
    bparam = new TextField("0.1",10);
    bparam2= new TextField("0.2",10);
  
    Button resetbutton= new Button("RESET");
    resetbutton.addActionListener(this); 
    add(resetbutton);

    size = this.size();
    buffer = this.createImage(size.width, size.height);
    //pred = getImage(getCodeBase(), "predmask.jpg" );
    //prey = getImage(getCodeBase(), "prey.jpg" );
    gbuffer = buffer.getGraphics();
    background = new Color(255,255,255);
    setBackground(background);
    t=0;
    dt=0.01;
    click=true;
    Pprev=1.7;
    Q1prev=1.0;
    Q2prev=1.7;
    //d(resetbutton);
    add(bparam);//added textfield to new window
    
  }

  /** Draw the circle at its current position, using double-buffering */
  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    // Draw into the off-screen buffer.
    

    if (click){
    gbuffer.setColor(background);
    gbuffer.fillRect(0, 0, 3*size.width, 3*size.height); // clear the buffer
    click=false;
    } 
    //Draw lines
    //bufferGraphics.drawImage(pred,x,y,this);
    //bufferGraphics.drawImage(prey,100,100,this);
    gbuffer.setColor(new Color (0,0,0));
    gbuffer.drawLine(60,100,60,450);
    //draw tickmarks
    gbuffer.drawLine(55,100,65,100);
    gbuffer.drawLine(55,200,65,200);
    gbuffer.drawLine(55,300,65,300);
    gbuffer.drawLine(50,400,1100,400);
    gbuffer.setColor(new Color(0,0,0));
    gbuffer.drawString("T",405,1100);
    gbuffer.drawString("#",50,100);
    gbuffer.drawString("30",30,100);
    gbuffer.drawString("20",30,200);
    gbuffer.drawString("10",30,300);
    gbuffer.drawImage(buffer, 0, 0, this);

    //bottom of vertical axis-(pnow/200(max population)*pixel difference of top to bottom)

    gbuffer.drawLine(60,100,60,450);
    gbuffer.drawLine(50,400,1100,400);
    
    gbuffer.drawString("#",50,100);
    gbuffer.drawString("t",1100, 410);

    //400-(Pnow/400)*300

    gbuffer.fillOval((int)(0.5*(t)+60),(int)(400-Pprev*100),2,2);  
    gbuffer.setColor(new Color(255,0,0));
   // g2.drawString("T",405,900);

    gbuffer.fillOval((int)(0.5*(t)+60),(int)(400-Q1prev*100),2,2); 
    gbuffer.setColor(new Color(0, 255, 0));
    gbuffer.fillOval((int)(0.5*(t)+60),(int)(400-Q2prev*10),2,2);


    g2.drawImage(buffer,0,0,this); 
  }

  public void actionPerformed(ActionEvent event){
    String newb = bparam.getText();
    double newb2=Double.parseDouble(newb);
    String newb3 = bparam2.getText();
    double newb4=Double.parseDouble(newb3);
    b=newb2;
    b2=newb4;

    t=0;
    Pprev=1.7;
    Q2prev=1.7;
    Q1prev=1.0;

    //Pnow=Pprev+(a*Pprev*(1-Pprev/K)-(b*Pprev*Qprev)/(1+b*Pprev))*dt;
    //Qnow=Qprev+(m*Qprev*(1-Qprev/(k*Pprev)))*dt;

    Pnow=Pprev+(a*Pprev*(1-Pprev/K)-(b1*Q1prev+b2*Q2prev)*Pprev)*dt;
    Q1now=Q1prev+(e1*b1*Pprev*Q1prev-m1*Q1prev)*dt;
    Q2now=Q2prev+(e2*b2*Pprev*Q2prev-m2*Q2prev)*dt;

    Q1prev=Q1now;
    Q2prev=Q2now;
    Pprev=Pnow;
    click=true;
 
    repaint();

  }

  public void update(Graphics g) { paint(g);}

  /** The body of the animation thread */
  public void run() {
    while(!please_stop) {

      // Move the ball 
      t=t+dt;    
    Pnow=Pprev+(a*Pprev*(1-Pprev/K)-(b1*Q1prev+b2*Q2prev)*Pprev)*dt;
    Q1now=Q1prev+(e1*b1*Pprev*Q1prev-m1*Q1prev)*dt;
    Q2now=Q2prev+(e2*b2*Pprev*Q2prev-m2*Q2prev)*dt;

    Q1prev=Q1now;
    Q2prev=Q2now;
    Pprev=Pnow;

    System.out.println(Pnow+" "+Q1now+" "+Q2now);

      repaint();


      // Now pause some milliseconds before drawing the shrimp again.
      try {
      Thread.sleep(1);
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
