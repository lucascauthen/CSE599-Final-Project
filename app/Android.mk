LOCAL_PATH := $(call /home/justin/)

include $(CLEAR_VARS)
LOCAL_MODULE := fftw3
LOCAL_SRC_FILES := /Users/admin/Documents/GitHub/Recorder/app/lib/$(TARGET_ARCH_ABI)/libfftw3.a
LOCAL_EXPORT_C_INCLUDES := /Users/admin/Documents/GitHub/Recorder/app/include
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := native-lib
LOCAL_SRC_FILES := /Users/admin/Documents/GitHub/Recorder/app/src/main/cpp/native-lib.cpp
LOCAL_LDLIBS := -llog -lm
LOCAL_STATIC_LIBRARIES := fftw3
include $(BUILD_SHARED_LIBRARY)
