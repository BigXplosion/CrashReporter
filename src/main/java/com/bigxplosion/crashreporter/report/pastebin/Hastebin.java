package com.bigxplosion.crashreporter.report.pastebin;

import java.net.URL;

import com.bigxplosion.crashreporter.report.base.IPastebinProvider;
import com.bigxplosion.crashreporter.util.Http;

public class Hastebin implements IPastebinProvider {

	@Override
	public String paste(String title, String text) throws PasteException {
		try {
			String json = Http.post(new URL("http://hastebin.com/documents"), text).text;
			return "http://hastebin.com/" + json.substring(8, json.length() - 3) + ".hs";
		} catch (Throwable e) {
			throw new PasteException(e);
		}
	}
}
