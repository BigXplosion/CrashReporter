package com.bigxplosion.crashreporter.handler;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.DefaultErrorHandler;

import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import net.minecraft.crash.CrashReport;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import com.bigxplosion.crashreporter.report.Reporter;
import com.bigxplosion.crashreporter.util.Util;

public class ServerLogHandler implements Appender {

	private static ServerLogHandler instance;

	private boolean started;
	private ErrorHandler handler = new DefaultErrorHandler(this);

	public static void init() {
		instance = new ServerLogHandler();
		((Logger) ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, FMLCommonHandler.instance().getMinecraftServerInstance(), "LOG", "field_147145_h")).addAppender(instance);
		startAppender();
	}

	public static ServerLogHandler instance() {
		return instance;
	}

	public static void startAppender() {
		instance.start();
	}

	@Override
	public void append(LogEvent event) {
		if (event.getMessage().getFormattedMessage().startsWith("Failed to handle packet for ")) {
			Reporter.INSTANCE.report(new CrashReport("Exception while handling packet from " + event.getMessage().getFormattedMessage().substring(28).split("/")[0], event.getThrown()));
		} else if (event.getMessage().getFormattedMessage().startsWith("This crash report has been saved to: ")) {
			kickAllPlayers();

			String report;
			try {
				report = Util.readFileToString(new File(event.getMessage().getFormattedMessage().substring(37)));
			} catch (Throwable e) {
				StringWriter writer = new StringWriter();
				writer.write("Crash report could not be read!\r\n\r\n");
				e.printStackTrace(new PrintWriter(writer));
				report = writer.toString();
			}

			Reporter.INSTANCE.report("Server Crash", report);
		} else if (event.getMessage().getFormattedMessage().equals("We were unable to save this crash report to disk.")) {
			kickAllPlayers();

			Reporter.INSTANCE.report("Server crash", "Crash report could not be saved!");
		}
	}

	@Override
	public String getName() {
		return "CrashReporterAppender";
	}

	@Override
	public Layout<? extends Serializable> getLayout() {
		return null;
	}

	@Override
	public boolean ignoreExceptions() {
		return false;
	}

	@Override
	public ErrorHandler getHandler() {
		return handler;
	}

	@Override
	public void setHandler(ErrorHandler handler) {

	}

	@Override
	public void start() {
		this.started = true;
	}

	@Override
	public void stop() {
		this.started = false;
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	private void kickAllPlayers() {
		PlayerList list = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();

		while (!list.getPlayerList().isEmpty()) {
			list.getPlayerList().get(0).connection.kickPlayerFromServer("Server Crashed");
			list.getPlayerList().remove(0);
		}
	}
}
