package com.bigxplosion.crashreporter.report.base;

public interface INotificationProvider {

	void notify(String title, String text, String link) throws NotifyException;

	class NotifyException extends RuntimeException {
		public NotifyException(String message) {
			super(message);
		}

		public NotifyException(Throwable cause) {
			super(cause);
		}
	}
}
