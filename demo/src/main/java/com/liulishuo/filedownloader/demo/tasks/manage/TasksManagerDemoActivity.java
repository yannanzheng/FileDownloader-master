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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.demo.R;

import java.lang.ref.WeakReference;

/**
 * Created by Jacksgong on 1/9/16.
 */
public class TasksManagerDemoActivity extends AppCompatActivity {

    private TaskItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_manager_demo);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new TaskItemAdapter());

        TasksManager.getImpl().onCreate(new WeakReference<>(this));
    }

    public void postNotifyDataChanged() {
        if (adapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        TasksManager.getImpl().onDestroy();
        adapter = null;
        FileDownloader.getImpl().pauseAll();
        super.onDestroy();
    }


    // ============================================================================ view adapter ===

//    public static class TaskItemViewHolder extends RecyclerView.ViewHolder {
//        public TaskItemViewHolder(View itemView) {
//            super(itemView);
//            assignViews();
//        }
//
//        private View findViewById(final int id) {
//            return itemView.findViewById(id);
//        }
//
//        /**
//         * viewHolder position
//         */
//        public int position;
//        /**
//         * download id
//         */
//        public int id;
//
//        public void update(final int id, final int position) {
//            this.id = id;
//            this.position = position;
//        }
//
//
//        public void updateDownloaded() {
//            taskPb.setMax(1);
//            taskPb.setProgress(1);
//
//            taskStatusTv.setText(R.string.tasks_manager_demo_status_completed);
//            taskActionBtn.setText(R.string.delete);
//        }
//
//        public void updateNotDownloaded(final int status, final long sofar, final long total) {
//            if (sofar > 0 && total > 0) {
//                final float percent = sofar
//                        / (float) total;
//                taskPb.setMax(100);
//                taskPb.setProgress((int) (percent * 100));
//            } else {
//                taskPb.setMax(1);
//                taskPb.setProgress(0);
//            }
//
//            switch (status) {
//                case FileDownloadStatus.error:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_error);
//                    break;
//                    case FileDownloadStatus.paused:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_paused);
//                    break;
//                default:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_not_downloaded);
//                    break;
//            }
//            taskActionBtn.setText(R.string.start);
//        }
//
//        public void updateDownloading(final int status, final long sofar, final long total) {
//            final float percent = sofar
//                    / (float) total;
//            taskPb.setMax(100);
//            taskPb.setProgress((int) (percent * 100));
//
//            switch (status) {
//                case FileDownloadStatus.pending:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_pending);
//                    break;
//                case FileDownloadStatus.started:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_started);
//                    break;
//                case FileDownloadStatus.connected:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_connected);
//                    break;
//                case FileDownloadStatus.progress:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_progress);
//                    break;
//                default:
//                    taskStatusTv.setText(DemoApplication.CONTEXT.getString(
//                            R.string.tasks_manager_demo_status_downloading, status));
//                    break;
//            }
//
//            taskActionBtn.setText(R.string.pause);
//        }
//
//        public TextView taskNameTv;
//        public TextView taskStatusTv;
//        public ProgressBar taskPb;
//        public Button taskActionBtn;
//
//        private void assignViews() {
//            taskNameTv = (TextView) findViewById(R.id.task_name_tv);
//            taskStatusTv = (TextView) findViewById(R.id.task_status_tv);
//            taskPb = (ProgressBar) findViewById(R.id.task_pb);
//            taskActionBtn = (Button) findViewById(R.id.task_action_btn);
//        }
//
//    }
}
