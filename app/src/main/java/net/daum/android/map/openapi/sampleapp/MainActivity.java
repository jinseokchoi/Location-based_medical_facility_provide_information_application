package net.daum.android.map.openapi.sampleapp;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.widget.DrawerLayout;


public class MainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mMenuTitles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //navigation drawer 제목
        mTitle = mDrawerTitle = getTitle();
        //navigation listview 목록 (string.xml)
        mMenuTitles = getResources().getStringArray(R.array.menus_array);
        // DrawerLayout 정의
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // DrawerLayout Shadow 정의
         mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // ListView 정의
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // ListView 데이터 정의
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mMenuTitles));

        // ListView 아이템 클릭 리스너
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        // ActionBar의 홈버튼을 Navigation Drawer 토글기능으로 사용
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // 토글 정의
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                 //MainActivity 자체
                mDrawerLayout,        //DrawerLayout
                R.drawable.ic_drawer,  //Navigation drawer 이미지
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); //onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // onPrepareOptionsMenu()
            }
        };

        // Drawer Layout의 리스너를 mDrawerToggle로 정의
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //  인스턴스 상태가 존재 안하면 가장 첫번째 아이템으로 시작
        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    /* nvalidateOptionsMenu()을 불렀을 때*/
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

      /*   websearch할 때 필요
      boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
      menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);*/
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Navigation drawer 이미지를 클릭하면 drawer가 열리거나 닫힘
        // ActionBarDrawerToggle이 관리해줌.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle 액션 버튼
        switch(item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // navigation drawer 안의 listview를 클릭
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        Fragment fragment = null;
        //listview 안의 item(position)에 따라 바뀌는 Fragment
        switch (position) {
            case 0:
                fragment = new HomeFragment(); //fragment 부분이 HomeFagment로 바뀜
                break;
            case 1:
                fragment = new LoginFragment();//fragment 부분이 LoginFagment로 바뀜
                break;
            case 2:
                fragment = new MyPageFragment();//fragment 부분이 LoginFagment로 바뀜
                break;
            default:
                break;
        }

        // Fragment에 추가적인 정보 저장
        Bundle args = new Bundle();
        args.putInt(HomeFragment.ARG_NUMBER, position);
        fragment.setArguments(args);

        // FragmentManger가 Fragment가 바뀔때마다 교체해줌
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // 아이템이 계속 클릭된 상태로 유지
        mDrawerList.setItemChecked(position, true);

        // ActionBar 제목 변경
        setTitle(mMenuTitles[position]);

        // Drawer Layout 닫기
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    //ActionBar 제목
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * ActionBarDrawerToggle을 사용할 때
     * onPostCreate() and onConfigurationChanged()를
     * 무조건 call
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


}