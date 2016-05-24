package com.bigxplosion.crashreporter;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import com.bigxplosion.crashreporter.command.CommandDebug;
import com.bigxplosion.crashreporter.config.Config;
import com.bigxplosion.crashreporter.handler.ServerLogHandler;
import com.bigxplosion.crashreporter.lib.Reference;
import com.bigxplosion.crashreporter.report.ReportRegistry;
import com.bigxplosion.crashreporter.report.notify.NotifyGMail;
import com.bigxplosion.crashreporter.report.notify.NotifyGoogleDriveForm;
import com.bigxplosion.crashreporter.report.notify.NotifyIRC;
import com.bigxplosion.crashreporter.report.pastebin.Asie;
import com.bigxplosion.crashreporter.report.pastebin.Hastebin;
import com.bigxplosion.crashreporter.report.pastebin.Pastebin;
import com.bigxplosion.crashreporter.report.pastebin.Sprunge;
import com.bigxplosion.crashreporter.report.pastebin.Ubuntu;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, acceptableRemoteVersions = "*")
public class CrashReporter {

	@Mod.Instance(Reference.MOD_ID)
	public static CrashReporter INSTANCE;

	public Logger log;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		log = event.getModLog();

		Config.init(event.getSuggestedConfigurationFile());

		ReportRegistry.registerPastebinProvider("pastebin", new Pastebin());
		ReportRegistry.registerPastebinProvider("hastebin", new Hastebin());
		ReportRegistry.registerPastebinProvider("sprunge", new Sprunge());
		ReportRegistry.registerPastebinProvider("ubuntu", new Ubuntu());
		ReportRegistry.registerPastebinProvider("asie", new Asie());

		ReportRegistry.registerNotificationProvider("irc", new NotifyIRC());
		ReportRegistry.registerNotificationProvider("googleDriveForm", new NotifyGoogleDriveForm());
		ReportRegistry.registerNotificationProvider("gmail", new NotifyGMail());
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
			ServerLogHandler.init();

		//TODO: ALWAYS remove on release!!!
		event.registerServerCommand(new CommandDebug());
	}
}
