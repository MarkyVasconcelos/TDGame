package jgf.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class UdpMessenger {
	private DatagramSocket socket = null;
	private InetSocketAddress remoteHost = null;
	private Set<UdpMessengerListener> listeners = new HashSet<UdpMessengerListener>();

	public synchronized void open(InetSocketAddress remoteHost,
			boolean daemonThread) throws SocketException {
		if (isOpen()) {
			return;
		}
		this.remoteHost = remoteHost;

		socket = new DatagramSocket();
		socket.setReuseAddress(true);
		socket.connect(remoteHost);

		startListening(daemonThread);
	}

	public synchronized void open(InetSocketAddress remoteHost)
			throws SocketException {
		open(remoteHost, true);
	}

	public synchronized void open(int port, boolean daemonThread)
			throws SocketException {
		if (isOpen()) {
			return;
		}
		socket = new DatagramSocket(port);
		startListening(daemonThread);
	}

	public synchronized void open(int port) throws SocketException {
		open(port, true);
	}

	public synchronized void send(ByteBuffer data) throws SocketException {
		if (remoteHost == null) {
			throw new IllegalStateException("Not bound to remote host!");
		}
		send(remoteHost, data);
	}

	public synchronized void send(InetSocketAddress remoteHost, ByteBuffer data)
			throws SocketException {
		if (!isOpen()) {
			throw new IllegalStateException("UDP service not openned!");
		}
		try {
			byte[] buffer = new byte[data.remaining()];
			data.get(buffer);
			socket.send(new DatagramPacket(buffer, buffer.length, remoteHost));
		} catch (SocketException e) {
			throw e;
		} catch (IOException e) {
			throw new RuntimeException("Unexpected I/O error!", e);
		}
	}

	public synchronized void close() {
		if (isOpen()) {
			remoteHost = null;
			socket.close();
			socket = null;
			for (UdpMessengerListener listener : listeners) {
				listener.messengerClosed();
			}
		}
	}

	public synchronized void addListener(UdpMessengerListener listener) {
		listeners.add(listener);
	}

	public synchronized void removeListener(UdpMessengerListener listener) {
		listeners.remove(listener);
	}

	private synchronized void fireMessageArrived(DatagramPacket packet) {
		ByteBuffer buffer = ByteBuffer.allocate(packet.getLength());
		buffer.put(packet.getData(), 0, packet.getLength()).flip();

		UdpMessageEvent event = new UdpMessageEvent(new InetSocketAddress(
				packet.getAddress(), packet.getPort()), buffer);

		for (UdpMessengerListener listener : listeners) {
			listener.messageArrived(event);
		}
	}

	private void startListening(boolean daemon) {
		Thread t = new Thread(new Watcher(), "UDP listener");
		t.setDaemon(daemon);
		t.start();
	}

	private class Watcher implements Runnable {
		public void run() {
			try {
				while (isOpen()) {
					byte buf[] = new byte[8096];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					socket.receive(packet);
					fireMessageArrived(packet);
				}
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).fine(e.getMessage());
			} finally {
				close();
			}
		}
	}

	public synchronized boolean isOpen() {
		return socket != null;
	}

	public synchronized InetSocketAddress getRemoteAddress() {
		return remoteHost;
	}

	public synchronized int getBindPort() {
		return socket.getLocalPort();
	}
}
