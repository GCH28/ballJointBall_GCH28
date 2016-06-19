import java.nio.file.Paths;
ArrayList<Object> servoMeasurments = new ArrayList<Object>();
double printerNozzelDiameter= 0.45;
//Blue SG90 servo
/*
servoMeasurments.add(12.3) //servoThinDimentionThickness
servoMeasurments.add(23.6) // servoThickDimentionThickness
servoMeasurments.add(28.1) // servoShaftSideHeight
servoMeasurments.add(4.4) // outputShaftDiameter
servoMeasurments.add(6.4) //shaftToShortSideDistance
servoMeasurments.add(12.2) // shaftToShortSideFlandgeEdge
servoMeasurments.add(17.0) // tipOfShaftToBottomOfFlange
servoMeasurments.add(3.0 ) //flangeThickness
servoMeasurments.add(35.0) // flangeLongDimention
servoMeasurments.add(11.8) //bottomOfFlangeToTopOfBody
*/

//Standard Servo

servoMeasurments.add(19.5) //servoThinDimentionThickness
servoMeasurments.add(41.0) // servoThickDimentionThickness
servoMeasurments.add(38.3) // servoShaftSideHeight
servoMeasurments.add(5.6) // outputShaftDiameter
servoMeasurments.add(10.7) //shaftToShortSideDistance
servoMeasurments.add(18.3) // shaftToShortSideFlandgeEdge
servoMeasurments.add(18.5) // tipOfShaftToBottomOfFlange
servoMeasurments.add(3.0 ) //flangeThickness
servoMeasurments.add(56.5) // flangeLongDimention
servoMeasurments.add(11.2) //bottomOfFlangeToTopOfBody


double cableRatio = 1/1;
double servoPlacmentThickness  =5;
double horntipRad = 4.8/2;         
double hornBaseRad = 12.6/2;             
double hornLength = 20;   
double hornThickness=4.2;
int numberofHornArms =4;	   

double spindalHeight = 14;	
double spindalRadius = 16;
double grooveheight = spindalHeight/4;
double pinShoulder= 2;
double pinRadius=4;
double grooveRadius = spindalRadius;
double cableGuideHoles=1.7
double cubicCrossLen =hornLength+hornBaseRad+horntipRad;
double bottomToSpindalBottom = servoMeasurments.get(6) +servoPlacmentThickness- hornThickness/2;
double bottomToSpindalTop=bottomToSpindalBottom+spindalHeight+pinShoulder  
double housingDiameter=3.7
double cableAttachPlateThickness=15;
double mountScrewDiameter  =2.9;
double moduleWidth= (spindalRadius+grooveheight)*2;
double ballJointBaseThickness = 5;
double ballJointPinSize=8;
double socketAllignemntPinRadius=2;
double hornKeepaway = hornLength+horntipRad+2;
int sphereNumSlices=40;//
int sphereNumStacks=20;//
int numPins = 4;
int numServoBlocks =2

println "Bulding Servo "+bottomToSpindalBottom
CSG servoReference =  (CSG)ScriptingEngine
	                    .gitScriptRun(
                                "https://gist.github.com/3f9fef17b23acfadf3f7.git", // git location of the library
	                              "servo.groovy" , // file to load
	                              servoMeasurments )
println "Bulding Horn"

CSG h =  new Cylinder(	hornBaseRad, // Radius at the top
                      		horntipRad, // Radius at the bottom
                      		hornLength, // Height
                      			         (int)30 //resolution
                      			         ).toCSG()
CSG tip  = new Sphere(horntipRad)
					.toCSG()
					.movez(hornLength)
CSG base  = new Sphere(hornBaseRad)
					.toCSG()
CSG cube = new Cube(	0.1,// X dimention
			hornBaseRad*2,// Y dimention
			cubicCrossLen//  Z dimention
			).toCSG()
			.movez(cubicCrossLen/2-hornBaseRad)
			
CSG horn=h
	.union(tip,base)	
	.intersect(cube)										                     			         
	.roty(90)
	.rotz(90)
	.scalez(hornThickness*10)
for(int i=1;i<numberofHornArms;i++){
	horn=horn.union(horn.rotz(360/numberofHornArms*i))
}
horn=horn.union(new Cylinder(	hornBaseRad, // Radius at the top
                      		hornBaseRad, // Radius at the bottom
                      		hornThickness+0.5, // Height
                      			         (int)30 //resolution
                      			         ).toCSG())

println "Assembling Servo"
CSG totalServo = servoReference
				.union(
				horn
				.movez(servoMeasurments.get(6) - hornThickness/2))

totalServo = totalServo
			.movez(servoPlacmentThickness)
			.makeKeepaway(printerNozzelDiameter)	

println "Spindal Servo"
CSG cableSpindal = new Cylinder(	spindalRadius+grooveheight/2, // Radius at the top
                      				spindalRadius+grooveheight/2, // Radius at the bottom
                      				spindalHeight, // Height
                      			         (int)30 //resolution
                      			         ).toCSG()
CSG pin = new Cylinder(	pinRadius, // Radius at the top
  					pinRadius, // Radius at the bottom
       				8, // Height
       			         (int)30 //resolution
       			         ).toCSG()
       			         .union(new Cylinder(	pinRadius+1, // Radius at the top
		                      				pinRadius+1, // Radius at the bottom
		                      				pinShoulder, // Height
       			         (int)30 //resolution
       			         ).toCSG()
       			         )
       			         



CSG innerGroove = new Cylinder(	grooveRadius+grooveheight/2, // Radius at the bottom
                 				grooveRadius, // Radius at the top
                 				grooveheight, // Height
                 			         (int)30 //resolution
                 			         ).toCSG()
CSG grooveRing = new Cylinder(	grooveRadius+grooveheight/2+1, // Radius at the top
                      				grooveRadius+grooveheight/2+1, // Radius at the bottom
                      				grooveheight, // Height
                      			         (int)30 //resolution
                      			         )
  			         	.toCSG()
					.difference(innerGroove)

CSG cableHoles = new Cylinder(	cableGuideHoles, // Radius at the top
                      				cableGuideHoles, // Radius at the bottom
                      				spindalHeight, // Height
                      			         (int)6 //resolution
                      			         )
  			         	.toCSG()
cableHoles = cableHoles
			.union(new Sphere(cableGuideHoles).toCSG())// nub at the corner of the intersection
			.union(new Cylinder(	cableGuideHoles/2, // Radius at the top
                      				cableGuideHoles/2, // Radius at the bottom
                      				spindalHeight*2, // Height
                      			         (int)6 //resolution
                      			         )
                      			         
  			         	.toCSG().movex(5)
  			         	.movez(-spindalHeight))// screw hole
			.union(cableHoles.rotx(90))
			
CSG placedHoles = cableHoles
				.movey(spindalRadius/1.5)
				.movez(cableGuideHoles)
grooveRing= grooveRing
				.union(placedHoles)
				.union(placedHoles.rotz(180))


               			       
cableSpindal=cableSpindal
			.difference(  horn
						.makeKeepaway(printerNozzelDiameter)
					)
			.difference(grooveRing.movez(spindalHeight/5))
			.difference(grooveRing.movez(spindalHeight*3/5).rotz(90))
			.difference(new Cylinder(	pinRadius+0.5, // Radius at the top
  					pinRadius+0.5, // Radius at the bottom
       				spindalHeight*2, // Height
       			         (int)30 //resolution
       			         ).toCSG()
       			         .movez(-spindalHeight))
			//.union(pin)
println "Mount base Servo"


CSG mountBase = new Cube(	moduleWidth,// X dimention
			servoMeasurments.get(8)+servoPlacmentThickness,// Y dimention
			servoPlacmentThickness+servoMeasurments.get(7)//  Z dimention
			)
			.noCenter()
			.toCSG()

CSG cableAttach = new Cube(	moduleWidth,// X dimention
			cableAttachPlateThickness,// Y dimention
			bottomToSpindalTop//  Z dimention
			)
			.noCenter()
			.toCSG()
cableAttach=cableAttach
			.movey(mountBase.getMaxY())
			.union(cableAttach.movey(-mountBase.getMinY()-cableAttachPlateThickness))
if(spindalRadius>hornKeepaway)
	hornKeepaway= spindalRadius+1;		
CSG spindalKeepaway = new Cylinder(	hornKeepaway, // Radius at the top
                      				hornKeepaway, // Radius at the bottom
                      				spindalHeight+pinShoulder*2, // Height
                      			         (int)30 //resolution
                      			         ).toCSG()
                      			         .movez(bottomToSpindalBottom-pinShoulder)
CSG cableOutlet = new Cylinder(	cableGuideHoles/2, // Radius at the top
                 				cableGuideHoles/2, // Radius at the bottom
                 				10, // Height
                 			         (int)30 //resolution
                 			         )
                 			         .toCSG()
                 			         .union(new Cylinder(	housingDiameter/2, // Radius at the top
					                 				housingDiameter/2, // Radius at the bottom
					                 				20, // Height
					                 			         (int)30 //resolution
					                 			         )
            			         			.toCSG()
                 			         		.movez(-20)
                 			         )
                 			         
CSG cableConnector= cableOutlet.clone()
cableOutlet= cableOutlet
			    .rotx(-90)
			    .movex(-spindalRadius)
			    .movey(servoMeasurments.get(5)+10)
			    .movez(bottomToSpindalBottom+cableGuideHoles/2)
			    .movez(spindalHeight/5 +grooveheight/2)

cableOutlet = cableOutlet.union(
					cableOutlet
						.movez(spindalHeight*2/5 )
					)
cableOutlet = cableOutlet.union(
					cableOutlet
						.movex(spindalRadius*2)
					)
mountBase = mountBase
			.union(cableAttach)
			.movex(-mountBase.getMaxX()/2)
			.movey(-mountBase.getMaxY()+servoMeasurments.get(5)+servoPlacmentThickness/2)
			.difference(totalServo,spindalKeepaway,cableOutlet)
			
CSG topPlate = new Cube(	moduleWidth,// X dimention
			mountBase.getMaxY()-mountBase.getMinY(),// Y dimention
			servoPlacmentThickness//  Z dimention
			)
			.toCSG()
			.toYMax()
			.movey(mountBase.getMaxY())
			.union(pin.movez(servoPlacmentThickness/2))


 //Create a function to make mount holes in the correct place      			         
CSG makeMountScrewHole(	double screwDiam, 
					double bottomToSpindalTop,
					double servoPlacmentThickness,
					double printerNozzelDiameter,
					double cableAttachPlateThickness,
					CSG mountBase){
	CSG ms = new Cylinder(	screwDiam/2, // Radius at the top
		  					screwDiam/2, // Radius at the bottom
		       				bottomToSpindalTop+servoPlacmentThickness*3, // Height
	       			         (int)30 //resolution
	       			         ).toCSG()
	       			         .movez(-servoPlacmentThickness)
	       			         .makeKeepaway(printerNozzelDiameter)
	return ms
			.movey(mountBase.getMaxY()-cableAttachPlateThickness/2)
			.union(ms
					.movey(mountBase.getMinY()+cableAttachPlateThickness/2)
			)
}


topPlate=topPlate
		.difference(makeMountScrewHole(	4.3,
									bottomToSpindalTop,
					 				servoPlacmentThickness,
									printerNozzelDiameter,
									cableAttachPlateThickness,
									mountBase))
mountBase=mountBase	
		.difference(makeMountScrewHole(	mountScrewDiameter,
									bottomToSpindalTop,
					 				servoPlacmentThickness,
									printerNozzelDiameter,
									cableAttachPlateThickness,
									mountBase))	
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

CSG singleTop  = 		topPlate.clone()	
CSG singleBase=	mountBase.clone();				
println "Setting up print bed"
for(int i=1;i<numServoBlocks;i++){
	topPlate= topPlate.union(singleTop.movex(topPlate.getMaxX()-singleTop.getMinX()))
	mountBase=mountBase.union(singleBase.movex(mountBase.getMaxX()-singleBase.getMinX())	)
}

//place parts in the  assembly

CSG displaySpindal = cableSpindal
					.movez(bottomToSpindalBottom)
					.setColor(javafx.scene.paint.Color.YELLOW)	
cableSpindal= cableSpindal.rotx(180)
			.toZMin()					
topPlatePlaced=topPlate
			.movey(mountBase.getMinY()-topPlate.getMaxY()-2)
			.movez(servoPlacmentThickness/2)

CSG placedSpindal = cableSpindal
				.movex(mountBase.getMaxX()-cableSpindal.getMinX()+2)
				.movey(mountBase.getMaxY()-cableSpindal.getMaxX())
for(int i=1;i<numServoBlocks;i++){
	CSG tmp=cableSpindal
				.movex(mountBase.getMaxX()-cableSpindal.getMinX()+2)
				.movey(placedSpindal.getMinY() -cableSpindal.getMaxY()-2)
	
	placedSpindal=placedSpindal
				.union(
					tmp
					)
}
					
CSG ballShaft1 = ballShaft
				.movex(mountBase.getMaxX()-ballShaft.getMinX()+2)
				.movey(placedSpindal.getMinY() -ballShaft.getMaxY()-2)
CSG ballShaft2 = ballShaft
				.movex(mountBase.getMaxX()-ballShaft.getMinX()+2)
				.movey(ballShaft1.getMinY() -ballShaft.getMaxY()-2)
CSG ballJointBasePlaced = ballJointBase
						.movex(mountBase.getMaxX()-ballJointBase.getMinX()+2)
						.movey(ballShaft2.getMinY() -ballJointBase.getMaxY()-2)
CSG ballJointHatPlaced = ballJointHat
						.movex(mountBase.getMaxX()-ballJointHat.getMinX()+2)
						.movey(ballJointBasePlaced.getMinY() -ballJointHat.getMaxY()-2)
//println "Union for up print bed";CSG stlUnion = ballJointHatPlaced.union(placedSpindal,topPlatePlaced,ballShaft1,ballShaft2,ballJointBasePlaced,mountBase).rotz(90);			
//println "Exporting .stl"	; FileUtil.write(Paths.get("standardServoballJointPrintBed.stl"),stlUnion.toStlString());
CSG build =new Cube(225,145,1).noCenter().toCSG()
				.rotz(90)
				.movex(mountBase.getMinX())
				.movey(mountBase.getMaxY())
				.movez(-1)
				.setColor(javafx.scene.paint.Color.CYAN)
CSG servoPlaced = totalServo			
				.setColor(javafx.scene.paint.Color.BLUE)			
						
return [	placedSpindal,
		topPlatePlaced,
		mountBase,
		ballShaft1,
		ballShaft2,
		ballJointBasePlaced,
		ballJointHatPlaced,
		//build,
		servoPlaced,
		displaySpindal
		]
