package com.sevak_avet.timeinroad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.joda.time.DateTime;
import org.joda.time.Period;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
	private static final String BUS_NUMBER = "busNumber";
	private static final String FILE_NAME = "/times.txt";
	private static final String STARTED = "started";
	private static final String START_TIME = "startTime";
	
	private static final int bus53_id = R.id.bus53;
	private static final int bus6_id = R.id.bus6;
	
	private static Animation anim;
	private static Period between;
	private static ImageView btnStartStop;
	private static RadioButton bus53;
	
	private static RadioButton bus6;
	private static RadioGroup busNumbers;
	private static SharedPreferences.Editor editor;
	private static DateTime end_date;
	private static SharedPreferences sPref;
	private static DateTime start_date;
	private static boolean started = false;
	
	private static final int started_image = R.drawable.btn_start_with_text;
	private static final int stoped_image = R.drawable.btn_stop_with_text;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.main_layout);
		
		init();
		btnStartStop.setOnClickListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_show_bus_count:
				ParseDateFile.busCounter(this);
				break;
				
			case R.id.menu_show_all_data:
				Intent intent = new Intent(this, AllDataActivity.class);
				startActivity(intent);
				break;
				
/*			case R.id.menu_clear_data:
				File file = new File(getFilesDir() + FILE_NAME);
				file.delete();
				break;
				
			case R.id.menu_show_minmax:
				Toast.makeText(this, "Whle not realised!", Toast.LENGTH_SHORT).show();
				break;*/
				
			default:
				return super.onOptionsItemSelected(item);	
		}
		
		return true;
	}
	

	public void onClick(View v) {
		v.startAnimation(anim);
		
		int bus_number = getCheckedBusNumber();
		
		if(started) {
			disable(false);
			editor.putBoolean(STARTED, false);
			calculateTime();
		} else {
			disable(true);
			editor.putBoolean(STARTED, true);
			editor.putInt(BUS_NUMBER, bus_number);
			editor.putLong(START_TIME, DateTime.now().getMillis());
		}
		
		editor.commit();
		started = !started;
	}
	
	private void init() {
		btnStartStop = (ImageView) findViewById(R.id.btnStartStop);
		busNumbers = (RadioGroup) findViewById(R.id.busNumber);
		bus53 = (RadioButton) findViewById(R.id.bus53);
		bus6 = (RadioButton) findViewById(R.id.bus6);
		anim = AnimationUtils.loadAnimation(this, R.anim.btn_click_animation);

		sPref = getPreferences(MODE_PRIVATE);
		editor = sPref.edit();

		started = sPref.getBoolean(STARTED, false);
		
		if (started) {
			disable(true);
		} else {
			disable(false);
		}
		
		checkRadioButton();
	}
	
	private static int getCheckedBusNumber() {
		switch (busNumbers.getCheckedRadioButtonId()) {
		case bus53_id:
			return 53;
		case bus6_id:
			return 6;
		default:
			return 3;
		}
	}
	
	private static void checkRadioButton() {
		int bus_number = sPref.getInt(BUS_NUMBER, -1);
		
		switch (bus_number) {
		case 6:
			busNumbers.check(bus6_id);
			break;
		case 53:
			busNumbers.check(bus53_id);
			break;
		default:
			busNumbers.check(R.id.bus3);
		}
	}
	
	private void calculateTime() {
		long start_time = sPref.getLong(START_TIME, -1);
		long end_time = DateTime.now().getMillis();
		int bus = sPref.getInt(BUS_NUMBER, -1);

		start_date = new DateTime(start_time);
		end_date = new DateTime(end_time);
		between = new Period(start_date, end_date);

		writeToFile(bus, start_time, end_time);
		showTotalTime();
	}

	@SuppressLint("NewApi")
	private void disable(boolean disable) {
		if (!disable) {
			btnStartStop.setImageResource(started_image);
			busNumbers.setAlpha(1.0F);
			bus53.setEnabled(true);
			bus6.setEnabled(true);
		} else {
			btnStartStop.setImageResource(stoped_image);
			busNumbers.setAlpha(0.3F);
			bus53.setEnabled(false);
			bus6.setEnabled(false);
		}
	}

	private String getTime() {
		return between.getHours() + ":" + between.getMinutes() + ":" + between.getSeconds();
	}

	private void showTotalTime() {
		String time = getTime();
		Toast.makeText(getApplicationContext(), time, Toast.LENGTH_SHORT).show();
	}

	private void writeToFile(long bus, long start_time, long end_time) {
		try {
			File file = new File(getFilesDir() + FILE_NAME);
			PrintWriter pw = new PrintWriter(new FileOutputStream(file, true));
			pw.print(bus);
			pw.print(" " + start_time);
			pw.print(" " + end_time);
			pw.print(" " + getTime());
			pw.print("\n");
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}