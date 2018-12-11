#include <jni.h>
#include <string.h>
#include <fftw3.h>
#include <math.h>

extern "C" JNIEXPORT jdoubleArray JNICALL Java_com_example_admin_padometer_MainActivity_fftnative(
        JNIEnv *env,
        jobject,
        jdoubleArray data,
        jint N) {

    fftw_complex *in , *out;
    fftw_plan p;

    in = (fftw_complex*) fftw_malloc(sizeof(fftw_complex) * N);
    out = (fftw_complex*) fftw_malloc(sizeof(fftw_complex) * N);

    for (int i = 0; i < N; i++) {
        in[i][0] = 0;
        in[i][1] = 0;
        out[i][0] = 0;
        out[i][1] = 0;
    }

    jdouble *doubleArray = env->GetDoubleArrayElements(data, NULL);
    int datalen = env -> GetArrayLength(data);
    for (int i = 0; i < datalen; i++) {
        in[i][0] = doubleArray[i];
    }

    p = fftw_plan_dft_1d(N, in, out, FFTW_FORWARD, FFTW_ESTIMATE);
    fftw_execute(p);

    jdouble mag[N];
    int counter=0;
    for (int i = 0; i < N; i++) {
        double real = out[i][0];
        double imag = out[i][1];

        mag[i] = sqrt((real*real)+(imag*imag));
    }

    jdoubleArray result;
    result = env->NewDoubleArray(N);
    env->SetDoubleArrayRegion(result, 0, N, mag);

    fftw_destroy_plan(p);
    fftw_free(in); fftw_free(out);

    return result;
}


extern "C" JNIEXPORT jobjectArray JNICALL Java_com_example_admin_padometer_MainActivity_fftcomplexoutnative(
        JNIEnv *env,
        jobject,
        jdoubleArray data,
        jint N) {

    fftw_complex *in , *out;
    fftw_plan p;

    in = (fftw_complex*) fftw_malloc(sizeof(fftw_complex) * N);
    out = (fftw_complex*) fftw_malloc(sizeof(fftw_complex) * N);

    for (int i = 0; i < N; i++) {
        in[i][0] = 0;
        in[i][1] = 0;
        out[i][0] = 0;
        out[i][1] = 0;
    }

    jdouble *doubleArray = env->GetDoubleArrayElements(data, NULL);
    int datalen = env -> GetArrayLength(data);
    for (int i = 0; i < datalen; i++) {
        in[i][0] = doubleArray[i];
    }

    p = fftw_plan_dft_1d(N, in, out, FFTW_FORWARD, FFTW_ESTIMATE);
    fftw_execute(p);

    jdouble real[N];
    jdouble imag[N];
    for (int i = 0; i < N; i++) {
        real[i] = out[i][0];
        imag[i] = out[i][1];
    }

    jdoubleArray realResult;
    jdoubleArray imagResult;
    realResult = env->NewDoubleArray(N);
    imagResult = env->NewDoubleArray(N);
    env->SetDoubleArrayRegion(realResult, 0, N, real);
    env->SetDoubleArrayRegion(imagResult, 0, N, imag);

    jobjectArray outarray = env->NewObjectArray(2, env->GetObjectClass(realResult), 0);
    env->SetObjectArrayElement(outarray, 0, realResult);
    env->SetObjectArrayElement(outarray, 1, imagResult);

    fftw_destroy_plan(p);
    fftw_free(in); fftw_free(out);

    return outarray;
}

extern "C" JNIEXPORT jdoubleArray JNICALL Java_com_example_admin_padometer_MainActivity_ifftnative(
        JNIEnv *env,
        jobject,
        jobjectArray data) {

    jdoubleArray real = (jdoubleArray) env->GetObjectArrayElement(data, 0);
    jdoubleArray imag = (jdoubleArray) env->GetObjectArrayElement(data, 1);

    jint N = env -> GetArrayLength(real);

    fftw_complex *in , *out;
    fftw_plan p;

    in = (fftw_complex*) fftw_malloc(sizeof(fftw_complex) * N);
    out = (fftw_complex*) fftw_malloc(sizeof(fftw_complex) * N);

    for (int i = 0; i < N; i++) {
        in[i][0] = 0;
        in[i][1] = 0;
        out[i][0] = 0;
        out[i][1] = 0;
    }

    jdouble *realArray = env->GetDoubleArrayElements(real, NULL);
    jdouble *imagArray = env->GetDoubleArrayElements(imag, NULL);
    for (int i = 0; i < N; i++) {
        in[i][0] = realArray[i];
    }
    for (int i = 0; i < N; i++) {
        in[i][1] = imagArray[i];
    }

    p = fftw_plan_dft_1d(N, in, out, FFTW_BACKWARD, FFTW_ESTIMATE);
    fftw_execute(p);

    jdouble realout[N/2];
    int counter = 0;
    for (int i = 0; i < N; i+=2) {
        realout[counter++] = out[i][0];
    }

    jdoubleArray result;
    result = env->NewDoubleArray(N/2);
    env->SetDoubleArrayRegion(result, 0, N/2, realout);

    fftw_destroy_plan(p);
    fftw_free(in); fftw_free(out);

    return result;
}
