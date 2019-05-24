package tessting.monah.com.kukueats2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class Client extends FragmentActivity implements OnMapReadyCallback {

    EditText numberOfChicks;
    Button placeOrder;
    EditText vegetablePackets;
    EditText spinachEditText;

    private GoogleMap mMap;


    LocationManager locationManager;
    LocationListener locationListener;

    public void ClientLocationAndOrder (){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null) {

                ParseObject request = new ParseObject("Request");
                request.put("username", ParseUser.getCurrentUser().getUsername());
                request.put("Chicken", numberOfChicks.getText().toString());
                request.put("spinach", spinachEditText.getText().toString());
                request.put("managu", vegetablePackets.getText().toString());

                ParseGeoPoint parseGeoPoint = new ParseGeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                request.put("location", parseGeoPoint);
                request.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Toast.makeText(Client.this, "Order successful", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else{
                Toast.makeText(Client.this, "Could not get your location please reload application", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Calculation(){

        ClientLocationAndOrder();

        Double spinachNumber = Double.parseDouble(spinachEditText.getText().toString());
        Double spinachAmount = spinachNumber * 30;

        Double chickNumber = Double.parseDouble(numberOfChicks.getText().toString());
        Double chicksAmount = chickNumber * 550;
        //Toast.makeText(Client.this, "Total chick cost is ksh" + chicksAmount.toString(), Toast.LENGTH_LONG).show();


        Double packetNumber = Double.parseDouble(vegetablePackets.getText().toString());
        Double packetsAmount = packetNumber * 40;
        //Toast.makeText(Client.this, "Total cost of vegetable is ksh" + packetsAmount.toString(), Toast.LENGTH_LONG).show();

        Double total = chicksAmount + packetsAmount + spinachAmount;
        String totalAmount = total.toString();
        Intent intent = new Intent(Client.this, TotalPriceActivity.class);
        intent.putExtra("Total", totalAmount);
        startActivity(intent);

    }

    public void emptyInput() {
        if (numberOfChicks.getText().toString().matches("") || spinachEditText.getText().toString().matches("")
                || vegetablePackets.getText().toString().matches("")) {
            if(numberOfChicks.getText().toString().matches("")){
                numberOfChicks.setText(0);
                Calculation();
            }
            else if (spinachEditText.getText().toString().matches("")){
                spinachEditText.setText(0);
                Calculation();
            }
            else if(vegetablePackets.getText().toString().matches("")){
                vegetablePackets.setText(0);
                Calculation();
            }
        } else{
            Calculation();
        }
    }

    public void UpdateMap(Location location){

        LatLng UserLocation = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.clear();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UserLocation, 15));
        mMap.addMarker(new MarkerOptions().position(UserLocation).title("Your location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(UserLocation));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.Logout) {

            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    UpdateMap(lastKnownLocation);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        numberOfChicks = findViewById(R.id.numberOfChicks);
        placeOrder = findViewById(R.id.placeOrder);
        vegetablePackets = findViewById(R.id.vegetablePackets);
        spinachEditText = findViewById(R.id.spinachEditText);



        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyInput();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            UpdateMap(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
        else{

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation!= null) {

                UpdateMap(lastKnownLocation);
            }
        }


    }
}
