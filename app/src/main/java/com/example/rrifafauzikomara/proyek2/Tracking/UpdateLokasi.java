package com.example.rrifafauzikomara.proyek2.Tracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.rrifafauzikomara.proyek2.R;
import com.example.rrifafauzikomara.proyek2.Server.AppController;
import com.example.rrifafauzikomara.proyek2.Server.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateLokasi extends AppCompatActivity {

    EditText txt_id, txt_nama, txt_lng, txt_lat, txt_idk, txt_tgl, txt_time;

    //untuk delete lokasi
    private String urld = Server.URLH + "delete_lokasi.php";
    ProgressDialog pdd;

    //untuk update lokasi
    ProgressDialog pdu;
    int success;
    ConnectivityManager conMgr;
    private String urlu = Server.URLH + "update_lokasi.php";
    private static final String TAG = UpdateLokasi.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_lokasi);

        txt_id = findViewById(R.id.id);
        txt_nama = findViewById(R.id.nama);
        txt_lng = findViewById(R.id.lng);
        txt_lat = findViewById(R.id.lat);
        txt_idk = findViewById(R.id.idk);
        txt_tgl = findViewById(R.id.tgl);
        txt_time = findViewById(R.id.waktu);

        txt_id.setText(getIntent().getStringExtra("id_lokasi"));
        txt_nama.setText(getIntent().getStringExtra("nama"));
        txt_lng.setText(getIntent().getStringExtra("lng"));
        txt_lat.setText(getIntent().getStringExtra("lat"));
        txt_tgl.setText(getIntent().getStringExtra("tgl"));
        txt_time.setText(getIntent().getStringExtra("time"));
        txt_idk.setText(getIntent().getStringExtra("id_kendaraan"));

        pdd = new ProgressDialog(UpdateLokasi.this);


        //membuat back button toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void update (View v) {
        // TODO Auto-generated method stub
        String id = txt_id.getText().toString();
        String idk = txt_idk.getText().toString();

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);{
            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                updateLokasi(id, idk);
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateLokasi (final String id, final String idk) {
        pdu = new ProgressDialog(this);
        pdu.setCancelable(false);
        pdu.setMessage("Update ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, urlu, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Update Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    // Check for error node in json
                    if (success == 1) {
                        Intent login = new Intent(UpdateLokasi.this, DetailLokasi.class);
                        startActivity(login);
                        Log.e("Update berhasil", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        txt_id.setText("");
                        txt_idk.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Error : " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_lokasi", id);
                params.put("id_kendaraan", idk);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pdu.isShowing())
            pdu.show();
    }

    private void hideDialog() {
        if (pdu.isShowing())
            pdu.dismiss();
    }

    public void delete (View view) {
        deleteData();
    }

    //membuat fungsi back dengan mengirim data session
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(UpdateLokasi.this, DetailLokasi.class);
        startActivity(intent);
        return true;
    }

    //melakukan eksekusi delete data lokasi
    private void deleteData() {
        pdd.setMessage("Delete Data ...");
        pdd.setCancelable(false);
        pdd.show();
        StringRequest delReq = new StringRequest(Request.Method.POST, urld, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pdd.cancel();
                Log.d("volley","response : " + response.toString());
                try {
                    JSONObject res = new JSONObject(response);
                    Toast.makeText(UpdateLokasi.this,res.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(UpdateLokasi.this, DetailLokasi.class));
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                        Toast.makeText(UpdateLokasi.this, "Gagal menghapus lokasi", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id_lokasi", txt_id.getText().toString());
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(delReq);
    }

}
