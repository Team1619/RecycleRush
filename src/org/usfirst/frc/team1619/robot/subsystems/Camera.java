package org.usfirst.frc.team1619.robot.subsystems;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
//import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 *
 */
public class Camera extends Subsystem {

	private int cameraSession;
    private Image cameraFrame;
    
	// Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	
	private Camera() {
		cameraFrame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        // the camera name (ex "cam1") can be found through the roboRIO web interface
		cameraSession = NIVision.IMAQdxOpenCamera("cam1",
    		NIVision.IMAQdxCameraControlMode.CameraControlModeController);
    	NIVision.IMAQdxConfigureGrab(cameraSession);
    	
    	new Thread() {
    		@Override
    		public void run() {
    			initCamera();
    			while(true)
    				makeCameraImage();
    		}
    	}.start();
	}
	
	private static final Camera theSystem = new Camera();
	
	public static Camera getInstance() {
		return theSystem;
	}
	

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    }

    public void initCamera() {
    	NIVision.IMAQdxStartAcquisition(cameraSession);
    }
    
    public void makeCameraImage() {
    	NIVision.IMAQdxGrab(cameraSession, cameraFrame, 1);
        CameraServer.getInstance().setImage(cameraFrame);
    }
}

