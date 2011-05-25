package laserschein;

public class OptimizerSettings {
	
	public boolean reorderFrame = true; // does not work yet

	public boolean analyzeCornerAngles = true; // add extra points for sharp
												// angles
	public int extraCornerPointsAngleDependent = 3; 	// extra corner points that are added on
										// sharp angles

	public int extraCornerPoints = 6; // corner points that are always added
	public int extraCornerPointsStart = 3; // 
	public int extraCornerPointsEnd = 3; // 

	
	
	public int maxTravel = 600; // maximum drawing travel without adding more
								// points

	public int extraBlankPointsStart = 6; // add extra blanks at the beginning 
	
	public int extraBlankPointsEnd = 6; // add extra blanks at  end


	
	
	
}
