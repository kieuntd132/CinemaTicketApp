package trong.fpt.duan1_nhom4_cp17310.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import trong.fpt.duan1_nhom4_cp17310.Fragment.FragmentDatVe;
import trong.fpt.duan1_nhom4_cp17310.Fragment.FragmentKhac;
import trong.fpt.duan1_nhom4_cp17310.Fragment.FragmentQuanLy;
import trong.fpt.duan1_nhom4_cp17310.Fragment.FragmentTinTuc;
import trong.fpt.duan1_nhom4_cp17310.Fragment.FragmentTrangChu;
import trong.fpt.duan1_nhom4_cp17310.Interfaces.SetSelectedItemMenuBottom;
import trong.fpt.duan1_nhom4_cp17310.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, SetSelectedItemMenuBottom {

    public BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new FragmentTrangChu());

        navigation = findViewById(R.id.navigation_bottom);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.trangchu:
                fragment = new FragmentTrangChu();
                break;

            case R.id.datve:
                fragment = new FragmentDatVe();
                break;

            case R.id.tintuc:
                fragment = new FragmentTinTuc();
                break;
            case R.id.khac:
                fragment = new FragmentKhac();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fr_main, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onSelected() {
        navigation.setSelectedItemId(R.id.datve);
    }
}
