package test;

import net.cakemc.proxy.RedirectProxyServer;
import net.cakemc.proxy.events.ProxyRedirectEvent;

public class ProxyTest {

	public static void main(String[] args) {
		RedirectProxyServer proxy = new RedirectProxyServer("127.0.0.1", 25577, "proxy-test");

		proxy.getEventManager().register(ProxyRedirectEvent.class, proxyRedirectEvent -> {
			proxyRedirectEvent.setCancelState(false);
		});

		proxy.start();
	}

}
