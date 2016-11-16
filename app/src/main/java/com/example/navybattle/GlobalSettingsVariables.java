package com.example.navybattle;

import android.app.Application;
import android.widget.RadioGroup;
import android.widget.Switch;

public class GlobalSettingsVariables extends Application	{
	private RadioGroup difficulty;
	private Switch sw;
	public String level = "Normal";
	public String sound = "On";
	public String vibration = "On";
	
	public void setDifficulty(RadioGroup rg)	{
		difficulty = rg;
		int id = difficulty.getCheckedRadioButtonId();
		
		if (id== R.id.radioEasy)	{
			level = "Easy";
		}
		else if (id == R.id.radioHard)	{
			level = "Hard";
		}
		else if(id ==R.id.radioVeryHard){
			level = "VeryHard";
		}
		else	{
			level = "Normal";
		}
	}
	
	public void setSound (Switch snd)	{
		sw = snd;
		boolean state = sw.isChecked();
		if (state)	sound = "On";
		else	sound = "Off";
	}
	
	public void setVibration (Switch snd)	{
		sw = snd;
		boolean state = sw.isChecked();
		if (state)	vibration = "On";
		else	vibration = "Off";
	}
	
	public String getDifficulty ()	{	return level;	}	
	public String getSound ()	{	return sound;	}
	public String getVibration()	{	return vibration;	}


}
