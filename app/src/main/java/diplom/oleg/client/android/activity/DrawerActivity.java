package diplom.oleg.client.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import diplom.oleg.client.android.FirebaseService;
import diplom.oleg.client.android.R;
import diplom.oleg.client.android.RestClient;
import diplom.oleg.client.android.model.User;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * Created by alena on 20.06.16.
 */
public class DrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    NavigationView navigationView;
    DrawerLayout drawer;

    ImageView drawerAvatar;
    TextView drawerEmail;
    TextView drawerName;
    Toolbar toolbar;


    protected void onCreate(@Nullable Bundle savedInstanceState, int layoutRes) {
        super.onCreate(savedInstanceState, layoutRes);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.drawerAvatar);
        drawerEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawerEmail);
        drawerName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawerName);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            initToolbar(drawer, toolbar, navigationView);
    }

    private void initToolbar(DrawerLayout drawer, Toolbar toolbar, NavigationView navigationView) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }else{
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            toggle.setDrawerIndicatorEnabled(true);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }

    }

    protected void updateUI(){
        if (user != null){
            drawerEmail.setText(user.getEmail());
            drawerName.setText(user.getFirstName() + " " + user.getLastName());

            if (user.getAvatar() != null){
                ImageLoader.getInstance().displayImage(user.getAvatar(), drawerAvatar);
            }else{
                drawerAvatar.setImageResource(R.drawable.avatar_empty);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, FirstScreenActivity.class));
        } else if (id == R.id.nav_profile) {
            if (this instanceof ProfileActivity){
            }else{
                startActivity(new Intent(this, ProfileActivity.class));
            }

        } else if (id == R.id.nav_tasks) {
            startActivity(new Intent(this, TaskListActivity.class));
        } else if (id == R.id.logout) {
            restClient.logout();
            firebaseService.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
