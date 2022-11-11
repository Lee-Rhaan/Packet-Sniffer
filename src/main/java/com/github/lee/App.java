package com.github.lee;

import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.NifSelector;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Packet Sniffer that's using Npcap (Nmap Project's packet sniffing and sending library for windows) through
 * Pcap4j (which is a java library for capturing, crafting and sending packets).
 *
 * Note: Pcap4j wraps a native packet capture library (libpcap, WinPcap, or Npcap) via JNA and provides you
 *       Java-Oriented API's
 */
public class App {

    private static final String url = "https://hpd.gasmi.net/api.php?format=text&data=";
    private static final int snapshotLength = 65536; //in bytes
    private static final int readTimeout = 50; //in milliseconds

    /**
     * List available network devices
     * @return chosen network device
     */
    static PcapNetworkInterface getNetworkDevice() {
        //The class that will store the network device we want to use for capturing
        PcapNetworkInterface device = null;
        //Pcap4j comes with a convenient method for listing and choosing a
        //network interface from the terminal
        try {
            //List the network devices available with a prompt
            device = new NifSelector().selectNetworkInterface();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return device;
    }

    /**
     * Captures 50 raw packets then decode the packets through integration with the HPD API
     * @param args
     * @throws PcapNativeException
     * @throws NotOpenException
     */
    public static void main( String[] args ) throws PcapNativeException, NotOpenException {
        PcapNetworkInterface device = getNetworkDevice();
        System.out.println("You chose: " + device);

        if (device == null) {
            System.out.println("No device chosen!!");
            System.exit(1);
        }

        //open the device and get a handle
        final PcapHandle handle;
        //set to promiscuous mode in order to capture incoming and outgoing packets (all the packets on this network)
        handle = device.openLive(snapshotLength, PromiscuousMode.PROMISCUOUS, readTimeout);

        //Create a listener that defines what to do with the received packets
        PacketListener listener = new PacketListener() {
            @Override
            public void gotPacket(Packet packet) {
                //Override the default gotPacket() function and process packet
                printPacketData(handle, packet);
                decodeHexStream(url.concat(retrievePacketHexStream(packet.toString())));
            }
        };

        //Tell the handle to loop using the listener we created
        try {
            int maxPackets = 50;
            handle.loop(maxPackets, listener);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        // Cleanup when complete
        handle.close();
    }

    /**
     * Prints packet timestamp + packet raw hex stream on console
     * @param handle can be used to listen for packets
     * @param packet a small segment of a larger message
     */
    public static void printPacketData(PcapHandle handle, Packet packet) {
        System.out.println("======================================================================================================================");
        System.out.println(handle.getTimestamp());//time packet was received or send
        System.out.println(packet);//packet raw hex stream
        System.out.println("======================================================================================================================");
    }

    /**
     * Retrieves Packet Raw Hex Stream value & removing unnecessary spacing between hex characters
     * @param packet a small segment of a larger message
     * @return packet hex stream
     */
    public static String retrievePacketHexStream(String packet) {
        int indexBehindColon = packet.indexOf(":") + 1;
        String hexStreamValue = packet.substring(indexBehindColon);
        return hexStreamValue.replaceAll(" ", "");
    }

    /**
     * Integrating Hex Packet Decoder API and decoding + printing raw packet hex streams into readable text output
     * on the console
     * @param url HPD API location
     */
    public static void decodeHexStream(String url) {
        try {
            System.setProperty("http.agent", "Chrome");
            URLConnection connection = new URL(url).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
