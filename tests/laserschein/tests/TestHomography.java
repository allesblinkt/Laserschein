package laserschein.tests;


import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.media.opengl.GL;  

import laserschein.Homography;


@SuppressWarnings("serial")
public class TestHomography extends PApplet{

	

	

	GL gl;


	//PImage back;
	

	float scaling = 0.7f;

	boolean stuff = false;
	boolean displayBall = false;


	PickQuad quad;
	Dragger dragger;
	

	public void setup() {
	  size(1024, 768, OPENGL);
	  frameRate(60);
	  noCursor();

	  quad = new PickQuad();

	  dragger = new Dragger();
	  dragger.add(quad.corner1);
	  dragger.add(quad.corner2);
	  dragger.add(quad.corner3);
	  dragger.add(quad.corner4);

	  //back = loadImage("backdrop.jpg");

	  gl=((PGraphicsOpenGL)g).gl;	  


	  hint(ENABLE_OPENGL_4X_SMOOTH);
	  noSmooth();

  	  loadHomography();
	}


	public void draw() {

	  if (stuff) {
	    cursor(CROSS);
	  } 
	  else {
	    noCursor();
	  }


	  background(0); 

	  //image(back, 0, 0);

	  hint(DISABLE_DEPTH_TEST);

	  gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	  gl.glBlendEquation(GL.GL_FUNC_ADD);
	  gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);    

	
	  if (stuff) quad.draw();


	  //  PVector[] input = { 
	  //    new PVector(0, 0), new PVector(width, 0), new PVector(width, height), new PVector(0, height),
	  //  };

	  float myLeft = width * 0.5f - width * 0.5f * scaling;
	  float myRight = width * 0.5f + width * 0.5f * scaling;

	  float myTop = height * 0.5f - height * 0.5f * scaling;
	  float myBottom = height * 0.5f + height * 0.5f * scaling;


	  PVector[] input = { 
	    new PVector(myLeft, myTop, 0.0f), new PVector(myRight, myTop, 0.0f), new PVector(myRight, myBottom, 0.0f), new PVector(myLeft, myBottom, 0f),
	  };


	  PVector[] output = { 
	    quad.corner1.toVector(), quad.corner2.toVector(), quad.corner3.toVector(), quad.corner4.toVector()
	    };

	    Homography homo = new Homography( input, output );

	  pushMatrix();

	  applyMatrix(homo.modelViewMatrix());

	  box(100);
	  
	  ArrayList<Point2D> points = new ArrayList<Point2D>();

	  for (int col = 0; col < 20; col++) {

	    for (int row = 0; row < 20; row++) {

	      float x = col / 20.0f * width;
	      float y = row / 20.0f * height;

	      Point2D p = new Point2D.Float(x, y);

	      points.add(p);


	      stroke(255, 0, 0);
	      noFill();
	      if (stuff)  drawPoint(p);
	    }
	  }






	  stroke(0, 255, 0, 200);
	  strokeWeight(1);
	  // line(0, 0, mouseX, mouseY);

	 


	 
	  popMatrix();

	  PVector myInvMouse =  homo.transformInverse(new PVector(mouseX, mouseY));
	  rect((float)myInvMouse.x, (float)myInvMouse.y, 5, 5);
	  
	  
	  println(myInvMouse.x + " " + myInvMouse.y);
	  
	  //resetMatrix();

	  for (Point2D p:points) {
	    Point2D np = new Point2D.Float(homo.transform( new PVector((float)p.getX(), (float)p.getY())).x, homo.transform( new PVector((float)p.getX(), (float)p.getY())).y);
	    // np = t.transform(p, np);
	    // np.x();
	    //   np.x(  );

	    stroke(0, 255, 0);
	    noFill();
	    if (stuff)    drawPoint(np);
	  }





	 
	}



	void drawPoint(Point2D theP) {
	  rectMode(CENTER);
	  rect((float)theP.getX(), (float)theP.getY(), 5, 5);
	}

	public void mouseDragged() {
	  dragger.dragged();
	}


	public void mouseReleased() {
	  dragger.released();
	}





	void saveHomography() {
	  println(quad.corner1.x + " " + quad.corner1.y);
	  println(quad.corner2.x + " " + quad.corner2.y);

	  println(quad.corner3.x + " " + quad.corner3.y);

	  println(quad.corner4.x + " " + quad.corner4.y);
	}

	void loadHomography() {

	  quad.corner1.x = 354; 
	  quad.corner1.y = 167;
	  quad.corner2.x = 643; 
	  quad.corner2.y = 166;
	  quad.corner3.x = 816; 
	  quad.corner3.y = 380;
	  quad.corner4.x = 126; 
	  quad.corner4.y = 367;

	  scaling = 0.4f;
	}


	public void mousePressed() {
	  dragger.pressed();
	}


	public void keyPressed() {

	  if (key == 'b') {
	    displayBall = !displayBall;
	  }

	  if (key == 's') {
	    saveHomography();
	  }


	  if (key == 'l') {
	    loadHomography();
	  }


	  if (key == CODED) {
	    if (keyCode == UP) {
	      scaling += 0.1;
	    }

	    if (keyCode == DOWN) {
	      scaling -= 0.1;
	    }
	  }


	  if (key == ' ') {
	    stuff = !stuff;
	  }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	class PickQuad {
		  DragCorner corner1; 
		  DragCorner corner2; 
		  DragCorner corner3; 
		  DragCorner corner4;


		  public PickQuad() {
		    corner1 = new DragCorner(0, 0);
		    corner2 = new DragCorner(width, 0);
		    corner3 = new DragCorner(width, height);
		    corner4 = new DragCorner(0, height);
		  } 


		  void draw() {
		    corner1.draw();
		    corner2.draw();
		    corner3.draw();
		    corner4.draw();

		    noFill();
		    stroke(255,0,255);
		    
		    beginShape();
		    vertex(corner1.x, corner1.y);
		    vertex(corner2.x, corner2.y);
		    vertex(corner3.x, corner3.y);
		    vertex(corner4.x, corner4.y);

		    endShape(CLOSE);
		  }
		}



		class Dragger {
		  ArrayList<Draggable> elements; 
		  Draggable active = null;

		  float diffX;
		  float diffY;

		  public Dragger() {
		    elements = new ArrayList<Draggable>();
		  } 

		  public void add(Draggable theElement) {
		    elements.add(theElement);
		  }

		  public void draw() {

		    if (active != null) {
		    } 
		    else {
		    }
		  }


		  public void pressed() {
		    for (Draggable element: elements) {
		      if (element.mouseOver() ) {
		        active = element;
		      }
		    } 

		    if (active != null) {
		      diffX = active.x() - mouseX;
		      diffY = active.y() - mouseY;
		    }
		  }


		  public void dragged() {
		    if (active != null) {
		      active.x(mouseX - diffX); 
		      active.y(mouseY - diffY);
		    }
		  }


		  public void released() {
		    active = null;
		  }
		}



		public interface Draggable {
		   public float x();   
		   public float y();

		   public void x(float theX);
		   public void y(float theY);

		   public boolean mouseOver();
		   
		}


		class DragCorner implements Draggable {

		  float x;
		  float y;

		  int W = 10;

		  boolean isDragged = false;

		  public DragCorner() {
		    this(0, 0);
		  } 


		  public float x() {
		    return this.x;
		  }   
		  public float y() {
		    return this.y;
		  }   
		  public void x(float theX) {
		    this.x = theX;
		  }
		  public void y(float theY) {
		    this.y = theY;
		  }



		  public DragCorner(float theX, float theY) {
		    this.x = theX; 
		    this.y = theY;
		  }


		  void startDrag() {
		    isDragged = true;
		  }


		  void endDrag() {
		    isDragged = false;
		  }


		  PVector toVector() {
		    return new PVector(this.x, this.y);
		  }


		  public boolean mouseOver() {
		    return (mouseX < this.x + W * 0.5f &&  mouseX > this.x - W * 0.5f &&  mouseY < this.y + W * 0.5f &&  mouseY > this.y - W * 0.5f ) ;
		  }

		  void draw() {
		    rectMode(CENTER);
		    stroke(255);

		    if (mouseOver() || isDragged) {
		      stroke(255, 0, 0);
		      fill(128);
		    }

		    rect(this.x, this.y, W, W);
		  }
		}
		
		
		public static void main(String[] args) {
			PApplet.main(new String[]{TestHomography.class.getCanonicalName()});
		}


}
