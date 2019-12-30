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

package android.slc.mp.po;

import android.slc.mp.po.i.IVideoFolder;
import android.slc.mp.po.i.IVideoItem;

import android.slc.medialoader.bean.BaseFolder;

import java.util.List;

/**
 * Created by Taurus on 2016/8/29.
 */
public class VideoFolder extends BaseFolder<IVideoItem> implements IVideoFolder {
    public VideoFolder() {
    }

    public VideoFolder(List<IVideoItem> items) {
        super(items);
    }

    public VideoFolder(String id, String name) {
        super(id, name);
    }

}
