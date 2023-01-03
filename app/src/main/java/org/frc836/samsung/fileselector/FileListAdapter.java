/*
 * This class taken from the Samsung tutorial on File Dialogs.
 * http://developer.samsung.com/android/technical-docs/Implementing-a-file-selector-dialog
 */
package org.frc836.samsung.fileselector;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.growingstems.scouting.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter used to display a files list
 */
public class FileListAdapter extends BaseAdapter {

    /**
     * Array of FileData objects that will be used to display a list
     */
    private final ArrayList<FileData> mFileDataArray;

    private final Context mContext;

    public FileListAdapter(Context context, List<FileData> aFileDataArray) {
        mFileDataArray = (ArrayList<FileData>) aFileDataArray;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mFileDataArray.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileDataArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileData tempFileData = mFileDataArray.get(position);
        TextViewWithImage tempView = new TextViewWithImage(mContext);
        tempView.setText(tempFileData.getFileName());
        int imgRes = -1;
        switch (tempFileData.getFileType()) {
            case FileData.UP_FOLDER: {
                imgRes = R.drawable.up_folder;
                break;
            }
            case FileData.DIRECTORY: {
                imgRes = R.drawable.folder;
                break;
            }
            case FileData.FILE: {
                imgRes = R.drawable.file;
                break;
            }
        }
        tempView.setImageResource(imgRes);
        return tempView;
    }

}
