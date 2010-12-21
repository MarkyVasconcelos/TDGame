package jgf.net.udp;

public interface UdpMessengerListener {
    void messageArrived(UdpMessageEvent evt);
    void messengerClosed();
}
