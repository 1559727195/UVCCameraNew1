//
// Created by zhu on 2019/6/4.
//

#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <assert.h>
#include "../utilbase.h"
#include "UVCCamera.h"


/**
 * set the value into the long field
 * @param env: this param should not be null
 * @param bullet_obj: this param should not be null
 * @param field_name
 * @params val
 */
static jlong setField_long(JNIEnv *env, jobject java_obj, const char *field_name, jlong val) {
#if LOCAL_DEBUG
    LOGV("setField_long:");
#endif

    jclass clazz = env->GetObjectClass(java_obj);
    jfieldID field = env->GetFieldID(clazz, field_name, "J");
    if (LIKELY(field))
        env->SetLongField(java_obj, field, val);
    else {
        LOGE("__setField_long:field '%s' not found", field_name);
    }
#ifdef ANDROID_NDK
    env->DeleteLocalRef(clazz);
#endif
    return val;
}


static ID_TYPE nativeCreate(JNIEnv *env, jobject thiz) {

    ENTER();
    UVCCamera *camera = new UVCCamera();
    setField_long(env, thiz, "mNativePtr", reinterpret_cast<ID_TYPE>(camera));
    RETURN(reinterpret_cast<ID_TYPE>(camera), ID_TYPE);
}

// native側のカメラオブジェクトを破棄
static void nativeDestroy(JNIEnv *env, jobject thiz,
                          ID_TYPE id_camera) {

    ENTER();
    setField_long(env, thiz, "mNativePtr", 0);
    UVCCamera *camera = reinterpret_cast<UVCCamera *>(id_camera);
    if (LIKELY(camera)) {
        SAFE_DELETE(camera);
    }
    EXIT();
}

//======================================================================
// カメラへ接続
static jint nativeConnect(JNIEnv *env, jobject thiz,
                          ID_TYPE id_camera,
                          jint vid, jint pid, jint fd,
                          jint busNum, jint devAddr, jstring usbfs_str) {

    ENTER();
    int result = JNI_ERR;
    UVCCamera *camera = reinterpret_cast<UVCCamera *>(id_camera);
    const char *c_usbfs = env->GetStringUTFChars(usbfs_str, JNI_FALSE);
    if (LIKELY(camera && (fd > 0))) {
//		libusb_set_debug(NULL, LIBUSB_LOG_LEVEL_DEBUG);
        result =  camera->connect(vid, pid, fd, busNum, devAddr, c_usbfs);
    }
    env->ReleaseStringUTFChars(usbfs_str, c_usbfs);
    RETURN(result, jint);
}

// カメラとの接続を解除
static jint nativeRelease(JNIEnv *env, jobject thiz,
                          ID_TYPE id_camera) {

    ENTER();
    int result = JNI_ERR;
    UVCCamera *camera = reinterpret_cast<UVCCamera *>(id_camera);
    if (LIKELY(camera)) {
        result = camera->release();
    }
    RETURN(result, jint);
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
        { "nativeCreate",					"()J", (void *) nativeCreate },
        { "nativeDestroy",					"(J)V", (void *) nativeDestroy },
        { "nativeConnect",					"(JIIIIILjava/lang/String;)I", (void *) nativeConnect },
        { "nativeRelease",					"(J)I", (void *) nativeRelease },
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