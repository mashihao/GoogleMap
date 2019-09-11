package com.fujjitsu.cityguide;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements PlaceSelectionListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener {

    private GoogleMap mMap;

    FloatingActionButton fab;

    //连接 Google Play Services 库中的 Google API，你需要先创建一个 GoogleApiClient。
    private GoogleApiClient mGoogleApiClient;

    //权限分为两种类别：普通权限和危险权限。对于危险权限需要在运行时向用户请求授权。要求访问用户隐私的权限比如访问用户通讯录、日历、定位等就属于危险权限。
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    //定位服务的最常见任务是获得用户当前坐标。我们通过 Google Play 服务定位 API 请求用户设备的最新坐标来实现这个目的。
    private Location mLastLocation;

    //实时接收用户位置的变化
    /*
    1声明一个 LocationRequest 变量以及一个保存位置更新状态的变量。
    2REQUEST_CHECK_SETTINGS 是用于传递给 onActivityResult 方法的 request code。
    然后添加方法：
     */
    // 1
    private LocationRequest mLocationRequest;
    private boolean mLocationUpdateState;
    // 2
    private static final int REQUEST_CHECK_SETTINGS = 2;

    //查询兴趣点
    private static final int PLACE_PICKER_REQUEST = 3;

    //这个方法创建了新的 builder 用于创建 intent，这个 Intent 用于打开一个 Place Picker UI，然后打开这个 PlacePicker Intent
//    private void loadPlacePicker() {
//
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//        try {
//            startActivityForResult(builder.build(MapsActivity.this), PLACE_PICKER_REQUEST);
//        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //创建GoogleAPIClient实例
        if (null == mGoogleApiClient) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        //接收位置
        createLocationRequest();

        // 编译运行，点击地图下方的 search 按钮，会弹出 place picker：
        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadPlacePicker();
//            }
//        });
        makeFragment();
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
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        googleMap.setMyLocationEnabled(true);
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        /*这里我们开启了地图的缩放控制并指定了 MapsActdivity 作为回调，这样当用户点击大头钉时能够进行处理。
        现在，点击地图上位于悉尼的大头钉，你会看到显示了标题文本：*/
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(this);

        //设置坐标
        LatLng myPlace = new LatLng(40.73,-73.99);// this is New York
        mMap.addMarker(new MarkerOptions().position(myPlace).title("this is a title test in New York"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPlace));

        /*
        注意，地图自动将中心和大头钉对齐，moveCamera() 的作用就在于次。但是，地图的缩放比例不正确，因为它是缩得太小了。
        将 moveCamera() 方法调用修改为：
        缩方比例 0 表示将地图缩小为最小的世界地图。大部分地图都支持缩放比例到 20，
        更远的地区仅仅支持到 13，将它设为二者之间的 12 比较合适，显示较多的细节且不会太近。
         */
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace,12));
        /*
        1setMyLocationEnabled 一句打开了 my-location 图层，用于在用户坐标出绘制一个浅蓝色的圆点。同时加一个按钮到地图上，当你点击它，地图中心会移动到用户的坐标。
        2getLocationAvailability 一句判断设备上的位置信息是否有效。
        3getLastLocation 一句允许你获得当前有效的最新坐标。
        4如果能够获得最新坐标，将镜头对准用户当前坐标。
         */
        // 1
        mMap.setMyLocationEnabled(true);
        //修改地图类型 Android 地图 API 提供了几种地图类型：MAP_TYPE_NORMAL、MAP_TYPE_SATELLITE、 MAP_TYPE_TERRAIN、MAP_TYPE_HYBRID。
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        // 2
        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            // 3
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            // 4
            if (mLastLocation != null) {
                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation
                        .getLongitude());
                //添加大头钉
                placeMarkerOnMap(currentLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
            }
        }
    }

    //LocationListener 定义了 onLocationChanged() 方法，当用户位置改变时调用。这个方法只有 LocationListener 注册以后才会调用
    @Override
    public void onLocationChanged(Location location) {
        //这里，我们修改 mLastLocation 为最新的位置并用新位置坐标刷新地图显示。
        //
        //你的 app 现在已经可以接受位置变化通知了。当你改变位置，地图上的大头钉会随位置的改变而变。注意，点击大头钉仍然能够看到地址信息。
        mLastLocation = location;
        if (null != mLastLocation) {
            placeMarkerOnMap(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }
    }



    //GoogleApiClient.ConnectionCallbacks 提供了一个回调，当客户端和服务器成功建立连接时调用(onConnected()) 或者临时性的断开时调用 (onConnectionSuspended())。
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpMap();

        //如果用户的位置设置是打开状态的话，启动位置更新。
        if (mLocationUpdateState) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    //GoogleApiClient.OnConnectionFailedListener 提供了一个回调方法 (onConnectionFailed()) ，当客户端连接服务器失败时调用。
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //GoogleMap.OnMarkerClickListener 定义了一个 onMarkerClick() 方法，当大头钉被点击时调用
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 2
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 3
        if( null != mGoogleApiClient && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }

    //上述代码判断 app 是否获得了 ACCESS_FINE_LOCATION 权限。如果没有，向用户请求授权
    private void setUpMap(){
        if(ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

    }

    /*
    1创建了一个 MarkerOptions 对象并将大头钉要放在的位置设置为用户当前坐标。
    2将大头钉添加到地图。
     */
    protected void placeMarkerOnMap(LatLng location) {
        // 1
        MarkerOptions markerOptions = new MarkerOptions().position(location);

        //在这个方法中添加了一句 getAddress() 调用，并将地址设置为大头钉标题。
        String titleStr = getAddress(location);  // add these two lines
        markerOptions.title(titleStr);

        //自定义大头钉样式
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(
                getResources(),R.mipmap.ic_user_location)));

        //修改大头钉颜色
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        // 2
        mMap.addMarker(markerOptions);
    }

    /*
    1创建一个 Geocoder 对象，用于将一个经纬度坐标转换成地址或进行相反的转换。
    2使用 geocoder 将方法参数接收到的经纬度转换成地址信息。
    3如果响应的 addresses 中包含有地址信息，将这些信息拼接为一个字符串返回
     */
    private String getAddress(LatLng latLng){
        //1
        Geocoder geocoder = new Geocoder(this);
        String addressText = "";
        List<Address> addresses = null;
        Address address = null;
        try{
            //2
            addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            //3
            if(null != addresses && !addresses.isEmpty()){
                address = addresses.get(0);
                for (int i = 0 ;i<address.getMaxAddressLineIndex();i++)
                {
                    addressText += (i==0)?address.getAddressLine(i):("\n"+address.getAddressLine(i));
                }
            }

        }catch (IOException e){
        }
        return addressText;
    }

    /*
    1startLocationUpdates() 中，如果 ACCESS_FINE_LOCATION 权限未获取，则请求授权并返回。
    2如果已经获得授权，请求位置变化信息。
     */
    protected void startLocationUpdates() {
        //1
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        //2
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                (com.google.android.gms.location.LocationListener) this);
    }

    /*
    接收位置变化
    1创建一个 LocationRequest 对象，将它添加到一个 LocationSettingsRequest.Builder 对象，并基于用户位置设置的当前状态查询位置变化信息并处理。
    2setInterval() 指定了 app 多长时间接受一次变化通知。
    3setFastestInterval() 指定 app 能够处理的变化通知的最快速度。设置fastestInterval 能够限制位置变化通知发送给你的 app 的频率。在开始请求位置变化通知之前，需要检查用户位置设置的状态。
    4SUCCESS 状态说明一切正常，你可以初始化一个 location request。
    5RESOLUTION_REQUIRED 状态表明位置设置有一个问题有待修复。有可能是因为用户的位置设置被关闭了。你可以向用户显示一个对话框：
     */
    // 1
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        // 2
        mLocationRequest.setInterval(10000);
        // 3
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

//        SettingsClient client = LocationServices.getSettingsClient(this);
//        client.checkLocationSettings(builder.build());
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    // 4
                    case LocationSettingsStatusCodes.SUCCESS:
                        mLocationUpdateState = true;
                        startLocationUpdates();
                        break;
                    // 5
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    // 6
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }
    /*
    1覆盖 FragmentActivity 的 onActivityResult() 方法，如果REQUEST_CHECK_SETTINGS 请求返回的是一个 RESULT_OK，则发起位置更新请求。
    2覆盖 onPause() 方法，停止位置变化请求。
    3覆盖 onResume() 方法，重新开始位置更新请求。
     */
    // 1
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                mLocationUpdateState = true;
                startLocationUpdates();
            }
        }
        /*
        在这里，如果请求代码是 PLACE_PICKER_REQUEST 且返回码是 RESULT_OK，则读取所选地点的信息。然后放一个大头钉在该位置。

        搜索 PIO 基本搞定——剩下的就是调用 loadPlacePicker() 方法。
         */
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String addressText = place.getName().toString();
                addressText += "\n" + place.getAddress().toString();

                placeMarkerOnMap(place.getLatLng());
            }
        }
    }

    // 2
    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
    }

    // 3
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mLocationUpdateState) {
            startLocationUpdates();
        }
    }


    @Override
    public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {

    }


    @Override
    public void onError(@NonNull Status status) {

    }

    private void makeFragment(){
        Places.initialize(getApplicationContext(),"");
        //设置地区过滤器
        AutocompleteFilter.Builder typeFilterBuilder = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE);
        typeFilterBuilder.setCountry("JP");


        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setCountry("JP");

//        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS)
//                .setCountry("JP")
//                .build();


        PlacesClient placesClient = Places.createClient(this);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                // TODO: Get info about the selected place.
                Log.i("info", "Place: " + place.getName() + ", " + place.getId());
                Toast.makeText(getApplication(),place.getName()+"+"+place.getId(),Toast.LENGTH_LONG).show();
                makeLine(mMap);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("tag", "An error occurred: " + status);
            }
        });
    }

    //绘制路线
    private void makeLine(GoogleMap mMap) {
        String line = "agryDundzRnXibE~yM{lBlyXaM|}O{y@`_SgrIb_OamDnpOmkA`bSseJ" +
                "jyTe|MlqHsgKpfBw|If`F}kBpiFefCp}EgeDdpO{uHjsIe`QnnHcy_@nmKk}Et`NsaMlsQisVbqJq" +
                "iTreNciY~eKcmSxkI{vOlnKonHtmZqwNr{PwtJ~pK_vDlfI}sQvwB}sLzeHodLvhEboBdbCcUzmApl" +
                "CniDaUr}CoyDzyCtDxmFkxDzi@kpJfgC_~EtcApt@hbA}fDuy@ycEpnAwgDkcC}lBjqDgsKpdDmqIx" +
                "d@_iIlbCawB`}AqoD|nBw`DjcGebAxnGjyA~jOv|@vpCyoB~oJcJf_InaAfcCl~BriInj@flAklCtk" +
                "GuxD`lHsqBxhDu~D~~Em{Nx_@gtH_`BkzVll@}eIomBy`GzaDemDdsD~rBnmOd}HxrMvjD~wGbVtuEe" +
                "hBjkLx`DhkKflDlwE`A`zBtfCd_KuWz_Jrj@ptDnqA|mMkVnsL}iAvkL`|AnyJ{LvdBd_@nyB{rHjzG" +
                "ijC~tDsm@dkFwzAbrHi_D~uAjqC||C{pFxkEg|SdpDgoZjmIikPleFo~LlmEqjN~eBm~VxnJk~OvcFo" +
                "dJjWwdKxoFae`@~yEe~Hfo@ckMxyG_nSnrLii_@r_CmxOtzGaoRlxBauJ`LifN~`D}uE_ZuyHmr@o|I" +
                "mfCsh\\giEmuIkeAkyLhmC_u\nwAkd\ti@ugSxqG{uf@npD_jFsvAkhMsvCkuIlyAehFqt@wlMzvEs_G" +
                "h`EiePr`DgcLbiCmfExgJ|lAtlk@upH~gRo`C`jGuoCnmFolCnvEhpExpFd{@fgDyt@xqJ{rRjqE}xD" +
                "`xIosB|hQktGzbQa{EnxG_|FfuJwdG|qFsvBye@}fFhkBqaM~fGulPwZe_\\dt@g`IzfDsrFx`FmpCf|A" +
                "qyEhxE{eJ`_BwjGpbIuzF`cHm`Ixz@yfEbwCarDcnBuzNbgFolk@uIooW`pLouAd`L{`B`dP}rEfyK}x" +
                "Bx~FinFx{D{cKxc@amGfzA_qHnbBghSlk@_gN`gHyfFbbN{jJnvKafKv_OkgA~bFefBlrDgvGbdHipFr}" +
                "CuiH~pIs~Ip~JqtKndIidR~`@kgKrcDgqHpfRcnMrrCeoDlnG{|@|eD_`K|rBoeK|uAom\\wpFwlO``@wsO" +
                "ocAyeG~pBw|G_k@cvJbcFw|UfnNesNblGorHtwEam@vsLpuDppLqnErkBeaLpzCkuD~fImkHz|KnqBn" +
                "`Dyw@ryBcnDvoHmdI~gAywG~wBadOuRgp@";

       List<LatLng> decodedPath = PolyUtil.decode(line);  //来源Google Map Util
        PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.addAll(decodedPath); //添加路线
            lineOptions.color(Color.GREEN);  //线条设置
            lineOptions.jointType(JointType.ROUND);
            lineOptions.width(15f);
        mMap.addPolyline(lineOptions);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
