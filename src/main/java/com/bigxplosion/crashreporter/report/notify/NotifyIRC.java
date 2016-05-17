package com.bigxplosion.crashreporter.report.notify;

import com.bigxplosion.crashreporter.config.Config;
import com.bigxplosion.crashreporter.report.base.INotificationProvider;
import com.bigxplosion.crashreporter.util.IRC;

public class NotifyIRC implements INotificationProvider {

	@Override
	public void notify(String title, String text, String link) throws NotifyException {
		if (!Config.ircEnabled)
			return;

		Thread irc  = new Thread(new IRC(Config.ircServer, Config.ircPort, Config.ircServerPass, Config.ircNick, Config.ircChannels, Config.ircMessage, link));
		irc.start();

		try {
			irc.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
