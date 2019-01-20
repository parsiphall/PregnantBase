package com.example.parsiphal.pregnantbase.view

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.inteface.MainView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : MvpAppCompatActivity(), MainView, NavigationView.OnNavigationItemSelectedListener {

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

        fragmentPlace(ListFragment())
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
