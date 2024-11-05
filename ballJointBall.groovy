import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Sphere
import eu.mihosoft.vrl.v3d.Cylinder
import eu.mihosoft.vrl.v3d.Cube

class AnimatronicHead {

    double printerOffset = 1.0 // Offset for printer precision
    double ballSize = 5.0 // Size of the ball joint
    double boltDiameterMeasurement = 2.5 // Bolt diameter measurement
    double overallThickness = 6.4 // Overall thickness
    double leftEyeDiameter = 20.0 // Left Eye Diameter
    double rightEyeDiameter = 20.0 // Right Eye Diameter
    double headDiameter = 50.0 // Head Diameter
    double jawHeight = 15.0 // Jaw Height
    double eyeCenter = 10.0 // Eye Center
    double thickness = 3.5 // Material Thickness

    HashMap<Double, CSG> eyeCache = [:] // Cache for eyes

    CSG generateHead() {
        // Generate the main head structure
        CSG head = new Sphere(headDiameter / 2, 30, 15).toCSG()
        head = head.union(generateEyeRings(head, 0, headDiameter / 2))
        return head
    }

    CSG generateEyeRings(CSG upperHead, double xdist, double height) {
        double cheekWidth = headDiameter / 6
        double attachLevel = jawHeight + thickness - height

        CSG lRing = new Cylinder(leftEyeDiameter / 2, leftEyeDiameter / 2, thickness, 30).toCSG()
            .toZMin()
            .roty(-90)

        CSG rRing = new Cylinder(rightEyeDiameter / 2, rightEyeDiameter / 2, thickness, 30).toCSG()
            .toZMin()
            .roty(-90)

        // Bolt and lug configurations
        CSG lug = new Cylinder(boltDiameterMeasurement, boltDiameterMeasurement, thickness, 30).toCSG()
            .toZMin()
            .roty(-90)

        CSG attach = new Cube(thickness, cheekWidth, boltDiameterMeasurement + thickness).toCSG()
            .toZMin()
            .toXMin()
            .movez(attachLevel)

        CSG plate = lRing.union(attach.movez(-attachLevel)).hull().union(rRing.union(attach.movez(-attachLevel)).hull())

        // Add tilting mechanism for the eyes
        CSG leftTiltMount = generateTiltMount(-eyeCenter / 2, height)
        CSG rightTiltMount = generateTiltMount(eyeCenter / 2, height)

        return plate.union(leftTiltMount).union(rightTiltMount)
    }

    CSG generateTiltMount(double xOffset, double height) {
        double tiltMountHeight = 5.0 // Height of the tilt mount
        double tiltMountWidth = 3.0 // Width of the tilt mount

        return new Cube(thickness, tiltMountWidth, tiltMountHeight).toCSG()
            .move(xOffset, 0, height + tiltMountHeight / 2) // Position the tilt mount
            .toZMin()
            .rotx(45) // Tilt the mount if needed (adjust angle as required)
    }

    static void main(String[] args) {
        AnimatronicHead headDesign = new AnimatronicHead()
        CSG head = headDesign.generateHead()

        // Output the STL string instead of saving directly
        String stlString = head.toStlString()

        // Debug: Print the STL string to console
        println "STL String Content:"
        println stlString

        // Write the STL string to a file
        try {
            def file = new File("C:/path/to/your/directory/animatronic_head_with_tilt.stl")
            file.write(stlString)
            println "Model saved successfully at: ${file.absolutePath}"
        } catch (IOException e) {
            println "Error saving model: ${e.message}"
        }
    }
}
