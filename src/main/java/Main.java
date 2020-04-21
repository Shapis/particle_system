import java.awt.Frame;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;




public class Main extends GLCanvas implements GLEventListener, MouseWheelListener {
	
			static double timeSync = 0.01646666666;
			static double timeAcceleration = 1E-1;
	
			// Minhas constantes.
			static double currentTime = 0;
			static double massaDaTerra = 5.98*1E24;
			static double raioDaTerra = 6.38*1E6;
			static double deltaTime = timeAcceleration*timeSync;
			static double scale = 1E7;
			static double totalTime = 1;
			static double gravitationalConstant = 6.67384 * 1E-11;
			// Fim das minhas constantes.
			
	
	private static final int height = 1000;
	private static final int width = height;
	
	
	//static Particle myParticleDemo("Name", Mass, Radius, PositionX, PositionY, PositionZ, VelocityX, VelocityY, VelocityX, SpinX, SpinY, SpinZ) 
	
	
	static Particle myParticle1 = new Particle("Terra",massaDaTerra,raioDaTerra,0,0,0,0,0,0,0,0,0);
	static Particle myParticle2 = new Particle("1",1,1E5+2,0,0,1E7,550*25000,0,0,0,0,0);
	static Particle myParticle3 = new Particle("2",1,1E5+2,0,0,-1E7,-550*25000,0,0,0,0,0);
	//static Particle myParticle4 = new Particle("Lua",1E20,3E6,0,0,3E8,900,0,0,0,0,0);
	static ArrayList<Particle> myParticles = new ArrayList<Particle>();
	
	public static void main(String[] args) {
		
		myParticles.add(myParticle1);
		myParticles.add(myParticle2);
		myParticles.add(myParticle3);
		//myParticles.add(myParticle4);
		//myParticles.add(myParticle5);
		//myParticles.add(myParticle6);
		//myParticles.add(myParticle7);
		//myParticles.add(myParticle8);
		//myParticles.add(myParticle9);
		//myParticles.add(myParticle10);
		//myParticles.add(myParticle11);
		//myParticles.add(myParticle12);
		//myParticles.add(myParticle13);
		
		
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities capabilities = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(capabilities);
        
        
        Frame frame = new Frame("AWT Window Test");
        frame.setSize(height, width);
        frame.add(canvas);
        frame.setVisible(true);

        
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }});
        
        canvas.addGLEventListener(new Main());
        canvas.addMouseWheelListener(new Main());
        Animator animator = new Animator(canvas);
        animator.start();
	}
	
	

		private void update() {
			//System.out.println(currentTime);
			
			
			
			
			for (int i = 0; i < myParticles.size(); i++){
			executeAlgorithm(myParticles, deltaTime, totalTime);
			}
			
			
			
			
			
			
			
			
		}
		
			
			
		
			private void drawCircle (GL2 gl, double bodyRadius, double scale, double positionX, double positionY, double screenHeight , double screenWidth) {
			float red = (float)Math.abs(Math.sin(bodyRadius));
			float green = (float)Math.abs(Math.cos(bodyRadius));
			float blue = (float)Math.abs(Math.tan(bodyRadius));
			gl.glBegin(GL.GL_POINTS);
			gl.glColor3f(red, green, blue);
			bodyRadius = bodyRadius/scale;
		    for(int i =0; i <= 300; i++){
		    double angle = 2 * Math.PI * i / 300;
		    double x = Math.cos(angle);
		    double y = Math.sin(angle);
		    for (double j = 1 ; j >= 0.1 ; j = j - 0.02) {
		    gl.glVertex2d(positionX+((x*0.001*width)*j)*bodyRadius,positionY+((y*0.001*height)*j)*bodyRadius);
		    }
		    }
		}



			private void render(GLAutoDrawable drawable) {
				
			    GL2 gl =  drawable.getGL().getGL2();
			    
			    for (int i = 0 ; i < myParticles.size() ; i ++ ) {
				    gl = drawable.getGL().getGL2();
				    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
				    drawCircle (gl, myParticles.get(i).getRadius(),scale,myParticles.get(i).getxX()/scale, myParticles.get(i).getxZ()/scale, height, width);

			    }
			    gl.glEnd();

			}
	
	public void display(GLAutoDrawable drawable) {
		 	update();
		    render(drawable);
		    
	}
	
	

	
	
	public void init(GLAutoDrawable drawable) {
	    // put your OpenGL initialization code here
		GLU glu = new GLU();
		 drawable.getGL().setSwapInterval(1);
		 
	}

	public void dispose(GLAutoDrawable drawable) {
	    // put your cleanup code here
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl = drawable.getGL();
        gl.glViewport(0, 0, width, height);
	}
	
	private static void executeAlgorithm(ArrayList<Particle> myParticles, double deltaTime, double totalTime ) {
			ArrayList<ArrayList<Double>> netForces = new ArrayList<ArrayList<Double>>();
			//double distance = Math.sqrt((Math.pow(myParticle1.getxX()-myParticle2.getxX(),2)+Math.pow(myParticle1.getxY()-myParticle2.getxY(),2)+Math.pow(myParticle1.getxZ()-myParticle2.getxZ(),2)));
			//double velocityModule = Math.sqrt((Math.pow(myParticle1.getvX()-myParticle2.getvX(),2)+Math.pow(myParticle1.getvY()-myParticle2.getvY(),2)+Math.pow(myParticle1.getvZ()-myParticle2.getvZ(),2)));
			
			
			//returnList.add(currentTime);
			//returnList.add(myParticle.getxX());
			//returnList.add(myParticle.getxY());
			//returnList.add(myParticle.getxZ());
			//returnList.add(distance-myPlanet.getRadius()-myParticle.getRadius());
			//returnList.add(((Math.pow(velocityModule,2)*myParticle.getMass())/2)+(9.8*myParticle.getMass()*(distance-myPlanet.getRadius())));
			//returnList.add(Math.sqrt(Math.pow(myParticle.getxX()-0,2)+Math.pow(myParticle.getxY()-0,2)+Math.pow(myParticle.getxZ()-myPlanet.getRadius()-myParticle.getRadius(),2)));
			//returnList.add(velocityModule);
			
			
			for (int i = 0; i < myParticles.size() ; i++) {
			netForces.add(applyForces(i,myParticles,deltaTime));
			}
			
			
			
			
			myParticles = integrateForcesEuler(netForces,myParticles,myParticles);
			
			
			//distance = Math.sqrt((Math.pow(myParticle1.getxX()-myParticle2.getxX(),2)+Math.pow(myParticle1.getxY()-myParticle2.getxY(),2)+Math.pow(myParticle1.getxZ()-myParticle2.getxZ(),2)));
			currentTime += deltaTime;

		
	}

		private static ArrayList<Double> applyForces(int i, ArrayList<Particle> myParticles, double deltaTime){
			ArrayList<ArrayList<Double>> tempList = new ArrayList<ArrayList<Double>>();
			ArrayList<Double> returnList = new ArrayList<Double>();

			double xNetForce = 0;
			double yNetForce = 0;
			double zNetForce = 0;


			for (int j = 0; j < myParticles.size(); j++) {
				if (i!=j) {
					
			tempList.add(forceGravitational(myParticles.get(i), myParticles.get(j)));
			//tempList.add(forceDrag(myParticle,myPlanet,1,0.1));
			//tempList.add(forceMagnus(myParticle, myPlanet));
			//tempList.add(forceSpring(myParticle1, myParticle2, 0, 0 , 0, 1));
			//tempList.add(forcePendulum(myParticle1, myParticle2, 0, 0, 10));
			
				}
			}
			
			for (ArrayList<Double> j: tempList){
				
			xNetForce = xNetForce + j.get(0);
			yNetForce = yNetForce + j.get(1);
			zNetForce = zNetForce + j.get(2);
			
			}
			
			
			returnList.add(xNetForce);
			returnList.add(yNetForce);
			returnList.add(zNetForce);

			return returnList;
		}
		
		 private static ArrayList<Particle> integrateForcesEuler(ArrayList<ArrayList<Double>> netForces, ArrayList<Particle> myParticles, ArrayList<Particle> cloneList) {
			 	ArrayList<Particle> returnList = new ArrayList<Particle>();

			 	for (int i = 0 ; i < myParticles.size() ; i++) {
			 	
			 	myParticles.get(i).setvX(myParticles.get(i).getvX() + (deltaTime*(netForces.get(i).get(0)/myParticles.get(i).getMass())));
				myParticles.get(i).setvY(myParticles.get(i).getvY() + (deltaTime*(netForces.get(i).get(1)/myParticles.get(i).getMass())));
				myParticles.get(i).setvZ(myParticles.get(i).getvZ() + (deltaTime*(netForces.get(i).get(2)/myParticles.get(i).getMass())));

				myParticles.get(i).setxX(myParticles.get(i).getxX() +  deltaTime*myParticles.get(i).getvX());
				myParticles.get(i).setxY(myParticles.get(i).getxY() +  deltaTime*myParticles.get(i).getvY());
				myParticles.get(i).setxZ(myParticles.get(i).getxZ() +  deltaTime*myParticles.get(i).getvZ());
				
				
			 	}
			 	
				return returnList;
		}



		private static ArrayList<Double> forceSpring(Particle myParticle1, Particle myParticle2, double springX, double springY, double springZ, double k) {
			 ArrayList<Double> returnList = new ArrayList<Double>();
			 double forceX, forceY, forceZ;
			 double distance = Math.sqrt((Math.pow(myParticle1.getxX()-springX,2)+Math.pow(myParticle1.getxY()-springY,2)+Math.pow(myParticle1.getxZ()-springZ,2)));
			 double x = Math.sqrt(Math.pow(myParticle1.getxX()-springX,2)+Math.pow(myParticle1.getxY()-springY,2)+Math.pow(myParticle1.getxZ()-springZ,2));
			 double temp = -k*x;
			 
			 
			 
			 if (distance != 0) {
				 	forceX = (myParticle1.getxX()-springX)/distance;
					forceY = (myParticle1.getxY()-springY)/distance;
					forceZ = (myParticle1.getxZ()-springZ)/distance;
				 	} else {
				 		forceX = 0;
				 		forceY = 0;
				 		forceZ = 0;
				 	}
				 	
			 	forceX = forceX*temp;
				forceY = forceY*temp;
				forceZ = forceZ*temp;
			 
				returnList.add(forceX);
				returnList.add(forceY);
				returnList.add(forceZ);
			 
			 return returnList;
			 
			 
		 }
		 
		 private static ArrayList<Double> forceGravitational(Particle myParticle1, Particle myParticle2){
				ArrayList<Double> returnList = new ArrayList<Double>();
				double temp;
				double forceX, forceY, forceZ, distance;
				distance = Math.sqrt((Math.pow(myParticle1.getxX()-myParticle2.getxX(),2)+Math.pow(myParticle1.getxY()-myParticle2.getxY(),2)+Math.pow(myParticle1.getxZ()-myParticle2.getxZ(),2)));

				temp = -distance;
				//System.out.println((distance-myPlanet.getRadius()));


				forceX = (myParticle1.getxX()-myParticle2.getxX())/distance;
				forceY = (myParticle1.getxY()-myParticle2.getxY())/distance;
				forceZ = (myParticle1.getxZ()-myParticle2.getxZ())/distance;
				forceX = forceX*temp;
				forceY = forceY*temp;
				forceZ = forceZ*temp;


				returnList.add(forceX);
				returnList.add(forceY);
				returnList.add(forceZ);
				
				return returnList;
			}



		@Override
		  public void mouseWheelMoved(MouseWheelEvent e) {
		       int notches = e.getWheelRotation();
		       double wheelFactor = 0.90;
		       if (notches < 0) {
		           scale = scale*0.2;
		       } else {
		           scale = scale*5;
		       }
		      
		  
		}
		 
}
