import laserschein.*;

Laserschein laser;

void setup() {
  	size(400,400, P3D);
  	frameRate(-1); // as fast as possible

	/* Make sure you have installed the easylase driver and windows can find the jmlaser.dll */
	laser = new Laserschein(this, "Easylase"); 
	
	// laser = new Laserschein(this, "LD2000"); // same goes for the Pangolin
	
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