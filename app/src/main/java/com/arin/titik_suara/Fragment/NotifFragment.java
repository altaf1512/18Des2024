package com.arin.titik_suara.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arin.titik_suara.R;
import com.arin.titik_suara.Adapter.MyNotifAdapter;
import com.arin.titik_suara.Model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotifFragment extends Fragment {

    private RecyclerView rvMyNotifications;
    private TextView titleEmpty;
    private MyNotifAdapter myNotifAdapter;
    private List<NotificationModel> notificationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        rvMyNotifications = view.findViewById(R.id.rvMyPengaduan);
        titleEmpty = view.findViewById(R.id.titleEmpty);

        notificationList = new ArrayList<>();
        myNotifAdapter = new MyNotifAdapter(getContext(), notificationList);

        rvMyNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMyNotifications.setAdapter(myNotifAdapter);

        loadNotifications();

        return view;
    }

    private void loadNotifications() {
        // TODO: Load actual notifications from your data source
        // For now, we'll add some dummy data
        long currentTime = System.currentTimeMillis();
        notificationList.add(new NotificationModel("Kategori 1", "Notifikasi 1 (Baru)", currentTime));
        notificationList.add(new NotificationModel("Kategori 2", "Notifikasi 2 (Lama)", currentTime - 86400000)); // 1 day ago

        // Simulate a read notification
        NotificationModel readNotif = new NotificationModel("Kategori 3", "Notifikasi 3 (Sudah Dibaca)", currentTime - 172800000); // 2 days ago
        readNotif.setRead(true);
        notificationList.add(readNotif);

        if (notificationList.isEmpty()) {
            titleEmpty.setVisibility(View.VISIBLE);
            rvMyNotifications.setVisibility(View.GONE);
        } else {
            titleEmpty.setVisibility(View.GONE);
            rvMyNotifications.setVisibility(View.VISIBLE);
            myNotifAdapter.notifyDataSetChanged();
        }
    }

    // Method to add a new notification (call this when a new notification is received)
    public void addNewNotification(String category, String message) {
        NotificationModel newNotification = new NotificationModel(category, message, System.currentTimeMillis());
        notificationList.add(0, newNotification); // Add to the top of the list
        myNotifAdapter.notifyItemInserted(0);
        rvMyNotifications.scrollToPosition(0); // Scroll to the top to show the new notification

        // Hide empty state if it was visible
        if (titleEmpty.getVisibility() == View.VISIBLE) {
            titleEmpty.setVisibility(View.GONE);
            rvMyNotifications.setVisibility(View.VISIBLE);
        }
    }
}