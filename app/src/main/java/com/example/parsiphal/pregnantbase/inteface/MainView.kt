package com.example.parsiphal.pregnantbase.inteface

import android.os.Bundle
import android.support.v4.app.Fragment

interface MainView: BaseView {
    fun fragmentPlace(fragment: Fragment)
    fun fragmentPlaceWithArgs(fragment: Fragment, args: Bundle)
    fun prevousFragment()
}