package src.app.Classes.Threads;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import src.app.Interfaces.INotifierThreads;

/**
 * NotifierThreads
 */
public class NotifierThreads extends Thread implements INotifierThreads {
    protected static final String SOLICITATIONS_MADE_CHANNELADDR = "230.0.0.1";
    protected static final String APPROVALS_MADE_CHANNELADDR = "230.0.0.2";
    protected static final String CONNECTIONS_MADE_CHANNELADDR = "230.0.0.3";

    private static final String FILE_PATH = "sd-tp01/src/main/java/src/app/Data/Stats.json";

    private DatagramSocket socket;
    private FileReader in;
    private volatile boolean running = true;
    private Map<String, InetAddress> channelGroups = new HashMap<>();

    public NotifierThreads() {
        super("[Notifier Threads]");

        try {
            this.socket = new DatagramSocket(12322);

            channelGroups.put("numberSolicitations", InetAddress.getByName(SOLICITATIONS_MADE_CHANNELADDR));
            channelGroups.put("numberApprovals", InetAddress.getByName(APPROVALS_MADE_CHANNELADDR));
            channelGroups.put("numberConnections", InetAddress.getByName(CONNECTIONS_MADE_CHANNELADDR));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the file is empty
     * 
     * @return true if the file is empty, false otherwise
     */
    private boolean isFileEmpty() {
        try {
            long fileSize = Files.size(Path.of(FILE_PATH));

            if (fileSize == 0) {
                return true;
            }
        } catch (IOException e) {
        }

        return false;
    }

    /**
     * Stops the thread from running
     */
    private void stopRunning() {
        this.running = false;
        this.interrupt(); // If the thread is sleeping, wake it up so that it can stop running
    }

    /**
     * Notifies all the solicitations made to the server
     * to anyone who tunes in to the channel
     */
    public void notifyAllSolicitationsMade() {
        // Read number of solicitations
        // and send a message to the channel
        // with the number of solicitations
        JSONParser jsonParser = new JSONParser();
        try {
            this.in = new FileReader(new File(Path.of(FILE_PATH).toString()));

            if (this.in == null || isFileEmpty()) {
                System.out.println("File is null");

                return;
            }

            JSONObject obj = (JSONObject) jsonParser.parse(this.in);
            this.in.close();

            JSONObject jsonStat = (JSONObject) obj.get("stats");
            int numberSolicitations = Integer.parseInt(jsonStat.get("numberSolicitations").toString());

            String message = "[Number of solicitations: " + numberSolicitations + "]";
            byte[] buffer = message.getBytes();

            InetAddress group = channelGroups.get("numberSolicitations");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 12323);
            socket.send(packet);
        } catch (Exception e) {
            System.out.println("Error notifying all solicitations made");
        } finally {
        }
    }

    /**
     * Notifies all the approvals made to the server
     * to anyone who tunes in to the channel
     */
    public void notifyAllApprovalsMade() {
        // Read number of approvals
        // and send a message to the channel
        // with the number of approvals
        JSONParser jsonParser = new JSONParser();
        try {
            this.in = new FileReader(new File(Path.of(FILE_PATH).toString()));

            if (this.in == null || isFileEmpty()) {
                System.out.println("File is null");

                return;
            }

            JSONObject obj = (JSONObject) jsonParser.parse(this.in);
            this.in.close();

            JSONObject jsonStat = (JSONObject) obj.get("stats");
            int numberApprovals = Integer.parseInt(jsonStat.get("numberApprovals").toString());

            String message = "[Number of approvals: " + numberApprovals + "]";
            byte[] buffer = message.getBytes();

            InetAddress group = channelGroups.get("numberApprovals");

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 12323);
            socket.send(packet);
        } catch (Exception e) {
            System.out.println("Error notifying all approvals made");
        } finally {
        }
    }

    /**
     * Notifies all the connections made to the server
     * to all the generals who tune in to the channel
     */
    public void notifyAllConnectionsMadeOnlyToGenerals() {
        // Read number of connections
        // and send a message to the channel
        // with the number of connections
        JSONParser jsonParser = new JSONParser();
        try {
            this.in = new FileReader(new File(Path.of(FILE_PATH).toString()));

            if (this.in == null || isFileEmpty()) {
                System.out.println("File is null");

                return;
            }

            JSONObject obj = (JSONObject) jsonParser.parse(this.in);
            this.in.close();

            JSONObject jsonStat = (JSONObject) obj.get("stats");
            int numberConnections = Integer.parseInt(jsonStat.get("numberConnections").toString());

            String message = "[Number of connections: " + numberConnections + "]";

            byte[] buffer = message.getBytes();

            InetAddress group = channelGroups.get("numberConnections");

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 12323);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error notifying all connections made");
        } finally {
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                notifyAllSolicitationsMade();
                notifyAllApprovalsMade();
                notifyAllConnectionsMadeOnlyToGenerals();

                if (!Thread.interrupted()) {
                    Thread.sleep(2000);
                } else {
                    this.stopRunning();
                    System.out.println("Notifier thread interrupted");
                }
            } catch (InterruptedException e) {
                this.stopRunning();
                System.out.println("Notifier thread interrupted");
            } catch (Exception e) {
                this.stopRunning();
                System.out.println("Error notifying all stats");
            }
        }
    }
}