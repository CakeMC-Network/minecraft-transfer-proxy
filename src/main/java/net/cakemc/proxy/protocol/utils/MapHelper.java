package net.cakemc.proxy.protocol.utils;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class MapHelper {
	public static <K, V> @Nullable K findKey(final Map<K, V> map, final V value) {
		final Map.Entry<K, V> entry = map.entrySet().stream()
		                                 .filter((entry1) -> entry1.getValue().equals(value))
		                                 .findFirst()
		                                 .orElse(null);
		return entry == null ? null : entry.getKey();
	}
}
