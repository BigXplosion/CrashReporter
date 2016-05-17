package com.bigxplosion.crashreporter.util;

import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import com.bigxplosion.crashreporter.CrashReporter;

public class IRC implements Runnable {

	private BufferedWriter out;
	private BufferedReader in;
	private boolean connected;
	private String server;
	private int port;
	private String serverPass;
	private String nick;
	private List<String> channelList;
	private Map<String, String> channels;
	private String message;
	private String crashReport;
	private Socket socket;


	public IRC(String server, int port, String serverPass, String nick, List<String> channelList, String message, String crashReport) {
		this.server = server;
		this.port = port;
		this.serverPass = serverPass;
		this.nick = nick;
		this.channelList = channelList;
		this.channels = Maps.newHashMap();
		this.message = message;
		this.crashReport = crashReport;
	}

	public boolean connect() throws IOException {
		if (nick == null || nick.isEmpty() || connected)
			return false;

		socket = new Socket(server, port);
		connected = true;
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		if (serverPass != null && !serverPass.isEmpty()) {
			out.write("PASS " + serverPass + "\n");
		}

		out.write("USER " + nick + " * * :" + nick + "\n");
		out.write("NICK " + nick + "\n");
		out.flush();

		String l;
		while((l = in.readLine()) != null) {
			CrashReporter.INSTANCE.log.log(Level.INFO, "[IRC] " + l);
			if (l.startsWith("PING ")) {
				sendRaw("PONG :" + l.substring(l.indexOf(':') + 1));
				break;
			}
		}

		return true;
	}

	public void join(String channel) {
		String c;
		String p;

		if (channel.contains(":")) {
			c = channel.substring(0, channel.indexOf(':'));
			p = channel.substring(channel.indexOf(':') + 1);
		} else {
			c = channel;
			p = "";
		}

		if (!c.startsWith("#"))
			c = "#" + c;

		if (p.isEmpty()) {
			if (sendRaw("JOIN " + c)) {
				channels.put(c, "");
			} else {
				CrashReporter.INSTANCE.log.log(Level.WARN, "can't connect to IRC channel: " + c);
			}
		} else {
			if (sendRaw("JOIN " + c + " " + p)) {
				channels.put(c, p);
			} else {
				CrashReporter.INSTANCE.log.log(Level.WARN, "can't connect to IRC channel: " + c);
			}
		}
	}

	public void join(List<String> channels) {
		for (String channel : channels) {
			join(channel);
		}
	}

	public void sendMessage(String message) {
		for (String channel : channels.keySet()) {
			sendRaw("PRIVMSG " + channel + " :" + message);
		}
	}

	public void quit() {
		sendRaw("QUIT");
	}

	public void close () throws IOException {
		connected = false;
		if(socket != null) {
			socket.close();
		}
		in = null;
	}

	public boolean sendRaw (final String s) {
		if (connected) {
			try {
				this.out.write(s + "\n");
				this.out.flush();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return connected = false;
			}
		}
		return false;
	}

	@Override
	public void run() {
		try {
			connect();
		} catch (IOException e) {
			e.printStackTrace();
		}

		join(channelList);
		sendMessage(message + " " + crashReport);
		quit();

		try {
			String l;
			while((l = in.readLine()) != null) {
				CrashReporter.INSTANCE.log.log(Level.INFO, "[IRC] " + l);
			}

			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
