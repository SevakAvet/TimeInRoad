package com.sevak_avet.timeinroad;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AllDataActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_data_layout);
		
		ListView lw = (ListView) findViewById(R.id.allDataListView);
		
		List<String> all_data = ParseDateFile.getAllData(getApplicationContext());
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, all_data);
		lw.setAdapter(adapter);
	}
}
