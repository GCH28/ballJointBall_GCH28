import eu.mihosoft.vrl.v3d.parametrics.*;
import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;
ArrayList<CSG> makeBallJoint(){
	
	double printerNozzelDiameter= 0.45;
	int sphereNumSlices=40;//
	int sphereNumStacks=20;//
	double socketAllignemntPinRadius=2;
	
	LengthParameter ballJointBaseThickness 		= new LengthParameter("Material Thickness",3.5,[10,1])
	LengthParameter ballJointPinSize 		= new LengthParameter("Ball Joint Ball Radius",8,[50,4])
	LengthParameter centerOfBall 		= new LengthParameter("Center Of Ball",18.5,[50,ballJointPinSize.getMM()])
	LengthParameter ballJointPin		= new LengthParameter("Ball Joint Pin Size",8,[50,ballJointPinSize.getMM()])
	LengthParameter printerOffset		= new LengthParameter("printerOffset",0.5,[socketAllignemntPinRadius,0.001])
	if(socketAllignemntPinRadius<=printerOffset.getMM())
		socketAllignemntPinRadius*=1.5
	printerNozzelDiameter=printerOffset.getMM()
	CSG ballShaftHole= new Cube(	ballJointPinSize.getMM(),// X dimention
							ballJointPinSize.getMM(),// Y dimention
							ballJointBaseThickness.getMM()//  Z dimention
							).toCSG()
							.movez(ballJointBaseThickness.getMM()/2)
							.makeKeepaway(printerNozzelDiameter)
	
				
	CSG ballShaft = new RoundedCube(	ballJointBaseThickness.getMM()+1,// X dimention
							ballJointPin.getMM(),// Y dimention
							ballJointPin.getMM()//  Z dimention
							)
							.cornerRadius(1)
							.toCSG()	
							.movex(-1)			
	ballShaft=ballShaft.union(
						new Cylinder(	ballJointPin.getMM()/2+2, // Radius at the top
	                      				ballJointPin.getMM()/2+2, // Radius at the bottom
	                      				centerOfBall.getMM(), // Height
	                      			         (int)30 //resolution
	                      			         ).toCSG()
	                      			         .roty(90)
	                      			         .movex(-ballJointBaseThickness.getMM()/2)	        
					)
	
	ballShaft=ballShaft.union(new Sphere(ballJointPinSize.getMM(), sphereNumSlices,sphereNumStacks).toCSG()
		       			         .movex(-centerOfBall.getMM())	        
					)	
	ballShaft= ballShaft.intersect(new Cube(	centerOfBall.getMM()*4,// X dimention
							centerOfBall.getMM()*4,// Y dimention
							centerOfBall.getMM()*2//  Z dimention
							)
							.toCSG()	
							.movez(centerOfBall.getMM())
							)
	CSG ballPin= new Cylinder(	ballJointPinSize.getMM()+socketAllignemntPinRadius, // Radius at the top
	                      		ballJointPinSize.getMM()+socketAllignemntPinRadius, // Radius at the bottom
	                      		socketAllignemntPinRadius*2, // Height
	       			         (int)30 //resolution
	       			         ).toCSG()
	       			         .difference(
	       			         	new Cylinder(	ballJointPinSize.getMM()-socketAllignemntPinRadius, // Radius at the top
				                      		ballJointPinSize.getMM()-socketAllignemntPinRadius, // Radius at the bottom
				                      		socketAllignemntPinRadius*2, // Height
				       			         (int)30 //resolution
				       			         ).toCSG()
	       			         	)		         		
	       			         .movex(-centerOfBall.getMM())
	       			         .movez(-socketAllignemntPinRadius)
	       			         .rotx(90)
	                      			         
	ballShaft=ballShaft.difference(ballPin)
					.movex(centerOfBall.getMM())
		
	double tabSize =socketAllignemntPinRadius-printerNozzelDiameter			
	CSG hatTab = new Cylinder(	tabSize, // Radius at the top
							tabSize, // Radius at the top
							tabSize, // Radius at the top
							(int)20
	            			         ).toCSG()
	            			         .movez(-tabSize/2)
	            			         .roty(90)
	            			         

	            			         .movex(ballJointPinSize.getMM())	      			         
	CSG ballSocket = new Sphere(ballJointPinSize.getMM()+printerNozzelDiameter, sphereNumSlices,sphereNumStacks).toCSG()
	            			        .roty(90)
	            			        .difference(hatTab,hatTab.rotz(180))	
	            			        .roty(90)	    	
	ballShaft
		.setParameter(ballJointPinSize)
		.setParameter(centerOfBall)
		.setParameter(ballJointPinSize)
		.setParameter(printerOffset)
		.setRegenerate({ makeBallJoint().get(0)})
	return [ballShaft,ballSocket]
}
//CSGDatabase.clear()//set up the database to force only the default values in	
return makeBallJoint()
//end Ball joint
