package com.sevak_avet.timeinroad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import android.content.Context;
import android.widget.Toast;

public class ParseDateFile {
	
	public static void busCounter(Context context) {
		File file = new File(context.getFilesDir() + "/times.txt");
		
		int bus53 = 0;
		int bus6 = 0;
		int currentBus = 0;
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			
			while((line = reader.readLine()) != null) {
				line = line.substring(0, 2).trim();
				currentBus = Integer.parseInt(line);
				
				if(currentBus == 53) {
					++bus53;
				} else {
					++bus6;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Toast.makeText(context, "Bus 53: " + bus53, Toast.LENGTH_SHORT).show();
		Toast.makeText(context, "Bus 6: " + bus6, Toast.LENGTH_SHORT).show();	
	}

	public static List<String> getAllData(Context context) {
		File file = new File(context.getFilesDir() + "/times.txt");
		
		List<String> data = new ArrayList<String>();
		String[] source = new String[4];
		
		
		DateTime start;
		DateTime end;
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			String formatString = "";
			int id = 0;
			
			while((line = reader.readLine()) != null) {
				source = line.split(" ");
				start = new DateTime(Long.parseLong(source[1]));
				end = new DateTime(Long.parseLong(source[2]));
				
				if("53".equals(source[0])) {
					formatString = "{0})  {1}   {2} - {3}   {4}";
				} else {
					formatString = "{0})  {1}     {2} - {3}   {4}";
				}
				
				data.add(MessageFormat.format(formatString, ++id, source[0], getTime(start), getTime(end), source[3]));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	
	private static String getTime(DateTime date) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(date.getHourOfDay());
		stringBuilder.append(":");
		stringBuilder.append(date.getMinuteOfHour());
		stringBuilder.append(":");
		stringBuilder.append(date.getSecondOfMinute());
		return stringBuilder.toString();
	}
}
