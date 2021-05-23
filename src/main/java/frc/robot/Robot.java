// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.spi.ResourceBundleControlProvider;
import javax.lang.model.util.ElementScanner6;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.LinearFilter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Solenoid; 


/**
 * This is a demo program showing the use of the RobotDrive class, specifically it contains the code
 * necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private XboxController stick_0 = new XboxController(0);
  private XboxController stick_1 = new XboxController(1);
  private TalonFX chassis_motor_0 = new TalonFX(20);
  private TalonFX chassis_motor_1 = new TalonFX(21);
  private TalonFX chassis_motor_2 = new TalonFX(22);
  private TalonFX chassis_motor_3 = new TalonFX(23);
  private TalonFX shooter_motor_left = new TalonFX(25);
  private TalonFX shooter_motor_right = new TalonFX(26);
  private VictorSPX intake_motor = new VictorSPX(11);
  private VictorSPX transmission_motor_belt = new VictorSPX(14);
  private VictorSPX transmission_motor_wheel = new VictorSPX(13);
  private VictorSPX motor_color_spinner = new VictorSPX(16);
  private VictorSPX climb_motor_shaft = new VictorSPX(15);
  private VictorSPX climb_motor_hook = new VictorSPX(10);
  private TalonSRX shooter_motor_rotate = new TalonSRX(12);
  private TalonSRX shooter_motor_rotate_encoder = new WPI_TalonSRX(12);//Defining the Encode for the Shooter
  private PIDController shooter_motor_rotatePID = new PIDController(0.05, 0, 0.0012);
  private Solenoid intake_solenoid = new Solenoid(0);
  private PIDController pid = new PIDController(0.05,0.08, 0.01);
  private double k_shooter_zero = 37.0;
  private NetworkTable table;
  private Timer timer = new Timer();

  private boolean has_five_balls = false;

  private DigitalInput climb_sw_top = new DigitalInput(1);
  private DigitalInput climb_sw_bottom = new DigitalInput(0);
  private Boolean ShootingAutomation = false;
  //private PIDController DrivePID = new PIDController(0.05, 0, 0);
  //private PIDController ShooterPID = new PIDController(kp, ki, kd);


  public void Shooter_aimByLimeLight() {
    double tx0 = table.getEntry("tx").getDouble(0);
    double Deg = convertToDeg(shooter_motor_rotate_encoder.getSelectedSensorPosition(0));
    SmartDashboard.putNumber("Degree of the shooter_motor_rotate",Deg);
    SmartDashboard.putNumber("tx",tx0);
    double Turn = shooter_motor_rotatePID.calculate(tx0,0)/1.5;
    shooter_motor_rotate.set(ControlMode.PercentOutput, -Turn);
  }
  
  public double convertToDeg(double units) {
    return (units * (360.0/4096.0)*(18.0/120.0)) - k_shooter_zero;
  }
  
  @Override
  public void robotInit() {
    timer.reset();
    table = NetworkTableInstance.getDefault().getTable("limelight");
    
    chassis_motor_0.setInverted(false);
    chassis_motor_1.setInverted(false);
    chassis_motor_2.setInverted(true);
    chassis_motor_3.setInverted(true);
    
    shooter_motor_left.setInverted(true);
    shooter_motor_right.setInverted(false);
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putBoolean("SW Bottom", climb_sw_bottom.get());
    SmartDashboard.putBoolean("SW Top", climb_sw_top.get());

    SmartDashboard.putBoolean("Full", has_five_balls);
    
  }
  @Override
  public void autonomousInit() {
    super.autonomousInit();
  }
  

  @Override
  public void autonomousPeriodic() {
    System.out.println("Sensor Vel:" + chassis_motor_0.getSelectedSensorVelocity());
    System.out.println("Sensor Pos:" + chassis_motor_0.getSelectedSensorPosition());
    System.out.println("Out %" + chassis_motor_0.getMotorOutputPercent());
    super.autonomousPeriodic();
  }

  @Override
  public void teleopPeriodic() {
    //double voltage = DriverStation.getInstance().getBatteryVoltage();

    double y = -stick_0.getY(Hand.kLeft);
    double z = -(stick_0.getTriggerAxis(Hand.kLeft) - stick_0.getTriggerAxis(Hand.kRight));

    y *= 0.4;
    z *= 0.15;

    chassis_motor_0.set(ControlMode.PercentOutput, y + z);
    chassis_motor_1.set(ControlMode.PercentOutput, y + z);
    chassis_motor_2.set(ControlMode.PercentOutput, y - z);
    chassis_motor_3.set(ControlMode.PercentOutput, y - z);

    // stick_0.setRumble(RumbleType.kLeftRumble, Drive);
    // stick_0.setRumble(RumbleType.kRightRumble, Turn);、

    boolean is_shooting = stick_1.getBumper(Hand.kRight);

    if (stick_1.getAButton()) {
      if (is_shooting) {
        intake_motor.set(ControlMode.PercentOutput, 0);
        transmission_motor_belt.set(ControlMode.PercentOutput, 0.9);
        transmission_motor_wheel.set(ControlMode.PercentOutput, 0.9);
        shooter_motor_left.set(ControlMode.PercentOutput, 1);
        shooter_motor_right.set(ControlMode.PercentOutput, 1);
      }
      else {
        intake_motor.set(ControlMode.PercentOutput, 0.6);
        transmission_motor_belt.set(ControlMode.PercentOutput, 0.9);
        transmission_motor_wheel.set(ControlMode.PercentOutput, 0.9);
        shooter_motor_left.set(ControlMode.PercentOutput, 0);
        shooter_motor_right.set(ControlMode.PercentOutput, 0);
      }
    }
    else if (stick_1.getBButton()) {
      intake_motor.set(ControlMode.PercentOutput, -0.4);
      transmission_motor_belt.set(ControlMode.PercentOutput, -0.3);
      transmission_motor_wheel.set(ControlMode.PercentOutput, -0.3);
      shooter_motor_left.set(ControlMode.PercentOutput, -0.3);
      shooter_motor_right.set(ControlMode.PercentOutput, -0.3);
    }
    else {
      intake_motor.set(ControlMode.PercentOutput, 0);
      transmission_motor_belt.set(ControlMode.PercentOutput, 0);
      transmission_motor_wheel.set(ControlMode.PercentOutput, 0);
      shooter_motor_left.set(ControlMode.PercentOutput, 0);
      shooter_motor_right.set(ControlMode.PercentOutput, 0);
    }

    if (stick_1.getBumper(Hand.kLeft)) {
      Shooter_aimByLimeLight();
    }
    else {
      shooter_motor_rotate.set(ControlMode.PercentOutput, stick_1.getX(Hand.kLeft));
    }


    if (stick_0.getStartButton()) {
      climb_motor_shaft.set(ControlMode.PercentOutput, 1);
    }
    else {
      climb_motor_shaft.set(ControlMode.PercentOutput, 0);
    }

    motor_color_spinner.set(ControlMode.PercentOutput, stick_0.getX(Hand.kRight));


    if (stick_0.getPOV() == 0 || stick_0.getPOV() == 45 || stick_0.getPOV() == 315) {
      climb_motor_hook.set(ControlMode.PercentOutput, 1);
    }
    else if(stick_0.getPOV() == 180 || stick_0.getPOV() == 135 || stick_0.getPOV() == 225) {
      climb_motor_hook.set(ControlMode.PercentOutput, -1);
    }
    else {
      climb_motor_hook.set(ControlMode.PercentOutput, 0);
    }
    
    if (stick_0.getAButton()) {
      intake_solenoid.set(true);
    }
    else if (stick_0.getBButton()) {
      intake_solenoid.set(false);
    }
  }
  
  @Override
  public void testPeriodic() {
    if (stick_1.getStartButton()) {
      climb_motor_shaft.set(ControlMode.PercentOutput, .4);
    }
    if (stick_1.getBackButton()) {
      climb_motor_shaft.set(ControlMode.PercentOutput, -.4);
    }
    else {
      climb_motor_shaft.set(ControlMode.PercentOutput, 0);
    }
  }
}
