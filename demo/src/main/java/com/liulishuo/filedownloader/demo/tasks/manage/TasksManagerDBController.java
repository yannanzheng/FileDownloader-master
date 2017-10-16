/*
 * Copyright (c) 2015 LingoChamp Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liulishuo.filedownloader.demo.tasks.manage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.liulishuo.filedownloader.demo.DemoApplication;
import com.liulishuo.filedownloader.demo.R;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jfyang on 10/16/17.
 */

public class TasksManagerDBController {
    public final static String TABLE_NAME = "tasksmanger";
    private final SQLiteDatabase db;

    public TasksManagerDBController() {
        TasksManagerDBOpenHelper openHelper = new TasksManagerDBOpenHelper(DemoApplication.CONTEXT);

        db = openHelper.getWritableDatabase();
    }

    public List<TasksManagerModel> getAllTasks() {
        final Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        final List<TasksManagerModel> list = new ArrayList<>();
        try {
            if (!c.moveToLast()) {
                return list;
            }

            do {
                TasksManagerModel model = new TasksManagerModel();
                model.setId(c.getInt(c.getColumnIndex(TasksManagerModel.ID)));
                model.setName(c.getString(c.getColumnIndex(TasksManagerModel.NAME)));
                model.setUrl(c.getString(c.getColumnIndex(TasksManagerModel.URL)));
                model.setPath(c.getString(c.getColumnIndex(TasksManagerModel.PATH)));
                list.add(model);
            } while (c.moveToPrevious());
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return list;
    }

    public TasksManagerModel addTask(final String url, final String path) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }

        // have to use FileDownloadUtils.generateId to associate TasksManagerModel with FileDownloader
        final int id = FileDownloadUtils.generateId(url, path);

        TasksManagerModel model = new TasksManagerModel();
        model.setId(id);
        model.setName(DemoApplication.CONTEXT.getString(R.string.tasks_manager_demo_name, id));
        model.setUrl(url);
        model.setPath(path);

        final boolean succeed = db.insert(TABLE_NAME, null, model.toContentValues()) != -1;
        return succeed ? model : null;
    }

}
