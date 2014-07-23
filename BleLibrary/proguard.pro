-injars bin/blelibrary.jar
-outjars bin/blelibrary-0.1.jar

-libraryjars /Users/teamx/bin/adt-bundle-mac-x86_64-20130917/sdk/platforms/android-18/android.jar
-libraryjars libs/com.broadcom.bt.jar
-libraryjars libs/commons-codec-1.8.jar
-libraryjars libs/samsung_ble_sdk_200.jar

-target 1.6
-useuniqueclassmembernames
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod
-renamesourcefileattribute SourceFile
-adaptresourcefilenames **.properties
-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF
-verbose


# Keep - Library. Keep all public and protected classes, fields, and methods.
-keep public class * {
    public protected <fields>;
    public protected <methods>;
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}
