package org.firstinspires.ftc.teamcode.drivePose;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drivePose.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drivePose.StandardTrackingWheelLocalizer;

public class DrivePose extends SubsystemBase {
    private SampleMecanumDrive drive;
    private final Telemetry telemetry;
    public DrivePose(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drive.setLocalizer(new StandardTrackingWheelLocalizer(hardwareMap));//reset pose
    }

    public void driveJoy(double left_stick_y, double left_stick_x, double right_stick_x) {
        drive.setWeightedDrivePower(
                new Pose2d(
                        left_stick_y * .6,
                        left_stick_x * .6,
                        -right_stick_x * .6
                )
        );
        drive.update();
        Pose2d poseEstimate = drive.getPoseEstimate();
        telemetry.addData("TWLPoseX", poseEstimate.getX());
        telemetry.addData("TWLPoseY", poseEstimate.getY());
        telemetry.addData("TWLPoseH", Math.toDegrees(poseEstimate.getHeading()));
    }
}