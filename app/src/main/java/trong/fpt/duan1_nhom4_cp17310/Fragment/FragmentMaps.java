package trong.fpt.duan1_nhom4_cp17310.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import trong.fpt.duan1_nhom4_cp17310.R;

public class FragmentMaps extends Fragment implements GoogleMap.OnMarkerClickListener{

    private String addres;

    public static FragmentMaps newInstance(String address){
        FragmentMaps fragmentMaps = new FragmentMaps();
        Bundle bundle = new Bundle();
        bundle.putString("address", address);
        fragmentMaps.setArguments(bundle);
        return fragmentMaps;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            addres = getArguments().getString("address");
        }
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            try {
                //Chuyển đổi từ address sang lat long
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                List<Address> list = geocoder.getFromLocationName(addres, 3);
                Address location = list.get(0);

                //Lấy lat long cho vào map
                LatLng fPoly = new LatLng(location.getLatitude(), location.getLongitude());

                googleMap.addMarker(new MarkerOptions().position(fPoly).title("CGV Vincom Gò Vấp").snippet("CGV Vincom Gò Vấp. Tầng 5 TTTM Vincom Plaza Gò Vấp, 12 Phan Văn Trị, Phường 7, Quận Gò Vấp"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fPoly, 15));

            }catch (Exception e){

            }
        }
    };


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Integer clickCount = (Integer) marker.getTag();
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}