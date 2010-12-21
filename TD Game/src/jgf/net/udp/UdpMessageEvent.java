/*
 */

package jgf.net.udp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 *
 * @author Vinicius
 */
public class UdpMessageEvent {
    private InetSocketAddress source;
    private ByteBuffer data;
    
    public UdpMessageEvent(InetSocketAddress source, ByteBuffer data)
    {
        this.source = source;
        this.data = data.asReadOnlyBuffer();
    }

    public InetSocketAddress getSource() {
        return source;
    }

    public ByteBuffer getData() {
        return data;
    }
}
