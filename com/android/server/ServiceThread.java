/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server;

import android.os.HandlerThread;
import android.os.Process;
import android.os.StrictMode;

/**
 * Special handler thread that we create for system services that require their own loopers.
 */
public class ServiceThread extends HandlerThread {
    private static final String TAG = "ServiceThread";

    private final boolean mAllowIo;

    public ServiceThread(String name, int priority, boolean allowIo) {
        super(name, priority);
        mAllowIo = allowIo;
    }

    @Override
    public void run() {
        //如果传递了后台级线程优先级，则使用 'false' 调用会导致将来对setThreadPriority(int)调用引发异常。
        // 这仅在 JNI 层使用定义为 1 的 GUARD_THREAD_PRIORITY 构建时才有效。
        //进程优化级比GUARD_THREAD_PRIORITY还低时会抛出异常
        Process.setCanSelfBackground(false);

        if (!mAllowIo) {
            StrictMode.initThreadDefaults(null);
        }

        super.run();
    }
}
