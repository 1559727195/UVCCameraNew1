//
// Created by zhu on 2019/6/4.
//

#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <assert.h>
#include "../utilbase.h"


//实现
jstring getStr(JNIEnv *jniEnv, jobject ob) {
    return jniEnv->NewStringUTF(
            "动态注册JNI test");
}

jint addInt(JNIEnv *jniEnv, jobject ob, jint a, jint b) {
    return a + b;
}


//**********************************************************************
//
//**********************************************************************
jint registerNativeMethods(JNIEnv *env, const char *class_name, JNINativeMethod *methods,
                           int num_methods) {
    int result = 0;

    jclass clazz = env->FindClass(class_name);
    if (LIKELY(clazz)) {
        int result = env->RegisterNatives(clazz, methods, num_methods);
        if (UNLIKELY(result < 0)) {
            LOGE("registerNativeMethods failed(class=%s)", class_name);
        }
    } else {
        LOGE("registerNativeMethods: class'%s' not found", class_name);
    }
    return result;
}

static JNINativeMethod methods[] = {
        {"getStr", "()Ljava/lang/String;", (void *) getStr},
        {"addInt",    "(II)I",                (void *) addInt},
};

int register_uvccamera(JNIEnv *env) {
    LOGV("register_uvccamera:");
    if (registerNativeMethods(env,
                              "com/serenegiant/usb/UVCCamera",
                              methods, NUM_ARRAY_ELEMENTS(methods)) < 0) {
        return -1;
    }
    return 0;
}