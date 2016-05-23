package com.bigxplosion.crashreporter.config;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.config.Configuration;

public class Config {

	private static Configuration config;

	/** PASTEBIN **/
	public static String preferredPastebin;

	/** IRC **/
	public static boolean ircEnabled;
	public static String ircServer;
	public static int ircPort;
	public static String ircServerPass;
	public static String ircNick;
	public static List<String> ircChannels;
	public static String ircMessage;

	/** GOOGLE DRIVE FORMS **/
	public static boolean formEnabled;
	public static String formURL;

	/** GMAIL **/
	public static boolean gmailEnabled;
	public static String gmailFrom;
	public static String gmailPass;
	public static String gmailTo;

	public static void init(File configFile) {
		config = new Configuration(configFile);

		readConfig();
	}

	private static void readConfig() {
		config.load();

		preferredPastebin = config.get("pastebin", "preferredPastebin", "pastebin", "This pastebin will be tried first before the others. Others possible options are: pastebin, hastebin, ubuntu, sprunge and asie.").getString();

		ircEnabled = config.get("irc", "enabled", false).getBoolean();
		ircServer = config.get("irc", "server", "irc.esper.net").getString();
		ircPort = config.get("irc", "port", 6667).getInt();
		ircServerPass = config.get("irc", "password", "", "Password for the server. Leave it empty when there is none").getString();
		ircNick = config.get("irc", "nick", "CrashReporter").getString();
		String channels = config.get("irc", "channels", "", "Multiple channels should be separated with a comma. If the channel has a password you type the password after a :. example: #big_Xplosion,#MyPrivateChannel:myPrivatePassword256").getString();
		ircChannels = Lists.newArrayList();
		for (String channel : channels.trim().split(",")) {
			ircChannels.add(channel);
		}
		ircMessage = config.get("irc", "message", "Server Crashed. Find crash report at:").getString();

		formEnabled = config.get("googleDriveForm", "enabled", false).getBoolean();
		formURL = config.get("googleDriveForm", "url", "").getString();

		config.addCustomCategoryComment("gmail", "When you want to send emails from a gmail adress you need to enable less secure apps for the account: https://www.google.com/settings/security/lesssecureapps");
		gmailEnabled = config.get("gmail", "enabled", false).getBoolean();
		gmailFrom = config.get("gmail", "from", "", "The email adress used to send the mail from. Must be GMail.").getString();
		gmailPass = config.get("gmail", "pass", "", "The password for the email adress used to send the mail from").getString();
		gmailTo = config.get("gmail", "to", "", "Can be a comma separated list for multiple recepients.").getString();

		if (config.hasChanged())
			config.save();
	}
}
