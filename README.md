## Packet-Sniffer
|Java Core|Maven|Batch Scripting|
|---|---|---|
---
### What is a Packet Sniffer?

A Packet Sniffer is a tool that can intercept and log traffic that passes over a computer network or part of a network.
> Packet capturing helps to diagnose and investigate network problems like congestion. Helps to filter network traffic. 
Discovering network misuse, vulnerability, malware, etc.
Sniffers in an unethical way are used to steal confidential information exchanged between source and destination.

---
## Overview:

- Packet Sniffer that's using Npcap (Nmap Project's packet sniffing and sending library for windows) through 
Pcap4j (which is a java library for capturing, crafting and sending packets).
> So basically i'm using the Pcap4j Java library as a Maven Dependency to hook in to the native Npcap.

> **Note:** Pcap4j wraps a native packet capture library (libpcap, WinPcap, or Npcap) via JNA and provides you Java-Oriented API's

- This program lists the available network devices and gives you an option to choose which device you're going to run the packet capture on.
- A Hanlde is then created -> which is used to listen for packets.
- Once the network device has been selected and opened, a PacketListener is created, which defines how to handle the packets
once they are received.
- The program then tells the opened device to loop and process packets using the listener I defined.
> I'm capturing 50 packets.

- The Packet TimeStamp along with the Packet's Raw **Hex Stream** gets printed to the console.
- I've Integrated the **Hex Packet Decoder API** with this program, which then allows me to decode the packet Hex Stream after extracting it from
the packet, then the decoded packet data gets printed to the console underneath it's Hex Stream.

---

## Additional Information:

### Packet Sniffer JAR

- Contained in the target folder in this projects repo, you'll find already packaged JAR files (2 JAR's).
- Remove it and rebuild the project (enter this command in the project directory): mvn clean package 
> NOTE: I've added the JAR's just for reference purposes.
> The same two JAR files should appear once you've build the project successfuly.

The JAR's:
- pcap-1.0.0.jar
- uber-pcap-1.0.0.jar

> Use the uber jar when running this program through the jar file with a script, cmd etc.

### Batch Script

- Contained in this projects repo, you'll notice an already made batch script as well.
- Edit the script with notepad/notepad++
- Copy the location of the Packet Sniffer JAR once it has been successfuly build.
- Place the path in the {place_holder) field and save the script.

> Now you're ready to go!!!

https://user-images.githubusercontent.com/81378094/201360248-d59a3bbe-9a1d-4e19-9d1e-b810d1d4b19a.mp4
