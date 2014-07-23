package com.example.bluetooth.le;

import java.util.UUID;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xtremeprog.sdk.ble.BleGattCharacteristic;
import com.xtremeprog.sdk.ble.BleService;
import com.xtremeprog.sdk.ble.IBle;

public class CharacteristicActivity extends Activity {

	private String mDeviceAddress;
	private IBle mBle;
	private BleGattCharacteristic mCharacteristic;

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_read) {
				mBle.requestReadCharacteristic(mDeviceAddress, mCharacteristic);
			} else if (v.getId() == R.id.btn_notify) {
				if (mNotifyStarted) {
					mBle.requestStopNotification(mDeviceAddress,
							mCharacteristic);
				} else {
					mBle.requestCharacteristicNotification(mDeviceAddress,
							mCharacteristic);
				}
			} else if (v.getId() == R.id.btn_indicate) {
				mBle.requestIndication(mDeviceAddress, mCharacteristic);
			} else if (v.getId() == R.id.btn_write) {
				String val = et_hex.getText().toString();
				try {
					byte[] data = Hex.decodeHex(val.toCharArray());
					mCharacteristic.setValue(data);
					mBle.requestWriteCharacteristic(mDeviceAddress,
							mCharacteristic, "");
				} catch (DecoderException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private final BroadcastReceiver mBleReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			if (!mDeviceAddress.equals(extras.getString(BleService.EXTRA_ADDR))) {
				return;
			}

			String uuid = extras.getString(BleService.EXTRA_UUID);
			if (uuid != null
					&& !mCharacteristic.getUuid().toString().equals(uuid)) {
				return;
			}

			String action = intent.getAction();
			if (BleService.BLE_GATT_DISCONNECTED.equals(action)) {
				Toast.makeText(CharacteristicActivity.this,
						"Device disconnected...", Toast.LENGTH_SHORT).show();
				finish();
			} else if (BleService.BLE_CHARACTERISTIC_READ.equals(action)
					|| BleService.BLE_CHARACTERISTIC_CHANGED.equals(action)) {
				byte[] val = extras.getByteArray(BleService.EXTRA_VALUE);
				tv_ascii.setText(new String(val));
				tv_hex.setText("0x" + new String(Hex.encodeHex(val)));
			} else if (BleService.BLE_CHARACTERISTIC_NOTIFICATION
					.equals(action)) {
				Toast.makeText(CharacteristicActivity.this,
						"Notification state changed!", Toast.LENGTH_SHORT)
						.show();
				mNotifyStarted = extras.getBoolean(BleService.EXTRA_VALUE);
				if (mNotifyStarted) {
					btn_notify.setText("Stop Notify");
				} else {
					btn_notify.setText("Start Notify");
				}
			} else if (BleService.BLE_CHARACTERISTIC_INDICATION.equals(action)) {
				Toast.makeText(CharacteristicActivity.this,
						"Indication state changed!", Toast.LENGTH_SHORT).show();
			} else if (BleService.BLE_CHARACTERISTIC_WRITE.equals(action)) {
				Toast.makeText(CharacteristicActivity.this, "Write success!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	private TextView tv_ascii;
	private TextView tv_hex;
	private EditText et_hex;
	private boolean mNotifyStarted;
	private Button btn_notify;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_characteristic);

		mDeviceAddress = getIntent().getStringExtra("address");
		String service = getIntent().getStringExtra("service");
		String characteristic = getIntent().getStringExtra("characteristic");
		BleApplication app = (BleApplication) getApplication();
		mBle = app.getIBle();
		mCharacteristic = mBle.getService(mDeviceAddress,
				UUID.fromString(service)).getCharacteristic(
				UUID.fromString(characteristic));
		mNotifyStarted = false;

		TextView tv_name = (TextView) findViewById(R.id.tv_name);
		TextView tv_uuid = (TextView) findViewById(R.id.tv_uuid);
		tv_ascii = (TextView) findViewById(R.id.tv_ascii);
		tv_hex = (TextView) findViewById(R.id.tv_hex);

		tv_name.setText(Utils.BLE_CHARACTERISTICS.containsKey(characteristic) ? Utils.BLE_CHARACTERISTICS
				.get(characteristic) : "unknown characteristic");
		tv_uuid.setText(mCharacteristic.getUuid().toString());

		View btn_read = findViewById(R.id.btn_read);
		btn_notify = (Button) findViewById(R.id.btn_notify);
		View btn_indicate = findViewById(R.id.btn_indicate);
		View ll_write = findViewById(R.id.ll_write);
		View btn_write = findViewById(R.id.btn_write);
		et_hex = (EditText) findViewById(R.id.et_hex);
		btn_read.setOnClickListener(onClickListener);
		btn_notify.setOnClickListener(onClickListener);
		btn_indicate.setOnClickListener(onClickListener);
		btn_write.setOnClickListener(onClickListener);

		final int charaProp = mCharacteristic.getProperties();
		if ((charaProp & BleGattCharacteristic.PROPERTY_READ) > 0) {
			btn_read.setVisibility(View.VISIBLE);
		}
		if ((charaProp & BleGattCharacteristic.PROPERTY_NOTIFY) > 0) {
			btn_notify.setVisibility(View.VISIBLE);
		}
		if ((charaProp & BleGattCharacteristic.PROPERTY_INDICATE) > 0) {
			btn_indicate.setVisibility(View.VISIBLE);
		}
		if ((charaProp & BleGattCharacteristic.PROPERTY_WRITE) > 0) {
			ll_write.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.characteristic, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mBleReceiver, BleService.getIntentFilter());
	}

	@Override
	public void onPanelClosed(int featureId, Menu menu) {
		super.onPanelClosed(featureId, menu);
		unregisterReceiver(mBleReceiver);
	}
}
