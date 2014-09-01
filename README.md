GizwitsBLE
==========

GizwitsBLE solves the compatibility and reliability issues that have troubled developers. First, many BLE-ready Android phones (e.g. Samsung S2 and Note2) have not been upgraded to Android 4.3. Therefore adding Android app-level BLE support requires developers to code against a number of vendor specific BLE SDK's. Second, the Android 4.3 native BLE support has a number of pitfalls that seriously affect user experience. Since android users rarely upgrade their Operating Systems, Android 4.2 and 4.3 will continue to dominate the Android smartphone market for the next few years. 

GizwitsBLE Android SDK was build to be a quick and reliable solution for developers to add robust BLE support for Android 4.2 and Android 4.3+ smartphones with a simple common interface. On Android 4.2 phones, GizwitsBLE automatically detects and adapts to the corresponding vendor specific SDK (e.g. those provided by Samsung and Broadcom), so that developers don't have to learn and code against the specifies of each implementation. 

On Android 4.3 phones, BLE related calls tends to crash the app and even the Operating System for no apparent reasons. Through trial and error, the Gizwits team has found that BLE calls must be made serially to avoid system crashes, and therefore all  requests (connection, discovery service, read or write characteristics) are queued and executed sequentially. This has made BLE communications much more stable. 

GizwitsBLE has a simple interface that's easy to learn and is open sourced. It is also been heavily tested and is commercially ready. It powers dozens of BLE products. We encourage all developers frustrated by the fragmented and inconsistent BLE support on try it out.

# Main Features
* Support Android 4.2 (Broadcom, Sumsung SDK) and Android 4.3+ 
* Built in Service, just start the service and receive BLE events by broadcast
* Built in BLE request queue

