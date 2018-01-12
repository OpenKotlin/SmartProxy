package me.smartproxy.tunnel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class RawTunnel extends Tunnel {

	private static final String TAG = RawTunnel.class.getSimpleName();

	public RawTunnel(InetSocketAddress serverAddress, Selector selector) throws Exception{
		super(serverAddress,selector);
	}
	
	public RawTunnel(SocketChannel innerChannel, Selector selector) {
		super(innerChannel, selector);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onConnected(ByteBuffer buffer) throws Exception {
		onTunnelEstablished();
//		Log.d(TAG,"onConnected:"+new String(buffer.array()));
	}

	@Override
	protected void beforeSend(ByteBuffer buffer) throws Exception {
		// TODO Auto-generated method stub
//		Log.d(TAG,"beforeSend:"+new String(buffer.array()));
		
	}

	@Override
	protected void afterReceived(ByteBuffer buffer) throws Exception {
		// TODO Auto-generated method stub
//		Log.d(TAG,"afterReceived:"+new String(buffer.array()));
	}

	@Override
	protected boolean isTunnelEstablished() {
		return true;
	}

	@Override
	protected void onDispose() {
		// TODO Auto-generated method stub
		
	}

}
