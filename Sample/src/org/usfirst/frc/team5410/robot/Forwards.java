package org.usfirst.frc.team5410.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Forwards extends InstantCommand {
double time;
    public Forwards() {
        super();
        
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        time = Timer.getFPGATimestamp();
    }

    // Called once when the command executes
    protected void initialize() {
    	while (Timer.getFPGATimestamp() < time + 3){
    	Robot.motor1.set(1);
    	Robot.motor2.set(1);
    	Robot.motor3.set(-1);
    	Robot.motor4.set(-1);
    	}
    	Robot.motor1.set(0);
    	Robot.motor2.set(0);
    	Robot.motor3.set(0);
    	Robot.motor4.set(0);
    	SmartDashboard.putString("Finished", "Yes");
    }

}
