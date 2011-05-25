import laserschein.*;

Laserschein laser;

void setup() {
  	size(400,400, P3D);
  	frameRate(-1); // as fast as possible

	laser = new Laserschein(this);
}

void draw() {
  	background(60);
 
	Laser3D renderer = laser.renderer(); 
 	beginRaw(renderer);
 	
 	noFill(); // important!
 	
 	stroke(255);
 	renderer.noSmooth();
 	rect(50, 50, 300, 300);
 	
 	stroke(255,0,0);
 	renderer.smooth();
 	ellipse(width / 2, height / 2 , 300, 300);
 	
 	stroke(0,0,255);
 	renderer.noSmooth();
 	rect(mouseX, mouseY, 30, 30);
 	
 	endRaw();
 
}