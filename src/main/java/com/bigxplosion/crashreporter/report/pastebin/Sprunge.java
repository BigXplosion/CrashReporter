package com.bigxplosion.crashreporter.report.pastebin;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.bigxplosion.crashreporter.report.base.IPastebinProvider;
import com.bigxplosion.crashreporter.util.Http;

public class Sprunge implements IPastebinProvider {

	@Override
	public String paste(String title, String text) throws PasteException {
		Map<String, String> postvars = new HashMap<String, String>(1);
		postvars.put("sprunge", text);

		try {
			return Http.post(new URL("http://sprunge.us"), postvars).text.trim().replaceAll("\n", "");
		} catch (Throwable e) {
			throw new PasteException(e);
		}
	}
}
