package net.cakemc.proxy.protocol.api.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.*;

public final class AuthService {
	private static final AuthService AUTH_SERVICE = new AuthService();

	private final String profileEndpoint = "https://sessionserver.mojang.com/session/minecraft/profile/%PLACEHOLDER%?unsigned=false";
	private final String hasJoinedEndpoint = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=%PLACEHOLDER%&serverId=%SERVER%";

	// TODO: ... | maybe add speiger-collections? owo
	private final ExecutorService cachedLimitedExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
	                                                                             3L, TimeUnit.SECONDS,
	                                                                             new SynchronousQueue<>(),
	                                                                             runnable -> {
		                                                                             final Thread thread = new Thread(runnable);
		                                                                             thread.setName("AuthService-Thread-%d");
		                                                                             thread.setDaemon(true);
		                                                                             thread.setPriority(5);
		                                                                             return thread;
	                                                                             }
	);

	private final ThreadLocal<HttpClient> httpClient = ThreadLocal.withInitial(() -> HttpClient.newBuilder()
	                                                                                           .connectTimeout(Duration.ofSeconds(8L))
	                                                                                           .followRedirects(Redirect.NEVER)
	                                                                                           .build());

	private final ObjectMapper objectMapper = new ObjectMapper();

	public static AuthService get() {
		return AUTH_SERVICE;
	}

	public CompletableFuture<JoinRequest> byName(final String username, final String serverId) {
		return CompletableFuture.supplyAsync(() -> {
			JoinRequest request;
			try {
				request = objectMapper.readValue(httpClient.get().sendAsync(HttpRequest.newBuilder()
				                                                                       .version(Version.HTTP_2)
				                                                                       .uri(URI.create(hasJoinedEndpoint
					                                                                                        .replace("%PLACEHOLDER%", username)
					                                                                                        .replace("%SERVER%", serverId)))
				                                                                       .build(), BodyHandlers.ofString()).join().body(), JoinRequest.class);
			} catch (Throwable throwable) {
				request = null;
				throwable.printStackTrace();
			}
			return request;
		}, cachedLimitedExecutor);
	}

	public CompletableFuture<User> byUUID(final UUID uuid) {
		return byUUID(uuid.toString());
	}

	public CompletableFuture<User> byUUID(final String uuid) {
		return CompletableFuture.supplyAsync(() -> {
			User user;
			try {
				user = objectMapper.readValue(httpClient.get()
				                                        .sendAsync(HttpRequest.newBuilder()
				                                                              .version(Version.HTTP_2)
				                                                              .uri(URI.create(profileEndpoint.replace("%PLACEHOLDER%", uuid)))
				                                                              .build(), BodyHandlers.ofString()).join().body(), User.class);
			} catch (Throwable throwable) {
				user = null;
				throwable.printStackTrace();
			}
			return user;
		}, cachedLimitedExecutor);
	}

	public record JoinRequest(
		 @JsonAlias("id") String id,
		 @JsonAlias("properties") User.Properties[] properties) {
	}

	public record User(
		 @JsonAlias("id") String id,
		 @JsonAlias("name") String name,
		 @JsonAlias("properties") Properties[] properties,
		 @JsonAlias("profileActions") Object[] profileActions // TODO: ... | check if this is correct ~luzey
	) {

		@Override
		public String toString() {
			return String.format(
				 "User{id=%s, name=%s, properties=%s}",
				 this.id(),
				 this.name(),
				 Arrays.toString(this.properties()),
				 Arrays.toString(this.profileActions())
			);
		}

		public record Properties(
			 @JsonAlias("name") String name,
			 @JsonAlias("value") String value,
			 @JsonAlias("signature") String signature) {
			@Override
			public String toString() {
				return String.format(
					 "Properties{name=%s, value=%s, signature=%s}",
					 this.name(),
					 this.value(),
					 this.signature()
				);
			}
		}
	}
}
