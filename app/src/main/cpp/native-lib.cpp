#include "substrate.h"
#include <jni.h>
#include <string>
#include <android/log.h>


#define TAG "AQ"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG,__VA_ARGS__)

MSConfig(MSFilterLibrary, "libdvm.so");

bool (*_dvmLoadNativeCode)(char* pathName, void* classLoader, char** detail);

bool fake_dvmLoadNativeCode(char* soPath, void* classLoader, char** detail){
    LOGD("fake_dvmLoadNativeCode soPath:%s", soPath);
    return _dvmLoadNativeCode(soPath, classLoader, detail);
}

MSInitialize{
    LOGD("Substrate initialized.");
    MSImageRef image;
    image = MSGetImageByName("/system/lib/libdvm.so");
    if(image != NULL)
    {
        LOGD("dvm image: %08X", (void*)image);
        void * dvmload = MSFindSymbol(image, "_Z17dvmLoadNativeCodePKcP6ObjectPPc");
        if(dvmload == NULL)
            LOGD("error find dvmLoadNativeCode.");
        else
        {
            MSHookFunction(dvmload, (void*)&fake_dvmLoadNativeCode,
                           (void**)&_dvmLoadNativeCode);
            LOGD("hook dvmLoadNativeCode sucess.");
        }
    }
    else
        LOGD("can not find libdvm.");
};

extern "C"
JNIEXPORT jstring JNICALL
Java_aq_substratedemo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
