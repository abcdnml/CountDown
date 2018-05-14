package com.aaa.cd.ui.main;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.aaa.cd.util.FileUtils;
import com.aaa.cd.util.LogUtil;
import com.aaa.cd.view.ExpandableLinearLayout;
import com.aaa.cd.view.TabView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Stack;

import static android.app.Activity.RESULT_OK;

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

    private ExpandableLinearLayout ell_function;
    private TextView tv_import;
    private TextView tv_synchronize;

    private ExpandableLinearLayout ell_sort;
    private RecyclerView rv_sortOption;
    private ImageView iv_sort;
    private SortModeAdapter sortModeAdapter;

    private SwipeMenuRecyclerView rv_catalogue;
    private CatalogueAdapter catalogueAdapter;
    private List<Catalogue> list_catalogue;
    private User user;
    private TabView tiv_path;
    private View header;

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
            DocumentDao.createFile("临时", "懒得选文件夹就放这里", -1, Catalogue.FOLDER, user.getId());
            DocumentDao.createFile("学习", "你们别拦着我 我要学习 我爱学习 学习使我快乐", -1, Catalogue.FOLDER, user.getId());
            DocumentDao.createFile("生活", "小丑摘下面具 走进人群里", -1, Catalogue.FOLDER, user.getId());
            DocumentDao.createFile("思想", "人是一颗会思维的苇草", -1, Catalogue.FOLDER, user.getId());
            DocumentDao.createFile("娱乐", "睡你麻痹起来嗨", -1, Catalogue.FOLDER, user.getId());
            DocumentDao.createFile("README.md", "Welcome to use ", -1, Catalogue.FILE, user.getId());

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
                ell_function.toggle();
            }
        });
    }

    @Override
    public void initView(View view)
    {
        ell_function = (ExpandableLinearLayout) view.findViewById(R.id.ell_function);
        tv_import = (TextView) view.findViewById(R.id.tv_import_markdown);
        setTextViewLeftDrawable(tv_import, R.drawable.selector_import);
        tv_import.setOnClickListener(this);
        tv_synchronize = (TextView) view.findViewById(R.id.tv_synchronize_article);
        setTextViewLeftDrawable(tv_synchronize, R.drawable.selector_synchronize);
        tv_synchronize.setOnClickListener(this);

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
        rv_catalogue = (SwipeMenuRecyclerView) view.findViewById(R.id.rv_article_catalogue);
        rv_catalogue.requestFocus();
        rv_catalogue.setSwipeMenuCreator(swipeMenuCreator);
        rv_catalogue.setSwipeMenuItemClickListener(mMenuItemClickListener);
        rv_catalogue.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(getActivity(), R.color.divider_color)));

        catalogueAdapter = new CatalogueAdapter(getActivity(), displayModeAdapter.getCurrentMode(), sortModeAdapter.getSortMode(), list_catalogue, catalogueItemListener);
        rv_catalogue.setAdapter(catalogueAdapter);

        setDisplayMode(DisplayMode.MODE_LIST.getMode());
        setSortMode(SortMode.SORT_ALPHA_ASC.getMode());
        // FooterView。
        setMoveView(view);
    }

    public void setMoveView(View view)
    {
        header =view.findViewById(R.id.include_move);

        TextView tv_move_to = (TextView) view.findViewById(R.id.tv_move_to);
        Drawable moveDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.selector_move);
        moveDrawable.setBounds(0, 0, 64, 64);
        tv_move_to.setCompoundDrawables( null,moveDrawable, null, null);
        tv_move_to.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Catalogue catalogue = (Catalogue) header.getTag();
                DocumentDao.moveFile(catalogue.getId(), currentCatalogueId);
                catalogueAdapter.updateList(DocumentDao.getFileListByParent(currentCatalogueId, user.getId()));

                hideMoveView();
                Toast.makeText(v.getContext(), catalogue.getName() + "已经移动到" + getCurrentTagString() + "下", Toast.LENGTH_SHORT).show();
            }
        });

        TextView tv_move_cancel = (TextView) view.findViewById(R.id.tv_move_cancel);
        Drawable cancelDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.selector_cancel);
        cancelDrawable.setBounds(0, 0, 64, 64);
        tv_move_cancel.setCompoundDrawables( null,cancelDrawable, null, null);
        tv_move_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideMoveView();
                Toast.makeText(v.getContext(), " move cancel ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    CatalogueAdapter.ItemClickListener catalogueItemListener = new CatalogueAdapter.ItemClickListener()
    {
        @Override
        public void onItemClick(Catalogue catalogue)
        {

            if (catalogue.getType() == Catalogue.FOLDER)
            {
                if (showHeader)
                {
                    Catalogue catalogueToMove = (Catalogue) header.getTag();
                    if (catalogueToMove.getId() == catalogue.getId())
                    {
                        Toast.makeText(getActivity(), "不要递归移动文件夹,会乱掉的...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
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

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator()
    {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType)
        {
            int width = getResources().getDimensionPixelSize(R.dimen.dimen_48);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;ViewGroup.LayoutParams.MATCH_PARENT
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {
                SwipeMenuItem addItem = new SwipeMenuItem(getActivity()).setBackground(R.drawable.selector_blue).setImage(R.drawable.selector_export).setWidth(width).setHeight(height);
                swipeLeftMenu.addMenuItem(addItem); // 添加菜单到左侧。

                SwipeMenuItem closeItem = new SwipeMenuItem(getActivity()).setBackground(R.drawable.selector_blue).setImage(R.drawable.selector_move).setWidth(width).setHeight(height);
                swipeLeftMenu.addMenuItem(closeItem); // 添加菜单到左侧。

                SwipeMenuItem shareItem = new SwipeMenuItem(getActivity()).setBackground(R.drawable.selector_blue).setImage(R.drawable.selector_share).setWidth(width).setHeight(height);
                swipeLeftMenu.addMenuItem(shareItem); // 添加菜单到左侧。

            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity()).setBackground(R.drawable.selector_red).setImage(R.drawable.selector_delete).setTextColor(Color.WHITE).setWidth(width).setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

            }
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener()
    {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge)
        {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION)
            {
                showDeleteDialog(catalogueAdapter.getItem(adapterPosition).getId());
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION)
            {
                if (menuPosition == 0)
                {
                    export(catalogueAdapter.getItem(adapterPosition));
                } else if (menuPosition == 1)
                {
                    move(catalogueAdapter.getItem(adapterPosition));
                } else if (menuPosition == 2)
                {
                    share();
                }
            }
        }
    };

    public void export(Catalogue catalogue)
    {
        //TODO 此处写的固定的文件夹 这里需要添加选择文件夹的操作
        String exportPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/coutdown/";
        if (TextUtils.isEmpty(exportPath + catalogue.getName()))
        {
            Toast.makeText(getActivity(), "export fail ,path is null !", Toast.LENGTH_SHORT).show();
        } else
        {
            File file = new File(exportPath);
            if (!file.exists() || !file.isDirectory())
            {
                file.mkdirs();
            }
            file = new File(exportPath + catalogue.getName());
            if (file.exists())
            {
                //原始文件不存在，目标文件已经存在
                showOverrideDialog(catalogue, file);
            } else
            {
                boolean isSuccess = FileUtils.writeArticleFile(catalogue, file);
                Toast.makeText(getActivity(), isSuccess
                                              ? "export success !"
                                              : "export fail  !", Toast.LENGTH_SHORT).show();
            }
        }

    }

    boolean showHeader = false;

    public void move(Catalogue catalogue)
    {
        showMoveView(catalogue);
    }

    private void showMoveView(Catalogue catalogue)
    {
        if (!showHeader)
        {
            showHeader = true;
            header.setTag(catalogue);
            header.setVisibility(View.VISIBLE);
            catalogueAdapter.setFunctionMode(1);
        }
    }

    private void hideMoveView()
    {
        showHeader = false;
        header.setVisibility(View.GONE);
        catalogueAdapter.setFunctionMode(0);
    }

    public void share()
    {
        Toast.makeText(getActivity(), "分享 暂未实现", Toast.LENGTH_SHORT).show();
    }

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


    public void addTab(String title)
    {
        tiv_path.addTab(title, currentCatalogueId, tabItemClickListener);
    }

    public String getCurrentTagString()
    {
        return tiv_path.getCurrentTag();
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
                Intent intentMarkdown = new Intent(getActivity(), MarkdownActivity.class);
                intentMarkdown.putExtra(Constants.INTENT_KEY_ARTICLE_PARENT, currentCatalogueId);
                intentMarkdown.putExtra(Constants.INTENT_KEY_ARTICLE_USER, user.getId());
                startActivityForResult(intentMarkdown, Constants.REQUEST_CODE_ARTICLE);
                break;
            case R.id.fab_create_folder:
                createFolder();
                break;
            case R.id.tv_import_markdown:
                Intent intentImport = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType(“image/*”);//选择图片
                //intent.setType(“audio/*”); //选择音频
                //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
                //intent.setType(“video/*;image/*”);//同时选择视频和图片
                //intent.setType("*/*");//同时选择视频和图片
                intentImport.setType("text/plain");//无类型限制
                intentImport.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intentImport, Constants.REQUEST_CODE_ARTICLE_IMPORT);
                ell_function.collapse();
                break;
            case R.id.tv_synchronize_article:
                ell_function.collapse();
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
        final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.DialogTheme).setTitle(R.string.create_folder).setView(rootView).show();

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

        rootView.findViewById(R.id.tv_create_folder_cancel).setOnClickListener(new View.OnClickListener()
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

    private void showOverrideDialog(final Catalogue catalogue, final File file)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        builder.setMessage(getString(R.string.dialog_override));
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
                boolean isSuccess = FileUtils.writeArticleFile(catalogue, file);
                Toast.makeText(getActivity(), isSuccess
                                              ? "export success !"
                                              : "export fail  !", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }

    public void deleteFile(int id)
    {
        DocumentDao.deleteFileById(id);
        refresh();
    }

    public void setTextViewLeftDrawable(TextView view, int id)
    {
        Drawable drawableImport = ContextCompat.getDrawable(getActivity(), id);
        drawableImport.setBounds(0, 0, 48, 48);
        view.setCompoundDrawables(drawableImport, null, null, null);
    }

    public String getFileContent(File file)
    {
        StringBuilder result = new StringBuilder();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null)
            {//使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i("onActivityResult");
        if (resultCode != RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case Constants.REQUEST_CODE_ARTICLE:
                refresh();
                break;
            case Constants.REQUEST_CODE_ARTICLE_IMPORT:
                Uri uri = data.getData();
                String path = FileUtils.getPathFormUri(uri);
                if (TextUtils.isEmpty(path))
                {
                    Toast.makeText(getActivity(), R.string.file_not_exist, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(), path, Toast.LENGTH_SHORT).show();
                File file = new File(path);
                if (!file.exists() || !file.isFile())
                {
                    Toast.makeText(getActivity(), R.string.file_not_exist, Toast.LENGTH_SHORT).show();
                    return;
                }
                DocumentDao.createFile(file.getName(), getFileContent(file), currentCatalogueId, Catalogue.FILE, user.getId());
                refresh();
                break;
        }
    }


}

