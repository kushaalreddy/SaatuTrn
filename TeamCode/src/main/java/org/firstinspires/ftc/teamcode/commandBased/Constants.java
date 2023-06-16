package org.firstinspires.ftc.teamcode.commandBased;

import com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficientsEx;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.PwmControl;

import org.firstinspires.ftc.teamcode.classes.Vector2d;

@SuppressWarnings("SpellCheckingInspection")
@Config
public class Constants {

    //debug toggles
    public static boolean DEBUG_DRIVE = false;
    public static boolean DEBUG_ELE = false;
    public static boolean DEBUG_ARM = false;
    public static boolean DEBUG_ROTATOR = true;
    public static boolean DEBUG_INTAKE = false;

    //motor types
    public static Motor.GoBILDA DRIVE_MOTOR = Motor.GoBILDA.RPM_312;
    public static Motor.GoBILDA ELEVATOR_MOTOR = Motor.GoBILDA.RPM_312;
    public static Motor.GoBILDA ARM_MOTOR = Motor.GoBILDA.RPM_117;

    //drivetrain constants
    public static double DRIVE_FAST_STRAFE = 1;
    public static double DRIVE_FAST_FORWARD = 1;
    public static double DRIVE_FAST_TURN = 1;

    public static double DRIVE_SLOW_STRAFE = 0.5;
    public static double DRIVE_SLOW_FORWARD = 0.5;
    public static double DRIVE_SLOW_TURN = 0.5;

    public static double TRACK_WIDTH = 13;
    public static double DRIVE_KV = 0;
    public static PIDCoefficientsEx TURN_COEFFS = new PIDCoefficientsEx(1, 0.2, 0.2, 1, 0, 0);
    public static Vector2d TARGET = new Vector2d(-10, 0);
    public static Pose2d STARTING_POINT = new Pose2d(0, 0, Math.toRadians(0));
    public static double ANGLE_OFFSET = 180;

    //elevator pid
    public static PIDCoefficients ELE_COEFFS = new PIDCoefficients(0.0075, 0, 0.00005);
    public static double ELE_KG = 0.05;
    public static double ELE_KV = 0;
    public static double ELE_KA = 0;
    public static double ELE_KS = 0;

    //elevator motion profile
    public static double ELE_MAX_VEL = 200;
    public static double ELE_MAX_ACCEL = 250;

    //elevator positions
    public static double ELE_LOW = 0;
    public static double ELE_MID_LOW = 4;
    public static double ELE_MID_HIGH = 10;
    public static double ELE_HIGH = 12;

    //command-ending deadzones
    public static double ELE_DONE_DEADZONE = 5;
    public static double ELE_TRIGGER = 8;

    //arm pid
    //public static PIDCoefficients ARM_COEFFS = new PIDCoefficients(.004, 0.00075, 0);
    public static PIDCoefficients ARM_COEFFS = new PIDCoefficients(0, 0, 0);

    public static double ARM_KV = 0;
    public static double ARM_KA = 0;
    public static double ARM_KS = 0.05;
    public static double ARM_KSIN = .15;

    public static double ARM_IDLE_VELO = 800;
    public static double ARM_IDLE_ACCEL = 1600;
    public static double ARM_MAX_VELO = 1600;
    public static double ARM_MAX_ACCEL = 4000;

    //arm angle positions
    public static double ARM_ANGLE_BACK = -110;
    public static double ARM_ANGLE_IDLE = 45;
    public static double ARM_ANGLE_FRONT = 110;
    public static double ARM_ANGLE_MAX = 120;

    //arm encoder positions
    public static double ARM_ENC_BACK_MAX = -670;
    public static double ARM_ENC_BACK_PARALLEL = -520;
    public static double ARM_ENC_FRONT_PARALLEL = 220;
    public static double ARM_ENC_FRONT_MAX = 350;
    public static double ARM_ENC_CENTER = -145;

    //rotator limits
    public static double ROTATOR_FORWARD = 0;
    public static double ROTATOR_BACK = 1;
    public static PwmControl.PwmRange TUNED_RANGE = new PwmControl.PwmRange(530, 2340);

    public static double ROTATOR_SMALL_INCREMENT = 10;
    public static double ROTATOR_LARGE_INCREMENT = 100;

}