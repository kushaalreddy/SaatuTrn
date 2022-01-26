package org.firstinspires.ftc.Team19567; //The "namespace" for the project is declared here. To distinguish it from other teams' packages, Team19567 is used.

//Import necessary packages/libraries

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="TeleOP", group="Dababy") //Gives the TeleOp its name in the driver station menu and categorizes it as an Iterative OpMode
public class TeleOP extends OpMode {           //Declares the class TestOPIterative, which is a child of OpMode
    //Declare OpMode members
    private final ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDCFront = null;
    private DcMotor rightDCFront = null;
    private DcMotor leftDCBack = null;
    private DcMotor rightDCBack = null;
    private DcMotor carouselLeft = null;
    private DcMotor carouselRight = null;
    private DcMotor intakeDC = null;
    private DcMotor armDC = null;
    private Servo releaseServo = null;
    private Servo balanceServo = null;
    private TouchSensor limitSwitch = null;
    //private DistanceSensor distanceSensor = null;
    private double carouselLeftPower = 0.0;
    private double carouselRightPower = 0.0;
    private double armPos = 0;
    private double armPower = 0.5;
    private double intakePower = 0.0;
    private double releaseServoPos = 0.0;
    private double balanceServoPos = 0.0;
    //private RevBlinkinLedDriver blinkin = null;
    private boolean isSlowmode = false;
    private double acc = 1.0;
    //private RevBlinkinLedDriver.BlinkinPattern blinkinPattern = RevBlinkinLedDriver.BlinkinPattern.BEATS_PER_MINUTE_RAINBOW_PALETTE;
    private Mechanisms mechanisms = null;

    public enum PRESETSTATE {
        SHARED_HUB,
        ALLIANCE_FIRST,
        ALLIANCE_SECOND,
        ALLIANCE_THIRD,
        GOING_DOWN,
        NO_PRESET
    }

    private PRESETSTATE presetState = PRESETSTATE.NO_PRESET;

    @Override
    public void init() {
        //Get the motors from the robot's configuration

        leftDCFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightDCFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftDCBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightDCBack = hardwareMap.get(DcMotor.class, "rightBack");
        carouselLeft = hardwareMap.get(DcMotor.class, "carouselLeft");
        carouselRight = hardwareMap.get(DcMotor.class,"carouselRight");
        intakeDC = hardwareMap.get(DcMotor.class, "intakeDC");
        armDC = hardwareMap.get(DcMotor.class, "armDC");
        releaseServo = hardwareMap.get(Servo.class, "releaseServo");
        balanceServo = hardwareMap.get(Servo.class, "balanceServo");
        limitSwitch = hardwareMap.get(TouchSensor.class,"limitSwitch");
        //blinkin = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        //distanceSensor = hardwareMap.get(DistanceSensor.class,"distanceSensor");

        //Set direction to be forward in case the robot's motors are oriented otherwise; can change FORWARD to REVERSE if necessary

        leftDCFront.setDirection(DcMotor.Direction.FORWARD);
        rightDCFront.setDirection(DcMotor.Direction.REVERSE);
        leftDCBack.setDirection(DcMotor.Direction.FORWARD);
        rightDCBack.setDirection(DcMotor.Direction.REVERSE);
        intakeDC.setDirection(DcMotor.Direction.FORWARD);
        armDC.setDirection(DcMotor.Direction.REVERSE);
        balanceServo.setDirection(Servo.Direction.REVERSE);

        releaseServoPos = 0.97;
        balanceServoPos = balanceServo.MIN_POSITION;
        mechanisms = new Mechanisms(armDC,carouselLeft,carouselRight,intakeDC,balanceServo,releaseServo,telemetry);

        telemetry.update();
    }

    @Override
    public void init_loop() {
        telemetry.addData("Status", "Awaiting Start");
        telemetry.update();
    }

    @Override
    public void start() {
        armDC.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armDC.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armDC.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        telemetry.addData("Status", "Started");
        telemetry.update();
        runtime.reset(); //Reset runtime
    }

    @Override
    public void loop() {
        /* Mecanum drive applies forces at a 45 degree angle to its wheels, instead of directly (like most non-holonomic chassis);
        this means that its axis of movement is not the tradition al x-y plane, but rather said plane rotated 45 degrees. This also means that the wheels
        of a mecanum chassis should be as close to a square as possible. That way, the rotated plane's axes push straight through the wheels for
        maximum efficiency. */

        double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y); //Gets the amount that we want to translate
        double angleDC = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4; /* This gets the angle theta we desire to turn at. It is subtracted
        by PI/4 radians, which equals 45 degrees. One can also add 45 degrees. It doesn't matter so long as the axes of the robot are rotated 45 degrees. */

        /* The axes we want to move on are the hypotenuse of the right triangle with non-right angle angleDC. The size of this hypotenuse is determined by the
        value of r, which we computed earlier. Thus, the amount that we want to move in the standard x-y plane are the legs of the right triangle. To determine
        which wheels drive th x leg and which drive the y leg (base), we must take a look at the configuration of the wheels. In most robots, which have the wheels
        pointed outwards in an X shape (maximum efficiency), we see that the front left and rear right wheels point in the y direction, while the rear left
        and front right wheels point in the x direction. Thus, we use cosine for the first group and sine for the second group. */
//DRIVETRAIN
        if(gamepad1.y) isSlowmode = !isSlowmode;
        if(isSlowmode) acc = 0.3;
        else acc = 1.0;
        final double leftFrontSpeed = (r * Math.sin(angleDC) - gamepad1.right_stick_x)*acc; //Using the math explained above, we can obtain the values we want to multiply each wheel by. acc is the variable which controls the overall multiplier of how fast we want to go.
        final double rightFrontSpeed = (r * Math.cos(angleDC) + gamepad1.right_stick_x)*acc;
        final double leftBackSpeed = (r * Math.cos(angleDC) - gamepad1.right_stick_x)*acc;
        final double rightBackSpeed = (r * Math.sin(angleDC) + gamepad1.right_stick_x)*acc;
        //INTAKE
        if(gamepad1.right_trigger > 0) mechanisms.moveIntake(0.68*gamepad1.right_trigger);
        else if(gamepad1.right_bumper) mechanisms.moveIntake(-0.7);
        else if(limitSwitch.isPressed()) mechanisms.moveIntake(0.0);
        else mechanisms.moveIntake(0.1);

        if(gamepad1.right_bumper || gamepad2.right_bumper) intakePower = -1.0;
        //CAROUSEL
        if(gamepad1.dpad_right || gamepad2.dpad_right) {
            mechanisms.rotateCarousel(0.5);
        }
        else if(gamepad1.dpad_left || gamepad2.dpad_left) {
            mechanisms.rotateCarousel(-0.5);
        }
        else {
            mechanisms.rotateCarousel(0.0);
        }
        //ARM
        if(gamepad1.left_trigger > 0 || gamepad2.left_trigger > 0) armPos = Range.clip(armPos+gamepad1.left_trigger*4,0,1000);
        else if(gamepad1.left_trigger > 0 || gamepad2.left_trigger > 0) armPos = Range.clip(armPos+gamepad2.left_trigger*4,0,1000);
        if(gamepad1.left_bumper || gamepad2.left_bumper) armPos = Range.clip(armPos-4,0,1000);
        if(gamepad1.x || gamepad2.x) {
            presetState = PRESETSTATE.ALLIANCE_FIRST;
        }
        else if(gamepad2.y) {
            presetState = PRESETSTATE.ALLIANCE_THIRD;
        }
        else if(gamepad1.a || gamepad2.a) {
            presetState = PRESETSTATE.ALLIANCE_THIRD;
        }

//SERVOS
        if(gamepad1.dpad_down || gamepad2.dpad_down) releaseServoPos = Range.clip(releaseServoPos-0.006,releaseServo.MIN_POSITION,releaseServo.MAX_POSITION);
        else if(gamepad1.dpad_up || gamepad2.dpad_up) releaseServoPos = Range.clip(releaseServoPos+0.006,releaseServo.MIN_POSITION,releaseServo.MAX_POSITION);
        if(gamepad1.b || gamepad2.b) {
            presetState = PRESETSTATE.GOING_DOWN;
        }
        if(gamepad2.right_bumper) releaseServoPos = 0.83;
        if(presetState != PRESETSTATE.NO_PRESET) {
            armPower = 0.18;
            switch(presetState) {
                case ALLIANCE_FIRST: {
                    armPower = 0.12;
                    armPos = 875;
                    if(armDC.getCurrentPosition() >= 870) {
                        presetState = PRESETSTATE.NO_PRESET;
                    }
                    break;
                }
                case ALLIANCE_SECOND: {
                    armPos = 750;
                    if(armDC.getCurrentPosition() >= 750) {
                        presetState = PRESETSTATE.NO_PRESET;
                    }
                    break;
                }
                case ALLIANCE_THIRD: {
                    armPos = 595;
                    if(armDC.getCurrentPosition() >= 595) {
                        presetState = PRESETSTATE.NO_PRESET;
                    }
                    break;
                }
                case GOING_DOWN: {
                    armPower = 0.125;
                    armPos = 0;
                    releaseServoPos = 0.9;
                    if(armDC.getCurrentPosition() <= 5) {
                        presetState = PRESETSTATE.NO_PRESET;
                    }
                }
                default: {
                    break;
                }
            }
        }
        else {
            armPower = 0.5;
        }
// BOUNCE BOUNCE BOUNCE BOUNCE BOUNCE BOUNCE BOUNCE BOUNCE BOUNCE BOUNCE BOUNCE BOUNCE BOUNCE BOUNCE BOUNCE BOUNCE BOUNCE
        balanceServoPos = Range.clip((armDC.getCurrentPosition()-50)/1050.1,balanceServo.MIN_POSITION,balanceServo.MAX_POSITION);
        /* if(runtime.milliseconds() >= 85000 && runtime.milliseconds() <= 90000) blinkinPattern = RevBlinkinLedDriver.BlinkinPattern.BEATS_PER_MINUTE_LAVA_PALETTE;
        else if(presetState != PRESETSTATE.NO_PRESET) blinkinPattern = RevBlinkinLedDriver.BlinkinPattern.TWINKLES_PARTY_PALETTE;
        else if(distanceSensor.getDistance(DistanceUnit.MM) <= 80) {
            blinkinPattern = RevBlinkinLedDriver.BlinkinPattern.STROBE_GOLD;
            telemetry.addData("Distance Sensor","Freight Detected");
        }
        else blinkinPattern = RevBlinkinLedDriver.BlinkinPattern.BEATS_PER_MINUTE_RAINBOW_PALETTE;
        blinkin.setPattern(blinkinPattern); */

//MOTOR SET POWER
        leftDCFront.setPower(leftFrontSpeed); //Set all the motors to their corresponding powers/speeds
        rightDCFront.setPower(rightFrontSpeed);
        leftDCBack.setPower(leftBackSpeed);
        rightDCBack.setPower(rightBackSpeed);
        carouselLeft.setPower(carouselLeftPower);
        //intakeDC.setPower(intakePower);
        armDC.setTargetPosition((int) armPos);
        armDC.setPower(armPower);
        releaseServo.setPosition(releaseServoPos);
        balanceServo.setPosition(balanceServoPos);
        //mechanisms.maintainBalance(); //TODO: SEE IF THIS ACTUALLY WORKS
//TELEMETRY
        telemetry.addData("Status", "Looping"); //Add telemetry to show that the program is currently in the loop function
        telemetry.addData("Runtime", runtime.toString() + " Milliseconds"); //Display the runtime
        telemetry.addData("DCMotors", "leftFront (%.2f), rightFront (%.2f), leftBack (%.2f), rightBack(%.2f), carouselLeft(%.2f), carouselRight(%.2f), intakeDC(%.2f), armDC(%.2f)",
                leftFrontSpeed, rightFrontSpeed, leftBackSpeed, rightBackSpeed, carouselLeftPower, carouselRightPower, intakePower, armPos); //In (%.2f), the % means that special modifier is to follow, that modifier being .2f. In .2f, the .2 means to round to to digits after the decimal point, and the f means that the value to be rounded is a float.
        //(%.2f) is used here so that the displayed motor speeds aren't excessively long and don't cldfasdfasdtter(andy's one contribution) the screen.
        telemetry.addData("Servos","releaseServoPos(%.2f), balanceServoPos(%.2f)",releaseServoPos, balanceServoPos);
        telemetry.update(); //Updates the telemetry
    }

    @Override
    public void stop() {
        telemetry.addData("Status", "Stopped");
        telemetry.addData("Runtime","Total runtime was " + runtime.toString() + " Milliseconds");
        telemetry.update();
    }
}