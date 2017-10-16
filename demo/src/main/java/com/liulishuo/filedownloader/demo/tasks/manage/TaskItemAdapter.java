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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.demo.R;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;

/**
 * Created by jfyang on 10/16/17.
 */

public class TaskItemAdapter extends RecyclerView.Adapter<TasksManagerDemoActivity.TaskItemViewHolder>  {


    public FileDownloadListener taskDownloadListener = new FileDownloadSampleListener() {

        private TasksManagerDemoActivity.TaskItemViewHolder checkCurrentHolder(final BaseDownloadTask task) {
            final TasksManagerDemoActivity.TaskItemViewHolder tag = (TasksManagerDemoActivity.TaskItemViewHolder) task.getTag();
            if (tag.id != task.getId()) {
                return null;
            }

            return tag;
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.pending(task, soFarBytes, totalBytes);
            final TasksManagerDemoActivity.TaskItemViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloading(FileDownloadStatus.pending, soFarBytes
                    , totalBytes);
            tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_pending);
        }

        @Override
        protected void started(BaseDownloadTask task) {
            super.started(task);
            final TasksManagerDemoActivity.TaskItemViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_started);
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
            final TasksManagerDemoActivity.TaskItemViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloading(FileDownloadStatus.connected, soFarBytes
                    , totalBytes);
            tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_connected);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.progress(task, soFarBytes, totalBytes);
            final TasksManagerDemoActivity.TaskItemViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloading(FileDownloadStatus.progress, soFarBytes
                    , totalBytes);
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            super.error(task, e);
            final TasksManagerDemoActivity.TaskItemViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateNotDownloaded(FileDownloadStatus.error, task.getLargeFileSoFarBytes()
                    , task.getLargeFileTotalBytes());
            TasksManager.getImpl().removeTaskForViewHolder(task.getId());
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.paused(task, soFarBytes, totalBytes);
            final TasksManagerDemoActivity.TaskItemViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateNotDownloaded(FileDownloadStatus.paused, soFarBytes, totalBytes);
            tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_paused);
            TasksManager.getImpl().removeTaskForViewHolder(task.getId());
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            super.completed(task);
            final TasksManagerDemoActivity.TaskItemViewHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloaded();
            TasksManager.getImpl().removeTaskForViewHolder(task.getId());
        }
    };
    private View.OnClickListener taskActionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag() == null) {
                return;
            }

            TasksManagerDemoActivity.TaskItemViewHolder holder = (TasksManagerDemoActivity.TaskItemViewHolder) v.getTag();

            CharSequence action = ((TextView) v).getText();
            if (action.equals(v.getResources().getString(R.string.pause))) {
                // to pause
                FileDownloader.getImpl().pause(holder.id);
            } else if (action.equals(v.getResources().getString(R.string.start))) {
                // to start
                // to start
                final TasksManagerModel model = TasksManager.getImpl().get(holder.position);
                final BaseDownloadTask task = FileDownloader.getImpl().create(model.getUrl())
                        .setPath(model.getPath())
                        .setCallbackProgressTimes(100)
                        .setListener(taskDownloadListener);

                TasksManager.getImpl()
                        .addTaskForViewHolder(task);

                TasksManager.getImpl()
                        .updateViewHolder(holder.id, holder);

                task.start();
            } else if (action.equals(v.getResources().getString(R.string.delete))) {
                // to delete
                new File(TasksManager.getImpl().get(holder.position).getPath()).delete();
                holder.taskActionBtn.setEnabled(true);
                holder.updateNotDownloaded(FileDownloadStatus.INVALID_STATUS, 0, 0);
            }
        }
    };

    @Override
    public TasksManagerDemoActivity.TaskItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TasksManagerDemoActivity.TaskItemViewHolder holder = new TasksManagerDemoActivity.TaskItemViewHolder(
                LayoutInflater.from(
                        parent.getContext())
                        .inflate(R.layout.item_tasks_manager, parent, false));

        holder.taskActionBtn.setOnClickListener(taskActionOnClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(TasksManagerDemoActivity.TaskItemViewHolder holder, int position) {
        final TasksManagerModel model = TasksManager.getImpl().get(position);

        holder.update(model.getId(), position);
        holder.taskActionBtn.setTag(holder);
        holder.taskNameTv.setText(model.getName());

        TasksManager.getImpl()
                .updateViewHolder(holder.id, holder);

        holder.taskActionBtn.setEnabled(true);


        if (TasksManager.getImpl().isReady()) {
            final int status = TasksManager.getImpl().getStatus(model.getId(), model.getPath());
            if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started ||
                    status == FileDownloadStatus.connected) {
                // start task, but file not created yet
                holder.updateDownloading(status, TasksManager.getImpl().getSoFar(model.getId())
                        , TasksManager.getImpl().getTotal(model.getId()));
            } else if (!new File(model.getPath()).exists() &&
                    !new File(FileDownloadUtils.getTempPath(model.getPath())).exists()) {
                // not exist file
                holder.updateNotDownloaded(status, 0, 0);
            } else if (TasksManager.getImpl().isDownloaded(status)) {
                // already downloaded and exist
                holder.updateDownloaded();
            } else if (status == FileDownloadStatus.progress) {
                // downloading
                holder.updateDownloading(status, TasksManager.getImpl().getSoFar(model.getId())
                        , TasksManager.getImpl().getTotal(model.getId()));
            } else {
                // not start
                holder.updateNotDownloaded(status, TasksManager.getImpl().getSoFar(model.getId())
                        , TasksManager.getImpl().getTotal(model.getId()));
            }
        } else {
            holder.taskStatusTv.setText(R.string.tasks_manager_demo_status_loading);
            holder.taskActionBtn.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return TasksManager.getImpl().getTaskCounts();
    }

}
