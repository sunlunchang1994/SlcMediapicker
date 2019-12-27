/*
 * Copyright 2016 jiajunhui
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.slc.mp.po;

import com.slc.medialoader.bean.AudioBaseFolder;
import com.slc.mp.po.i.IAudioFolder;
import com.slc.mp.po.i.IAudioItem;

import java.util.List;

/**
 * Created by Taurus on 2016/8/29.
 */
public class AudioFolder extends AudioBaseFolder<IAudioItem> implements IAudioFolder {
    public AudioFolder() {
    }

    public AudioFolder(List<IAudioItem> items) {
        super(items);
    }

    public AudioFolder(String id, String name) {
        super(id, name);
    }

}
