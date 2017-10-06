import java.applet.*;
import java.awt.*;
import java.math.*;
import java.lang.*;
import java.util.Random;
import java.awt.event.*;

/*      <applet code="predprey" width=1200 height=550>
         </applet>
*/
public class predprey extends Applet implements Runnable, ActionListener {

  int x = 0, y = 0;         
  double m=0.1; //mass in kg, so grams
  double K=500;
  double k=0.2;
  double a=0.2;
  double b=0.1;
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
  double Pprev, Pnow, Qprev, Qnow;
  double t,dt;
  double p, q;
  TextField bparam;
  double rand; 
  Random m_r=new Random();

  /** Set up an off-screen Image for double-buffering */
  public void init() {
    bparam = new TextField("0.1",10);
  
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
    Pprev=10;
    Qprev=5;
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
    gbuffer.drawLine(50,400,800,400);
    gbuffer.drawLine(900,200,900,400);
    gbuffer.drawLine(900,400,1100,400);
    gbuffer.setColor(new Color(0,0,0));
    gbuffer.drawString("T",405,800);
    gbuffer.drawString("#",50,100);
    gbuffer.drawString("300",30,100);
    gbuffer.drawString("200",30,200);
    gbuffer.drawString("100",30,300);
    gbuffer.drawString("P",1100, 305);
    gbuffer.drawString("Q",1005, 200);
    gbuffer.drawImage(buffer, 0, 0, this);

    gbuffer.drawLine(60,100,60,450);
    gbuffer.drawLine(50,400,800,400);
    
    gbuffer.drawString("#",50,100);
    gbuffer.drawString("t",800, 410);

  
    gbuffer.fillOval((int)(.5*(t)+60),(int)(400-Pprev/500*300),2,2);  
    gbuffer.setColor(new Color(255,0,0));
   // g2.drawString("T",405,900);

    gbuffer.fillOval((int)(.5*(t)+60),(int)(400-Qprev/500*300),2,2); 
// phase space cetner is at 1000,300
    gbuffer.fillOval((int)(0.45*Pprev+900),(int)(350-2*Qprev),2,2);

    g2.drawImage(buffer,0,0,this); 
  }

  public void actionPerformed(ActionEvent event){
    String newb = bparam.getText();
    double newb2=Double.parseDouble(newb);
    b=newb2;

    t=0;
    Pprev=10;
    Qprev=5;
    // Pprev=2;
    // Qprev=1;

      Pnow=dt*(a*Pprev*(1-(Pprev/K))-((b*Pprev*Qprev)/(1+(b*Pprev))))+Pprev;
      Qnow=dt*(m*Qprev*(1-(Qprev/(k*Pprev))))+Qprev;



    Qprev=Qnow;
    Pprev=Pnow;
    click=true;
 
    repaint();

  }

  public void update(Graphics g) { paint(g);}

  /** The body of the animation thread */
  public void run() {
    while(!please_stop) {
      if (t<=1450){
      // Move the ball 
      t=t+dt;
      //random noise using mortality because it is dependent on a delicate balance. Habitat loss, food/water shortage, sudden competition (which would lead to food shortage),
      //hunting, new food supplies (e.g., if non-native seeds are brought into the area), lots of rainfall, etc. 
      // if (t>=250 && t<=500){
      //   rand=Math.random();
      //   m=0.1+0.5*rand;
      // } else {
      //   m=0.1;
      // }
      Pnow=Pprev+((a*Pprev*(1-Pprev/K))-((b*Pprev*Qprev)/(1+b*Pprev)))*dt;
      Qnow=Qprev+(m*Qprev*(1-Qprev/(k*Pprev)))*dt;

        //dp=dt*(a*p*(1-(p/K))-((b*p*q)/(1+(b*p))));
        
        //Change in predator:
        //dq=dt*(m*q*(1-(q/(k*p))));

      Qprev=Qnow;
      Pprev=Pnow;

      System.out.println(t+" "+Pnow+" "+Qnow);
      //System.out.println("Qnow"+Qnow);

      repaint();
    } else {
      please_stop=true;
    }


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
