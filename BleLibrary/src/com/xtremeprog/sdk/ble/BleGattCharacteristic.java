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

import java.util.UUID;

import android.annotation.SuppressLint;

import com.xtremeprog.sdk.ble.BleService.BLESDK;

@SuppressLint("NewApi")
public class BleGattCharacteristic {

	public static final int PROPERTY_READ = 2;
	public static final int PROPERTY_WRITE = 8;
	public static final int PROPERTY_NOTIFY = 16;
	public static final int PROPERTY_INDICATE = 32;

	/**
	 * Characteristic value format type uint8
	 */
	public static final int FORMAT_UINT8 = 0x11;

	/**
	 * Characteristic value format type uint16
	 */
	public static final int FORMAT_UINT16 = 0x12;

	/**
	 * Characteristic value format type uint24 Note: this is not a standard data
	 * type!
	 */
	public static final int FORMAT_UINT24 = 0x13;

	/**
	 * Characteristic value format type uint32
	 */
	public static final int FORMAT_UINT32 = 0x14;

	/**
	 * Characteristic value format type sint8
	 */
	public static final int FORMAT_SINT8 = 0x21;

	/**
	 * Characteristic value format type sint16
	 */
	public static final int FORMAT_SINT16 = 0x22;

	/**
	 * Characteristic value format type sint32
	 */
	public static final int FORMAT_SINT32 = 0x24;

	/**
	 * Characteristic value format type sfloat (16-bit float)
	 */
	public static final int FORMAT_SFLOAT = 0x32;

	/**
	 * Characteristic value format type float (32-bit float)
	 */
	public static final int FORMAT_FLOAT = 0x34;

	private android.bluetooth.BluetoothGattCharacteristic mGattCharacteristicA;
	private com.broadcom.bt.gatt.BluetoothGattCharacteristic mGattCharacteristicB;
	private com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic mGattCharacteristicS;
	private BLESDK mBleSDK;
	private String name;

	public BleGattCharacteristic(android.bluetooth.BluetoothGattCharacteristic c) {
		mBleSDK = BLESDK.ANDROID;
		setGattCharacteristicA(c);
		initInfo();
	}

	public BleGattCharacteristic(
			com.broadcom.bt.gatt.BluetoothGattCharacteristic c) {
		mBleSDK = BLESDK.BROADCOM;
		setGattCharacteristicB(c);
	}

	public BleGattCharacteristic(
			com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic c) {
		mBleSDK = BLESDK.SAMSUNG;
		setGattCharacteristicS(c);
	}

	private void initInfo() {
		name = "Unknown characteristic";
	}

	public UUID getUuid() {
		if (mBleSDK == BLESDK.ANDROID) {
			return getGattCharacteristicA().getUuid();
		} else if (mBleSDK == BLESDK.BROADCOM) {
			return getGattCharacteristicB().getUuid();
		} else if (mBleSDK == BLESDK.SAMSUNG) {
			return getGattCharacteristicS().getUuid();
		}

		return null;
	}

	protected android.bluetooth.BluetoothGattCharacteristic getGattCharacteristicA() {
		return mGattCharacteristicA;
	}

	public int getProperties() {
		if (mBleSDK == BLESDK.ANDROID) {
			return getGattCharacteristicA().getProperties();
		} else if (mBleSDK == BLESDK.BROADCOM) {
			return getGattCharacteristicB().getProperties();
		} else if (mBleSDK == BLESDK.SAMSUNG) {
			return getGattCharacteristicS().getProperties();
		}

		return 0;
	}

	protected com.broadcom.bt.gatt.BluetoothGattCharacteristic getGattCharacteristicB() {
		return mGattCharacteristicB;
	}

	protected void setGattCharacteristicB(
			com.broadcom.bt.gatt.BluetoothGattCharacteristic mBCGattCharacteristic) {
		this.mGattCharacteristicB = mBCGattCharacteristic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean setValue(byte[] val) {
		if (mBleSDK == BLESDK.ANDROID) {
			return getGattCharacteristicA().setValue(val);
		} else if (mBleSDK == BLESDK.SAMSUNG) {
			return mGattCharacteristicS.setValue(val);
		} else if (mBleSDK == BLESDK.BROADCOM) {
			return mGattCharacteristicB.setValue(val);
		}

		return false;
	}

	public byte[] getValue() {
		if (mBleSDK == BLESDK.ANDROID) {
			return getGattCharacteristicA().getValue();
		} else if (mBleSDK == BLESDK.SAMSUNG) {
			return mGattCharacteristicS.getValue();
		} else if (mBleSDK == BLESDK.BROADCOM) {
			return mGattCharacteristicB.getValue();
		}

		return null;
	}

	public boolean setValue(int value, int formatType, int offset) {
		if (mBleSDK == BLESDK.ANDROID) {
			return getGattCharacteristicA().setValue(value, formatType, offset);
		} else if (mBleSDK == BLESDK.SAMSUNG) {
			return mGattCharacteristicS.setValue(value, formatType, offset);
		} else if (mBleSDK == BLESDK.BROADCOM) {
			return mGattCharacteristicB.setValue(value, formatType, offset);
		}

		return false;
	}

	public boolean setValue(int mantissa, int exponent, int formatType,
			int offset) {
		if (mBleSDK == BLESDK.ANDROID) {
			return getGattCharacteristicA().setValue(mantissa, exponent,
					formatType, offset);
		} else if (mBleSDK == BLESDK.SAMSUNG) {
			return mGattCharacteristicS.setValue(mantissa, exponent,
					formatType, offset);
		} else if (mBleSDK == BLESDK.BROADCOM) {
			return mGattCharacteristicB.setValue(mantissa, exponent,
					formatType, offset);
		}

		return false;
	}

	public boolean setValue(String value) {
		return setValue(value.getBytes());
	}

	public String getStringValue(int offset) {
		if (mBleSDK == BLESDK.ANDROID) {
			return getGattCharacteristicA().getStringValue(offset);
		} else if (mBleSDK == BLESDK.SAMSUNG) {
			return mGattCharacteristicS.getStringValue(offset);
		} else if (mBleSDK == BLESDK.BROADCOM) {
			return mGattCharacteristicB.getStringValue(offset);
		}

		return null;
	}

	public Float getFloatValue(int formatType, int offset) {
		if (mBleSDK == BLESDK.ANDROID) {
			return getGattCharacteristicA().getFloatValue(formatType, offset);
		} else if (mBleSDK == BLESDK.SAMSUNG) {
			return mGattCharacteristicS.getFloatValue(formatType, offset);
		} else if (mBleSDK == BLESDK.BROADCOM) {
			return mGattCharacteristicB.getFloatValue(formatType, offset);
		}

		return null;
	}

	public Integer getIntValue(int formatType, int offset) {
		if (mBleSDK == BLESDK.ANDROID) {
			if (formatType == FORMAT_UINT24) {
				byte[] value = getGattCharacteristicA().getValue();
				return byte2uint24(offset, value);
			} else {
				return getGattCharacteristicA().getIntValue(formatType, offset);
			}
		} else if (mBleSDK == BLESDK.SAMSUNG) {
			if (formatType == FORMAT_UINT24) {
				byte[] value = mGattCharacteristicS.getValue();
				return byte2uint24(offset, value);
			} else {
				return mGattCharacteristicS.getIntValue(formatType, offset);
			}
		} else if (mBleSDK == BLESDK.BROADCOM) {
			if (formatType == FORMAT_UINT24) {
				byte[] value = mGattCharacteristicB.getValue();
				return byte2uint24(offset, value);
			} else {
				return mGattCharacteristicB.getIntValue(formatType, offset);
			}
		}

		return null;
	}

	private Integer byte2uint24(int offset, byte[] value) {
		if ((offset + 3) > value.length)
			return null;
		return Integer.valueOf((value[offset] & 0xFF)
				| (value[offset + 1] & 0xFF) << 8
				| (value[offset + 2] & 0xFF) << 16);
	}

	protected com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic getGattCharacteristicS() {
		return mGattCharacteristicS;
	}

	protected void setGattCharacteristicS(
			com.samsung.android.sdk.bt.gatt.BluetoothGattCharacteristic mSSGattCharacteristic) {
		this.mGattCharacteristicS = mSSGattCharacteristic;
	}

	protected void setGattCharacteristicA(
			android.bluetooth.BluetoothGattCharacteristic mGattCharacteristicA) {
		this.mGattCharacteristicA = mGattCharacteristicA;
	}
}
