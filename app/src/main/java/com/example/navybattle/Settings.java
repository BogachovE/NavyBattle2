package com.example.navybattle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Switch;

public class Settings extends Activity {
	
	private RadioGroup radio;
	private Switch sound, vibration;
	private GlobalSettingsVariables gSettings;
	private String gDifficulty, gSound, gVibration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		gSettings = ((GlobalSettingsVariables)getApplicationContext());
		gDifficulty = gSettings.getDifficulty();
		gSound = gSettings.getSound();
		gVibration = gSettings.getVibration();
		
		radio = (RadioGroup)findViewById(R.id.radioGroup1);
		sound = (Switch)findViewById(R.id.switchSound);
		vibration = (Switch)findViewById(R.id.switchVibration);
		
		if (gDifficulty == "Easy")	radio.check(R.id.radioEasy);
		else if (gDifficulty == "Hard")	radio.check(R.id.radioHard);
		else if (gDifficulty == "VeryHard") radio.check(R.id.radioVeryHard);
		
		if (gSound == "On")	sound.setChecked(true);
		else	sound.setChecked(false);
		
		if (gVibration == "On")	vibration.setChecked(true);
		else	vibration.setChecked(false);
	
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
				getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	public void save (View view)	{
		GlobalSettingsVariables level = ((GlobalSettingsVariables)getApplicationContext());
		level.setDifficulty(radio);
		level.setSound(sound);
		level.setVibration(vibration);
		Intent intent = new Intent(this,Main.class);
		startActivity(intent);
	}
	
	public void cancel(View view)	{
		Intent intent = new Intent(this,Main.class);
		startActivity(intent);
	}
}
