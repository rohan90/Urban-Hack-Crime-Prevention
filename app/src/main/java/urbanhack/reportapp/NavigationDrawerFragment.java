package urbanhack.reportapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NavigationDrawerFragment extends Fragment {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolBar;
    private ActionBarDrawerToggle mDrawerToggle;
    private View mContainerView;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    private String PREF_FILE_NAME = "app_prefs";
    private String KEY_DRAWER_LEARNED = "learnedDrawer";

    private RecyclerView mRecyclerView; //drawer menu items
    private DrawerMenuAdapter menuAdapter; // adapter


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserLearnedDrawer = Boolean.getBoolean(SharedPreferenceUtil.getInstance(getActivity()).getData( KEY_DRAWER_LEARNED, "false"));

        if(savedInstanceState != null){
            mFromSavedInstanceState = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        TextView nameTv = (TextView) layout.findViewById(R.id.tv_nav_drawer_profile_name);
        ImageView profileIv = (ImageView) layout.findViewById(R.id.iv_custom_nav_drawer_profile_image);
        SharedPreferenceUtil preferenceUtil = SharedPreferenceUtil.getInstance(getActivity());

        String name = preferenceUtil.getData("name", "");
        String url = preferenceUtil.getData("url","null");
        Logger.logError("author is" +name);
        Logger.logError("url is "+url);
        if(url!=null)
             Picasso.with(getActivity()).load(url).placeholder(R.drawable.icon_profile_placeholder).into(profileIv);

        if(name!=null)
            nameTv.setText(name);
        else
            nameTv.setText("");

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_drawer_menu_list);
        menuAdapter = new DrawerMenuAdapter(getActivity(),getData());
        mRecyclerView.setAdapter(menuAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolBar) {
        mDrawerLayout = drawerLayout;
        mToolBar = toolBar;
        mContainerView = getActivity().findViewById(fragmentId);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolBar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer =true;
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        if(!mUserLearnedDrawer && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(mContainerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }



    /**
     * Drawer Items classes
     */

    private class DrawerMenuAdapter extends RecyclerView.Adapter<DrawerMenuItemViewHolder>{

        private LayoutInflater inflater;
        private List<DrawerMenuItem> mData = Collections.EMPTY_LIST;
        private Context context;

        public DrawerMenuAdapter(FragmentActivity activity, List<DrawerMenuItem> data) {
            inflater = LayoutInflater.from(activity);
            this.mData = data;
            this.context = activity;
        }

        @Override
        public DrawerMenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.row_drawer_menu, parent, false);

            DrawerMenuItemViewHolder holder = new DrawerMenuItemViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(DrawerMenuItemViewHolder holder, int position) {

            DrawerMenuItem item = mData.get(position);
            holder.tittle.setText(item.title);
            holder.icon.setImageResource(item.iconId);

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private class DrawerMenuItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tittle;
        ImageView icon;
        LinearLayout container;

        public DrawerMenuItemViewHolder(View itemView) {
            super(itemView);
            tittle = (TextView) itemView.findViewById(R.id.tv_drawer_menu_item_title);
            icon = (ImageView) itemView.findViewById(R.id.iv_drawer_menu_item_icon);
            container = (LinearLayout) itemView.findViewById((R.id.ll_menu_item_drawer));
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Logger.logInfo("clicked menu item "+getPosition());
            ((HomeActivity)getActivity()).drawerItemSelected(getPosition());
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    private class DrawerMenuItem{
        public int iconId;
        public String title;

        public DrawerMenuItem(int iconId, String title) {
            this.iconId = iconId;
            this.title = title;
        }
    }


    /**
     * Drawer MENUs
     */

    //TODO extract this to resource?
    private List<DrawerMenuItem> getData(){
        List<DrawerMenuItem> data = new ArrayList<>();
        data.add(new DrawerMenuItem(R.drawable.ic_home_black_24dp, "Search"));
        data.add(new DrawerMenuItem(R.drawable.map, "Maps "));
        data.add(new DrawerMenuItem(R.drawable.info, "Information"));
        data.add(new DrawerMenuItem(R.drawable.ic_account_circle_black_24dp, "My Profile"));
        data.add(new DrawerMenuItem(R.drawable.logout, "Logout"));

        return data;
    }

}
