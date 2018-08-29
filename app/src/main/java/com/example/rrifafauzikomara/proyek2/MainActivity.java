package com.example.rrifafauzikomara.proyek2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.rrifafauzikomara.proyek2.Profile.Profile;
import com.example.rrifafauzikomara.proyek2.Scanner.Scanner;
import com.example.rrifafauzikomara.proyek2.Server.RequestHandler;
import com.example.rrifafauzikomara.proyek2.Server.Server;
import com.example.rrifafauzikomara.proyek2.Tracking.DetailLokasi;
import com.example.rrifafauzikomara.proyek2.Tracking.TampilLokasi;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    View header;
    TextView txt_nama, txt_email;
    SliderLayout sliderLayout;
    CircleImageView user_picture;
    String idx;

    private String url = Server.URLH + "get_profile.php?id=";

    //untuk login session
    SharedPreferences sharedpreferences;
    public static final String TAG_ID = "id";
    public final static String TAG_NAMA = "nama";
    public final static String TAG_EMAIL = "email";
    public final static String TAG_FOTO = "foto";
    public final static String TAG_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //untuk mengambil data login session
        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        idx = sharedpreferences.getString(TAG_ID, null);

        //untuk memunculkan fungsi navbar
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //menampilkan data pada navigation drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        txt_nama = header.findViewById(R.id.nama);
        txt_email = header.findViewById(R.id.email);
        user_picture = header.findViewById(R.id.profile_image);

        //untuk memunculkan fungsi gambar slide show
        sliderLayout = findViewById(R.id.slider);
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Gambar 1",R.drawable.g1);
        file_maps.put("Gambar 3",R.drawable.g3);
        file_maps.put("Gambar 2",R.drawable.g2);
        for(String nama : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView.description(nama).image(file_maps.get(nama)).setScaleType(BaseSliderView.ScaleType.Fit);
            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra",nama);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);

        getUserProfile();
    }

    private void getUserProfile(){
        class GetUser extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Tunggu",".....",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showUser(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(url,idx);
                return s;
            }
        }
        GetUser gu = new GetUser();
        gu.execute();
    }

    private void showUser(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject c = result.getJSONObject(0);
            String nama = c.getString(TAG_NAMA);
            String email = c.getString(TAG_EMAIL);
            String foto = c.getString(TAG_FOTO);

            txt_nama.setText(nama);
            txt_email.setText(email);
            Picasso.with(this).load(foto).placeholder(R.drawable.user).error(R.drawable.user).into(user_picture);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Anda yakin ingin logout ?");
        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(Login.session_status, false);
                        editor.putString(TAG_ID, null);
                        editor.putString(TAG_USERNAME, null);
                        editor.putString(TAG_NAMA, null);
                        editor.putString(TAG_EMAIL, null);
                        editor.putString(TAG_FOTO, null);
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        finish();
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // TODO Auto-generated method stub
            moveTaskToBack(true);
            //membuat method tombol keluar dari aplikasi
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //untuk membuat fungsi ketika menekan tombol setting di pojok kanan atas
        //if (id == R.id.action_settings) {
        //  return true;
        //}
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, Profile.class);
            intent.putExtra(TAG_ID, idx);
            startActivity(intent);
        } else if (id == R.id.nav_scanner) {
            Intent scan = new Intent(MainActivity.this, Scanner.class);
            startActivity(scan);
        } else if (id == R.id.nav_tracking) {
            Intent track = new Intent(MainActivity.this, TampilLokasi.class);
            startActivity(track);
        } else if (id == R.id.nav_detail) {
            Intent detail = new Intent(MainActivity.this, DetailLokasi.class);
            startActivity(detail);
        } else if (id == R.id.nav_logout) {
            //untuk logout login session
            logout();
            //untuk logout login facebook
        }
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}