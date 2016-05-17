package com.bigxplosion.crashreporter.report.base;

public interface IPastebinProvider {

	String paste(String title, String text) throws PasteException;

	class PasteException extends RuntimeException {
		public PasteException(String message) {
			super(message);
		}

		public PasteException(Throwable cause) {
			super(cause);
		}
	}

}
