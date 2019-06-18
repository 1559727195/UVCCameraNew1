LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES := \
		$(LOCAL_PATH)/


LOCAL_SRC_FILES := \
          utilbase.cpp\
          _onload.cpp  \
          serenegiant_usb_UVCCamera.cpp \
          UVCCamera.cpp \

LOCAL_LDFLAGS += -llog


LOCAL_MODULE    := UVCCamera

include $(BUILD_SHARED_LIBRARY)