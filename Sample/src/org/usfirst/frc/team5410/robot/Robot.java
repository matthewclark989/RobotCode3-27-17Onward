package org.usfirst.frc.team5410.robot;

import java.awt.Color;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SPI;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	SendableChooser chooser = new SendableChooser<>();//Used to store the command we want
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	double time = 0;
	static AHRS gyro; //Instantiate Gyro
	static CANTalon motor1 = new CANTalon(28);
	static CANTalon motor2 = new CANTalon(30);
	static CANTalon motor3 = new CANTalon(20);
	static CANTalon motor4 = new CANTalon(21);
//	static CANTalon motor1 = new CANTalon(1);
//	static CANTalon motor2 = new CANTalon(2);
//	static CANTalon motor3 = new CANTalon(3);
//	static CANTalon motor4 = new CANTalon(4);
//	static CANTalon gear = new CANTalon(5);

	
	
	boolean autoCheck1;
	boolean autoCheck2;
	boolean autoCheck3;
	boolean autoCheck4;
	boolean autoCheck5;
	
	static RobotDrive drive = new RobotDrive(motor1,motor2,motor3,motor4);
	Joystick joystick1 = new Joystick(0);
	Joystick joystick2 = new Joystick(1);
	static CANTalon side = new CANTalon(22);	
	static CANTalon gear = new CANTalon(26);
	CANTalon shooter1 = new CANTalon(23);
	CANTalon shooter2 = new CANTalon(24);
	CANTalon climber1 = new CANTalon(29);
	CANTalon climber2 = new CANTalon(31);
	CANTalon pickUp = new CANTalon(27);
	CANTalon agitator = new CANTalon(25);
	CANTalon loaders = new CANTalon(0);
	boolean pickup = false;
	boolean toggle = false;
	
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		gear.setFeedbackDevice(FeedbackDevice.QuadEncoder);//Motor Settings
		gear.reverseOutput(true);
		gear.reverseSensor(false);
		gear.configEncoderCodesPerRev(420);
		gear.setProfile(0);//Used for positioning
		gear.setF(0.1);
		gear.setP(0.7);
		gear.setI(0.001);
		gear.setD(0.1);
		gear.configNominalOutputVoltage(+0.0f, -0.0f);
		gear.configPeakOutputVoltage(+12.0f, -12.0f);
		gear.setForwardSoftLimit(5);
		gear.setReverseSoftLimit(5);
		gear.setPosition(0);
		gear.changeControlMode(TalonControlMode.Position);
		shooter1.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		shooter1.reverseSensor(true);
		shooter1.configEncoderCodesPerRev(1024);
		shooter1.setProfile(0);
		shooter1.setF(0.1);
		shooter1.setP(0.1);
		shooter1.setI(0);
		shooter1.setD(0);
		shooter1.configNominalOutputVoltage(+0.0f, -0.0f);
		shooter1.configPeakOutputVoltage(+12.0f, -12.0f);
		shooter1.setPosition(0);
		shooter1.setForwardSoftLimit(15);
		shooter1.setReverseSoftLimit(15);
		shooter1.changeControlMode(TalonControlMode.Speed);
		shooter2.changeControlMode(TalonControlMode.Follower);
		shooter2.set(shooter1.getDeviceID());
		climber1.changeControlMode(TalonControlMode.PercentVbus);
//		climber2.changeControlMode(TalonControlMode.PercentVbus);

		climber2.changeControlMode(TalonControlMode.Follower);
		climber2.set(climber1.getDeviceID());
		
		UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
		
		chooser.addDefault("0neSizeFitsAll(ForwardTime)", "ForwardTime");
		chooser.addObject("ERROR", "ForwardEnc");
		chooser.addObject("TheShroud(Lacking of substance)", "Nothing");
		chooser.addObject("BackShot", "ForwardTime2");
		chooser.addObject("Im Tired", "forward");
		SmartDashboard.putData("Auto Chooser", chooser);
		
		gyro = new AHRS(SPI.Port.kMXP);
				gyro.reset();
				
		
				
		
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
//		SmartDashboard.putBoolean("Works", false);
//		
//		
//		command.start();
		// autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		time = Timer.getFPGATimestamp();
		gyro.reset();
		//motor1.setPosition(0);
		//motor3.setPosition(0);
		autoCheck1 = true;
		autoCheck2 = true;
		autoCheck3 = true;
		autoCheck4 = true;
		autoCheck5 = true;
		motor1.set(0);
    	motor2.set(0);
    	motor3.set(0);
    	motor4.set(0);

//    	if(chooser.getSelected() == "Forward"){
//	    	while (Timer.getFPGATimestamp() < time+3){
//	    		motor1.set(1);
//	        	motor2.set(1);
//	        	motor3.set(-1);
//	        	motor4.set(-1);
//	    	}
//	    	motor1.set(0);
//	    	motor2.set(0);
//	    	motor3.set(0);
//	    	motor4.set(0);
//	    	gear.set(-0.625);
//	    	while (Timer.getFPGATimestamp() < time + 5);
//	    	while(Timer.getFPGATimestamp() < time + 7){
//	    	motor1.set(-1);
//        	motor2.set(-1);
//        	motor3.set(1);
//        	motor4.set(1);
//	    	}
//	    	gear.set(0);
//	    	motor1.set(0);
//	    	motor2.set(0);
//	    	motor3.set(0);
//	    	motor4.set(0);
//	    	
//	    	SmartDashboard.putString("Finished", "Yes");
//    	}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {	
		if(chooser.getSelected() == "ForwardTime"){
	    	if (Timer.getFPGATimestamp() < time+4){
	    		motor1.set(-.45);
	        	motor2.set(-.45);
	        	motor3.set(.46);
	        	motor4.set(.46);
		    	gear.set(0);

//	    		moveStraight(0.35);
	    	}
	    	else if (Timer.getFPGATimestamp() < time + 5){
	    		motor1.set(0);
		    	motor2.set(0);
		    	motor3.set(0);
		    	motor4.set(0);
		    	gear.set(-0.625);
	    	}
	    	else if (Timer.getFPGATimestamp() < time + 6.5){
		    	motor1.set(0.3);
		    	motor2.set(0.3);
		    	motor3.set(-0.3);
		    	motor4.set(-0.3);
	    	}
	    	else{
		    	gear.set(0);
		    	motor1.set(0);
		    	motor2.set(0);
		    	motor3.set(0);
		    	motor4.set(0);
	    	}
	    	SmartDashboard.putString("Finished", "Yes");
		}
		
		
		else if(chooser.getSelected() == "ForwardTime2"){
	    	if (Timer.getFPGATimestamp() < time){
//	    		motor1.set(-.67);
//	        	motor2.set(-.67);
//	        	motor3.set(.7);
//	        	motor4.set(.7);
	    		//moveStraight(0.35);
	    	}
	    	else if (Timer.getFPGATimestamp() < time + 4){
	    		motor1.set(-.35);
	        	motor2.set(-.35);
	        	motor3.set(.35);
	        	motor4.set(.35);
	    	}
	    	else if (Timer.getFPGATimestamp() < time + 15)
	    	{
	    		shooter1.set(0.75);
				agitator.set(100);
				motor1.set(0);
		    	motor2.set(0);
		    	motor3.set(0);
		    	motor4.set(0);
		    	gear.set(0);

	    	}
	    	else{
	    		shooter1.set(0);
	    		agitator.set(0);
		    	motor1.set(0);
		    	motor2.set(0);
		    	motor3.set(0);
		    	motor4.set(0);
		    	gear.set(0);

	    	}
	    	SmartDashboard.putString("Finished", "Yes");
		}		
		
		else if(chooser.getSelected() == "ForwardEnc"){
	    	if (motor1.getEncPosition() > -200){
	    		motor1.set(-1);
	        	motor2.set(-1);
	    	}
	    	else if (motor1.getEncPosition() > -252){
	    		motor1.set(-.7);
	    		motor2.set(-.7);
	    	}
	    	else{
	    		motor1.set(0);
	    		motor2.set(0);
	    	}
	    	if (motor3.getEncPosition() < 200){
	        	motor3.set(1);
	        	motor4.set(1);
	    	}
	    	else if (motor3.getEncPosition() < 252){
	    		motor3.set(.7);
	    		motor4.set(.7);
	    	}
	    	else{
	    		motor3.set(0);
	    		motor4.set(0);
	    		autoCheck2 = false;
	    	}
	    	if (autoCheck2 == false){
		    	if (autoCheck1) {
		    		autoCheck1 = false;
		    		time = Timer.getFPGATimestamp();
		    		}
		    	if (Timer.getFPGATimestamp() < time + 2){
			    	gear.set(-0.625);
		    	}
		    	else if (Timer.getFPGATimestamp() < time + 3){
			    	motor1.set(1);
			    	motor2.set(1);
			    	motor3.set(-1);
			    	motor4.set(-1);
		    	}
		    	else{
			    	gear.set(0);
			    	motor1.set(0);
			    	motor2.set(0);
			    	motor3.set(0);
			    	motor4.set(0);
		    	}
		    	SmartDashboard.putString("Finished", "Yes");
		    	SmartDashboard.putNumber("Enc1", motor1.getEncPosition());
		    	SmartDashboard.putNumber("Enc2", motor3.getEncPosition());
			}
		}
		else if (chooser.getSelected() == "forward"){
			
			if (Timer.getFPGATimestamp() < time+4){
	    		motor1.set(-.45);
	        	motor2.set(-.45);
	        	motor3.set(.46);
	        	motor4.set(.46);
//	    		moveStraight(0.35);
		    	gear.set(0);

	    	}
			else {
				motor1.set(0);
		    	motor2.set(0);
		    	motor3.set(0);
		    	motor4.set(0);
		    	gear.set(0);

			}
			
		}
		else {
			motor1.set(0);
	    	motor2.set(0);
	    	motor3.set(0);
	    	motor4.set(0);
	    	gear.set(0);

		}
//		Command command;
//		command = (Command) chooser.getSelected();
//		command = (Command) new WeAreTrying();
//		command.start();
//		SmartDashboard.putBoolean("Workssss", true);
//		

//		switch (autoSelected) {
//		case customAuto:
//			// Put custom auto code here
//			break;
//		case defaultAuto:
//		default:
//			// Put default auto code here
//			break;
//		}
		
		
	
		//if ((Timer.getFPGATimestamp() - time) < 2.5)drive.tankDrive(-0.8, -0.8);
		//gear.set(0);
		
		
//		if (Timer.getFPGATimestamp() < time+1 && autoCheck1)drive.tankDrive(-0.8, -0.8);
//		else if (gyro.getAngle() < 50 && autoCheck4)
//			{
//			autoCheck1 = false;
//			motor1.set(-0.8);
//			motor2.set(-0.8);
//			motor3.set(0.8);
//			motor4.set(0.8);
//			}
//		else if (gyro.getAngle() >= 50 && autoCheck2)
//		{
//			time = Timer.getFPGATimestamp();
//			autoCheck4 = false;
//			autoCheck2 = false;
//		}
//		else if (Timer.getFPGATimestamp() - time < 1 && autoCheck3)
//		{
//			drive.tankDrive(-0.8, -0.8);
//		}
//		else if (Timer.getFPGATimestamp() - time < 2)
//		{
//			autoCheck3 = false;
//			gear.set(-0.625);
//		}
//		else if (Timer.getFPGATimestamp() - time < 3)
//		{
//			drive.tankDrive(-0.8, -0.8);
//		}
//		else {
//			gear.set(0);
//		}
		
		
	}

	/**
	 * This func0tion is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
//		if (joystick2.getRawButton(1)) motor1.set(1);
//		if (joystick2.getRawButton(2)) motor2.set(1);
//		if (joystick2.getRawButton(3)) motor3.set(1);
//		if (joystick2.getRawButton(4)) motor4.set(1);
		
		
		//drive.tankDrive(joystick1.getRawAxis(1), joystick1.getRawAxis(1));
		
		if (gyro.getAngle() > 0 && joystick1.getRawAxis(1) > 0.1)
			drive.tankDrive(joystick1.getRawAxis(1) *0.75 - joystick1.getRawAxis(1) * ((gyro.getAngle() /360)*20), joystick1.getRawAxis(1)*0.75, true);//Drives the robot			
		
		else if (gyro.getAngle() < 0 && joystick1.getRawAxis(1) > 0)
			drive.tankDrive(joystick1.getRawAxis(1)*0.75,  joystick1.getRawAxis(1)*0.75 +  joystick1.getRawAxis(1) * ((gyro.getAngle() / 360)*20), true);//Drives the robot
		
		else if (gyro.getAngle() > 0 && joystick1.getRawAxis(1) < 0)
			drive.tankDrive(joystick1.getRawAxis(1)*0.75 + joystick1.getRawAxis(1) * ((gyro.getAngle() /360)*20), joystick1.getRawAxis(1)*0.75, true);//Drives the robot			
		
		else if (gyro.getAngle() < 0 && joystick1.getRawAxis(1) < 0)
			drive.tankDrive(joystick1.getRawAxis(1)*0.75,  joystick1.getRawAxis(1)*0.75 -  joystick1.getRawAxis(1) * ((gyro.getAngle() / 360)*20), true);//Drives the robot
//		
//		
		
		
		if(Math.abs(joystick1.getRawAxis(4)) > 0.1)
		{
		motor1.set(joystick1.getRawAxis(4));
		motor2.set(joystick1.getRawAxis(4));
		motor3.set(joystick1.getRawAxis(4));
		motor4.set(joystick1.getRawAxis(4));
		time = Timer.getFPGATimestamp();
		autoCheck5 = true;
		//Drive.tankDrive(joystick1.getRawAxis(4) * -1, joystick1.getRawAxis(4)) ;
		gyro.reset();
		}
		if ((time+0.5 < Timer.getFPGATimestamp()) && autoCheck5)
		{
			gyro.reset();
			autoCheck5 = false;
		}
		side.set(-joystick1.getRawAxis(0));
		if (joystick2.getRawButton(1)){
			gear.set(-0.625);
		}
		else if (joystick2.getRawButton(2)){
			gear.set(-0.125);
		}
		else{
			gear.set(0);
		}
		shooter1.changeControlMode(TalonControlMode.PercentVbus);
		//shooter1.set(deadZone(joystick1.getRawAxis(3)));
		if(Math.abs(joystick1.getRawAxis(3)) > 0){
			shooter1.set(0.76);
			agitator.set(100);
		//	loaders.set(-0.7);
		}
		else if(joystick1.getRawButton(6)){
			shooter1.set(0.78);
			agitator.set(100);
			//loaders.set(-0.7);
		}
		else{
			shooter1.set(0);
			agitator.set(0);
			//loaders.set(0);
		}
		
		
		if (joystick1.getRawButton(4) && pickup == false){
			
			if (toggle) 
			{
				pickUp.set(1);
				toggle = false;
			}
			else 
			{
				toggle = true;
				pickUp.set(0);
			}
			pickup = true;
		}
		else if (!joystick1.getRawButton(4) && pickup == true)
		{
			pickup = false;
		}
		climber1.set(joystick2.getRawAxis(3));
		
		
		
		
//		
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
	public void teleopinit()
	{
		
		toggle = true;
	}
	
	public void moveStraight(double forward)
	{
		
		
		if (gyro.getAngle() < 0 && forward > 0.1)
			drive.tankDrive(forward *0.75 - forward * ((gyro.getAngle() /360)*20), forward*0.75, true);//Drives the robot			
		
		else if (gyro.getAngle() > 0 && forward > 0)
			drive.tankDrive(forward*0.75,  forward*0.75 +  forward * ((gyro.getAngle() / 360)*20), true);//Drives the robot
		
		else if (gyro.getAngle() < 0 && forward < 0)
			drive.tankDrive(forward*0.75 + forward * ((gyro.getAngle() /360)*20), forward*0.75, true);//Drives the robot			
		
		else if (gyro.getAngle() > 0 && forward < 0)
			drive.tankDrive(forward*0.75,  forward*0.75 -  forward * ((gyro.getAngle() / 360)*20), true);//Drives the robot
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
}

