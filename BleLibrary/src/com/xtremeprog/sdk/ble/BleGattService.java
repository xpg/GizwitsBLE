/**
 * This XPG software is supplied to you by Xtreme Programming Group, Inc.
 * ("XPG") in consideration of your agreement to the following terms, and your
 * use, installation, modification or redistribution of this XPG software
 * constitutes acceptance of these terms.� If you do not agree with these terms,
 * please do not use, install, modify or redistribute this XPG software.
 * 
 * In consideration of your agreement to abide by the following terms, and
 * subject to these terms, XPG grants you a non-exclusive license, under XPG's
 * copyrights in this original XPG software (the "XPG Software"), to use and
 * redistribute the XPG Software, in source and/or binary forms; provided that
 * if you redistribute the XPG Software, with or without modifications, you must
 * retain this notice and the following text and disclaimers in all such
 * redistributions of the XPG Software. Neither the name, trademarks, service
 * marks or logos of XPG Inc. may be used to endorse or promote products derived
 * from the XPG Software without specific prior written permission from XPG.�
 * Except as expressly stated in this notice, no other rights or licenses,
 * express or implied, are granted by XPG herein, including but not limited to
 * any patent rights that may be infringed by your derivative works or by other
 * works in which the XPG Software may be incorporated.
 * 
 * The XPG Software is provided by XPG on an "AS IS" basis.� XPG MAKES NO
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED
 * WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE, REGARDING THE XPG SOFTWARE OR ITS USE AND OPERATION ALONE OR IN
 * COMBINATION WITH YOUR PRODUCTS.
 * 
 * IN NO EVENT SHALL XPG BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION
 * AND/OR DISTRIBUTION OF THE XPG SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER
 * THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR
 * OTHERWISE, EVEN IF XPG HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * ABOUT XPG: Established since June 2005, Xtreme Programming Group, Inc. (XPG)
 * is a digital solutions company based in the United States and China. XPG
 * integrates cutting-edge hardware designs, mobile applications, and cloud
 * computing technologies to bring innovative products to the marketplace. XPG's
 * partners and customers include global leading corporations in semiconductor,
 * home appliances, health/wellness electronics, toys and games, and automotive
 * industries. Visit www.xtremeprog.com for more information.
 * 
 * Copyright (C) 2013 Xtreme Programming Group, Inc. All Rights Reserved.
 */

package com.xtremeprog.sdk.ble;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;

import com.xtremeprog.sdk.ble.BleService.BLESDK;

@SuppressLint("NewApi")
public class BleGattService {

	private BLESDK mBleSDK;
	private com.samsung.android.sdk.bt.gatt.BluetoothGattService mGattServiceS;
	private com.broadcom.bt.gatt.BluetoothGattService mGattServiceB;
	private android.bluetooth.BluetoothGattService mGattServiceA;
	private String mName;

	public BleGattService(com.samsung.android.sdk.bt.gatt.BluetoothGattService s) {
		mBleSDK = BLESDK.SAMSUNG;
		mGattServiceS = s;
		initInfo();
	}

	public BleGattService(com.broadcom.bt.gatt.BluetoothGattService s) {
		mBleSDK = BLESDK.BROADCOM;
		mGattServiceB = s;
		initInfo();
	}

	public BleGattService(android.bluetooth.BluetoothGattService s) {
		mBleSDK = BLESDK.ANDROID;
		mGattServiceA = s;
		initInfo();
	}

	private void initInfo() {
		mName = "Unknown Service";
	}

	public UUID getUuid() {
		if (mBleSDK == BLESDK.BROADCOM) {
			return mGattServiceB.getUuid();
		} else if (mBleSDK == BLESDK.SAMSUNG) {
			return mGattServiceS.getUuid();
		} else if (mBleSDK == BLESDK.ANDROID) {
			return mGattServiceA.getUuid();
		}

		return null;
	}

	public List<BleGattCharacteristic> getCharacteristics() {
		ArrayList<BleGattCharacteristic> list = new ArrayList<BleGattCharacteristic>();
		if (mBleSDK == BLESDK.BROADCOM) {
			for (com.broadcom.bt.gatt.BluetoothGattCharacteristic c : mGattServiceB
					.getCharacteristics()) {
				list.add(new BleGattCharacteristic(c));
			}
		} else if (mBleSDK == BLESDK.SAMSUNG) {
			for (Object o : mGattServiceS.getCharacteristics()) {
				com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic c = (com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic) o;
				list.add(new BleGattCharacteristic(c));
			}
		} else if (mBleSDK == BLESDK.ANDROID) {
			for (android.bluetooth.BluetoothGattCharacteristic c : mGattServiceA
					.getCharacteristics()) {
				list.add(new BleGattCharacteristic(c));
			}
		}

		return list;
	}

	public BleGattCharacteristic getCharacteristic(UUID uuid) {
		if (mBleSDK == BLESDK.ANDROID) {
			BluetoothGattCharacteristic c = mGattServiceA
					.getCharacteristic(uuid);
			if (c != null) {
				return new BleGattCharacteristic(c);
			}
		} else if (mBleSDK == BLESDK.SAMSUNG) {
			com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic c = mGattServiceS
					.getCharacteristic(uuid);
			if (c != null) {
				return new BleGattCharacteristic(c);
			}
		} else if (mBleSDK == BLESDK.BROADCOM) {
			com.broadcom.bt.gatt.BluetoothGattCharacteristic c = mGattServiceB
					.getCharacteristic(uuid);
			if (c != null) {
				return new BleGattCharacteristic(c);
			}
		}

		return null;
	}

	public void setInfo(JSONObject info) {
		if (info == null) {
			return;
		}

		try {
			setName(info.getString("name"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}
}
