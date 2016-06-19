//Ball Joint section
CSG cables = cableConnector
				.clone()
				.movex(spindalRadius)
				.rotx(180)
CSG output= cables.clone()
			
for (int i=1;i<numPins;i++){
	output = output.union(
		cables.rotz((360/numPins)*i)
		)
}

output=output
		.movez(ballJointBaseThickness/2)// how deep the the recess for housing sinks in
CSG ballJointBase = new Cylinder(	spindalRadius+	housingDiameter*2/3, // Radius at the top
                 				spindalRadius+	housingDiameter*2/3, // Radius at the bottom
                 				ballJointBaseThickness, // Height
            			         (int)30 //resolution
            			         ).toCSG()

CSG ballShaftHole= new Cube(	ballJointPinSize,// X dimention
						ballJointPinSize,// Y dimention
						ballJointBaseThickness//  Z dimention
						).toCSG()
						.movez(ballJointBaseThickness/2)
						.makeKeepaway(printerNozzelDiameter)

			
CSG ballShaft = new RoundedCube(	ballJointBaseThickness+1,// X dimention
						ballJointPinSize,// Y dimention
						ballJointPinSize//  Z dimention
						)
						.cornerRadius(1)
						.toCSG()	
						.movex(-1)			
ballShaft=ballShaft.union(
					new Cylinder(	ballJointPinSize/2+2, // Radius at the top
                      				ballJointPinSize/2+2, // Radius at the bottom
                      				spindalRadius, // Height
                      			         (int)30 //resolution
                      			         ).toCSG()
                      			         .roty(90)
                      			         .movex(-ballJointBaseThickness/2)	        
				)
double centerOfBall = -ballJointBaseThickness/2 - spindalRadius;
ballShaft=ballShaft.union(new Sphere(ballJointPinSize, sphereNumSlices,sphereNumStacks).toCSG()
	       			         .movex(centerOfBall)	        
				)	
ballShaft= ballShaft.intersect(new Cube(	centerOfBall*4,// X dimention
						centerOfBall*4,// Y dimention
						centerOfBall*2//  Z dimention
						)
						.toCSG()	
						.movez(centerOfBall)
						)
CSG ballPin= new Cylinder(	ballJointPinSize+socketAllignemntPinRadius, // Radius at the top
                      		ballJointPinSize+socketAllignemntPinRadius, // Radius at the bottom
                      		socketAllignemntPinRadius*2, // Height
       			         (int)30 //resolution
       			         ).toCSG()
       			         .difference(
       			         	new Cylinder(	ballJointPinSize-socketAllignemntPinRadius, // Radius at the top
			                      		ballJointPinSize-socketAllignemntPinRadius, // Radius at the bottom
			                      		socketAllignemntPinRadius*2, // Height
			       			         (int)30 //resolution
			       			         ).toCSG()
       			         	)		         		
       			         .movex(centerOfBall)
       			         .movez(-socketAllignemntPinRadius)
       			         .rotx(90)
                      			         
ballShaft=ballShaft.difference(ballPin)

CSG ballJointHat = new Cylinder(	spindalRadius+	housingDiameter, // Radius at the top
                 				spindalRadius+	housingDiameter, // Radius at the bottom
                 				ballJointPinSize/2, // Height
            			         (int)30 //resolution
            			         ).toCSG()
double heightOfTab=15;
CSG mountTabForEndEffector= new Cube(	6,// X dimention
						8,// Y dimention
						heightOfTab//  Z dimention
						).toCSG()

for(int i=0;i<2;i++){
	mountTabForEndEffector=mountTabForEndEffector
						.difference(
							new  Cylinder(mountScrewDiameter/2,10,30).toCSG()
							.movez(-5)
							.roty(90)
							.movez(heightOfTab/3*i)
							)						 
}
mountTabForEndEffector=mountTabForEndEffector
						.movey(spindalRadius) 
for(int i=1;i<4;i++){
	mountTabForEndEffector=mountTabForEndEffector
						.union(mountTabForEndEffector.rotz(i*90))						
}
mountTabForEndEffector=mountTabForEndEffector.movez(-mountTabForEndEffector.getMinZ())
ballJointHat = ballJointHat.union(mountTabForEndEffector);
for(int i=0;i<4;i++){
	ballJointBase = ballJointBase.difference(
			new  Cylinder(mountScrewDiameter/2,10,30).toCSG()
							.movez(-5)
							.movey(spindalRadius) 
							.rotz(45+90*i)
		)						
}
ballJointBase = ballJointBase
ballJointBase=ballJointBase
			.difference(output,ballShaftHole)		      			         
CSG ballSocket = new Sphere(ballJointPinSize+printerNozzelDiameter, sphereNumSlices,sphereNumStacks).toCSG()
            			        .roty(90)		    	
ballJointHat = ballJointHat.difference(ballSocket)
CSG hatTab = new Sphere(	socketAllignemntPinRadius-printerNozzelDiameter, // Radius at the top
            			         ).toCSG()
            			         .difference(new Cube(   socketAllignemntPinRadius*2,
					            			        socketAllignemntPinRadius*2,
					            			        socketAllignemntPinRadius*2
					            			         ) .toCSG().movez(-socketAllignemntPinRadius)
            			         )
            			         .movex(ballJointPinSize)
 ballJointHat =ballJointHat.union(hatTab,hatTab.rotz(180))           			         
CSG hatHoles = cableHoles.rotx(90)
				.movey(spindalRadius+1)
				.movez(3)
CSG holeGroup = hatHoles.clone()
for(int i=1;i<4;i++){
	 holeGroup =  holeGroup.union(hatHoles.rotz(90*i))
}

ballJointHat=ballJointHat
			.difference(holeGroup)
			.toZMin()


//end Ball joint
