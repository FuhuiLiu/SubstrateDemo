package aq.substratedemo;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import com.saurik.substrate.MS;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by AqCxBoM on 2017/11/26.
 */
public class subMain {
    private static String TAG = "AQ";
    static void initialize(){
        MS.hookClassLoad("aq.hookme.initClass", new MS.ClassLoadHook(){
            @Override
            public void classLoaded(Class<?> aClass) {
                try {
                    Field nValue = aClass.getField("nValue");
                    nValue.setInt(null, 999);

                    Field TAT = aClass.getDeclaredField("TAT");
                    TAT.setAccessible(true);
                    TAT.set(null, "AQ999");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        MS.hookClassLoad("aq.hookme.MainActivity", new MS.ClassLoadHook(){
            @Override
            public void classLoaded(Class<?> aClass) {
                Method myaddNum = null;
                Method myaddStr = null;

                try {
                    Constructor method = aClass.getConstructor();
                    final MS.MethodPointer old = new MS.MethodPointer();

                    MS.hookMethod(aClass, method, new MS.MethodHook(){
                        @Override
                        public Object invoked(Object o, Object... objects) throws Throwable {
                            Log.i(TAG, "MainActivity constructor be call.");
                            return old.invoke(o, objects);
                        }
                    }, old);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    myaddNum = aClass.getMethod("myAdd", Integer.TYPE, int.class);
                    final MS.MethodPointer old = new MS.MethodPointer();
                    myaddStr = aClass.getDeclaredMethod("myAdd", String.class, String.class);
                    final MS.MethodPointer old2 = new MS.MethodPointer();


                    MS.hookMethod(aClass, myaddNum, new MS.MethodHook() {
                        @Override
                        public Object invoked(Object o, Object... objects) throws Throwable {
                            Log.i(TAG, "org method result:" + old.invoke(o, objects));
                            return 4;
                        }
                    }, old);

                    MS.hookMethod(aClass, myaddStr, new MS.MethodAlteration<Object, Object>() {
                        @Override
                        public Object invoked(Object o, Object... objects) throws Throwable {
                            return "MethodAlteration";
                        }
                    });

                    MS.hookMethod(aClass, myaddStr, new MS.MethodHook() {
                        @Override
                        public Object invoked(Object o, Object... objects) throws Throwable {
                            Log.i(TAG, "org method result:" + old2.invoke(o, objects));
                            return "I don't know.";
                        }
                    }, old2);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });

        MS.hookClassLoad("android.telephony.TelephonyManager", new MS.ClassLoadHook(){
            @Override
            public void classLoaded(Class<?> aClass) {
                Method getDeviceID = null;
                try {
                    getDeviceID = aClass.getMethod("getDeviceId");

//                    final MS.MethodPointer old = new MS.MethodPointer();
//                    MS.hookMethod(aClass, getDeviceID, new MS.MethodHook(){
//                        @Override
//                        public Object invoked(Object o, Object... objects) throws Throwable {
//                            Object result;
//                            Log.i(TAG, "hook imei start...");
//                            result = old.invoke(o, objects);
//                            Log.i(TAG, "getDeviceId result:" + result);
//                            result = "fake imei";
//                            return result;
//                        }
//                    }, old);

                    MS.hookMethod(aClass, getDeviceID, new MS.MethodAlteration<Object, Object>() {
                        @Override
                        public Object invoked(Object o, Object... objects) throws Throwable {
                            Object result = "MethodAlteration imei";
                            return result;
                        }
                    });

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static void hookLocation(){

        MS.hookClassLoad("android.net.wifi.WifiManager", new MS.ClassLoadHook(){
            @Override
            public void classLoaded(Class<?> aClass) {
                Log.i(TAG, "WifiManager loading");

                try {
                    Method getScanResult = aClass.getMethod("getScanResults");

                    MS.hookMethod(aClass, getScanResult, new MS.MethodAlteration<Object, Object>(){
                        @Override
                        public Object invoked(Object o, Object... objects) throws Throwable {
                            Log.i(TAG, "getScanResult set null.");
                            return null;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        MS.hookClassLoad("android.telephony.TelephonyManager", new MS.ClassLoadHook(){
            @Override
            public void classLoaded(Class<?> aClass) {
                Log.i(TAG, "TelephonyManager loading");

                try {
                    Method getScanResult = aClass.getMethod("getCellLocation");

                    MS.hookMethod(aClass, getScanResult, new MS.MethodAlteration<Object, Object>(){
                        @Override
                        public Object invoked(Object o, Object... objects) throws Throwable {
                            Log.i(TAG, "getCellLocation set null.");
                            return null;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        MS.hookClassLoad("android.telephony.TelephonyManager", new MS.ClassLoadHook(){
            @Override
            public void classLoaded(Class<?> aClass) {
                Log.i(TAG, "TelephonyManager loading");

                try {
                    Method getScanResult = aClass.getMethod("getNeighboringCellInfo");

                    MS.hookMethod(aClass, getScanResult, new MS.MethodAlteration<Object, Object>(){
                        @Override
                        public Object invoked(Object o, Object... objects) throws Throwable {
                            Log.i(TAG, "getNeighboringCellInfo set null.");
                            return null;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        MS.hookClassLoad("android.location.LocationManager", new MS.ClassLoadHook(){
            @Override
            public void classLoaded(Class<?> aClass) {
                Log.i(TAG, "LocationManager loading");
                try {
                    Method requestLocationUpdates = aClass.getMethod("requestLocationUpdates",
                            String.class, long.class, float.class, LocationListener.class);

                    MS.hookMethod(aClass, requestLocationUpdates, new MS.MethodAlteration<Object, Object>(){
                        @Override
                        public Object invoked(Object o, Object... objects) throws Throwable {
                            Log.i(TAG, "requestLocationUpdates in.");

                            LocationListener ll = (LocationListener)objects[3];

                            Class clazz = LocationListener.class;
                            Method onLocationChanged = null;
                            for (Method method: clazz.getDeclaredMethods()){
                                Log.i(TAG, "" + method.toString());
                                if(method.getName().equals("onLocationChanged")){
                                    onLocationChanged = method;
                                    break;
                                }
                            }

                            try{
                                if( onLocationChanged != null){
                                    Object[] args = new Object[1];
                                    Location l = new Location(LocationManager.GPS_PROVIDER);
                                    double la = 121.53407;
                                    double lo = 25.077796;
                                    l.setLatitude(la);
                                    l.setLongitude(lo);
                                    args[0] = l;
                                    Log.i(TAG, "set 1 " + ll);
                                    onLocationChanged.invoke(ll, args);
                                    Log.i(TAG, "set 2");
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            return invoke(o, objects);

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        MS.hookClassLoad("android.location.LocationManager", new MS.ClassLoadHook(){
            @Override
            public void classLoaded(Class<?> aClass) {
                Log.i(TAG, "LocationManager loading");

                try {
                    Method getGpsStatus = aClass.getMethod("getGpsStatus");

                    MS.hookMethod(aClass, getGpsStatus, new MS.MethodAlteration<Object, Object>(){
                        @Override
                        public Object invoked(Object o, Object... objects) throws Throwable {
                            Log.i(TAG, "getGpsStatus in.");

                            GpsStatus gs = (GpsStatus)invoke(o, objects);
                            if (gs == null)
                                return null;

                            Class<?> clazz = GpsStatus.class;
                            Method m = null;
                            for (Method method : clazz.getDeclaredMethods()) {
                                if (method.getName().equals("setStatus")) {
                                    if (method.getParameterTypes().length > 1) {
                                        m = method;
                                        break;
                                    }
                                }
                            }

                            //access the private setStatus function of GpsStatus
                            m.setAccessible(true);

                            //make the apps belive GPS works fine now
                            int svCount = 5;
                            int[] prns = {1, 2, 3, 4, 5};
                            float[] snrs = {0, 0, 0, 0, 0};
                            float[] elevations = {0, 0, 0, 0, 0};
                            float[] azimuths = {0, 0, 0, 0, 0};
                            int ephemerisMask = 0x1f;
                            int almanacMask = 0x1f;

                            //5 satellites are fixed
                            int usedInFixMask = 0x1f;

                            try {
                                if (m != null) {
                                    m.invoke(gs,svCount, prns, snrs, elevations, azimuths, ephemerisMask, almanacMask, usedInFixMask);
                                    return gs;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
