import com.sun.corba.se.spi.activation.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
//This class represents the server application
public class ServerApp
{
    private Calendar cal;
    private DateFormat format;

    public static void main(String[] args) throws Exception
    {
        int dprop;  // ms
        int dtrans; // ms per byte
        switch(args.length){
            case 1 :
                System.out.println("Invalid arguments: requires specifying a " +
                        "dprop AND a dtrans.");
                dprop = 1000; dtrans = 200;
                System.out.println("Setting dprop = " + dprop +
                        " and dtrans = " + dtrans);
                break;
            case 2 :
                try{
                    dprop = Integer.parseInt(args[0]);
                    dtrans = Integer.parseInt(args[1]);
                } catch(Exception e){
                    System.out.println(e.getMessage());
                    dprop = 1000; dtrans = 200;
                    System.out.println("Setting dprop = " + dprop +
                            " and dtrans = " + dtrans);
                }
            default: dprop = 1000; dtrans = 200;
        }
        ServerApp s = new ServerApp();



        //create a new transport layer for server (hence true) (wait for client)
        TransportLayer transportLayer = new TransportLayer(true, dprop, dtrans);


        while( true )
        {
            //receive message from client, and send the "received" message back.
            byte[] byteArray = transportLayer.receive();
            //if client disconnected
            if(byteArray==null)
                break;
            String str = new String ( byteArray );
            System.out.println( str );
            String line = s.getCurTime() + "received";
            byteArray = line.getBytes();
            transportLayer.send( byteArray );
        }
    }

    public ServerApp(){
        this.cal = Calendar.getInstance();
        this.format = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        System.out.println(getCurTime());
    }

    public String getCurTime(){
        return (format.format(cal.getTime()));
    }
}
