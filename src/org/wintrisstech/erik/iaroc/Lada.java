package org.wintrisstech.erik.iaroc;

import android.os.SystemClock;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import java.util.Random;
import org.wintrisstech.irobot.ioio.IRobotCreateAdapter;
import org.wintrisstech.irobot.ioio.IRobotCreateInterface;

/**
 * A Lada is an implementation of the IRobotCreateInterface, inspired by Vic's
 * awesome API. It is entirely event driven.
 *
 * @author Erik
 */
public class Lada extends IRobotCreateAdapter
{
    private static final String TAG = "Lada";
    private final Dashboard dashboard;
    /*
     * State variables:
     */
    private int speed = 100; // The normal speed of the Lada when going straight

    /**
     * States
     */
    private static enum State
    {
        STRAIGHT_FORWARD,
        //LEFT_BACKWARD,
        //RIGHT_BACKWARD,
        //STRAIGHT_BACKWARD,
        SPINNING,
//        SOFTLEFT,
//        SOFTRIGHT,
//        HARDLEFT,
//        HARDRIGHT,
        //TODO: Add states as needed, e.g., RIGHT_FORWARD, TURNING_LEFT, etc.
    }

    /**
     * Events
     */
    private static enum Event
    {
//        BACKUP_DONE,
//        RIGHT_BUMP,
//        LEFT_BUMP,
//        FRONT_BUMP,
        RedAndGreenBuoy,
//        ForceField,
//        RedBuoy,
//        GreenBuoy,
//        GreenBuoyAndForceField,
//        RedBuoyAndForceField,
//        NoIRSignal;
        //TODO: Add events as needed, e.g., RED_BUOY_FOUND, RED_BUOY_LOST, etc. 
    }
    private State presentState;
    private int heading = 0;
    private static final int howFarToGoBackWhenBumped = 200;
    private int howFarBacked = 0;
    private boolean backingUp = false;
    //TODO Add variableas as needed, e.g., boolean redBouyInSight, boolean greenBouyInSight, etc
    private Random rand = new Random();

    /**
     * Constructs a Lada, an amazing machine!
     *
     * @param ioio the IOIO instance that the Lada can use to communicate with
     * other peripherals such as sensors
     * @param create an implementation of an iRobot
     * @param dashboard the Dashboard instance that is connected to the Lada
     * @throws ConnectionLostException
     */
    public Lada(IOIO ioio, IRobotCreateInterface create, Dashboard dashboard) throws ConnectionLostException
    {
        super(create);
        this.dashboard = dashboard;
        dashboard.speak("LADA");
        dashboard.log("LADA");
        song(0, new int[]
                {
                    58, 10
                });
    }

    public void initialize() throws ConnectionLostException
    {
        dashboard.speak("Skating Monkeys");
        heading = 0;
        backingUp = false;
//        presentState = State.STRAIGHT_FORWARD;
        presentState = State.SPINNING;
        readSensors(SENSORS_GROUP_ID6);//Resets all counters in the Create to 0.
        driveDirect(speed, speed);
    }

    /**
     * This method is called repeatedly
     *
     * @throws ConnectionLostException
     */
    public void loop() throws ConnectionLostException
    {
        //SystemClock.sleep(100); // Comment out or adjust sleep time as needed
        readSensors(SENSORS_GROUP_ID6);
        int iRbyte = this.getInfraredByte();
//        heading += getAngle();
//        if (isBumpLeft() && isBumpRight())
//        {
//            dashboard.log("Bump front");
//            fireEvent(Event.FRONT_BUMP);
//        } else if (isBumpLeft())
//        {
//            dashboard.log("Bump left");
//            fireEvent(Event.LEFT_BUMP);
//        } else if (isBumpRight())
//        {
//            dashboard.log("Bump right");
//            fireEvent(Event.RIGHT_BUMP);
//        } else if (backingUp)
//        {
//            howFarBacked -= getDistance(); //
//            if (howFarBacked > howFarToGoBackWhenBumped)
//            {
//                dashboard.log("Done backup");
//                fireEvent(Event.BACKUP_DONE);
//            }
//        }
//        // TODO: extend this with beacon reading events ...
        if (iRbyte == 252)
        {
            dashboard.speak("Red and green buoy");
            fireEvent(Event.RedAndGreenBuoy);
        }
//            else if (iRbyte == 242)
//        {
//            dashboard.speak("force field");
//            fireEvent(Event.ForceField);
//        } else if (iRbyte == 244)
//        {
//            dashboard.speak("green buoy");
//            fireEvent(Event.GreenBuoy);
//        } else if (iRbyte == 248)
//        {
//            dashboard.speak("red buoy");
//            fireEvent(Event.RedBuoy);
//        } else if (iRbyte == 246)
//        {
//            dashboard.speak("Green buoy and force field");
//            fireEvent(Event.GreenBuoyAndForceField);
//        } else if (iRbyte == 250)
//        {
//            dashboard.speak("red buoy and force field");
//            fireEvent(Event.RedBuoyAndForceField);
//        } else if (iRbyte == 255)
//        {
//            dashboard.speak("no signal");
//            fireEvent(Event.NoIRSignal);
//        }

    }

    /**
     * Implementation of a Moore finite state machine. The next state is
     * determined by the present state and the input event. The output is
     * determined by the state only (and, unlike Mealy FSM, not by the state and
     * the input event).
     *
     * @param event the input event
     * @throws ConnectionLostException
     */
    private void fireEvent(Event event) throws ConnectionLostException
    {
        SystemClock.sleep(200);
        presentState = nextState(presentState, event); // transit to the new state 
        switch (presentState)
        {
            case STRAIGHT_FORWARD:
                dashboard.log("Straight forward");
                dashboard.speak("YEEE BOY");
                dashboard.log("Heading = " + heading);
                backingUp = false;
                driveDirect(speed, speed);
                break;
//            case LEFT_BACKWARD:
//                dashboard.log("Left backward");
//                dashboard.speak("going left backward");
//                backingUp = true;
//                howFarBacked = 0;
//                driveDirect(-speed, -speed / 4);
//                break;
//            case RIGHT_BACKWARD:
//                dashboard.log("Right backward");
//                dashboard.speak("going right backward");
//                backingUp = true;
//                howFarBacked = 0;
//                driveDirect(-speed / 4, -speed);
//                break;
//            case STRAIGHT_BACKWARD:
//                dashboard.log("Straight backward");
//                backingUp = true;
//                howFarBacked = 0;
//                if (rand.nextBoolean())
//                {
//                    dashboard.speak("going slightly right backward");
//                    driveDirect(-speed / 2, -speed);
//                } else
//                {
//                    dashboard.speak("going slightly left backward");
//                    driveDirect(-speed, -speed / 2);
//                }
//                break;
            case SPINNING:
                dashboard.speak("spinning");
                driveDirect(-10, 10);
                break;
//            case SOFTLEFT:
//                dashboard.speak("soft left");
//                driveDirect(75, 100);
//                break;
//            case SOFTRIGHT:
//                dashboard.speak("soft right");
//                driveDirect(100, 75);
//                break;
//            case HARDLEFT:
//                dashboard.speak("hard left");
//                driveDirect(25, 75);
//                break;
//            case HARDRIGHT:
//                dashboard.speak("hard right");
//                driveDirect(75, 25);
//                break;
            default:
                // should never get here
                dashboard.log("What the ?!! am I doing here?");
        }
    }

    /**
     * Implementation of a state transition table.
     *
     * @param state the present state
     * @param event the input event
     * @return the next state
     */
    private State nextState(State state, Event event)
    {
        //TODO: Add state transitions for the new states and events
        switch (state)
        {
            case STRAIGHT_FORWARD:
                switch (event)
                {
                    case RedAndGreenBuoy:
                        return State.STRAIGHT_FORWARD;
//                    case ForceField:
//                        return State.STRAIGHT_FORWARD;
//                    case RedBuoy:
//                        return State.SOFTLEFT;
//                    case GreenBuoy:
//                        return State.SOFTRIGHT;
//                    case GreenBuoyAndForceField:
//                        return State.HARDRIGHT;
//                    case RedBuoyAndForceField:
//                        return State.HARDLEFT;
//                    case NoIRSignal:
//                        return State.SPINNING;
                }
//                    case BACKUP_DONE:
//                        //Should never get here
//                        dashboard.log("What the ?!! am I doing here?");
//                        return state; // no transition
//                    case FRONT_BUMP:
//                        return State.STRAIGHT_BACKWARD;
//                    case LEFT_BUMP:
//                        return State.RIGHT_BACKWARD;
//                    case RIGHT_BUMP:
//                        return State.LEFT_BACKWARD;
//                }
//            case LEFT_BACKWARD:
//                switch (event)
//                {
//                    case BACKUP_DONE:
//                        return State.STRAIGHT_FORWARD;
//                    case FRONT_BUMP:
//                        return State.STRAIGHT_BACKWARD;
//                    case LEFT_BUMP:
//                        return State.RIGHT_BACKWARD;
//                    case RIGHT_BUMP:
//                        return State.LEFT_BACKWARD;
//                }
//            case RIGHT_BACKWARD:
//                switch (event)
//                {
//                    case BACKUP_DONE:
//                        return State.STRAIGHT_FORWARD;
//                    case FRONT_BUMP:
//                        return State.STRAIGHT_BACKWARD;
//                    case LEFT_BUMP:
//                        return State.RIGHT_BACKWARD;
//                    case RIGHT_BUMP:
//                        return State.LEFT_BACKWARD;
//                }
//            case STRAIGHT_BACKWARD:
//                switch (event)
//                {
//                    case BACKUP_DONE:
//                        return State.STRAIGHT_FORWARD;
//                    case FRONT_BUMP:
//                        return State.STRAIGHT_BACKWARD;
//                    case LEFT_BUMP:
//                        return State.RIGHT_BACKWARD;
//                    case RIGHT_BUMP:
//                        return State.LEFT_BACKWARD;
//                }
            case SPINNING:
                switch (event)
                {
                    case RedAndGreenBuoy:
                        return State.STRAIGHT_FORWARD;
//                    case ForceField:
//                        return State.STRAIGHT_FORWARD;
//                    case RedBuoy:
//                        return State.SOFTLEFT;
//                    case GreenBuoy:
//                        return State.SOFTRIGHT;
//                    case GreenBuoyAndForceField:
//                        return State.HARDRIGHT;
//                    case RedBuoyAndForceField:
//                        return State.HARDLEFT;
//                    case NoIRSignal:
//                        return State.SPINNING;
                }
//            case SOFTLEFT:
//                switch (event)
//                {
//                    case RedAndGreenBuoy:
//                        return State.STRAIGHT_FORWARD;
//                    case ForceField:
//                        return State.STRAIGHT_FORWARD;
//                    case RedBuoy:
//                        return State.SOFTLEFT;
//                    case GreenBuoy:
//                        return State.SOFTRIGHT;
//                    case GreenBuoyAndForceField:
//                        return State.HARDRIGHT;
//                    case RedBuoyAndForceField:
//                        return State.HARDLEFT;
//                    case NoIRSignal:
//                        return State.SPINNING;
//                }
//            case SOFTRIGHT:
//                switch (event)
//                {
//                    case RedAndGreenBuoy:
//                        return State.STRAIGHT_FORWARD;
//                    case ForceField:
//                        return State.STRAIGHT_FORWARD;
//                    case RedBuoy:
//                        return State.SOFTLEFT;
//                    case GreenBuoy:
//                        return State.SOFTRIGHT;
//                    case GreenBuoyAndForceField:
//                        return State.HARDRIGHT;
//                    case RedBuoyAndForceField:
//                        return State.HARDLEFT;
//                    case NoIRSignal:
//                        return State.SPINNING;
//                }
//            case HARDLEFT:
//                switch (event)
//                {
//                    case RedAndGreenBuoy:
//                        return State.STRAIGHT_FORWARD;
//                    case ForceField:
//                        return State.STRAIGHT_FORWARD;
//                    case RedBuoy:
//                        return State.SOFTLEFT;
//                    case GreenBuoy:
//                        return State.SOFTRIGHT;
//                    case GreenBuoyAndForceField:
//                        return State.HARDRIGHT;
//                    case RedBuoyAndForceField:
//                        return State.HARDLEFT;
//                    case NoIRSignal:
//                        return State.SPINNING;
//                }
//            case HARDRIGHT:
//                switch (event)
//                {
//                    case RedAndGreenBuoy:
//                        return State.STRAIGHT_FORWARD;
//                    case ForceField:
//                        return State.STRAIGHT_FORWARD;
//                    case RedBuoy:
//                        return State.SOFTLEFT;
//                    case GreenBuoy:
//                        return State.SOFTRIGHT;
//                    case GreenBuoyAndForceField:
//                        return State.HARDRIGHT;
//                    case RedBuoyAndForceField:
//                        return State.HARDLEFT;
//                    case NoIRSignal:
//                        return State.SPINNING;
//                }
            default:
                //Should never get here
                dashboard.log("What the ?!! am I doing here?");
                return state; // no transition
        }
    }
}
//    public void stop() throws ConnectionLostException
//    {
//        driveDirect(0, 0); // stop the Create
//    }
//
//    private void smKeepGoing() throws ConnectionLostException
//    {
//        driveDirect(300, 300);
//    }
//
//    private void smBackUp() throws ConnectionLostException
//    {
//        driveDirect(-300, -300);
//    }
//
//    private void smDrive(int left, int right) throws ConnectionLostException
//    {
//        driveDirect(right, left);
//    }
//
//    private void smBeaconSearcher() throws ConnectionLostException
//    {
//        // readSensors(SENSORS_GROUP_ID6);
//        SystemClock.sleep(300);
//        dashboard.speak("bacon");
//        int iRbyte = this.getInfraredByte();
//        dashboard.log("irb=" + iRbyte);
//        if (iRbyte == 252)
//        {
//            dashboard.speak("both straight");
//            smDrive(100, 100);
//        } else if (iRbyte == 242)
//        {
//            dashboard.speak("Force straight");
//            smDrive(100, 100);
//        } else if (iRbyte == 244)
//        {
//            dashboard.speak("green right");
//            smDrive(100, 150);
//        } else if (iRbyte == 248)
//        {
//            dashboard.speak("red left");
//            smDrive(150, 100);
//        } else if (iRbyte == 246)
//        {
//            dashboard.speak("green force right slow");
//            smDrive(25, 75);
//        } else if (iRbyte == 250)
//        {
//            dashboard.speak("red force left slow");
//            smDrive(75, 25);
//        } else
//        {
//            dashboard.speak("poo");
//            smDrive(100, -100);
//        }
//    }
//}
