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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jfyang on 10/16/17.
 */

public class TasksManagerDBOpenHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "tasksmanager.db";
    public final static int DATABASE_VERSION = 2;

    public TasksManagerDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TasksManagerDBController.TABLE_NAME
                + String.format(
                "("
                        + "%s INTEGER PRIMARY KEY, " // id, download id
                        + "%s VARCHAR, " // name
                        + "%s VARCHAR, " // url
                        + "%s VARCHAR " // path
                        + ")"
                , TasksManagerModel.ID
                , TasksManagerModel.NAME
                , TasksManagerModel.URL
                , TasksManagerModel.PATH

        ));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.delete(TasksManagerDBController.TABLE_NAME, null, null);
        }
    }

}
