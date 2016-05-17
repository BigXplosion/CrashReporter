package com.bigxplosion.crashreporter.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.minecraftforge.fml.common.Loader;

import com.bigxplosion.crashreporter.lib.Reference;

public class Util {

	public static String readFileToString(File f) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String ret = "";

		char[] buffer = new char[1024];
		int read = 0;
		while ((read = reader.read(buffer)) != -1) {
			ret += String.valueOf(buffer, 0, read);
		}

		reader.close();
		return ret;
	}

	public static void copyStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read = 0;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	public static String getUserAgent() {
		Loader loader = Loader.instance();
		return "CrashReporter/" + Reference.MOD_VERSION + " " + loader.getMCVersionString().replace(' ', '/') + " FML/" + loader.getFMLVersionString();
	}
}
