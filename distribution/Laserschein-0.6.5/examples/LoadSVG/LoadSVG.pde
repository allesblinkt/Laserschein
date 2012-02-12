import laserschein.*;

Laserschein laser;
PShape svg;

void setup() {
  	size(400,400, P3D);
  	frameRate(-1); // as fast as possible

	laser = new Laserschein(this);
	
	svg = loadShape("foo.svg");
}

void draw() {
  	background(60);
 
	Laser3D renderer = laser.renderer(); 
 	beginRaw(renderer);
 	
 	renderer.noSmooth();
 	shape(svg, 0, 0);
 	
 	endRaw();
 
}