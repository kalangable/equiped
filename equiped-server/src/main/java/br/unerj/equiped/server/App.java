package br.unerj.equiped.server;

import java.io.IOException;

import lombok.val;

public class App
{
	public static void main( String[] args )
	{
		try {
			val server = new Server();
			server.run();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
