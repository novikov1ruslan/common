package com.ivygames.morskoiboi.bluetooth;

import com.ivygames.morskoiboi.bluetooth.BluetoothGame.MessageListener;

import org.apache.commons.lang3.Validate;

final class MessageReceivedCommand implements Runnable {

	private final MessageListener mListener;
	private final String mMessage;

	MessageReceivedCommand(MessageListener listener, String message) {
		Validate.notNull(listener, "listener cannot be null");
		mListener = listener;

		Validate.notNull(message, "message cannot be null");
		mMessage = message;
	}

	@Override
	public void run() {
		mListener.onMessageReceived(mMessage);
	}
}
