使用 Spring Websocket
---------------------
開發環境：

* Maven
* Tomcat7
* Spring 4.0.5
* GWT 2.6.1 + SDM

**注意：**這是 trial & error 硬幹式精簡的結果，不明其究。
（我跟 Spring 不熟啊...... [逃]）


### pom.xml ###
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-websocket</artifactId>
		<version>${org.springframework.version}</version>
	</dependency>

其他 Spring 的 dependency 貌似都不用加。


### web.xml ###
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:springContext.xml</param-value>
	</context-param> 
	
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
	
	<servlet>
		<servlet-name>springDispatcherServlet</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextClass</param-name>
			<param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>springDispatcherServlet</servlet-name>
		<url-pattern>/mvc/*</url-pattern>
	</servlet-mapping>

`<servlet-mapping>` 裡頭的 `<url-pattern>` 可以自訂，當然後續 `ws://` 也要跟著對應。


### springContext.xml ###
以 web.xml（`contextConfigLocation`）的設定，放在 `/resources/` 下

	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:context="http://www.springframework.org/schema/context"
		xsi:schemaLocation="
			http://www.springframework.org/schema/beans 
			http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
			http://www.springframework.org/schema/context 
			http://www.springframework.org/schema/context/spring-context-3.2.xsd">
	   
		<context:annotation-config />
		<bean id="webSocketServer" class="montyPan.gxt.generator.server.EchoWebSocketServer" />
	</beans>
	
後續對 `ApplicationContext.getBean("webSocketServer")` 就可以取得 `EchoWebSocketServer` 的 instance。
	
	
### EchoWebSocketServer ###
	package montyPan.gxt.generator.server;

	import java.util.ArrayList;
	import java.util.List;

	import org.springframework.web.socket.CloseStatus;
	import org.springframework.web.socket.TextMessage;
	import org.springframework.web.socket.WebSocketSession;
	import org.springframework.web.socket.config.annotation.EnableWebSocket;
	import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
	import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
	import org.springframework.web.socket.handler.TextWebSocketHandler;

	@EnableWebSocket
	public class EchoWebSocketServer extends TextWebSocketHandler implements WebSocketConfigurer {
		private List<WebSocketSession> list = new ArrayList<WebSocketSession>();
			
		@Override
		public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			session.sendMessage(message);
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			list.add(session);
			System.out.println("session.isOpen()=" + session.isOpen());
			System.out.println("session.getRemoteAddress()=" + session.getRemoteAddress());
			System.out.println("session.getUri()=" + session.getUri());
			System.out.println("session.size()=" + list.size());
		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
			System.out.println("status=" + status.toString());
			list.remove(session);
		}

		public void sendToClient() throws Exception {
			System.out.println("==== send to client ====");
			for (WebSocketSession session : list) {
				if (session.isOpen()) {
					session.sendMessage(new TextMessage(Math.random() + ""));
				}
			}
		}

		@Override
		public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
			registry.addHandler(this, "/echo");
		}
	}

結合 `web.xml` 與上頭 `registerWebSocketHandlers()` 的設定，
client 連線的網址就是 `ws://HOST_NAME/APP_NAME/mvc/echo`。