package com.aaa.cd.ui.main;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.dao.DocumentDao;
import com.aaa.cd.po.Catalogue;
import com.aaa.cd.po.User;
import com.aaa.cd.ui.article.CatalogueAdapter;
import com.aaa.cd.ui.article.DisplayMode;
import com.aaa.cd.ui.article.DisplayModeAdapter;
import com.aaa.cd.ui.article.ItemClickListener;
import com.aaa.cd.ui.article.MarkdownActivity;
import com.aaa.cd.ui.article.SortMode;
import com.aaa.cd.ui.article.SortModeAdapter;
import com.aaa.cd.util.Constants;
import com.aaa.cd.util.CountDownApplication;
import com.aaa.cd.util.LogUtil;
import com.aaa.cd.view.ExpandableLinearLayout;
import com.aaa.cd.view.TabView;

import java.util.List;
import java.util.Stack;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainArticleFragment extends MainBaseFragment implements View.OnClickListener
{

    //创建文件 文件夹按钮
    protected FloatingActionButton fab_createFile;
    protected FloatingActionButton fab_createFolder;

    //列表显示方式   列表 / 表格 / 详情
    private DisplayModeAdapter displayModeAdapter;
    private ExpandableLinearLayout ell_display;
    private RecyclerView rv_displayOption;
    private ImageView iv_display;


    private ExpandableLinearLayout ell_sort;
    private RecyclerView rv_sortOption;
    private ImageView iv_sort;
    private SortModeAdapter sortModeAdapter;

    private RecyclerView rv_catalogue;
    private CatalogueAdapter catalogueAdapter;
    private List<Catalogue> list_catalogue;
    private User user;
    private TabView tiv_path;
    private int currentCatalogueId = -1;
    private Stack<Integer> catalogueStack;
    private SearchView sv_search;
    private TabItemClickListener tabItemClickListener;
    private ItemClickListener displayItemClickListener;
    private ItemClickListener sortItemClickListener;


    public MainArticleFragment()
    {
        tabItemClickListener = new TabItemClickListener();
        displayItemClickListener = new DisplayItemClickListener();
        sortItemClickListener = new SortItemClickListener();
        catalogueStack = new Stack<>();
        catalogueStack.push(currentCatalogueId);
        // Required empty public constructor
        user = CountDownApplication.getApplication().getUser();
        list_catalogue = DocumentDao.getFileListByParent(-1, user.getId());
        if (list_catalogue == null || list_catalogue.size() <= 0)
        {
            Log.i("countdown", "article : no catalogue  ");
            int rootDir = DocumentDao.createFile("RootFolder", "root", -1, 0, user.getId());
            DocumentDao.createFile("file1", "file 1111111111111", rootDir, 1, user.getId());
            DocumentDao.createFile("file2", "file 2222222222222", rootDir, 1, user.getId());
            DocumentDao.createFile("file3", "file 3333333333333", rootDir, 1, user.getId());
            DocumentDao.createFile("file4", "file 4444444444444", rootDir, 1, user.getId());
            int folder1 = DocumentDao.createFile("folder1", "folder", rootDir, 0, user.getId());
            DocumentDao.createFile("folder2", "folder", rootDir, 0, user.getId());
            DocumentDao.createFile("filea", "file aaaaaaaaaaaaaaa", folder1, 1, user.getId());
            DocumentDao.createFile("fileb", "file bbbbbbbbbbbbbbb", folder1, 1, user.getId());
            DocumentDao.createFile("filec", "file 4cccccccccccccc", folder1, 1, user.getId());
            int folder11 = DocumentDao.createFile("folder11", "folder", folder1, 0, user.getId());
            int folder111 = DocumentDao.createFile("folder111", "folder", folder11, 0, user.getId());
            DocumentDao.createFile("filec", "file 4cccccccccccccc", folder111, 1, user.getId());
            list_catalogue = DocumentDao.getFileListByParent(-1, user.getId());
        }

        Log.i("countdown", "article : catalogue size : " + list_catalogue.size());

    }

    @Override
    public void initTitle(View view)
    {
        TextView tv_title_content = (TextView) view.findViewById(R.id.tv_title_content);
        tv_title_content.setText(R.string.menu_article);
        ImageView iv_left = (ImageView) view.findViewById(R.id.iv_title_left);
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setImageResource(R.mipmap.menu);
        iv_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mainCallback.openMenu(true);
            }
        });
        ImageView iv_right = (ImageView) view.findViewById(R.id.iv_title_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.drawable.selector_more);
        iv_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ell_display.toggle();
            }
        });

    }

    @Override
    public void initView(View view)
    {
        ell_display = (ExpandableLinearLayout) view.findViewById(R.id.ell_display);
        rv_displayOption = (RecyclerView) view.findViewById(R.id.rv_display_option);
        iv_display = (ImageView) view.findViewById(R.id.iv_markdown_display);
        iv_display.setOnClickListener(this);
        displayModeAdapter = new DisplayModeAdapter(getActivity(), DisplayMode.MODE_LIST, displayItemClickListener);
        rv_displayOption.setAdapter(displayModeAdapter);
        rv_displayOption.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        ell_sort = (ExpandableLinearLayout) view.findViewById(R.id.ell_sort);
        rv_sortOption = (RecyclerView) view.findViewById(R.id.rv_sort_option);
        iv_sort = (ImageView) view.findViewById(R.id.iv_markdown_sort);
        iv_sort.setOnClickListener(this);
        sortModeAdapter = new SortModeAdapter(getActivity(), SortMode.SORT_ALPHA_ASC, sortItemClickListener);
        sortModeAdapter.setSortMode(SortMode.SORT_ALPHA_ASC);
        rv_sortOption.setAdapter(sortModeAdapter);
        rv_sortOption.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        fab_createFile = (FloatingActionButton) view.findViewById(R.id.fab_create_file);
        fab_createFile.setOnClickListener(this);
        fab_createFolder = (FloatingActionButton) view.findViewById(R.id.fab_create_folder);
        fab_createFolder.setOnClickListener(this);
        tiv_path = (TabView) view.findViewById(R.id.tiv_path);
        addTab(user.getNickname());
        sv_search = (SearchView) view.findViewById(R.id.sv_document);
        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
        rv_catalogue = (RecyclerView) view.findViewById(R.id.rv_article_catalogue);
        rv_catalogue.requestFocus();


        catalogueAdapter = new CatalogueAdapter(getActivity(), displayModeAdapter.getCurrentMode(), sortModeAdapter.getSortMode(), list_catalogue, catalogueItemListener);
        rv_catalogue.setAdapter(catalogueAdapter);

        setDisplayMode(DisplayMode.MODE_LIST.getMode());
        setSortMode(SortMode.SORT_ALPHA_ASC.getMode());
    }

    CatalogueAdapter.ItemClickListener catalogueItemListener = new CatalogueAdapter.ItemClickListener()
    {
        @Override
        public void onItemClick(Catalogue catalogue)
        {
            if (catalogue.getType() == Catalogue.FOLDER)
            {
                currentCatalogueId = catalogue.getId();
                catalogueStack.push(currentCatalogueId);
                addTab(catalogue.getName());
                list_catalogue = DocumentDao.getFileListByParent(currentCatalogueId, user.getId());
                catalogueAdapter.updateList(list_catalogue);
            } else
            {
                Intent intent = new Intent(getActivity(), MarkdownActivity.class);
                intent.putExtra(Constants.INTENT_KEY_ARTICLE_ID, catalogue.getId());
                startActivityForResult(intent, Constants.REQUEST_CODE_ARTICLE);
            }
        }

        @Override
        public void onItemLongClick(Catalogue catalogue)
        {
            showDeleteDialog(catalogue.getId());
        }
    };

    @Override
    public int getLayoutId()
    {
        return R.layout.fragment_main_article;
    }

    public boolean onBackPressed()
    {
        if (catalogueStack.size() > 1)
        {
            catalogueStack.pop();
            currentCatalogueId = catalogueStack.peek();
            list_catalogue = DocumentDao.getFileListByParent(currentCatalogueId, user.getId());
            tiv_path.removeTab();
            catalogueAdapter.updateList(list_catalogue);
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i("onActivityResult");
        if (requestCode == Constants.REQUEST_CODE_ARTICLE)
        {
            refresh();
        }
    }

    public void addTab(String title)
    {
        tiv_path.addTab(title, currentCatalogueId, tabItemClickListener);
    }


    private boolean removeTab()
    {
        return tiv_path.removeTab();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_markdown_display:
                ell_sort.collapse();
                ell_display.toggle();
                break;
            case R.id.iv_markdown_sort:
                ell_display.collapse();
                ell_sort.toggle();
                break;
            case R.id.fab_create_file:
                Intent intent = new Intent(getActivity(), MarkdownActivity.class);
                intent.putExtra(Constants.INTENT_KEY_ARTICLE_PARENT, currentCatalogueId);
                intent.putExtra(Constants.INTENT_KEY_ARTICLE_USER, user.getId());
                startActivityForResult(intent, Constants.REQUEST_CODE_ARTICLE);
                break;
            case R.id.fab_create_folder:
                createFolder();
                break;
        }
    }

    class TabItemClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            Object tag = v.getTag(R.id.tag);
            if (tag != null && tag instanceof Integer)
            {//点击顶部导航
                int parentId = ((Integer) tag).intValue();
                while (parentId != catalogueStack.peek() && catalogueStack.size() > 1)
                {
                    catalogueStack.pop();
                    tiv_path.removeTab();
                }
                currentCatalogueId = parentId;
                list_catalogue = DocumentDao.getFileListByParent(currentCatalogueId, user.getId());
                catalogueAdapter.updateList(list_catalogue);
            }
        }
    }

    class DisplayItemClickListener implements ItemClickListener
    {
        @Override
        public void onItemClick(int position)
        {
            setDisplayMode(position);
            ell_display.collapse();
        }
    }

    public void setDisplayMode(int position)
    {
        DisplayMode mode = DisplayMode.getDisplay(position);
        if (mode == DisplayMode.MODE_GRID)
        {
            iv_display.setImageResource(R.drawable.selector_display_grid);
            rv_catalogue.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            catalogueAdapter = new CatalogueAdapter(getActivity(), DisplayMode.MODE_GRID, sortModeAdapter.getSortMode(), list_catalogue, catalogueItemListener);
        } else if (mode == DisplayMode.MODE_DETAIL)
        {
            iv_display.setImageResource(R.drawable.selector_display_detail);
            rv_catalogue.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            catalogueAdapter = new CatalogueAdapter(getActivity(), DisplayMode.MODE_DETAIL, sortModeAdapter.getSortMode(), list_catalogue, catalogueItemListener);
        } else
        {
            iv_display.setImageResource(R.drawable.selector_display_list);
            rv_catalogue.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            catalogueAdapter = new CatalogueAdapter(getActivity(), DisplayMode.MODE_LIST, sortModeAdapter.getSortMode(), list_catalogue, catalogueItemListener);
        }
        rv_catalogue.setAdapter(catalogueAdapter);
    }

    private class SortItemClickListener implements ItemClickListener
    {
        @Override
        public void onItemClick(int position)
        {
            setSortMode(position);
            ell_sort.collapse();
        }
    }

    /**
     * 设置排序方式
     *
     * @param position
     */
    public void setSortMode(int position)
    {
        SortMode mode = SortMode.getSort(position);
        LogUtil.i("sort mode : " + position + "  getmode " + mode.getMode());
        catalogueAdapter.setSortMode(mode);
        switch (mode)
        {
            case SORT_ALPHA_ASC:
                iv_sort.setImageResource(R.drawable.selector_sort_alpha_asc);
                break;
            case SORT_ALPHA_DESC:
                iv_sort.setImageResource(R.drawable.selector_sort_alpha_desc);
                break;
            case SORT_CREATE_TIME_ASC:
                LogUtil.i("sort mode 1: " + position);
                iv_sort.setImageResource(R.drawable.selector_sort_create_time_asc);
                break;
            case SORT_CREATE_TIME_DESC:
                LogUtil.i("sort mode 1: " + position);
                iv_sort.setImageResource(R.drawable.selector_sort_create_time_desc);
                break;
            case SORT_MODIFY_TIME_ASC:
                catalogueAdapter.setSortMode(SortMode.SORT_MODIFY_TIME_ASC);
                iv_sort.setImageResource(R.drawable.selector_sort_modify_time_asc);
                break;
            case SORT_MODIFY_TIME_DESC:
                iv_sort.setImageResource(R.drawable.selector_sort_modify_time_desc);
                break;
            case SORT_SIZE_ASC:
                iv_sort.setImageResource(R.drawable.selector_sort_size_asc);
                break;
            case SORT_SIZE_DESC:
                iv_sort.setImageResource(R.drawable.selector_sort_size_desc);
                break;
            default:
                iv_sort.setImageResource(R.drawable.selector_sort_alpha_asc);
                break;
        }
    }

    public void refresh()
    {
        currentCatalogueId = catalogueStack.peek();
        list_catalogue = DocumentDao.getFileListByParent(currentCatalogueId, user.getId());
        catalogueAdapter.updateList(list_catalogue);
    }

    public void createFolder()
    {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_folder, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.DialogTheme)
                .setTitle(R.string.create_folder)
                .setView(rootView)
                .show();

        final TextInputLayout titleHint = (TextInputLayout) rootView.findViewById(R.id.til_create_folder_name);
        final TextInputLayout linkHint = (TextInputLayout) rootView.findViewById(R.id.til_create_folder_remark);
        final EditText name = (EditText) rootView.findViewById(R.id.acet_create_folder_name);
        final EditText remark = (EditText) rootView.findViewById(R.id.acet_create_folder_remark);


        rootView.findViewById(R.id.tv_create_folder_ensure).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String titleStr = name.getText().toString().trim();
                String linkStr = remark.getText().toString().trim();

                if (TextUtils.isEmpty(titleStr))
                {
                    titleHint.setError(getString(R.string.should_not_be_null));
                    return;
                }
                if (TextUtils.isEmpty(linkStr))
                {
                    linkHint.setError(getString(R.string.should_not_be_null));
                    return;
                }

                if (titleHint.isErrorEnabled())
                {
                    titleHint.setErrorEnabled(false);
                }
                if (linkHint.isErrorEnabled())
                {
                    linkHint.setErrorEnabled(false);
                }
                DocumentDao.createFile(titleStr, linkStr, currentCatalogueId, Catalogue.FOLDER, user.getId());
                refresh();
                dialog.dismiss();
            }
        });

        rootView.findViewById(R.id.tv_create_folder_cancel)
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });

        dialog.show();
    }

    private void showDeleteDialog(final int id)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        builder.setMessage(getString(R.string.dialog_delete));
        builder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        }).setPositiveButton(getString(R.string.ensure), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                deleteFile(id);
            }
        }).show();
    }

    public void deleteFile(int id)
    {
        DocumentDao.deleteFileById(id);
        refresh();
    }

}
