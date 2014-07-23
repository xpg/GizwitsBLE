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
import java.util.UUID;

public interface IBle {

	public String getBTAdapterMacAddr();

	/**
	 * Will receive broadcast {@link BleService#BLE_DEVICE_FOUND} if device
	 * found.
	 */
	public void startScan();

	/**
	 * Stop BLE scan.
	 */
	public void stopScan();

	/**
	 * Check if bluetooth adapter is enabled.
	 * 
	 * @return enabled
	 */
	public boolean adapterEnabled();

	/**
	 * Disconnect BLE device. Will receive
	 * {@link BleService#BLE_GATT_DISCONNECTED} broadcast if device
	 * disconnected.
	 * 
	 * @param address
	 *            BLE device address.
	 */
	public void disconnect(String address);

	/**
	 * Discover BLE services. Will receive
	 * {@link BleService#BLE_SERVICE_DISCOVERED} broadcast if device service
	 * discovered.
	 * 
	 * @param address
	 * @return
	 */
	public boolean discoverServices(String address);

	/**
	 * Get discovered services for BLE device. Call this function after
	 * {@link BleService#BLE_SERVICE_DISCOVERED} broadcast is received.
	 * 
	 * @param address
	 * @return List of {@link BleGattService}
	 */
	public ArrayList<BleGattService> getServices(String address);

	/**
	 * Get discovered service by uuid. Call this function after
	 * {@link BleService#BLE_SERVICE_DISCOVERED} broadcast is received.
	 * 
	 * @param address
	 * @param uuid
	 * @return {@link BleGattService}
	 */
	public BleGattService getService(String address, UUID uuid);

	/**
	 * Request to connect a BLE device by address. Will receive
	 * {@link BleService#BLE_GATT_CONNECTED} broadcast if device connected.
	 * 
	 * @param address
	 * @return if request be inserted into queue successfully.
	 */
	public boolean requestConnect(String address);

	/**
	 * Request to read characteristic. Will receive
	 * {@link BleService#BLE_CHARACTERISTIC_READ} broadcast if characteristic
	 * read.
	 * 
	 * @param address
	 * @param characteristic
	 *            Get characteristic from {@link BleGattService}
	 * @return if request be inserted into queue successfully.
	 */
	public boolean requestReadCharacteristic(String address,
			BleGattCharacteristic characteristic);

	/**
	 * Request characteristic notification. Will receive
	 * {@link BleService#BLE_CHARACTERISTIC_NOTIFICATION} broadcast if
	 * notification set OK. When the characteristic's value changed,
	 * {@link BleService#BLE_CHARACTERISTIC_CHANGED} broadcast will be received
	 * also.
	 * 
	 * @param address
	 * @param characteristic
	 *            Get characteristic from {@link BleGattService}
	 * @return if request be inserted into queue successfully.
	 */
	public boolean requestCharacteristicNotification(String address,
			BleGattCharacteristic characteristic);

	public boolean requestStopNotification(String address,
			BleGattCharacteristic characteristic);

	/**
	 * Request characteristic indication. Will receive
	 * {@link BleService#BLE_CHARACTERISTIC_INDICATION} broadcast if indication
	 * set OK. When the characteristic's value changed,
	 * {@link BleService#BLE_CHARACTERISTIC_CHANGED} broadcast will be received
	 * also.
	 * 
	 * @param address
	 * @param characteristic
	 *            Get characteristic from {@link BleGattService}
	 * @return if request be inserted into queue successfully.
	 */
	public boolean requestIndication(String address,
			BleGattCharacteristic characteristic);

	/**
	 * Request write characteristic value. Will receive
	 * {@link BleService#BLE_CHARACTERISTIC_WRITE} broadcast if characteristic
	 * value be written.
	 * 
	 * @param address
	 * @param characteristic
	 *            Get characteristic from {@link BleGattService}
	 * @param remark
	 *            For debug purpose.
	 * @return if request be inserted into queue successfully.
	 */
	public boolean requestWriteCharacteristic(String address,
			BleGattCharacteristic characteristic, String remark);
}
