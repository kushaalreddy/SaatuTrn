package com.bravenatorsrobotics.drive;

import com.bravenatorsrobotics.core.Robot;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public abstract class AbstractDrive {

    protected final Robot<? extends AbstractDrive> robot;

    protected final double ticksPerInch;

    public AbstractDrive(Robot<? extends AbstractDrive> robot) {
        this.robot = robot;

        this.ticksPerInch = (robot.specifications.ticksPerMotorRev * robot.specifications.driveGearReduction) /
                (robot.specifications.wheelDiameterInches * Math.PI);
    }

    public enum TurnDirection {
        CLOCKWISE, COUNTER_CLOCKWISE
    }

    protected void SetPower(DcMotorEx motor, double power) {
        if(robot.specifications.useVelocity) motor.setVelocity(power * robot.specifications.maxVelocity);
        else motor.setPower(power);
    }

    protected void SetAllPower(double power) {
        if(robot.specifications.useVelocity) {
            for(DcMotorEx motor : robot.GetAllMotors())
                motor.setVelocity(power * robot.specifications.maxVelocity);
        }
        else {
            for(DcMotorEx motor : robot.GetAllMotors())
                motor.setPower(power);
        }
    }

    protected void IncrementTargetPosition(DcMotorEx motor, int amount) {
        motor.setTargetPosition(motor.getCurrentPosition() + amount);
    }

    // Abstract Methods

    public abstract void Drive(double v, double h, double r);
    public abstract void Stop();

    public abstract void DriveByEncoders(double power, int leftTicks, int rightTicks);
    public void DriveByEncoders(double power, int ticks) { DriveByEncoders(power, ticks, ticks); } // Binding

    public abstract void DriveByInches(double power, double leftInches, double rightInches);
    public void DriveByInches(double power, int inches) { DriveByInches(power, inches, inches); }

    public abstract void TurnDegrees(double power, int degrees, TurnDirection turnDirection);
}
