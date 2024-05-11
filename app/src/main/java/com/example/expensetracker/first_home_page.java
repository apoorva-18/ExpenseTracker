package com.example.expensetracker;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.ResolveInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class first_home_page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private DashboardFragment dashboardFragment;
    private IncomeFragment incomeFragment;
    private ExpenseFragment expenseFragment;
    ArrayList<HashMap<String,Object>> items;
    PackageManager pm ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_home_page);

        Toolbar toolbar=findViewById(R.id.my_toolbar);
        toolbar.setTitle("Expens-E");
        toolbar.setTitleTextColor(Color.BLACK);

        bottomNavigationView=findViewById(R.id.bottomNavigationbar);
        setSupportActionBar(null);


        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=findViewById(R.id.naView);
        navigationView.setNavigationItemSelectedListener(this);

        dashboardFragment=new DashboardFragment();
        incomeFragment=new IncomeFragment();
        expenseFragment=new ExpenseFragment();
        setFragment(dashboardFragment);


        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            DatabaseReference mUserInfoDatabase = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(uid);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object emailObject = dataSnapshot.child("Email").getValue();
                    if (emailObject != null) {
                        String userEmail = emailObject.toString();
                        TextView email = findViewById(R.id.user_email);
                        email.setText(userEmail);
                    } else {
                        Toast.makeText(getApplicationContext(), "Email not found", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mUserInfoDatabase.addListenerForSingleValueEvent(valueEventListener);
        }else {
            Toast.makeText(this, "User is not authenticated. Please login.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, home_screen.class));
            finish();
        }


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();


            if (itemId == R.id.dashboard) {
                setFragment(dashboardFragment);
                bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
                return true;
            } else if (itemId == R.id.income) {
                setFragment(incomeFragment);
                bottomNavigationView.setItemBackgroundResource(R.color.income_color);
                return true;
            } else if (itemId == R.id.income_search) {
                Intent intent = new Intent(getApplicationContext(), searchdata.class);
                startActivity(intent);
                bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                return true;
            } else if (itemId == R.id.expense) {
                setFragment(expenseFragment);
                bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                return true;
            } else if (itemId == R.id.expense_search) {
                Intent intent1 = new Intent(getApplicationContext(), searchdata2.class);
                startActivity(intent1);
                bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                return true;
            } else {
                return false;
            }


        });
        items = new ArrayList<>();
        pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            String appName = resolveInfo.loadLabel(pm).toString();
            String packageName = resolveInfo.activityInfo.packageName;
            HashMap<String, Object> map = new HashMap<>();
            map.put("appName", appName);
            map.put("packageName", packageName);
            items.add(map);
        }


    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBackPressed(){
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else{
            super.onBackPressed();
        }

    }
    public void displaySelectedListener(int itemId) {
        Fragment fragment = null;
        if (itemId == R.id.profile) {
            Intent profile_intent = new Intent(getApplicationContext(), Profile.class);
            startActivity(profile_intent);
        } else if (itemId == R.id.dashboard) {
            fragment = new DashboardFragment();
        } else if (itemId == R.id.income) {
            fragment = new IncomeFragment();
        } else if (itemId == R.id.search_income) {
            Intent intent_inc = new Intent(getApplicationContext(), searchdata.class);
            startActivity(intent_inc);
        } else if (itemId == R.id.expense) {
            fragment = new ExpenseFragment();
        } else if (itemId == R.id.search_expense) {
            Intent intent_exp = new Intent(getApplicationContext(), searchdata2.class);
            startActivity(intent_exp);
        } else if (itemId == R.id.income_tax_emi) {
            Intent intent3 = new Intent(getApplicationContext(), inc_emi.class);
            startActivity(intent3);

        } else if (itemId == R.id.calculator) {
            int d = 0;
            if (items.size() >= 1) {

                for (int j = 0; j < items.size(); j++) {
                    HashMap<String, Object> map = items.get(j);
                    if (map.containsKey("appName")) {
                        String appName = (String) map.get("appName");
                        if (appName != null && appName.contains("Calculator")) {
                            d = j;
                            break;
                        }
                    }
                }
                String packageName = (String) items.get(d).get("packageName");


                if (packageName != null) {
                    Intent i = pm.getLaunchIntentForPackage(packageName);
                    if (i != null) {
                        Toast.makeText(getApplicationContext(), "Opening Calculator..", Toast.LENGTH_SHORT).show();
                        startActivity(i);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.sec.android.app.popupcalculator"));
                        startActivity(intent);
                    }
                }
                } else {
                    Intent intent1 = new Intent(Intent.ACTION_VIEW);
                    intent1.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.sec.android.app.popupcalculator"));
                    startActivity(intent1);
                }
            } else if (itemId == R.id.feedback) {
                Intent intent2 = new Intent(getApplicationContext(), feedback.class);
                startActivity(intent2);

            } else if (itemId == R.id.about) {
                Intent intent4 = new Intent(getApplicationContext(), about.class);
                startActivity(intent4);
            } else if (itemId == R.id.logout) {
                AlertDialog.Builder builder = new AlertDialog.Builder(first_home_page.this);
                builder.setTitle("Logout");
                builder.setMessage("Do you really want to Logout?");
                builder.setPositiveButton("YES", (dialog, which) -> {
                    finishAffinity();
                    startActivity(new Intent(getApplicationContext(), home_screen.class));
                });
                builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
                builder.show();
            }

        if(fragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame,fragment);
            ft.commit();
        }

        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedListener(item.getItemId());
        return true;
    }
}