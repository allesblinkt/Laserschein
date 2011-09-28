package laserschein;

import processing.core.PVector;



public class Homography {

	
	double[][] matrix;


    private static final double PERSPECTIVE_DIVIDE_EPSILON = 1.0e-10;

	
	
	public Homography( final PVector[] theSources, final PVector[] theDestinations ) {
		 this(	theSources[0], theSources[1], theSources[2], theSources[3], 
				theDestinations[0], theDestinations[1],  theDestinations[2],  theDestinations[3]  );
	}
	
	
	public Homography(	final PVector theSrc1,final PVector theSrc2,final PVector theSrc3,final PVector theSrc4,
						final PVector theDst1,final PVector theDst2,final PVector theDst3,final PVector theDst4 ) {
	    double[][] Q= {  
	    	      {
	    	        -theSrc1.x, -theSrc1.y, -1, 0, 0, 0, theSrc1.x*theDst1.x, theSrc1.y*theDst1.x, -theDst1.x
	    	      }
	    	      , // h11  
	    	      {  
	    	        0, 0, 0, -theSrc1.x, -theSrc1.y, -1, theSrc1.x*theDst1.y, theSrc1.y*theDst1.y, -theDst1.y
	    	      }
	    	      , // h12  

	    	      {
	    	        -theSrc2.x, -theSrc2.y, -1, 0, 0, 0, theSrc2.x*theDst2.x, theSrc2.y*theDst2.x, -theDst2.x
	    	      }
	    	      , // h13  
	    	      {  
	    	        0, 0, 0, -theSrc2.x, -theSrc2.y, -1, theSrc2.x*theDst2.y, theSrc2.y*theDst2.y, -theDst2.y
	    	      }
	    	      , // h21  

	    	      {
	    	        -theSrc3.x, -theSrc3.y, -1, 0, 0, 0, theSrc3.x*theDst3.x, theSrc3.y*theDst3.x, -theDst3.x
	    	      }
	    	      , // h22  
	    	      {  
	    	        0, 0, 0, -theSrc3.x, -theSrc3.y, -1, theSrc3.x*theDst3.y, theSrc3.y*theDst3.y, -theDst3.y
	    	      }
	    	      , // h23  

	    	      {
	    	        -theSrc4.x, -theSrc4.y, -1, 0, 0, 0, theSrc4.x*theDst4.x, theSrc4.y*theDst4.x, -theDst4.x
	    	      }
	    	      , // h31  
	    	      {  
	    	        0, 0, 0, -theSrc4.x, -theSrc4.y, -1, theSrc4.x*theDst4.y, theSrc4.y*theDst4.y, -theDst4.y
	    	      } // h32
	    	    };  
	    
	    
	    double[][] P = gaussian_elimination(Q, 9);  

	    // gaussian elimination gives the results of the equation system  
	    // in the last column of the original matrix.  
	    // opengl needs the transposed 4x4 matrix:  
	    double[][] tmpmatrix= {
	      { 
	        P[0][8], P[3][8], 0, P[6][8]
	      }
	      , // h11  h21 0 h31  
	      {
	        P[1][8], P[4][8], 0, P[7][8]
	      }
	      , // h12  h22 0 h32  
	      {
	        0, 0, 0, 0
	      }
	      , // 0    0   0 0  
	      {
	        P[2][8], P[5][8], 0, 1
	      }
	    };      // h13  h23 0 h33  


	    matrix = tmpmatrix;
		
	}
	
	
	  double[][] gaussian_elimination(double[][] input, int n) {  
		    // arturo castro - 08/01/2010  
		    //  
		    // ported to c from pseudocode in  
		    // http://en.wikipedia.org/wiki/Gaussian_elimination  

		      double[][] A = input;  
		    int i = 0;  
		    int j = 0;  
		    int m = n-1;  
		    while (i < m && j < n) {  
		      // Find pivot in column j, starting in row i:  
		      int maxi = i;  
		      for (int k = i+1; k<m; k++) {  
		        if (Math.abs(A[k][j]) > Math.abs(A[maxi][j])) {  
		          maxi = k;
		        }
		      }  
		      if (A[maxi][j] != 0) {  
		        //swap rows i and maxi, but do not change the value of i  
		        if (i!=maxi)  
		          for (int k=0;k<n;k++) {  
		            double aux = A[i][k];  
		            A[i][k]=A[maxi][k];  
		            A[maxi][k]=aux;
		          }  
		        //Now A[i,j] will contain the old value of A[maxi,j].  
		        //divide each entry in row i by A[i,j]  
		        double A_ij=A[i][j];  
		        for (int k=0;k<n;k++) {  
		          A[i][k]/=A_ij;
		        }  
		        //Now A[i,j] will have the value 1.  
		        for (int u = i+1; u< m; u++) {  
		          //subtract A[u,j] * row i from row u  
		          double A_uj = A[u][j];  
		          for (int k=0;k<n;k++) {  
		            A[u][k]-=A_uj*A[i][k];
		          }  
		          //Now A[u,j] will be 0, since A[u,j] - A[i,j] * A[u,j] = A[u,j] - 1 * A[u,j] = 0.
		        }  

		        i++;
		      }  
		      j++;
		    }  

		    //back substitution  
		    for (int li=m-2;li>=0;li--) {  
		      for (int lj=li+1;lj<n-1;lj++) {  
		        A[li][m]-=A[li][lj]*A[lj][m];  
		        //A[i*n+j]=0;
		      }
		    }  

		    return A;
		  }  



		  PVector transform(PVector theV) {

		    PVector myResult = new PVector();







		    double w =matrix[0][3] * theV.x +  matrix[1][3] * theV.y + matrix[3][3];

		    myResult.x = (float)((matrix[0][0] * theV.x + matrix[1][0] * theV.y +  matrix[3][0] )/ w);
		    myResult.y = (float)((matrix[0][1] * theV.x + matrix[1][1] * theV.y +  matrix[3][1] )/ w);
		    // x = h11*x + h12 * y + h13 / (h31*x + h32*y + h33)
		    // y = h21*x + h22*y + h23 / (h31*x + h32*y + h33) 

		    return myResult;
		  }
		  
		  
		  PVector inverseTransform(PVector theV) {

		  double tmp_x = (matrix[1][1] * matrix[3][3] - matrix[3][1] * matrix[1][3]) * theV.x
		    + (matrix[3][0] * matrix[1][3] - matrix[1][0] * matrix[3][3]) * theV.y
		    + (matrix[1][0] * matrix[3][1] - matrix[3][0] * matrix[1][1]);
		  double tmp_y = (matrix[3][1] * matrix[0][3] - matrix[0][1] * matrix[3][3]) *  theV.x
		    + (matrix[0][0] * matrix[3][3] - matrix[3][0] * matrix[0][3]) * theV.y
		    + (matrix[3][0] * matrix[0][1] - matrix[0][0] * matrix[3][1]);
		  double w = (matrix[0][1] * matrix[1][3] - matrix[1][1] * matrix[0][3]) * theV.x
		    + (matrix[1][0] * matrix[0][3] - matrix[0][0] * matrix[1][3]) * theV.y
		    + (matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1]);

		  double wabs = w;
		  if (w < 0) {
		    wabs = -w;
		  }
		  if (wabs < PERSPECTIVE_DIVIDE_EPSILON) {
		    System.out.println("Pretty small");
		  }

		  final PVector myResult = new PVector();

		  myResult.x = (float)(tmp_x / w);
		  myResult.y = (float)(tmp_y / w);
		  
		  return myResult;
		}
	
	
	double[][] modelViewMatrix() {
		return new double[4][4];
	}
	
	
}
