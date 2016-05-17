package com.bigxplosion.crashreporter.report.pastebin;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.bigxplosion.crashreporter.report.base.IPastebinProvider;
import com.bigxplosion.crashreporter.util.Http;

public class Ubuntu implements IPastebinProvider {

	@Override
	public String paste(String title, String text) throws PasteException {
		Map<String, String> postvars = new HashMap<String, String>(3);
		postvars.put("syntax", "text");
		postvars.put("poster", "Crash Reporter");
		postvars.put("content", text);

		try {
			return Http.post(new URL("http://paste.ubuntu.com"), postvars).url;
		} catch (Throwable e) {
			throw new PasteException(e);
		}

	}
}
