package net.michaelzhou.optimusdim;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	// private Button button1;
	// private Button button2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// button1 = (Button) findViewById(R.id.button1);
		// button2 = (Button) findViewById(R.id.button2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void button1Clicked(View v) {
		try {
			setMaxcurrent(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void button2Clicked(View v) {
		try {
			setMaxcurrent(19);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static final int SCRIPT_SIZE = 745;
	private static final int MODULE_SIZE = 10400;

	private int setMaxcurrent(int level) throws IOException,
			InterruptedException {
		// Extract module.
		InputStream mod = getResources()
				.openRawResource(R.raw.lm3533_bl_custom);
		byte[] modData = new byte[MODULE_SIZE];
		mod.read(modData);
		mod.close();
		OutputStream cachedMod = new FileOutputStream(new File(getCacheDir(),
				"lm3533_bl_custom.ko"));
		cachedMod.write(modData);
		modData = null;
		cachedMod.close();

		// Run.
		InputStream script = getResources().openRawResource(R.raw.script);
		byte[] scriptData = new byte[SCRIPT_SIZE];
		script.read(scriptData);
		script.close();
		Process p = Runtime.getRuntime().exec("su");
		OutputStream cmds = p.getOutputStream();
		cmds.write(("MAXCURRENT=" + level + "\n").getBytes("UTF-8"));
		cmds.write(scriptData);
		scriptData = null;
		int ret = p.waitFor();
		p.destroy();
		Log.i("setMaxCurrent", "ret == " + ret);
		return ret;
	}
}
