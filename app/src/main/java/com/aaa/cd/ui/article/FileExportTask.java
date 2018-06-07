package com.aaa.cd.ui.article;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.aaa.cd.R;
import com.aaa.cd.dao.DocumentDao;
import com.aaa.cd.po.Catalogue;
import com.aaa.cd.util.FileUtils;
import com.aaa.cd.util.LogUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FileExportTask extends AsyncTask
{
    private Catalogue rootCatalogue;
    private String exportRootPath;
    private boolean isApplyAll;
    private boolean isOverride;
    private ReentrantLock lock;
    private Condition condition;
    private Context context;
    public FileExportTask(Context context,Catalogue rootCatalogue, String path)
    {
        this.context=context;
        this.rootCatalogue = rootCatalogue;
        this.exportRootPath = path;
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    @Override
    protected Object doInBackground(Object[] objects)
    {
        export(rootCatalogue, exportRootPath);
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values)
    {
        super.onProgressUpdate(values);
        LogUtil.i("onProgressUpdate  showOverrideDialog ");
        showOverrideDialog((Catalogue)values[0], (File)values[1]);
    }

    @Override
    protected void onPostExecute(Object o)
    {
        super.onPostExecute(o);
        Toast.makeText(context,"export file finish ",Toast.LENGTH_SHORT).show();
    }


    public void export(Catalogue catalogue, String path)
    {
        String currentPath = path + catalogue.getName();

        if (catalogue.getType() == Catalogue.FOLDER)
        {
            //如果当前是文件夹 那么查询其下所有文件 导出
            File file = new File(currentPath);
            if (!file.exists() || !file.isDirectory())
            {
                file.mkdirs();
            }

            List<Catalogue> list = DocumentDao.getFileListByParent(catalogue.getId(), catalogue.getUserId());
            LogUtil.i("export folder : "+currentPath+"   sub item : "+ list.size());
            for (Catalogue c : list)
            {
                export(c, currentPath + "/");
            }
        } else
        {
            LogUtil.i("export file : "+currentPath);
            exportFile(catalogue, currentPath);
        }
    }

    public void exportFile(Catalogue catalogue, String exportPath)
    {

        File file = new File(exportPath);
        //目标文件已经存在
        if (file.exists())
        {
            //是否每次都显示对话框
            if (isApplyAll)
            {
                if (isOverride)
                {
                    //覆盖文件 不再询问
                    boolean isSuccess = FileUtils.writeArticleFile(catalogue, file);
                    LogUtil.i("override file " + catalogue.getName() + isSuccess);
                }else{
                    LogUtil.i("file move cancel " + catalogue.getName());
                }
            } else
            {
                publishProgress(catalogue,file);
                pause();
            }
        } else
        {
            //原始文件不存在，可以直接移动
            boolean isSuccess = FileUtils.writeArticleFile(catalogue, file);
            LogUtil.i(" move file " + catalogue.getName() + isSuccess);
        }
    }

    private void pause()
    {
        lock.lock();
        try
        {
            condition.await();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        lock.unlock();
    }

    private void restart()
    {
        lock.lock();
        condition.signalAll();
        lock.unlock();
    }



    private void showOverrideDialog(final Catalogue catalogue, final File file)
    {
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_override, null);
        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.DialogTheme).create();
        dialog.setTitle(null);
        dialog.setView(rootView);

        final CheckBox cb_applyAll = (CheckBox) rootView.findViewById(R.id.cb_override_file_all);

        rootView.findViewById(R.id.tv_override_file_ensure).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                isApplyAll = cb_applyAll.isChecked();
                isOverride = true;
                restart();
                boolean isSuccess = FileUtils.writeArticleFile(catalogue, file);
                LogUtil.i(" override file " + catalogue.getName() + isSuccess);
                dialog.dismiss();
            }
        });

        rootView.findViewById(R.id.tv_override_file_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                isApplyAll = cb_applyAll.isChecked();
                isOverride = false;
                restart();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


/*    private void showOverrideDialog(final Catalogue rootCatalogue, final File file)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
        builder.setMessage(R.string.dialog_override);
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        }).setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                boolean isSuccess = FileUtils.writeArticleFile(rootCatalogue, file);
                Toast.makeText(context, isSuccess
                                              ? "exportFile success !"
                                              : "exportFile fail  !", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }*/
}
