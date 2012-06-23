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
    private int speed = 500; // The normal speed of the Lada when going straight
    private int heading = 0;
    private long count = 0;
    private static final int howFarToGoBackWhenBumped = 50;
    private int howFarBacked = 0;
    private boolean backingUp = false;
    //TODO Add variableas as needed, e.g., boolean redBouyInSight, boolean greenBouyInSight, etc
    private Random rand = new Random();
    private boolean operationMaze = false;
    private boolean iRSearch = false;

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
        dashboard.speak("Lada");
        dashboard.log("Lada");
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
        
        
        readSensors(SENSORS_GROUP_ID6);//Resets all counters in the Create to 0.
//        driveDirect(speed, speed);
    }
    
    public void loop( ) throws ConnectionLostException
    {
        drive(speed, 0);
    }
    public void loop2() throws ConnectionLostException
    {
        readSensors(SENSORS_GROUP_ID6);
        int iRbyte = this.getInfraredByte();
        
        if (isBumpRight() && isBumpLeft())
        { // front bump
            dashboard.log("" + getDistance());
            // back up a bit
            for (int i = 0; i < howFarToGoBackWhenBumped / 5; i++)
            {
                dashboard.speak("bam boom");
                driveDirect(-speed, -speed);
                readSensors(SENSORS_GROUP_ID6);
            }
            dashboard.speak("thats far enough");
            dashboard.log(">" + getDistance());
            if (operationMaze)
            {
                // always turn the same direction after back up
                for (int j = 0; j < 12; j++) {
                    driveDirect( -speed, speed );
                    readSensors(SENSORS_GROUP_ID6);
                } 
            } else
            { // turn in random direction following back up
                if (rand.nextBoolean())
                {
                    for (int j = 0; j < 12; j++)
                    {
                        driveDirect(speed, -speed);
                        readSensors(SENSORS_GROUP_ID6);
                    }
                } else
                {
                    for (int i = 0; i < 12; i++)
                    {
                        driveDirect(-speed, speed);
                        readSensors(SENSORS_GROUP_ID6);
                    }
                }
            }
        } else
        { // side bump
            if (isBumpLeft())
            {
                for (int i = 0; i < howFarToGoBackWhenBumped / 2; i++)
                { // reverse to left
                    driveDirect(-50, -150);
                    dashboard.speak("bada boom");
                    readSensors(SENSORS_GROUP_ID6);
                }
            }
            if (isBumpRight())
            {
                for (int i = 0; i < howFarToGoBackWhenBumped / 2; i++)
                {// reverse to right
                    driveDirect(-150, -50);
                    dashboard.speak("ping pop");
                    readSensors(SENSORS_GROUP_ID6);
                }
            }
        }
        if (iRSearch && count % 100 == 0)
        { // call the beacon search every 100th iteration
            smBeaconSearcher();
        }
        //  go forward
        drive(speed, 0);
        count++;
    }

//    @Override
//    public void driveDirect(int left, int right) throws ConnectionLostException
//    {
//        super.driveDirect(right, left);
//    }
    private void smBeaconSearcher() throws ConnectionLostException
    {
        readSensors(SENSORS_GROUP_ID6);
        dashboard.speak("bacon");
        int iRbyte = this.getInfraredByte();
        dashboard.log("irb=" + iRbyte);
        //int startAngle = getAngle();
        for (int i = 0; iRbyte == 255 && i < 69; i++)
        {
            readSensors(SENSORS_GROUP_ID6);
            iRbyte = this.getInfraredByte();
            //int currentAngle = getAngle();
            
            //dashboard.log("currentAngle= " + currentAngle);
            driveDirect(200, -200);
        }
        if (iRbyte == 252)
        {
            dashboard.log("both straight");
            for (int i = 0; i < 10; i++)
            {
                driveDirect(100, 100);
            }
        } else if (iRbyte == 242)
        {
            dashboard.log("Force straight");
            for (int i = 0; i < 10; i++)
            {
                driveDirect(100, 100);
            }
            
        } else if (iRbyte == 244)
        {
            dashboard.log("green right");
            for (int i = 0; i < 10; i++)
            {
                driveDirect(200, 100);
            }
            
        } else if (iRbyte == 248)
        {
            dashboard.log("red left");
            for (int i = 0; i < 10; i++)
            {
                driveDirect(100, 200);
            }
            
        } else if (iRbyte == 246)
        {
            dashboard.log("green force right slow");
            for (int i = 0; i < 10; i++)
            {
                driveDirect(75, 25);
            }
            
        } else if (iRbyte == 250)
        {
            dashboard.log("red force left slow");
            for (int i = 0; i < 10; i++)
            {
                driveDirect(25, 75);
            }
            
        }
    }
}
