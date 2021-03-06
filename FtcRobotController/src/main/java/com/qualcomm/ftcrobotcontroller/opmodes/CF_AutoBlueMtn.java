package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// PushBotAuto
//

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Provide a basic autonomous operational mode that uses the left and right
 * drive motors and associated encoders implemented using a state machine for
 * the Push Bot.
 *
 * @author Ryley Hindman
 * @version 2015-08-01-06-01
 */
public class CF_AutoBlueMtn extends CF_Hardware

{
    //--------------------------------------------------------------------------
    //
    // PushBotAuto
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instatiated.
     */
    public CF_AutoBlueMtn()

    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    } // PushBotAuto

    //--------------------------------------------------------------------------
    //
    // start
    //
    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     *
     * The system calls this member once when the OpMode is enabled.
     */
    @Override public void start ()

    {
        //
        // Call the PushBotHardware (super/base class) start method.
        //
        super.start ();

        //
        // Reset the motor encoders on the drive wheels.
        //
        reset_drive_encoders ();

    } // start

    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Implement a state machine that controls the robot during auto-operation.
     * The state machine uses a class member and encoder input to transition
     * between states.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()

    {
        switch (v_state)
        {
            //
            // Synchronize the state machine and hardware.
            //
            case 0:
                //
                // Reset the encoders to ensure they are at a known good value.
                //
                reset_drive_encoders ();

                //
                // Transition to the next state when this method is called again.
                //
                v_state++;

                break;
            //
            // Drive forward until the encoders exceed the specified values.
            //
            case 1:
                //
                // Tell the system that motor encoders will be used.  This call MUST
                // be in this state and NOT the previous or the encoders will not
                // work.  It doesn't need to be in subsequent states.
                //
                run_using_encoders ();

                //
                // Start the drive wheel motors at full power.
                //
                set_drive_power (0.15f, 0.15f);

                // Have the motor shafts turned the required amount?
                //
                // If they haven't, then the op-mode remains in this state (i.e this
                // block will be executed the next time this method is called).
                //5000 revs = 31 in

                if (have_drive_encoders_reached (1774, 483))
                {
                    //
                    // Reset the encoders to ensure they are at a known good value.
                    //
                    reset_drive_encoders ();

                    //
                    // Stop the motors.
                    //
                    set_drive_power (0.0f, 0.0f);

                    //
                    // Transition to the next state when this method is called
                    // again.
                    //
                    v_state++;
                }
                break;
            //
            // Wait...
            //
            case 2:
                if (have_drive_encoders_reset())
                {
                    v_state++;
                }
                break;
            case 3:
                run_using_encoders();
                set_drive_power(0.25f, 0.25f);

                if(have_drive_encoders_reached(12799, 12799)){
                    reset_drive_encoders();
                    set_drive_power(0.0f, 0.0f);
                    v_state++;

                }

                break;

            case 4:
                if(have_drive_encoders_reset())
                {
                    v_state++;
                }
                break;
            case 5:
                run_using_encoders();
                set_drive_power(0.50f, 0.0f);

                if(have_drive_encoders_reached(1288, 0)){
                    reset_drive_encoders();

                    set_drive_power(0.0f, 0.0f);
                    v_state++;
                }

                break;
            case 6:
                if(have_drive_encoders_reset()) {
                    v_state++;
                }
                break;

            case 7:
                run_using_encoders();
                set_drive_power(0.25f, 0.25f);

                if(have_drive_encoders_reached(1288, 1288)) {
                    reset_drive_encoders();

                    set_drive_power(0.0f, 0.0f);
                    v_state++;
                }
                break;
            case 8:
                if(have_drive_encoders_reset()) {
                    v_state++;
                }
                break;
            case 9:
                run_using_encoders();
                set_drive_power(-0.25f, 0.25f);

                if(have_drive_encoders_reached(-2000, 2000)) {
                    reset_drive_encoders();
                    set_drive_power(0.0f, 0.0f);
                    v_state++;
                }
                break;
            case 10:
                if(have_drive_encoders_reset()) {
                    v_state++;
                }
                break;
            case 11:
                run_using_encoders();
                set_drive_power(0.25f, 0.25f);

                if(have_drive_encoders_reached(644, 644))
                {
                    reset_drive_encoders();
                    set_drive_power(0.0f, 0.0f);
                    v_state++;
                }
                break;
            case 12:
                if(have_drive_encoders_reset()) {
                    v_state++;
                }
                break;
            case 13:
                if (AimMotor != null)
                {
                    AimMotor.setChannelMode
                            (
                                    DcMotorController.RunMode.RESET_ENCODERS
                            );
                }
                if (AimMotor != null)
                {
                    AimMotor.setChannelMode
                            (
                                    DcMotorController.RunMode.RUN_USING_ENCODERS
                            );
                }
                AimMotor.setPower(0.25f);
                if (Math.abs(AimMotor.getCurrentPosition()) > 1091)
                {
                    AimMotor.setPower(0.00f);
                }
                AimMotor.setChannelMode
                        (
                                DcMotorController.RunMode.RESET_ENCODERS
                        );
                break;

            default:
                //
                // The autonomous actions have been accomplished (i.e. the state has
                // transitioned into its final state.
                //
                //v_state = 20;
                break;
        }
        // Realign the motors
        // I'm not quite sure where to put this method, but I'll figure it out.
        //cf_realign_encoders();

        //
        // Send telemetry data to the driver station.
        //

        // Get the encoder count for both the left and right encoders
        int cfLeftCount = a_left_encoder_count();
        int cfRightCount = a_right_encoder_count();

        //update_telemetry (); // Update common telemetry
        // If state = 20, that means it has gotten to the default state.
        telemetry.addData ("18", "State: " + v_state);
        //This updates the encoder state for the left encoder
        telemetry.addData("19", "Left Count: " + cfLeftCount);
        //This updates the encoder state for the right encoder
        telemetry.addData("20", "Right Count: " + cfRightCount);

        telemetry.addData("21", "Left Drive Power: " + GetDriveMotorPower1());

        telemetry.addData("22", "Right Drive Power: " + GetDriveMotorPower2());
    } // loop

    //--------------------------------------------------------------------------
    //
    // v_state
    //
    /**
     * This class member remembers which state is currently active.  When the
     * start method is called, the state will be initialized (0).  When the loop
     * starts, the state will change from initialize to state_1.  When state_1
     * actions are complete, the state will change to state_2.  This implements
     * a state machine for the loop method.
     */
    private int v_state = 0;

} // PushBotAuto
