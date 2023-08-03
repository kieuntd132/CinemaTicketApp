package trong.fpt.duan1_nhom4_cp17310.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterFilmTrangChu;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.Film;

public class FragmentDatVe extends Fragment {

    private RecyclerView rv_datve_all;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dat_ve, container, false);
        rv_datve_all = view.findViewById(R.id.rv_datve_all);
        return view;
    }

    private void getDataFilm() {
        db.collection("films")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Film> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String tenPhim = map.get("tenPhim").toString();
                                String linkAnh = map.get("linkAnh").toString();
                                String giaVe = map.get("giaVe").toString();
                                String ngayKhoiChieu = map.get("ngayKhoiChieu").toString();
                                String details = map.get("details").toString();
                                Film film = new Film(tenPhim, ngayKhoiChieu, giaVe, linkAnh, details);
                                film.setIdFilm(document.getId());
                                list.add(film);
                            }
                            int numberOfColumns = 2;
                            rv_datve_all.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
                            AdapterFilmTrangChu adapterFilmTrangChu = new AdapterFilmTrangChu(getContext(), list);
                            rv_datve_all.setAdapter(adapterFilmTrangChu);
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFilm();
    }
}