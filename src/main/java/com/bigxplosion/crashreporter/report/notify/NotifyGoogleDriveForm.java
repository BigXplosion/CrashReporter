package com.bigxplosion.crashreporter.report.notify;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bigxplosion.crashreporter.config.Config;
import com.bigxplosion.crashreporter.report.base.INotificationProvider;
import com.bigxplosion.crashreporter.util.Http;

public class NotifyGoogleDriveForm implements INotificationProvider {

	private static final Pattern FORM_PATTERN = Pattern.compile("<form action=\"([^\"]+)\" ");
	private static final Pattern INPUT_PATTERN = Pattern.compile("<input type=\"hidden\" name=\"([^\"]+)\" ");

	@Override
	public void notify(String title, String text, String link) throws NotifyException {

		if (!Config.formEnabled)
			return;

		String[] forms = Config.formURL.trim().split(",");

		for (String f : forms) {

			String form;
			try {
				form = Http.post(new URL(f), null).text;
			} catch (Throwable e) {
				throw new NotifyException(e);
			}

			Matcher matcher;

			matcher = FORM_PATTERN.matcher(form);
			matcher.find();
			String formTarget = matcher.group(1);
			if (formTarget == null) {
				throw new NotifyException("Could not find the main form");
			}

			matcher = INPUT_PATTERN.matcher(form);
			matcher.find();
			String input = matcher.group(1);
			if (input == null) {
				throw new NotifyException("Could not find a text question on the form");
			}

			Map<String, String> postvars = new HashMap<String, String>(2);
			postvars.put(input, text);

			try {
				Http.post(new URL(formTarget), postvars);
			} catch (Throwable e) {
				throw new NotifyException(e);
			}
		}
	}
}
