LOCAL_PATH	:= $(call my-dir)/../..
include $(CLEAR_VARS)

LOCAL_C_INCLUDES += \
	$(LOCAL_PATH)/.. \
	$(LOCAL_PATH)/include \
	$(LOCAL_PATH)/include/libuvc

	LOCAL_EXPORT_LDLIBS := -llog

	LOCAL_SRC_FILES := \
    	src/test1.cpp


LOCAL_MODULE := libuvc_static
include $(BUILD_STATIC_LIBRARY)

######################################################################
# libuvc.so
######################################################################
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_EXPORT_LDLIBS += -llog

LOCAL_WHOLE_STATIC_LIBRARIES = libuvc_static
LOCAL_DISABLE_FATAL_LINKER_WARNINGS := true

LOCAL_MODULE := uvc
include $(BUILD_SHARED_LIBRARY)