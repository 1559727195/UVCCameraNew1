LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES := \
		$(LOCAL_PATH)/


LOCAL_SRC_FILES := \
          test1.cpp \
          test2.cpp  \
          utilbase.cpp\
          _onload.cpp  \
          serenegiant_usb_UVCCamera.cpp

LOCAL_LDFLAGS += -llog


LOCAL_MODULE    := test2

include $(BUILD_SHARED_LIBRARY)