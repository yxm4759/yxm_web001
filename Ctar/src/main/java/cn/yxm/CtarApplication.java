package cn.yxm;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CtarApplication {

	@Value("${project.Ctar.Socket.Client-duan}")
	private static Integer duan;
	
	public static boolean a = false;
	
	public static void main(String[] args) throws IOException {
		SpringApplication.run(CtarApplication.class, args);
		//ServerEcho();
	}

	public static void ServerEcho() throws IOException {
		System.err.println(duan);
		ServerSocket server = new ServerSocket(duan);
		System.err.println("等待连接");
		a = true;
		Socket socket = server.accept();
		System.err.println("连接成功");
		Scanner scan = new Scanner(socket.getInputStream());
		PrintStream out = new PrintStream(socket.getOutputStream());
		while(true) {	
			if(scan.hasNext()) {
				String str = scan.next();
				System.err.println("客户端说: "+str);
				if("exit".equalsIgnoreCase(str)) {
					break;
				}else {
					out.print(str);
				}
			}
			
		}
		System.err.println(123456);
	}

}
