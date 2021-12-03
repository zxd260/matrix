package com.tencent.matrix.openglleak.hook;

import android.opengl.EGL14;

import com.tencent.matrix.openglleak.comm.FuncNameString;
import com.tencent.matrix.openglleak.statistics.BindCenter;
import com.tencent.matrix.openglleak.statistics.resource.MemoryInfo;
import com.tencent.matrix.openglleak.statistics.resource.OpenGLID;
import com.tencent.matrix.openglleak.statistics.resource.OpenGLInfo;
import com.tencent.matrix.openglleak.statistics.resource.ResRecordManager;
import com.tencent.matrix.openglleak.utils.ActivityRecorder;
import com.tencent.matrix.util.MatrixLog;

import java.util.concurrent.atomic.AtomicInteger;

public class OpenGLHook {

    static {
        System.loadLibrary("matrix-opengl-leak");
    }

    private static final String TAG = "MicroMsg.OpenGLHook";

    private static OpenGLHook mInstance = new OpenGLHook();
    private ResourceListener mResourceListener;
    private ErrorListener mErrorListener;
    private BindListener mBindListener;
    private MemoryListener mMemoryListener;

    private OpenGLHook() {
    }

    public static OpenGLHook getInstance() {
        return mInstance;
    }

    public void setResourceListener(ResourceListener listener) {
        mResourceListener = listener;
    }

    public void setErrorListener(ErrorListener listener) {
        mErrorListener = listener;
    }

    public void setBindListener(BindListener listener) {
        mBindListener = listener;
    }

    public void setMemoryListener(MemoryListener listener) {
        mMemoryListener = listener;
    }

    public boolean hook(String targetFuncName, int index) {
        switch (targetFuncName) {
            case FuncNameString.GL_GET_ERROR:
                return hookGlGetError(index);
            case FuncNameString.GL_GEN_TEXTURES:
                return hookGlGenTextures(index);
            case FuncNameString.GL_DELETE_TEXTURES:
                return hookGlDeleteTextures(index);
            case FuncNameString.GL_GEN_BUFFERS:
                return hookGlGenBuffers(index);
            case FuncNameString.GL_DELETE_BUFFERS:
                return hookGlDeleteBuffers(index);
            case FuncNameString.GL_GEN_FRAMEBUFFERS:
                return hookGlGenFramebuffers(index);
            case FuncNameString.GL_DELETE_FRAMEBUFFERS:
                return hookGlDeleteFramebuffers(index);
            case FuncNameString.GL_GEN_RENDERBUFFERS:
                return hookGlGenRenderbuffers(index);
            case FuncNameString.GL_DELETE_RENDERBUFFERS:
                return hookGlDeleteRenderbuffers(index);
            case FuncNameString.GL_TEX_IMAGE_2D:
                return hookGlTexImage2D(index);
            case FuncNameString.GL_TEX_IMAGE_3D:
                return hookGlTexImage3D(index);
            case FuncNameString.GL_BIND_TEXTURE:
                return hookGlBindTexture(index);
            case FuncNameString.GL_BIND_BUFFER:
                return hookGlBindBuffer(index);
            case FuncNameString.GL_BIND_FRAMEBUFFER:
                return hookGlBindFramebuffer(index);
            case FuncNameString.GL_BIND_RENDERBUFFER:
                return hookGlBindRenderbuffer(index);
            case FuncNameString.GL_BUFFER_DATA:
                return hookGlBufferData(index);
            case FuncNameString.GL_RENDER_BUFFER_STORAGE:
                return hookGlRenderbufferStorage(index);
        }

        return false;
    }

    public native boolean init();

    public native void setNativeStackDump(boolean open);

    public native void setJavaStackDump(boolean open);

    private static native boolean hookGlGenTextures(int index);

    private static native boolean hookGlDeleteTextures(int index);

    private static native boolean hookGlGenBuffers(int index);

    private static native boolean hookGlDeleteBuffers(int index);

    private static native boolean hookGlGenFramebuffers(int index);

    private static native boolean hookGlDeleteFramebuffers(int index);

    private static native boolean hookGlGenRenderbuffers(int index);

    private static native boolean hookGlDeleteRenderbuffers(int index);

    private static native boolean hookGlGetError(int index);

    private static native boolean hookGlTexImage2D(int index);

    private static native boolean hookGlTexImage3D(int index);

    private static native boolean hookGlBindTexture(int index);

    private static native boolean hookGlBindBuffer(int index);

    private static native boolean hookGlBindFramebuffer(int index);

    private static native boolean hookGlBindRenderbuffer(int index);

    private static native boolean hookGlBufferData(int index);

    private static native boolean hookGlRenderbufferStorage(int index);

    public static void onGlGenTextures(int[] ids, String threadId, String javaStack, long nativeStackPtr) {
        if (ids.length > 0) {
            AtomicInteger counter = new AtomicInteger(ids.length);

            long eglContextId = 0L;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
            }

            for (int id : ids) {
                OpenGLInfo openGLInfo = new OpenGLInfo(OpenGLInfo.TYPE.TEXTURE, id, threadId, eglContextId, javaStack, nativeStackPtr, true, ActivityRecorder.getInstance().getCurrentActivityInfo(), counter);
                ResRecordManager.getInstance().gen(openGLInfo);

                if (getInstance().mResourceListener != null) {
                    getInstance().mResourceListener.onGlGenTextures(openGLInfo);
                }
            }
        }
    }

    public static void onGlDeleteTextures(int[] ids, String threadId) {
        if (ids.length > 0) {

            long eglContextId = 0L;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
            }

            for (int id : ids) {
                OpenGLInfo openGLInfo = new OpenGLInfo(OpenGLInfo.TYPE.TEXTURE, id, threadId, eglContextId, false);
                ResRecordManager.getInstance().delete(openGLInfo);

                if (getInstance().mResourceListener != null) {
                    getInstance().mResourceListener.onGlDeleteTextures(openGLInfo);
                }
            }
        }
    }

    public static void onGlGenBuffers(int[] ids, String threadId, String javaStack, long nativeStackPtr) {
        if (ids.length > 0) {
            AtomicInteger counter = new AtomicInteger(ids.length);

            long eglContextId = 0L;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
            }

            for (int id : ids) {
                OpenGLInfo openGLInfo = new OpenGLInfo(OpenGLInfo.TYPE.BUFFER, id, threadId, eglContextId, javaStack, nativeStackPtr, true, ActivityRecorder.getInstance().getCurrentActivityInfo(), counter);
                ResRecordManager.getInstance().gen(openGLInfo);

                if (getInstance().mResourceListener != null) {
                    getInstance().mResourceListener.onGlGenBuffers(openGLInfo);
                }
            }
        }
    }

    public static void onGlDeleteBuffers(int[] ids, String threadId) {
        if (ids.length > 0) {

            long eglContextId = 0L;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
            }

            for (int id : ids) {
                OpenGLInfo openGLInfo = new OpenGLInfo(OpenGLInfo.TYPE.BUFFER, id, threadId, eglContextId, false);
                ResRecordManager.getInstance().delete(openGLInfo);

                if (getInstance().mResourceListener != null) {
                    getInstance().mResourceListener.onGlDeleteBuffers(openGLInfo);
                }
            }
        }
    }

    public static void onGlGenFramebuffers(int[] ids, String threadId, String javaStack, long nativeStackPtr) {
        if (ids.length > 0) {
            AtomicInteger counter = new AtomicInteger(ids.length);

            long eglContextId = 0L;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
            }

            for (int id : ids) {
                OpenGLInfo openGLInfo = new OpenGLInfo(OpenGLInfo.TYPE.FRAME_BUFFERS, id, threadId, eglContextId, javaStack, nativeStackPtr, true, ActivityRecorder.getInstance().getCurrentActivityInfo(), counter);
                ResRecordManager.getInstance().gen(openGLInfo);

                if (getInstance().mResourceListener != null) {
                    getInstance().mResourceListener.onGlGenFramebuffers(openGLInfo);
                }
            }
        }
    }

    public static void onGlDeleteFramebuffers(int[] ids, String threadId) {
        if (ids.length > 0) {

            long eglContextId = 0L;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
            }

            for (int id : ids) {
                OpenGLInfo openGLInfo = new OpenGLInfo(OpenGLInfo.TYPE.FRAME_BUFFERS, id, threadId, eglContextId, false);
                ResRecordManager.getInstance().delete(openGLInfo);

                if (getInstance().mResourceListener != null) {
                    getInstance().mResourceListener.onGlDeleteFramebuffers(openGLInfo);
                }
            }
        }
    }

    public static void onGlGenRenderbuffers(int[] ids, String threadId, String javaStack, long nativeStackPtr) {
        if (ids.length > 0) {
            AtomicInteger counter = new AtomicInteger(ids.length);

            long eglContextId = 0L;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
            }

            for (int id : ids) {
                OpenGLInfo openGLInfo = new OpenGLInfo(OpenGLInfo.TYPE.RENDER_BUFFERS, id, threadId, eglContextId, javaStack, nativeStackPtr, true, ActivityRecorder.getInstance().getCurrentActivityInfo(), counter);
                ResRecordManager.getInstance().gen(openGLInfo);

                if (getInstance().mResourceListener != null) {
                    getInstance().mResourceListener.onGlGenRenderbuffers(openGLInfo);
                }
            }
        }
    }

    public static void onGlDeleteRenderbuffers(int[] ids, String threadId) {
        if (ids.length > 0) {

            long eglContextId = 0L;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
            }

            for (int id : ids) {
                OpenGLInfo openGLInfo = new OpenGLInfo(OpenGLInfo.TYPE.RENDER_BUFFERS, id, threadId, eglContextId, false);
                ResRecordManager.getInstance().delete(openGLInfo);

                if (getInstance().mResourceListener != null) {
                    getInstance().mResourceListener.onGlDeleteRenderbuffers(openGLInfo);
                }
            }
        }
    }

    public static void onGetError(int eid) {
        if (getInstance().mErrorListener != null) {
            getInstance().mErrorListener.onGlError(eid);
        }
    }


    public static void onGlBindTexture(int target, int id) {
        long eglContextId = 0L;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
        }
        OpenGLID openGLID = new OpenGLID(eglContextId, id);
        BindCenter.getInstance().glBindResource(OpenGLInfo.TYPE.TEXTURE, target, openGLID);

        if (getInstance().mBindListener != null) {
            getInstance().mBindListener.onGlBindTexture(target, eglContextId, id);
        }
    }

    public static void onGlBindBuffer(int target, int id) {
        long eglContextId = 0L;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
        }
        OpenGLID openGLID = new OpenGLID(eglContextId, id);
        BindCenter.getInstance().glBindResource(OpenGLInfo.TYPE.BUFFER, target, openGLID);

        if (getInstance().mBindListener != null) {
            getInstance().mBindListener.onGlBindBuffer(target, eglContextId, id);
        }
    }

    public static void onGlBindFramebuffer(int target, int id) {
        long eglContextId = 0L;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
        }
        OpenGLID openGLID = new OpenGLID(eglContextId, id);
        BindCenter.getInstance().glBindResource(OpenGLInfo.TYPE.FRAME_BUFFERS, target, openGLID);

        if (getInstance().mBindListener != null) {
            getInstance().mBindListener.onGlBindFramebuffer(target, eglContextId, id);
        }
    }

    public static void onGlBindRenderbuffer(int target, int id) {
        long eglContextId = 0L;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
        }
        OpenGLID openGLID = new OpenGLID(eglContextId, id);
        BindCenter.getInstance().glBindResource(OpenGLInfo.TYPE.RENDER_BUFFERS, target, openGLID);

        if (getInstance().mBindListener != null) {
            getInstance().mBindListener.onGlBindRenderbuffer(target, eglContextId, id);
        }
    }

    public static void onGlTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, long size, String javaStack, long nativeStack) {
        long eglContextId = 0L;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
        }
        OpenGLID openGLID = BindCenter.getInstance().findCurrentResourceIdByTarget(OpenGLInfo.TYPE.TEXTURE, eglContextId, target);
        if (openGLID == null) {
            MatrixLog.e(TAG, "onGlTexImage2D: getCurrentResourceIdByTarget openGLID == null");
            return;
        }
        OpenGLInfo openGLInfo = ResRecordManager.getInstance().findOpenGLInfo(OpenGLInfo.TYPE.TEXTURE, openGLID.getEglContextNativeHandle(), openGLID.getId());
        if (openGLInfo != null) {
            MemoryInfo memoryInfo = new MemoryInfo();
            memoryInfo.setTexturesInfo(target, level, internalFormat, width, height, 0, border, format, type, openGLID.getId(), openGLID.getEglContextNativeHandle(), size, javaStack, nativeStack);
            openGLInfo.setMemoryInfo(memoryInfo);
        }

        if (getInstance().mMemoryListener != null) {
            getInstance().mMemoryListener.onGlTexImage2D(target, level, internalFormat, width, height, border, format, type, openGLID.getId(), openGLID.getEglContextNativeHandle(), size);
        }

    }

    public static void onGlTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, long size, String javaStack, long nativeStack) {
        long eglContextId = 0L;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
        }
        OpenGLID openGLID = BindCenter.getInstance().findCurrentResourceIdByTarget(OpenGLInfo.TYPE.TEXTURE, eglContextId, target);
        if (openGLID == null) {
            MatrixLog.e(TAG, "onGlTexImage3D: getCurrentResourceIdByTarget result == null");
            return;
        }
        OpenGLInfo openGLInfo = ResRecordManager.getInstance().findOpenGLInfo(OpenGLInfo.TYPE.TEXTURE, openGLID.getEglContextNativeHandle(), openGLID.getId());
        if (openGLInfo != null) {
            MemoryInfo memoryInfo = new MemoryInfo();
            memoryInfo.setTexturesInfo(target, level, internalFormat, width, height, depth, border, format, type, openGLID.getId(), openGLID.getEglContextNativeHandle(), size, javaStack, nativeStack);
            openGLInfo.setMemoryInfo(memoryInfo);
        }

        if (getInstance().mMemoryListener != null) {
            getInstance().mMemoryListener.onGlTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, openGLID.getId(), openGLID.getEglContextNativeHandle(), size);
        }

    }

    public static void onGlBufferData(int target, long size, int usage, String javaStack, long nativeStack) {
        if (Thread.currentThread().getName().equals("RenderThread")) {
            return;
        }
        long eglContextId = 0L;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
        }
        OpenGLID openGLID = BindCenter.getInstance().findCurrentResourceIdByTarget(OpenGLInfo.TYPE.BUFFER, eglContextId, target);
        if (openGLID == null) {
            MatrixLog.e(TAG, "onGlBufferData: getCurrentResourceIdByTarget result == null");
            return;
        }
        OpenGLInfo openGLInfo = ResRecordManager.getInstance().findOpenGLInfo(OpenGLInfo.TYPE.BUFFER, openGLID.getEglContextNativeHandle(), openGLID.getId());
        if (openGLInfo != null) {
            MemoryInfo memoryInfo = new MemoryInfo();
            memoryInfo.setBufferInfo(target, usage, openGLID.getId(), openGLID.getEglContextNativeHandle(), size, javaStack, nativeStack);
            openGLInfo.setMemoryInfo(memoryInfo);
        }

        if (getInstance().mMemoryListener != null) {
            getInstance().mMemoryListener.onGlBufferData(target, usage, openGLID.getId(), openGLID.getEglContextNativeHandle(), size);
        }

    }

    public static void onGlRenderbufferStorage(int target, int internalformat, int width, int height, long size, String javaStack, long nativeStack) {
        long eglContextId = 0L;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            eglContextId = EGL14.eglGetCurrentContext().getNativeHandle();
        }
        OpenGLID openGLID = BindCenter.getInstance().findCurrentResourceIdByTarget(OpenGLInfo.TYPE.RENDER_BUFFERS, eglContextId, target);
        if (openGLID == null) {
            MatrixLog.e(TAG, "onGlRenderbufferStorage: getCurrentResourceIdByTarget result == null");
            return;
        }
        OpenGLInfo openGLInfo = ResRecordManager.getInstance().findOpenGLInfo(OpenGLInfo.TYPE.RENDER_BUFFERS, openGLID.getEglContextNativeHandle(), openGLID.getId());
        if (openGLInfo != null) {
            MemoryInfo memoryInfo = new MemoryInfo();
            memoryInfo.setRenderbufferInfo(target, width, height, internalformat, openGLID.getId(), openGLID.getEglContextNativeHandle(), size, javaStack, nativeStack);
            openGLInfo.setMemoryInfo(memoryInfo);
        }

        if (getInstance().mMemoryListener != null) {
            getInstance().mMemoryListener.onGlRenderbufferStorage(target, width, height, internalformat, openGLID.getId(), openGLID.getEglContextNativeHandle(), size);
        }
    }

    public interface ErrorListener {

        void onGlError(int eid);

    }

    public interface BindListener {

        void onGlBindTexture(int target, long eglContextId, int id);

        void onGlBindBuffer(int target, long eglContextId, int id);

        void onGlBindRenderbuffer(int target, long eglContextId, int id);

        void onGlBindFramebuffer(int target, long eglContextId, int id);

    }

    public interface MemoryListener {

        void onGlTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, int id, long eglContextId, long size);

        void onGlTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, int id, long eglContextId, long size);

        void onGlBufferData(int target, int usage, int id, long eglContextId, long size);

        void onGlRenderbufferStorage(int target, int width, int height, int internalFormat, int id, long eglContextId, long size);

    }

    public interface ResourceListener {

        void onGlGenTextures(OpenGLInfo info);

        void onGlDeleteTextures(OpenGLInfo info);

        void onGlGenBuffers(OpenGLInfo info);

        void onGlDeleteBuffers(OpenGLInfo info);

        void onGlGenFramebuffers(OpenGLInfo info);

        void onGlDeleteFramebuffers(OpenGLInfo info);

        void onGlGenRenderbuffers(OpenGLInfo info);

        void onGlDeleteRenderbuffers(OpenGLInfo info);
    }

    public static String getStack() {
        return stackTraceToString(new Throwable().getStackTrace());
    }

    private static String stackTraceToString(final StackTraceElement[] arr) {
        if (arr == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            if (i == 0) {
                continue;
            }
            StackTraceElement element = arr[i];
            String className = element.getClassName();
            // remove unused stacks
            if (className.contains("java.lang.Thread")) {
                continue;
            }
            sb.append("\t").append(element).append('\n');
        }
        return sb.toString();
    }

}
