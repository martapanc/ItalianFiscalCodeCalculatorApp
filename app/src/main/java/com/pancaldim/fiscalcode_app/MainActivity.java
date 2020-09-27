package com.pancaldim.fiscalcode_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.pancaldim.fiscalcode_app.ui.main.RefreshFragment;
import com.pancaldim.fiscalcode_app.ui.main.about.AboutFragment;
import com.pancaldim.fiscalcode_app.ui.main.compute.ComputeFragment;
import com.pancaldim.fiscalcode_app.ui.main.extract.ExtractFragment;
import com.pancaldim.fiscalcode_app.ui.main.verify.VerifyFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureToolbar();
        configureNavigationDrawer();

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_compute, R.id.nav_extract, R.id.nav_verify, R.id.nav_about_fc)
//                .setOpenableLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_info_24px);
    }

    private void configureNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(menuItem -> {
            Fragment f = null;
            int itemId = menuItem.getItemId();
            switch (itemId) {
                case R.id.nav_compute:
                    f = new ComputeFragment();
                    break;
                case R.id.nav_extract:
                    f = new ExtractFragment();
                    break;
                case R.id.nav_verify:
                    f = new VerifyFragment();
                    break;
                case R.id.nav_about_fc:
                    f = new AboutFragment();
                    break;
                case R.id.refresh:
                    f = new RefreshFragment();
                    break;
            }
            if (f != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, f);
                transaction.commit();
                drawerLayout.closeDrawers();
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            openSettingsActivity();
        } else if (item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.nothing);
    }
}