package com.kcumendigital.democraticcauca;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kcumendigital.democraticcauca.Adapters.CustomPagerAdapter;
import com.kcumendigital.democraticcauca.Adapters.SurveyListAdapter;
import com.kcumendigital.democraticcauca.Fragments.DiscussionHomeFragment;
import com.kcumendigital.democraticcauca.Fragments.SurveyHomeFragment;
import com.kcumendigital.democraticcauca.Util.AppUtil;
import com.kcumendigital.democraticcauca.Util.ColletionsStatics;
import com.kcumendigital.democraticcauca.parse.SunshineLogin;
import com.kcumendigital.democraticcauca.parse.SunshineQuery;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SurveyListAdapter.OnItemClickListenerSurvey, SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;


    DiscussionHomeFragment fragment;
    DrawerLayout drawer;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    FloatingActionButton new_encuesta, new_forum;
    String filtro;
    int avatar;
    Transformation transformation;
    DiscussionHomeFragment discussionFragment;
    SurveyHomeFragment surveyFragment;
    ImageView shadow;

    SunshineQuery query;
    boolean alreadyTouchedDogue;

    @Override
    protected void onRestart() {
        super.onRestart();
        discussionFragment.notifyDataChanged();
        surveyFragment.notifyDataChanged();
        ColletionsStatics.getDataComments().clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        alreadyTouchedDogue = false;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new_encuesta = (FloatingActionButton) findViewById(R.id.new_encuesta);
        shadow = (ImageView) findViewById(R.id.shadow);
        new_encuesta.setOnClickListener(this);
        new_forum = (FloatingActionButton) findViewById(R.id.new_forum);
        new_forum.setOnClickListener(this);
        fragment = new DiscussionHomeFragment();
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        nav = (NavigationView) findViewById(R.id.nav);
        nav.getMenu().setGroupVisible(R.id.persona_menu_nav, false);
        nav.getMenu().setGroupVisible(R.id.secciones_menu_nav, true);
        View header = nav.inflateHeaderView(R.layout.header_nav_home);


        final ImageView imm_nav = (ImageView) header.findViewById(R.id.img_user_nav);
        imm_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (alreadyTouchedDogue == false) {
                    Toast.makeText(getApplicationContext(), "cuidado con dogue", Toast.LENGTH_SHORT).show();
                    nav.getMenu().setGroupVisible(R.id.persona_menu_nav, true);
                    nav.getMenu().setGroupVisible(R.id.secciones_menu_nav, false);
                    alreadyTouchedDogue = true;
                } else {
                    nav.getMenu().setGroupVisible(R.id.persona_menu_nav, false);
                    nav.getMenu().setGroupVisible(R.id.secciones_menu_nav, true);
                    alreadyTouchedDogue = false;
                }
            }

        });
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
         transformation = new RoundedTransformationBuilder()
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .oval(true)
                .build();

        avatar = getResources().getDimensionPixelSize(R.dimen.nav_header_avatar);
        Picasso.with(this).load(AppUtil.getUserStatic().getImg())
                .resize(avatar, avatar)
                .centerCrop()
                .transform(transformation).into(imm_nav);
        TextView userName = (TextView) header.findViewById(R.id.txt_usr);
        userName.setText(AppUtil.getUserStatic().getName());

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close);
        nav.setNavigationItemSelectedListener(this);
        toggle.setToolbarNavigationClickListener(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        discussionFragment = new DiscussionHomeFragment();


        surveyFragment = new SurveyHomeFragment();


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(discussionFragment);
        fragments.add(surveyFragment);

        CustomPagerAdapter adapter = new CustomPagerAdapter(fragments, getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        if (getResources().getBoolean(R.bool.port) == true) {
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
        }

    }

    //region OptionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.navigate).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnSearchClickListener(this);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.navigate), this);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region DrawerLayout
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        filtro = menuItem.getTitle().toString();
        getSupportActionBar().setTitle(filtro);

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                query = null;
                break;
            case R.id.nav_gobierno:
                query = new SunshineQuery();
                query.addFieldValue("category", getString(R.string.c_gobierno));
                break;
            case R.id.nav_educacion:
                query = new SunshineQuery();
                query.addFieldValue("category", getString(R.string.c_educacion));
                break;
            case R.id.nav_salud:
                query = new SunshineQuery();
                query.addFieldValue("category", getString(R.string.c_salud));
                break;
            case R.id.nav_medio_ambiente:
                query = new SunshineQuery();
                query.addFieldValue("category", getString(R.string.c_ambiente));
                break;
            case R.id.nav_my_post:
                query = new SunshineQuery();
                query.addUser("user", AppUtil.getUserStatic().getObjectId());
                break;
            case R.id.nav_logout:
                SunshineLogin.logout();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        surveyFragment.reloadWithQuery(query);
        discussionFragment.reloadWithQuery(query);
        drawer.closeDrawers();
        return false;
    }
    //endregion

    //region FAB
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_encuesta:
                startActivity(new Intent(this, CreateSurveyAcitivty.class));
                break;
            case R.id.new_forum:
                startActivity(new Intent(this, CreateBoardDiscussion_activity.class));
                break;
            case R.id.navigate:
                Log.i("navigate", "true");
                break;

        }
    }
    //endregion

    @Override
    public void onItemClick(int position) {
        startActivity(new Intent(getApplicationContext(), SurveyDescriptionActivity.class).putExtra("pos", position - 3));
    }

    @Override
    public boolean onQueryTextSubmit(String queryString) {
        Log.i("query", queryString);
        query = new SunshineQuery();
        query.addFieldValue("title", queryString);
        surveyFragment.reloadWithQuery(query);
        discussionFragment.reloadWithQuery(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i("query", newText);
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        Log.i("expand", "expand");
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        Log.i("asdf", "asdf");
        query = null;
        surveyFragment.reloadWithQuery(query);
        discussionFragment.reloadWithQuery(query);
        return true;
    }


    //endregion


    private void zoomImageFromThumb(final View thumbView, int imageResId, int expandedImage) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(expandedImage);
        Picasso.with(this).load(AppUtil.getUserStatic().getImg())
                .resize(avatar, avatar)
                .centerCrop()
                .transform(transformation).into(expandedImageView);


        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
                shadow.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                        shadow.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                        shadow.setVisibility(View.GONE);
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
