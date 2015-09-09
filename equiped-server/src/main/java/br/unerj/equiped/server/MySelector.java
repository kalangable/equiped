package br.unerj.equiped.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import lombok.val;

public class MySelector {

	private Selector selector;
	private ServerSocketChannel serverSocket;
	private Set selectedKeys;
	private Iterator iter;
	private StringBuilder stringBuffer;
	private int bufferSize;

	public MySelector( Selector selector, ServerSocketChannel serverSocket, int bufferSize ) {
		this.selector = selector;
		this.serverSocket = serverSocket;
		this.selectedKeys = selector.selectedKeys();
		this.iter = selectedKeys.iterator();
		this.bufferSize = bufferSize;
	}

	public String run() {
		int v = 0;
		String retorno = null;
		while ( iter.hasNext() ) {
			System.out.println( v++ );
			val ky = (SelectionKey)iter.next();
			if ( ky.isAcceptable() ) {
				try {
					accept();
				} catch ( IOException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if ( ky.isReadable() ) {
				try {
					retorno = read( ky );
				} catch ( IOException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// } else if ( ky.isWritable() ) {
				// try {
				// client.socket().getOutputStream().write( 10 );
				// } catch ( IOException e ) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
			}

			iter.remove();

		} // end while loop
		return retorno;
	}

	private String read( SelectionKey ky ) throws IOException {
		return new String( readBuffer( (SocketChannel)ky.channel() ) );
	}

	private byte[] readBuffer( SocketChannel client ) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate( bufferSize );
		client.read( buffer );
		return buffer.array();
	}

	private void accept() throws IOException {
		// Accept the new client connection
		SocketChannel client = null;
		client = serverSocket.accept();
		client.configureBlocking( false );
		client.register( selector, SelectionKey.OP_READ );
		System.out.println( "Accepted new connection from client: " + client );
	}

}
