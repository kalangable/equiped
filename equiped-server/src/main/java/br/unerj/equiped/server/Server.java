package br.unerj.equiped.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import lombok.val;

public class Server {

	private Selector selector;
	private ServerSocketChannel serverSocket;
	private StringBuffer stringBuffer = new StringBuffer();
	int tamanho = 1;

	public Server() throws IOException {
		this.selector = Selector.open();
		System.out.println( "Selector aberto: " + selector.isOpen() );
		this.serverSocket = ServerSocketChannel.open();
		val hostAddress = new InetSocketAddress( "localhost", 19000 );
		serverSocket.bind( hostAddress );
		serverSocket.configureBlocking( false );
		int ops = serverSocket.validOps();
		serverSocket.register( selector, ops, null );

	}

	public void run() {
		for ( ;; ) {
			System.out.println( "Waiting for select..." );
			int noOfKeys = 0;
			try {
				noOfKeys = selector.select();
			} catch ( IOException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println( "Number of selected keys: " + noOfKeys );

			String valor = new MySelector( selector, serverSocket, tamanho ).run();
			if ( valor == null ) {
				continue;
			}
			if ( valor.equals( ":" ) ) {
				tamanho = new Integer( stringBuffer.toString() );
				stringBuffer = new StringBuffer();
				continue;
			}
			stringBuffer.append( valor );
			System.out.println( stringBuffer );

			// new Thread( new MySelector( selector, serverSocket ) );
		}
	}

}
