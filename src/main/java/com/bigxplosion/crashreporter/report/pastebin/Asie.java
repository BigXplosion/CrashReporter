package com.bigxplosion.crashreporter.report.pastebin;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.bigxplosion.crashreporter.report.base.IPastebinProvider;
import com.bigxplosion.crashreporter.util.Http;

public class Asie implements IPastebinProvider {

	@Override
	public String paste(String title, String text) throws PasteException {
		Map<String, String> postvars = new HashMap<String, String>(1);
		postvars.put("paste", text);

		try {
			return "http://paste.asie.pl/" + Http.post(new URL("http://paste.asie.pl/add"), postvars).text.trim().substring(238, 242);
		} catch (Throwable e) {
			throw new PasteException(e);
		}
	}
}
