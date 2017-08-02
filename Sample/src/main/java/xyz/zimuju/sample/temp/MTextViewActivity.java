package xyz.zimuju.sample.temp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;

import xyz.zimuju.sample.R;

public class MTextViewActivity extends Activity {
	/** Called when the activity is first created. */
	MyTextView3 mView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_main);
		
		mView = (MyTextView3) findViewById(R.id.textview);
		mView.setMovementMethod(ScrollingMovementMethod.getInstance());
		mView.setText(getFromAssets("1.txt"));
	}

	public String getFromAssets(String fileName) {
		StringBuffer sb = new StringBuffer();
		try {
			InputStreamReader inputReader = new InputStreamReader(
					getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			while ((line = bufReader.readLine()) != null)
				sb.append(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}