package com.example.parsiphal.pregnantbase.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.inteface.MainView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.text.MessageFormat

class MainActivity : MvpAppCompatActivity(), MainView, NavigationView.OnNavigationItemSelectedListener {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu!!)
        menu.findItem(R.id.menu_detail_pdf).isVisible = false
        menu.findItem(R.id.menu_detail_save).isVisible = false
        menu.findItem(R.id.menu_detail_edit).isVisible = false
        menu.findItem(R.id.menu_detail_add).isVisible = false
        menu.findItem(R.id.menu_detail_back).isVisible = false
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
        }

        initGUI()
        fragmentPlace(DistrictFragment())
    }

    override fun initGUI() {
        val district = MessageFormat.format(resources.getString(R.string.nav_header_subtitle), prefs.district)
        val navBarTextView = nav_view.getHeaderView(0).findViewById<TextView>(R.id.navBar_textView)
        if (prefs.district != 0) {
            navBarTextView.text = district
            nav_view.menu.findItem(R.id.add).isVisible = true
        } else {
            navBarTextView.text = resources.getString(R.string.nav_header_subtitle_all)
            nav_view.menu.findItem(R.id.add).isVisible = false
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.list -> fragmentPlace(ListFragment())
            R.id.search -> fragmentPlace(SearchFragment())
            R.id.add -> fragmentPlace(DetailsFragment())
            R.id.released -> fragmentPlace(ListReleasedFragment())
            R.id.maintenance -> fragmentPlace(MaintFragment())
            R.id.district -> fragmentPlace(DistrictFragment())
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun fragmentPlace(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.content_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun fragmentPlaceWithArgs(fragment: Fragment, args: Bundle) {
        fragment.arguments = args
        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.content_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun prevousFragment() {
        supportFragmentManager.popBackStack()
    }
}
