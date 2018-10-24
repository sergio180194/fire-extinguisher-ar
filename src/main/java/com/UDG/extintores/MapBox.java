package com.UDG.extintores;

import android.location.Location;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import java.util.List;

public class MapBox extends AppCompatActivity implements OnMapReadyCallback,LocationEngineListener,PermissionsListener {

    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.access_token));
        setContentView(R.layout.activity_map_box);

        mapView = (MapView)findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl("mapbox://styles/pancho1111/cjj811kmt2vwv2rqhw31zp7ps");
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        enableLocation();
    }

    private void enableLocation(){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            initializeLocationEnginer();
            initializeLocationLayer();

        }else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }
    @SuppressWarnings("MissingPermission")
    private void initializeLocationEnginer(){
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation!=null){
            originLocation = lastLocation;
            setCamaraPosition(lastLocation);
        }else{
            locationEngine.addLocationEngineListener(this);
        }
    }
    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer(){
        locationLayerPlugin = new LocationLayerPlugin(mapView,map,locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }

    private void setCamaraPosition(Location location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()),18));
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            originLocation = location;
            setCamaraPosition(location);
        }

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //present toast or dialog.

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    @SuppressWarnings("MissingPermission")
    protected void onStart() {
        super.onStart();
        if(locationEngine != null){
            locationEngine.requestLocationUpdates();
        }
        if(locationLayerPlugin!=null){
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(locationEngine != null){
            locationEngine.removeLocationUpdates();
        }
        if(locationLayerPlugin != null){
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationEngine != null){
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }
}
