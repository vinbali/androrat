package my.app.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LauncherActivity extends Activity {
	/** Called when the activity is first created. */

	Intent Client, ClientAlt;
	Button btnStart, btnStop;
	EditText ipfield, portfield;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.Client = new Intent(this, Client.class);
		this.Client.setAction(LauncherActivity.class.getName());

		this.btnStart = (Button) findViewById(R.id.buttonstart);
		this.btnStop = (Button) findViewById(R.id.buttonstop);
		this.ipfield = (EditText) findViewById(R.id.ipfield);
		this.portfield = (EditText) findViewById(R.id.portfield);

		// Update fields
		SharedPreferences settings = this.getSharedPreferences("preferences", 0);
		this.ipfield.setText(settings.getString("ip", K.serverIp));
		this.portfield.setText(Integer.toString(settings.getInt("port", K.serverPort)));

		this.btnStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				LauncherActivity.this.Client.putExtra("IP", LauncherActivity.this.ipfield.getText()
					.toString());
				LauncherActivity.this.Client.putExtra("PORT", new Integer(
					LauncherActivity.this.portfield.getText().toString()));
				startService(LauncherActivity.this.Client);
				LauncherActivity.this.btnStart.setEnabled(false);
				LauncherActivity.this.btnStop.setEnabled(true);
				// finish();
			}
		});

		this.btnStop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				stopService(LauncherActivity.this.Client);
				LauncherActivity.this.btnStart.setEnabled(true);
				LauncherActivity.this.btnStop.setEnabled(false);
				// finish();
			}
		});
	}
}