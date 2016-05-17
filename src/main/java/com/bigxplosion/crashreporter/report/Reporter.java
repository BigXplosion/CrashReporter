package com.bigxplosion.crashreporter.report;

import org.apache.logging.log4j.Level;

import net.minecraft.crash.CrashReport;

import com.bigxplosion.crashreporter.CrashReporter;
import com.bigxplosion.crashreporter.report.base.INotificationProvider;
import com.bigxplosion.crashreporter.report.base.IPastebinProvider;

public class Reporter {
	public static Reporter INSTANCE = new Reporter();

	public void report(CrashReport report) {
		report(report.getDescription(), report.getCompleteReport());
	}

	public void report(String title, String text) {
		String link = null;
		for (IPastebinProvider provider : ReportRegistry.getPastebinProviders()) {
			try {
				link = provider.paste(title, text);
				if (link.contains("Post limit, maximum pastes per 24h reached")) {
					continue;
				}
				break;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		if (link == null) {
			CrashReporter.INSTANCE.log.log(Level.ERROR, "No pastebin providers could handle the request");
			link = "<No link>";
		}

		CrashReporter.INSTANCE.log.log(Level.INFO, "Report posted to: " + link);

		for (INotificationProvider provider : ReportRegistry.getNotificationProviders()) {
			try {
				provider.notify(title, text, link);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
