package org.usfirst.frc.team1619.robot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;

public class UManualKeyboardControl {

	final private static UManualKeyboardControl highlander = new UManualKeyboardControl();

	private UManualKeyboardControl() {
	}

	public static UManualKeyboardControl getInstance() {
		return highlander;
	}

	private void handleKeysCommand(String data) {
		int[] keyCodes = parseKeys(data);
		for (int key : keyCodes) {
			System.out.println(key);
		}
		synchronized (rsc) {
			rsc.rainbowStorm = keyCodes;
		}
	}

	private int[] parseKeys(String data) {
		try {
			String[] keys = data.split(" ");
			int[] keyCodes = new int[keys.length];
			for (int i = 0; i < keys.length; i++) {
				keyCodes[i] = Integer.parseInt(keys[i]);
			}
			return keyCodes;
		} catch (Exception e) {
			return new int[0];
		}
	}

	private void handleCommand(String buffer) {
		String[] split = buffer.split(" ", 2);
		if (split.length < 1)
			return;

		String command = split[0];
		String data = split.length == 1 ? "" : split[1];
		switch (command) {
		case "KEYS":
			System.out.println("Got KEYS command");
			handleKeysCommand(data);
			break;
		}
	}

	public class RainbowSTORMContainer {
		public int[] rainbowStorm;
	}

	private final RainbowSTORMContainer rsc = new RainbowSTORMContainer();

	private static boolean searchRainbowSTORM(int[] rainbowSTORM, int key) {
		for (int i : rainbowSTORM) {
			if (i == key) {
				return true;
			}
		}
		return false;
	}

	public void startRainbowSTORMServer() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ServerSocket serverSocket = null;
				while (true) {
					try {
						serverSocket = new ServerSocket(
								URobotMap.RAINBOW_STORM_PORT);
						while (true) {
							Socket socket = serverSocket.accept();
							try {
								socket.setSoTimeout(500);
								BufferedReader br = new BufferedReader(
										new InputStreamReader(
												socket.getInputStream()));
								String line;
								while ((line = br.readLine()) != null) {
									System.out.println(line);
									handleCommand(line);
								}
							} catch (Exception e) {
								socket.close();
							}
						}
					} catch (Exception e) {
					} finally {
						try {
							serverSocket.close();
						} catch (Exception e) {
						}
					}
				}
			}
		}).start();
	}

	public void runRainbowSTORM() {
		int[] rainbowSTORM = getRainbowSTORM();
		for (URobotMap.MotorDefinition md : URobotMap.MotorDefinition.values()) {
			boolean forwardKey = searchRainbowSTORM(rainbowSTORM, md.forwardKey);
			boolean reverseKey = searchRainbowSTORM(rainbowSTORM, md.reverseKey);
			CANTalon motor = md.getMotor();
			motor.changeControlMode(ControlMode.PercentVbus);
			motor.set(forwardKey ? reverseKey ? 0.0 : md.forwardKeySpeed
					: reverseKey ? md.reverseKeySpeed : 0.0);
		}
	}

	private int[] getRainbowSTORM() {
		synchronized (rsc) {
			if (rsc.rainbowStorm == null)
				return new int[0];
			else
				return rsc.rainbowStorm;
		}

	}
}
