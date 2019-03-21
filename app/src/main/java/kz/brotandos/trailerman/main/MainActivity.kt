package kz.brotandos.trailerman.main

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import devlight.io.library.ntb.NavigationTabBar
import kotlinx.android.synthetic.main.activity_main.*
import kz.brotandos.trailerman.R
import kz.brotandos.trailerman.actors.ActorsFragment
import kz.brotandos.trailerman.movies.MoviesFragment
import kz.brotandos.trailerman.series.SeriesListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager.apply {
            adapter = object : FragmentPagerAdapter(supportFragmentManager) {
                override fun getCount() = 3

                override fun getItem(position: Int): Fragment = when (position) {
                    0 -> MoviesFragment()
                    1 -> SeriesListFragment()
                    else -> ActorsFragment()
                }
            }
            offscreenPageLimit = 3
            initNavigationTabBar()
        }
    }

    private fun initNavigationTabBar() {
        navigationTabBar.models = getNavigationModels()
        navigationTabBar.setViewPager(viewPager, 0)
    }

    private fun getNavigationModels(): ArrayList<NavigationTabBar.Model> {
        val colors = resources.getStringArray(R.array.default_preview)
        val models = ArrayList<NavigationTabBar.Model>()
        models.add(
            NavigationTabBar.Model.Builder(
                ContextCompat.getDrawable(this, R.drawable.ic_movie),
                Color.parseColor(colors.first())
            )
                .title(getString(R.string.menu_movie))
                .build()
        )
        models.add(
            NavigationTabBar.Model.Builder(
                ContextCompat.getDrawable(this, R.drawable.ic_series),
                Color.parseColor(colors[1])
            )
                .title(getString(R.string.menu_series))
                .build()
        )
        models.add(
            NavigationTabBar.Model.Builder(
                ContextCompat.getDrawable(this, R.drawable.ic_star),
                Color.parseColor(colors[2])
            )
                .title(getString(R.string.menu_actor))
                .build()
        )
        return models
    }
}
