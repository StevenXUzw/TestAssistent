package com.steven.testassistant

import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import android.widget.*

import com.steven.testassistant.common.Utils.InputMethodUtils
import com.steven.testassistant.common.adapter.ImageAdapter
import com.steven.testassistant.Generator.GeneratorActivity
import com.steven.testassistant.common.Content
import com.steven.testassistant.screenrecorder.ScreenRecorderActivity

class MainActivity : AppCompatActivity() {

    private var mDrawerLayout: DrawerLayout? = null
    private var mToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView(savedInstanceState)
    }




    private fun initView(savedInstanceState: Bundle?) {
        initNavi()
        initToolbar()
        initGrid()
    }

    private fun initGrid() {
        //To change body of created functions use File | Settings | File Templates.
        val gridview: GridView = findViewById(R.id.gridview)
        gridview.adapter = ImageAdapter(this)

        gridview.onItemClickListener =
                AdapterView.OnItemClickListener { parent, v, position, id ->
                    when(position){
                        0->GeneratorActivity.startGeneratorActivity(this, Content.TYPE_CONTACT)
                        1->GeneratorActivity.startGeneratorActivity(this, Content.TYPE_CALL_LOG)
                        2->GeneratorActivity.startGeneratorActivity(this, Content.TYPE_MESSAGE)
                        3->DeviceInfoActivity.startDeviceInfoActivity(this, 1)
                        4->DeviceInfoActivity.startDeviceInfoActivity(this, 1)
                        5->ScreenRecorderActivity.startScreenRecorderActivity(this)
                }
                    Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
                }
    }

    /**
     *
     */
    private fun initToolbar() {
        mDrawerLayout = findViewById<View>(R.id.main_drawer) as DrawerLayout
        mToolbar = findViewById<View>(R.id.main_toolbar) as Toolbar
        mToolbar!!.setNavigationOnClickListener { mDrawerLayout!!.openDrawer(Gravity.LEFT) }
        mDrawerLayout!!.setDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {
                InputMethodUtils.hideSoftInput(this@MainActivity)
            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })
    }

    private fun initNavi() {
        val navView = findViewById<View>(R.id.main_navigation) as NavigationView
        val headerView = navView.getHeaderView(0)
        //        headerView.findViewById(R.id.mine_layout_login).setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                login();
        //            }
        //        });
        //        mUserIconImg = (ImageView) headerView.findViewById(R.id.mine_img_user_icon);
        //        mUserNameTxt = (TextView) headerView.findViewById(R.id.mine_txt_user_name);
        //        mLogoutImg = headerView.findViewById(R.id.mine_img_logout);
        //        if (AccountFacade.isLogin(this)) {
        //            mLogoutImg.setVisibility(View.VISIBLE);
        //        } else {
        //            mLogoutImg.setVisibility(View.GONE);
        //        }
        //        mLogoutImg.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                checkLogout();
        //            }
        //        });

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
            //                    case R.id.nav_menu_item_home:
            //                        itemHomeNaviClick(item);
            //                        break;
            //                    case R.id.nav_menu_item_find:
            //                        itemFindNaviClick(item);
            //                        break;
            //                    case R.id.nav_menu_item_mine:
            //                        collect();
            //                        break;
            ////                    case R.id.nav_menu_item_points:
            ////                        points();
            ////                        break;
            //                    case R.id.nav_menu_item_msg:
            //                        mineMsg();
            //                        break;
            //                    case R.id.nav_menu_item_weixin:
            //                        weixin();
            //                        break;
            //                    case R.id.nav_menu_item_share:
            //                        share();
            //                        break;
            //                    case R.id.nav_menu_item_rate:
            //                        RateFacade.rate(MainActivity.this);
            //                        break;
            ////                    case R.id.nav_menu_item_check_update:
            ////                        UpdateFacade.manualCheckUpdate(MainActivity.this);
            ////                        break;
                else -> {
                }
            }
            false
        }
    }
}
