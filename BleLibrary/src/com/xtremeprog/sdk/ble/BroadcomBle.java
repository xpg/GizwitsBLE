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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;

import com.broadcom.bt.gatt.BluetoothGatt;
import com.broadcom.bt.gatt.BluetoothGattAdapter;
import com.broadcom.bt.gatt.BluetoothGattCallback;
import com.broadcom.bt.gatt.BluetoothGattCharacteristic;
import com.broadcom.bt.gatt.BluetoothGattDescriptor;
import com.broadcom.bt.gatt.BluetoothGattService;
import com.xtremeprog.sdk.ble.BleRequest.RequestType;

public class BroadcomBle implements IBle, IBleRequestHandler {
	private BluetoothAdapter mBtAdapter;
	private BleService mService;
	private BluetoothGatt mBluetoothGatt;
	private boolean mScanning;
	private String mAddress;

	private final BluetoothGattCallback mGattCallbacks = new BluetoothGattCallback() {
		@Override
		public void onAppRegistered(int status) {
		}

		@Override
		public void onScanResult(BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			mService.bleDeviceFound(device, rssi, scanRecord,
					BleService.DEVICE_SOURCE_SCAN);
		}

		@Override
		public void onConnectionStateChange(BluetoothDevice device, int status,
				int newState) {
			if (mBluetoothGatt == null) {
				return;
			}

			if (newState == BluetoothProfile.STATE_CONNECTED) {
				mService.bleGattConnected(device);
				mBluetoothGatt.discoverServices(device);
				mAddress = device.getAddress();
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				mService.bleGattDisConnected(device.getAddress());
				mAddress = null;
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothDevice device, int status) {
			mService.bleServiceDiscovered(device.getAddress());
		}

		@Override
		public void onCharacteristicRead(
				BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				mService.bleCharacteristicRead(mAddress, characteristic
						.getUuid().toString(), status, characteristic
						.getValue());
			}
		}

		@Override
		public void onCharacteristicChanged(
				BluetoothGattCharacteristic characteristic) {
			String address = mService.getNotificationAddress();
			mService.bleCharacteristicChanged(address, characteristic.getUuid()
					.toString(), characteristic.getValue());
		}

		@Override
		public void onDescriptorRead(BluetoothGattDescriptor descriptor,
				int status) {
			BleRequest request = mService.getCurrentRequest();
			String address = request.address;
			byte[] value = descriptor.getValue();
			byte[] val_set = null;
			if (request.type == RequestType.CHARACTERISTIC_NOTIFICATION) {
				val_set = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
			} else if (request.type == RequestType.CHARACTERISTIC_INDICATION) {
				val_set = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE;
			} else {
				val_set = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
			}

			if (Arrays.equals(value, val_set)) {
				if (request.type == RequestType.CHARACTERISTIC_NOTIFICATION) {
					mService.bleCharacteristicNotification(address, descriptor
							.getCharacteristic().getUuid().toString(), true,
							status);
				} else if (request.type == RequestType.CHARACTERISTIC_INDICATION) {
					mService.bleCharacteristicIndication(address, descriptor
							.getCharacteristic().getUuid().toString(), status);
				} else {
					mService.bleCharacteristicNotification(address, descriptor
							.getCharacteristic().getUuid().toString(), false,
							status);
				}
				return;
			}

			if (!descriptor.setValue(val_set)) {
				mService.requestProcessed(address, request.type, false);
			}

			mBluetoothGatt.writeDescriptor(descriptor);
		};

		@Override
		public void onDescriptorWrite(BluetoothGattDescriptor descriptor,
				int status) {
			BleRequest request = mService.getCurrentRequest();
			String address = request.address;
			if (request.type == RequestType.CHARACTERISTIC_NOTIFICATION
					|| request.type == RequestType.CHARACTERISTIC_INDICATION
					|| request.type == RequestType.CHARACTERISTIC_STOP_NOTIFICATION) {
				if (status != BluetoothGatt.GATT_SUCCESS) {
					mService.requestProcessed(address, request.type, false);
					return;
				}

				if (request.type == RequestType.CHARACTERISTIC_NOTIFICATION) {
					mService.bleCharacteristicNotification(address, descriptor
							.getCharacteristic().getUuid().toString(), true,
							status);
				} else if (request.type == RequestType.CHARACTERISTIC_INDICATION) {
					mService.bleCharacteristicIndication(address, descriptor
							.getCharacteristic().getUuid().toString(), status);
				} else {
					mService.bleCharacteristicNotification(address, descriptor
							.getCharacteristic().getUuid().toString(), false,
							status);
				}
				return;
			}
		};
	};

	private final BluetoothProfile.ServiceListener mProfileServiceListener = new BluetoothProfile.ServiceListener() {
		@Override
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			mBluetoothGatt = (BluetoothGatt) proxy;
			mBluetoothGatt.registerApp(mGattCallbacks);
		}

		@Override
		public void onServiceDisconnected(int profile) {
			for ( BluetoothDevice d : mBluetoothGatt.getConnectedDevices() ) {
				mBluetoothGatt.cancelConnection(d);
			}
			mBluetoothGatt = null;
		}
	};

	public BroadcomBle(BleService service) {
		mService = service;
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBtAdapter == null) {
			mService.bleNoBtAdapter();
			return;
		}
		BluetoothGattAdapter.getProfileProxy(mService, mProfileServiceListener,
				BluetoothGattAdapter.GATT);
	}

	@Override
	public void startScan() {
		if (mScanning) {
			return;
		}

		if (mBluetoothGatt == null) {
			mScanning = false;
			return;
		}

		mScanning = true;
		mBluetoothGatt.startScan();
	}

	@Override
	public void stopScan() {
		if (!mScanning || mBluetoothGatt == null) {
			return;
		}

		mScanning = false;
		mBluetoothGatt.stopScan();
	}

	@Override
	public boolean adapterEnabled() {
		if (mBtAdapter != null) {
			return mBtAdapter.isEnabled();
		}
		return false;
	}

	@Override
	public boolean connect(String address) {
		BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
		return mBluetoothGatt.connect(device, false);
	}

	@Override
	public void disconnect(String address) {
		BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
		mBluetoothGatt.cancelConnection(device);
	}

	@Override
	public ArrayList<BleGattService> getServices(String address) {
		ArrayList<BleGattService> list = new ArrayList<BleGattService>();
		BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
		List<BluetoothGattService> services = mBluetoothGatt
				.getServices(device);
		for (BluetoothGattService s : services) {
			list.add(new BleGattService(s));
		}
		return list;
	}

	@Override
	public boolean requestReadCharacteristic(String address,
			BleGattCharacteristic characteristic) {
		mService.addBleRequest(new BleRequest(RequestType.READ_CHARACTERISTIC,
				address, characteristic));
		return true;
	}

	@Override
	public boolean discoverServices(String address) {
		return true;
	}

	@Override
	public boolean readCharacteristic(String address,
			BleGattCharacteristic characteristic) {
		if (characteristic.getGattCharacteristicB() != null) {
			return mBluetoothGatt.readCharacteristic(characteristic
					.getGattCharacteristicB());
		}
		return false;
	}

	@Override
	public BleGattService getService(String address, UUID uuid) {
		BluetoothGattService service = mBluetoothGatt.getService(
				mBtAdapter.getRemoteDevice(address), uuid);
		if (service == null) {
			return null;
		} else {
			return new BleGattService(service);
		}
	}

	@Override
	public boolean requestCharacteristicNotification(String address,
			BleGattCharacteristic characteristic) {
		mService.addBleRequest(new BleRequest(
				RequestType.CHARACTERISTIC_NOTIFICATION, address,
				characteristic));
		return true;
	}

	@Override
	public boolean characteristicNotification(String address,
			BleGattCharacteristic characteristic) {
		BleRequest request = mService.getCurrentRequest();
		BluetoothGattCharacteristic b = characteristic.getGattCharacteristicB();

		boolean enable = true;
		if (request.type == RequestType.CHARACTERISTIC_STOP_NOTIFICATION) {
			enable = false;
		}
		if (!mBluetoothGatt.setCharacteristicNotification(b, enable)) {
			return false;
		}

		BluetoothGattDescriptor descriptor = b
				.getDescriptor(BleService.DESC_CCC);
		if (descriptor == null) {
			return false;
		}

		return mBluetoothGatt.readDescriptor(descriptor);
	}

	@Override
	public boolean requestWriteCharacteristic(String address,
			BleGattCharacteristic characteristic, String remark) {
		mService.addBleRequest(new BleRequest(RequestType.WRITE_CHARACTERISTIC,
				address, characteristic));
		return true;
	}

	@Override
	public boolean writeCharacteristic(String address,
			BleGattCharacteristic characteristic) {
		return mBluetoothGatt.writeCharacteristic(characteristic
				.getGattCharacteristicB());
	}

	@Override
	public boolean requestConnect(String address) {
		if (mAddress != null) {
			return false;
		}
		mService.addBleRequest(new BleRequest(RequestType.CONNECT_GATT, address));
		return true;
	}

	@Override
	public String getBTAdapterMacAddr() {
		if (mBtAdapter != null) {
			return mBtAdapter.getAddress();
		}
		return null;
	}

	@Override
	public boolean requestIndication(String address,
			BleGattCharacteristic characteristic) {
		mService.addBleRequest(new BleRequest(
				RequestType.CHARACTERISTIC_INDICATION, address, characteristic));
		return true;
	}

	@Override
	public boolean requestStopNotification(String address,
			BleGattCharacteristic characteristic) {
		mService.addBleRequest(new BleRequest(
				RequestType.CHARACTERISTIC_STOP_NOTIFICATION, address,
				characteristic));
		return true;
	}
}
