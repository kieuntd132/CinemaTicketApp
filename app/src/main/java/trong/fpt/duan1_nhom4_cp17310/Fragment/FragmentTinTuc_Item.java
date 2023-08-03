package trong.fpt.duan1_nhom4_cp17310.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterBannerManager;
import trong.fpt.duan1_nhom4_cp17310.Adapters.AdapterTinTuc;
import trong.fpt.duan1_nhom4_cp17310.R;
import trong.fpt.duan1_nhom4_cp17310.models.Banners;
import trong.fpt.duan1_nhom4_cp17310.models.News;
import trong.fpt.duan1_nhom4_cp17310.views.QuanLyBannerActivity;

public class FragmentTinTuc_Item extends Fragment {
    private RecyclerView rv_tintuc;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tin_tuc_item, container, false);
        rv_tintuc = view.findViewById(R.id.rv_tintuc);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        db.collection("news")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<News> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String linkAnh = map.get("linkAnh").toString();
                                String title = map.get("title").toString();
                                String linkWeb = map.get("linkWeb").toString();
                                News news = new News(linkAnh, title, linkWeb);
                                list.add(news);
                            }
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                            rv_tintuc.setLayoutManager(layoutManager);
                            AdapterTinTuc adapterTinTuc = new AdapterTinTuc(getContext(), list);
                            rv_tintuc.setAdapter(adapterTinTuc);
                        }
                    }
                });
    }
}