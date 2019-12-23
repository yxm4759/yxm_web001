package cn.yxm.socket;

import java.io.IOError;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@Component
@ServerEndpoint(value="/websocket")
public class MyWebSocket {
	private static int onlineCount = 0;
	private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<>();
	private Session session;
	
	@OnOpen
	public void onOpen(Session session) throws IOException {
		this.session = session;
		webSocketSet.add(this);
		addOnlineCount();
		System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
	}

	@OnClose
	public void onClose() {
		webSocketSet.remove(this);
		subOnlineCount();
		System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("来自客户端的消息:" + message);
		for (MyWebSocket myWebSocket : webSocketSet) {
			try {
				myWebSocket.sendMessage("我"+message);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("发生错误");
		error.printStackTrace();
	}
	
	public static synchronized void subOnlineCount() {
		MyWebSocket.onlineCount--;
	}

	private void sendMessage(String string) throws IOException {
		this.session.getBasicRemote().sendText(string);
		
		
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		MyWebSocket.onlineCount++;
		
	}
	
	public static void sendInfo(String message) {
		for (MyWebSocket myWebSocket : webSocketSet) {
			try {
				myWebSocket.sendMessage(message);
			} catch (Exception e) {
				continue;
			}
		}
	}
}
