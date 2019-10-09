package com.example.maps16171;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager lm;
    private lokasiListener ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button go = findViewById(R.id.btnGo);
        go.setOnClickListener(op);
        Button cari = findViewById(R.id.btnSearch);
        cari.setOnClickListener(op1);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new lokasiListener();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50, 200, ll);
    }

    private class lokasiListener implements LocationListener {
        private TextView txtLat, txtLong;
        @Override
        public void onLocationChanged(Location location) {
            txtLat = (TextView) findViewById(R.id.idLat);
            txtLong = (TextView) findViewById(R.id.idLong);

            txtLat.setText(String.valueOf(location.getLatitude()));
            txtLong.setText(String.valueOf(location.getLongitude()));
            Toast.makeText(getBaseContext(), "GPS Capture:", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    final View.OnClickListener op = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            gotoLokasi();
        }
    };
    final View.OnClickListener op1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            goCari();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.normal: mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); break;
            case R.id.hybrid: mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); break;
            case R.id.terrain: mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); break;
            case R.id.satellite: mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); break;
            case R.id.none: mMap.setMapType(GoogleMap.MAP_TYPE_NONE); break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoLokasi(){
        EditText lat = findViewById(R.id.idLat);
        EditText lng = findViewById(R.id.idLong);
        EditText zoom = findViewById(R.id.zoom);

        Double dbllat = Double.parseDouble(lat.getText().toString());
        Double dbllng = Double.parseDouble(lng.getText().toString());
        Float dblzoom = Float.parseFloat(zoom.getText().toString());

        Toast.makeText(this, "Move to Lat:" +dbllat + " Long: " +dbllng, Toast.LENGTH_LONG).show();
        gotoPeta(dbllat, dbllng, dblzoom);

    }

    private void sembunyikanKeyboard(View v){
        InputMethodManager a = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        a.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng ITS = new LatLng(-7.279691,112.797515);
//        mMap.addMarker(new MarkerOptions().position(ITS).title("Marker in Informatika ITS"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS,15));

    }

    private void gotoPeta(Double latt, Double lngg, float z){
        LatLng Lokasibaru = new LatLng(latt,lngg);
        mMap.addMarker(new MarkerOptions().position(Lokasibaru).title("Marker in  " +latt +":" +lngg));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Lokasibaru,z));
    }

    private void hitungJarak(double latAsal, Double lngAsal, double latTujuan, double lngTujuan){
        Location asal = new Location("asal");
        Location tujuan = new Location("tujuan");
        tujuan.setLatitude(latTujuan);
        tujuan.setLongitude(lngTujuan);
        asal.setLatitude(latAsal);
        asal.setLongitude(lngAsal);
        float jarak = (float) asal.distanceTo(tujuan)/1000;
        String jaraknya = String.valueOf(jarak);
        Toast.makeText(getBaseContext(), "jarak: " +jaraknya + " km", Toast.LENGTH_LONG).show();
    }

    private void goCari(){
        EditText tempat = findViewById(R.id.idSearch);
//        Log.d("MASOOOK", "goCari: " + tempat.getText());
        Geocoder g = new Geocoder(getBaseContext());

        try{
            List<Address> daftar = g.getFromLocationName(tempat.getText().toString(), 1);
            Address alamat = daftar.get(0);

            String nemuAlamat = alamat.getAddressLine(0);
            Double lintang = alamat.getLatitude();
            Double bujur = alamat.getLongitude();

            Toast.makeText(getBaseContext(), "Ketemu " +nemuAlamat, Toast.LENGTH_LONG).show();

            EditText zoom = findViewById(R.id.zoom);
            Float dblzoom = Float.parseFloat(zoom.getText().toString());
            Toast.makeText(this, "Move to: " +nemuAlamat +" Lat: " +lintang + " long: " +bujur, Toast.LENGTH_LONG).show();
            gotoPeta(lintang,bujur,dblzoom);

            EditText lat = findViewById(R.id.idLat);
            EditText lng = findViewById(R.id.idLong);

            lat.setText(lintang.toString());
            lng.setText(bujur.toString());
            Double dbllat = Double.parseDouble(lat.getText().toString());
            Double dbllng = Double.parseDouble((lng.getText().toString()));
            hitungJarak(dbllat,dbllng,lintang,bujur);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
