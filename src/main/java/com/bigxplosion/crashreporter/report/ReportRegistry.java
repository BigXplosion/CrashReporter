package com.bigxplosion.crashreporter.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import com.bigxplosion.crashreporter.config.Config;
import com.bigxplosion.crashreporter.report.base.INotificationProvider;
import com.bigxplosion.crashreporter.report.base.IPastebinProvider;

public class ReportRegistry {

	private static Map<String, IPastebinProvider> pastebinProviders = Maps.newHashMap();
	private static Map<String, INotificationProvider> notificationProviders = Maps.newHashMap();

	public static void registerPastebinProvider(String id, IPastebinProvider provider) {
		if (pastebinProviders.containsKey(id)) {
			throw new IllegalArgumentException("Pastebin provider " + id + " already registered by " + pastebinProviders.get(id) + " when registering " + provider);
		}

		pastebinProviders.put(id, provider);
	}

	public static IPastebinProvider getPastebinProvider(String id) {
		return pastebinProviders.get(id);
	}

	public static List<IPastebinProvider> getPastebinProviders() {
		List<IPastebinProvider> providers = new ArrayList<IPastebinProvider>(pastebinProviders.size());

		IPastebinProvider preferred = getPastebinProvider(Config.preferredPastebin);
		if (preferred != null) {
			providers.add(preferred);
		}

		providers.addAll(pastebinProviders.values());

		return providers;
	}

	public static void registerNotificationProvider(String id, INotificationProvider provider) {
		if (notificationProviders.containsKey(id)) {
			throw new IllegalArgumentException("Notification provider " + id + " already registered by " + notificationProviders.get(id) + " when registering " + provider);
		}

		notificationProviders.put(id, provider);
	}

	public static INotificationProvider getNotificationProvider(String id) {
		return notificationProviders.get(id);
	}

	public static List<INotificationProvider> getNotificationProviders() {
		List<INotificationProvider> providers = new ArrayList<INotificationProvider>(notificationProviders.size());
		providers.addAll(notificationProviders.values());

		return providers;
	}

}
