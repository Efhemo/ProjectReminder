package com.efhems.newinsideproject.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.efhems.newinsideproject.R;
import com.efhems.newinsideproject.data.local.entities.Project;

import java.util.List;

import static com.efhems.newinsideproject.ui.widget.ProjectReminderAppWidgetProvider.projectList;

/*we must register this service in the manifest file.
*/
public class WidgetService extends RemoteViewsService {

    List<Project> remoteViewProject;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewFactory(getApplicationContext(), intent);
    }


    class RemoteViewFactory implements RemoteViewsService.RemoteViewsFactory{

        Context context = null;

        public RemoteViewFactory(Context context, Intent intent) {
            this.context = context;
        }


        /**
         * Called when your factory is first constructed. The same factory may be shared across
         * multiple RemoteViewAdapters depending on the intent passed.
         */
        @Override
        public void onCreate() {

        }

        /**
         * Called when notifyDataSetChanged() is triggered on the remote adapter. This allows a
         * RemoteViewsFactory to respond to data changes by updating any internal references.
         * <p>
         * Note: expensive tasks can be safely performed synchronously within this method. In the
         * interim, the old data will be displayed within the widget.
         *
         *
         */
        @Override
        public void onDataSetChanged() {

            remoteViewProject = projectList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return remoteViewProject.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view_item);
            views.setTextViewText(R.id.tv_header, String.valueOf(remoteViewProject.get(position).getNameOfProject().charAt(0)));
            views.setTextViewText(R.id.tv_projectName, remoteViewProject.get(position).getNameOfProject());

            Intent intent = new Intent();
            views.setOnClickFillInIntent(R.id.item_wrapper, intent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
